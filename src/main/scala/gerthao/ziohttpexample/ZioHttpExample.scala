package gerthao.ziohttpexample

import zio.*
import zio.http.*
import zio.http.model.Method

object ZioHttpExample extends ZIOAppDefault:
  type HttpApp[-R, +E] = Http[R, E, Request, Response]
  type UHttpApp        = HttpApp[Any, Nothing]
  type RHttpApp[-R]    = HttpApp[R, Throwable]
  type UHttp[-A, +B]   = Http[Any, Nothing, A, B]

  val app: UHttpApp = Http.collect[Request] { case Method.GET -> !! / "hello" =>
    Response.text("hello")
  }

  val zApp: UHttpApp =
    Http.collectZIO[Request] { case Method.POST -> !! / "hello" =>
      Random.nextIntBetween(3, 6).map(n => Response.text("Hello! " * n))
    }

  val combined: UHttpApp = app ++ zApp

  val program: ZIO[Any, Throwable, ExitCode] =
    for _ <- Server.serve(combined).provide(Server.default)
    yield ExitCode.success

  override def run: ZIO[Any, Throwable, ExitCode] = program
