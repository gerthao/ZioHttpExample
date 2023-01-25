package gerthao.ziohttpexample.util

import scala.util.chaining.*

object Extensions {
  extension[T] (x: T)
    inline def |>[U](f: T => U): U = x pipe f
    inline def \>(f: T => Unit): T = x tap f
}
