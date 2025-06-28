import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import Demo.Data._

class ContactTest extends Simulation {

    val httpProtocol = http
        .baseUrl(url) 
        .acceptHeader("application/json")
        .contentTypeHeader("application/json")
        .header("Authorization", s"Bearer $token")

    val scn = scenario("Contact Test")
         .exec(
      http("Llamada autenticada")
        .post("/contacts")
        .body(StringBody(
          s"""{
                "firstName": "Eliana",
                "lastName": "Puerta",
                "birthdate": "2000-01-01",
                "email": "eliana@gmail.com",
                "phone": "5555555555",
                "street1": "1 Main St.",
                "street2": "Apartment A",
                "city": "Medellín",
                "stateProvince": "Antioquia",
                "postalCode": "0000000",
                "country": "CO"
          }"""
        )).asJson
        .header("Authorization", s"Bearer ${token}")
        .check(status.is(201))

    setUp(scn.inject(constantUsersPerSec(10).during(5.seconds))).protocols(httpProtocol)
}