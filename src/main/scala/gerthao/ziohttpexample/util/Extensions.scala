package gerthao.ziohttpexample.util

import scala.util.chaining.*

object Extensions {
  extension[T] (x: T)
    def |>[U](f: T => U): U = x pipe f
    def \\>(f: T => Unit): T = x tap f
}
