package Demo

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import Demo.Data._

class LoginTest extends Simulation {

  // 1 Http Conf
  val httpConf = http.baseUrl(url)
    .acceptHeader("application/json")
    //Verificar de forma general para todas las solicitudes
    .check(status.is(200))

  // 2 Scenario Definition
  val scn = scenario("Login").
    exec(http("login")
      .post(s"/users/login")
      .body(StringBody(
        """{
          "email": "${email}",
          "password": "${password}"
        }"""
      )).asJson
      //Recibir información de la cuenta
      .check(status.is(200))
      .check(jsonPath("$.token").notNull)
    )

  setUp(
    scn.inject(constantUsersPerSec(10).during(5.seconds))
  ).protocols(httpConf)
    .assertions(
      global.responseTime.percentile(95).lt(5000)
    );
}