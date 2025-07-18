package Demo

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import Demo.Data._

class LoginTest extends Simulation {

  val httpConf = http.baseUrl(url)
    .acceptHeader("application/json")
    .check(status.is(200))

  val scn = scenario("Login").
    exec(http("login")
      .post("/users/login")
      .body(StringBody(
        s"""{
          "email": "$email",
          "password": "$password"
        }"""
      )).asJson
      .check(status.is(200))
      .check(jsonPath("$.token").notNull)
    )

  setUp(
    scn.inject(constantUsersPerSec(10).during(5.seconds))
  ).protocols(httpConf);
}