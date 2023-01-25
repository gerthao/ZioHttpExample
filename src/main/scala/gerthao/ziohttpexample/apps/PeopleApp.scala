package gerthao.ziohttpexample.apps

import gerthao.ziohttpexample.util.Extensions.*
import gerthao.ziohttpexample.domain.{Address, Person}
import gerthao.ziohttpexample.services.PeopleService
import zio.http.model.*
import zio.http.*
import zio.json.JsonEncoder
import zio.json.EncoderOps
import zio.ZIO
import zio.URIO

final class PeopleApp(peopleService: PeopleService) {
  val base: Path = !! / "people"

  val app: Http[Any, Nothing, Request, Response] = Http.collectZIO[Request] {
    case r @ Method.GET -> base /: "id" /: id => handleGetPerson(r)
    case Method.GET -> base                   =>
      peopleService.getAll.fold(
        err => Response.text(err.toString),
        data => Response.json(data.toJsonPretty)
      )
    case r @ Method.POST -> base              =>
      true.toJsonPretty |> Response.json |> ZIO.succeed
  }

  private def handleGetPerson(r: Request): URIO[Any, Response] =
    val getId  = r.url.queryParams("id").map(_.toInt)
    val result = getId.mapZIO(peopleService.getById)
    result.fold(
      err => Response.text(err.toString),
      data => Response.json(data.toJsonPretty)
    )
}
