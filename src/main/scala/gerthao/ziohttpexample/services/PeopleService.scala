package gerthao.ziohttpexample.services

import gerthao.ziohttpexample.domain.Person
import gerthao.ziohttpexample.repos.PersonRepository
import zio.ZIO

import scala.concurrent.{ExecutionContext, Future}

final case class PeopleService(private val repo: PersonRepository):
  def getAll: ZIO[Any, Throwable, Seq[Person]] = repo.getPeople

  def getById(id: Int): ZIO[Any, Throwable, Option[Person]] =
    repo.getPersonById(id)

end PeopleService
