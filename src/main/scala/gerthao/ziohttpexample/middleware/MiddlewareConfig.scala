package gerthao.ziohttpexample.middleware

import zio.http.middleware.Cors.CorsConfig
import zio.http.model.Method
import pureconfig.*
import pureconfig.error.ConfigReaderFailures

object MiddlewareConfig {

  def getCorsConfig(at: String): Either[ConfigReaderFailures, CorsConfig] = {
    val cs = ConfigSource.default.at(at)

    for
      anyO     <- cs.at("anyOrigin").load[Boolean]
      anyM     <- cs.at("anyMethod").load[Boolean]
      allowedO <- cs.at("allowedOrigins").load[String]
      allowedM <-
        cs.at("allowedMethods").load[Set[String]].map(_.map(Method.fromString))
    yield CorsConfig(
      anyOrigin = anyO,
      anyMethod = anyM,
      allowedOrigins = _.equals(allowedO),
      allowedMethods = Some(allowedM)
    )
  }

}
