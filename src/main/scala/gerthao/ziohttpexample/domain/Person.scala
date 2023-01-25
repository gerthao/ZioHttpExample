package gerthao.ziohttpexample.domain

import zio.json.{DeriveJsonEncoder, JsonEncoder}

final case class Person(
    id: Int,
    firstName: String,
    lastName: String,
    age: Int,
    address: Option[Address]
)

object Person:
  given jsonEncoder: JsonEncoder[Person] = DeriveJsonEncoder.gen[Person]
end Person
