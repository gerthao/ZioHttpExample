package gerthao.ziohttpexample.middleware

import zio.http.{Http, Middleware, Request, Response}
import zio.{Console, IO, Trace, ZIO}

import java.io.IOException

object Verbose {

  def log[R, E >: Throwable](
      f: (=> Any) => IO[E, Unit]
  ): Middleware[R, E, Request, Response, Request, Response] =
    new Middleware[R, E, Request, Response, Request, Response]:

      override def apply[R1 <: R, E1 >: E](
          http: Http[R1, E1, Request, Response]
      )(using trace: Trace): Http[R1, E1, Request, Response] = http
        .contramapZIO[R1, E1, Request](logRequest)
        .mapZIO[R1, E1, Response](logHeader)

      private def logRequest(r: Request) =
        for
          _ <- f(s"> ${r.method}, ${r.path} ${r.version}")
          _ <- ZIO.foreach(r.headers.toList)(h => f(s"> ${h._1} ${h._2}"))
        yield r

      private def logHeader(r: Response) =
        for
          _ <- f(s"< ${r.status}")
          _ <- ZIO.foreach(r.headers.toList)(header =>
            f(s"< ${header._1} ${header._2}")
          )
        yield r

}
