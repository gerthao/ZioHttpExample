package gerthao.ziohttpexample.services

import gerthao.ziohttpexample.domain.Person
import gerthao.ziohttpexample.repos.PersonRepository
import zio.ZIO

import scala.concurrent.{ExecutionContext, Future}

trait Service[F[_], T]

trait ServiceCanRead[F[_], T] extends Service[F, T]:
  def list: F[Seq[T]]
  def find(f: T => Boolean): F[Option[T]]

trait ServiceCanWrite[F[_], T] extends Service[F, T]:
  def put(value: T)(f: T => Boolean): F[Boolean]
  def delete(f: T => Boolean): F[Boolean]

type ServiceResult[T] = ZIO[Any, Throwable, T]

final case class PeopleService(private val repo: PersonRepository):
  def getAll: ServiceResult[Seq[Person]] = repo.getPeople

  def getById(id: Int): ServiceResult[Option[Person]] = repo.getPersonById(id)

end PeopleService
