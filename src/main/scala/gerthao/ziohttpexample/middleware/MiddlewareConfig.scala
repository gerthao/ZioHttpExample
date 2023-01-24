package gerthao.ziohttpexample.middleware

import zio.http.middleware.Cors.CorsConfig
import zio.http.model.Method
import pureconfig.*
import pureconfig.error.ConfigReaderFailures

object MiddlewareConfig {

  def getCorsConfig(at: String): Either[ConfigReaderFailures, CorsConfig] = {
    val cs                   = ConfigSource.default.at(at)
    val anyOriginResult      = cs.at("anyOrigin").load[Boolean]
    val anyMethodResult      = cs.at("anyMethod").load[Boolean]
    val allowedOriginsResult = cs.at("allowedOrigins").load[String]
    val allowedMethodsResult = cs.at("allowedMethods").load[Set[String]]

    for
      anyO     <- anyOriginResult
      anyM     <- anyMethodResult
      allowedO <- allowedOriginsResult
      allowedM <- allowedMethodsResult.map(_.map(Method.fromString))
    yield CorsConfig(
      anyOrigin = anyO,
      anyMethod = anyM,
      allowedOrigins = _.equals(allowedO),
      allowedMethods = Some(allowedM)
    )
  }
}
