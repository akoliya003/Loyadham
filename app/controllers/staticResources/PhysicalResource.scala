package controllers.staticResources

import java.util.concurrent.locks.ReentrantReadWriteLock

// A cache of what "physical" resources we know of, shared by both `FrontEndServingController` and `AddNoCacheHeaders`, an Action which wraps `FrontEndServingController`s `serve` via Action composition in order to possibly add no-cache headers to index.html
object PhysicalResource {
  private val lock = new ReentrantReadWriteLock()
  private var cache = Set[String]()

  def exists(path: String) = {
    lock.readLock().lock()
    try {
      cache.contains(path)
    } finally {
      lock.readLock().unlock()
    }
  }

  def setExists(path: String) = {
    lock.writeLock().lock()
    try {
      cache += path
    } finally {
      lock.writeLock().unlock()
    }
  }
}
