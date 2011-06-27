/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.lib.memcache

import com.google.appengine.api.datastore.DatastoreServiceFactory
import com.google.appengine.api.datastore.DatastoreTimeoutException
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.FetchOptions
import com.google.appengine.api.datastore.Query
import com.google.appengine.api.memcache.MemcacheService
import com.google.appengine.api.memcache.MemcacheServiceFactory
import java.util.logging.Level
import java.util.logging.Logger
import scala.collection.JavaConversions._

object CounterLogService {
  val MC_KEY_COUNTER = "org.dotme.liquidtpl.service.CounterLogService#counter_";
  val COUNTER_KEY = "c";
  val COUNTER_KIND_PREFIX = "c_";
  val CLEANUP_AT_ONCE = 100;
  val KEY_NAME = "name"

  private val mcService:MemcacheService = MemcacheServiceFactory
  .getMemcacheService();

  private val log:Logger = Logger.getLogger(CounterLogService.getClass.getName)

  /**
   * Increments the counter value and returns it. The values will be unique
   * serial numbers across the application. The value is generated by
   * {@link MemcacheService#increment(Object, long)} and recorded by
   * {@link CounterLog} entities so that the counter may have durability while
   * it is scalable. You may need to add a cron task to delete the old
   * CounterLog entities at background, if the entities are growing too big.
   *
   * @return an incremented counter value
   */
  def increment(name:String):Long = {

    // try to get the next value from Memcache
    val countValue:Long =
      try {
        mcService.increment(MC_KEY_COUNTER + name, 1).longValue;
      } catch {
        case e => log.log(Level.WARNING, "Failed to increment on Memcache: ", e);
          // if failed, restore the value from Log
          restoreCountValue(name);
      }



    // save a countValue by a CounterLog
    try {
      saveCounterLog(countValue, name);
    } catch {
      case e:DatastoreTimeoutException => log.log(Level.WARNING, "Failed to save CounterLog: ", e);
    }

    // return the value
    return countValue;
  }

  // save a count value by a CounterLog
  private def saveCounterLog(countValue:Long, name:String):Unit = {
    val datastoreService = DatastoreServiceFactory.getDatastoreService
    val e:Entity = new Entity(COUNTER_KIND_PREFIX + name)
    e.setProperty(COUNTER_KEY, countValue)
    datastoreService.put(e)
  }

  // restore the largest count value from Datastore
  def restoreCountValue(name:String):Long = {
    val datastoreService = DatastoreServiceFactory.getDatastoreService
    val cls:List[Entity] = datastoreService.prepare(new Query(COUNTER_KIND_PREFIX + name)
                                                    .addSort(COUNTER_KEY, Query.SortDirection.ASCENDING)
    ).asList(
      FetchOptions.Builder.withLimit(1)
    ).toList

    // restore count value if it can
    val countValue:Long = if (cls == null) {
      1
    } else {
      cls.apply(0).getProperty(COUNTER_KEY).asInstanceOf[Long] + 1;
    }

    // save the new value to Memcache
    mcService.put(MC_KEY_COUNTER + name, countValue);
    log.log(Level.WARNING, "Restored count value: " + countValue);

    // return the value
    return countValue;
  }

  /**
   * Deletes the count value stored on Memcache. This should be called only
   * for testing purpose.
   */
  def deleteCountValueOnMemcache(name:String) {
    mcService.delete(MC_KEY_COUNTER + name);
  }

  def cleanupDatastore(name:String) {
    var isFirst = true;
    val datastoreService = DatastoreServiceFactory.getDatastoreService
    datastoreService.prepare(new Query(COUNTER_KIND_PREFIX + name)
                             .addSort(COUNTER_KEY, Query.SortDirection.ASCENDING)
    ).asList(
      FetchOptions.Builder.withLimit(CLEANUP_AT_ONCE)
    ).foreach {
      e => {
        if(!isFirst){
          datastoreService.delete(e.getKey)
        }
        isFirst = false
      }
    }
  }
}
