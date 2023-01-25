package gerthao.ziohttpexample.repos

import gerthao.ziohttpexample.domain.Person
import io.getquill.jdbczio.Quill
import io.getquill.*
import zio.ZIO

import java.sql.SQLException

final case class PersonRepository(quill: Quill.H2[SnakeCase]):
  import quill.*

  inline def people: EntityQuery[Person] = query[Person]

  def getPeople: ZIO[Any, SQLException, Seq[Person]] = run(people)

  def getPersonById(id: Int): ZIO[Any, SQLException, Option[Person]] = run(
    people.filter(_.id == lift(id)).take(1)
  ).map(_.headOption)

  def getPeopleByFullName(
      firstName: String,
      lastName: String
  ): ZIO[Any, SQLException, Seq[Person]] = run(
    people
      .filter(_.firstName == lift(firstName))
      .filter(_.lastName == lift(lastName))
  )

end PersonRepository

object PersonRepository:

  def getPeople: ZIO[PersonRepository, SQLException, Seq[Person]] =
    ZIO.serviceWithZIO[PersonRepository](_.getPeople)

  def getPersonById(id: Int): ZIO[PersonRepository, SQLException, Option[Person]] =
    ZIO.serviceWithZIO[PersonRepository](_.getPersonById(id))

  def getPeopleByFullName(
      firstName: String,
      lastName: String
  ): ZIO[PersonRepository, SQLException, Seq[Person]] =
    ZIO.serviceWithZIO[PersonRepository](
      _.getPeopleByFullName(firstName, lastName)
    )

end PersonRepository
