package gerthao.ziohttpexample.domain

import zio.json.{DeriveJsonEncoder, JsonEncoder}

final case class Address(
    id: Int,
    number: Int,
    street: String,
    city: String,
    zipCode: String
)

object Address:
  given jsonEncoder: JsonEncoder[Address] = DeriveJsonEncoder.gen[Address]
end Address
