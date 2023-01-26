package gerthao.ziohttpexample.apps

import zio.*
import zio.http.*
import zio.http.model.*

object AuthApp:

  val app =
    Http.collect[Request] { case Method.GET -> !! / "secret" / "hello" =>
      Response.text("i think he's talking to you.")
    } @@ Middleware.basicAuth("homer", "thompson")

end AuthApp
