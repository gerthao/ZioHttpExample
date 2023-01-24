package gerthao.ziohttpexample

import gerthao.ziohttpexample.middleware.{MiddlewareConfig, Verbose}
import gerthao.ziohttpexample.util.Extensions.*
import zio.*
import zio.http.*
import zio.http.middleware.HttpMiddleware
import zio.http.model.Method
import zio.json.*
import zio.json.ast.Json

import scala.util.chaining.*
import java.io.IOException

object MyServer extends ZIOAppDefault:
  type HttpApp[-R, +E] = Http[R, E, Request, Response]

  private val app = Http.collect[Request] {
    case Method.GET -> !! / "hello"  => Response.text("hello")
    case Method.GET -> !! / "people" => Response.json(getPeople.toJsonPretty)
  }

  final case class Person(id: Int, firstName: String, lastName: String)

  object Person {
    given encoder: JsonEncoder[Person] = DeriveJsonEncoder.gen[Person]
  }

  private def getPeople: Seq[Person] = Seq(
    Person(1, "Joe", "Smoe"),
    Person(2, "Homer", "Simpson"),
    Person(3, "Ned", "Flanders")
  )

  private val zApp =
    Http.collectZIO[Request] { case Method.POST -> !! / "hello" =>
      Random.nextIntBetween(3, 6).map(n => Response.text("Hello! " * n))
    }

  private val apps = Seq(app, zApp)

  private val middlewareCors = MiddlewareConfig
    .getCorsConfig("middleware.cors")
    .tap {
      case Left(failures) => println(
          s"Failed to parse config for \"middleware.cors\":\n\t${failures.toList.mkString("\n")}"
        )
      case Right(_)       => println("Parsed config for \"middleware.cors\"...")
    }
    .map(Middleware.cors.apply)

  private val middlewares = (middlewareCors +: Seq(
    Middleware.debug,
    Verbose.log(Console.printLine)
  ).map(Right.apply)).collect { case Right(mw) => mw }

  private val combined = apps.reduce(_ ++ _) @@ middlewares.reduce(_ ++ _)

  private val program =
    for _ <- Server.serve(combined).provide(Server.default)
    yield ExitCode.success

  override def run: ZIO[Any, Throwable, ExitCode] = program

end MyServer
