package nameService;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class NameService extends Simulation {



    @Override
    public void before() {
        System.out.println("Собственный код До теста!!");
    }


    // Читаем файл с данными

    //FeederBuilder<String> feeder1 = csv("id.csv").random();
    FeederBuilder<String> feeder = csv("name.csv").batch().random();

    // Создаём сценарий name
    ScenarioBuilder searchByName = scenario("name")
            .feed(feeder)
            .exec(http("Name in param 1 GET")
                    .get("/name?name=#{name}")
                    .check(status().is(200)))
            .pause(1,2)
            .exec(http("Name in param 2 GET")
                    .get("/name")
                    .queryParam("name", "#{name}")
                    .check(status().is(200)))
            .pause(1,2)
            .exec(http("Name in header POST")
                    .post("/name")
                    .header("name", "#{name}")
                    .header("content-type", "application/json")
                    .ignoreProtocolHeaders()
                    .check(status().is(200)))
            .pause(1,2)
            .exec(http("Name in JSON POST")
                    .post("/name")
                    .body(StringBody("{\"name\":\"#{name}\"}"))
                    .check(status().is(200)))
            .pause(1,2)
            .exec(http("Name in file JSON POST")
                    .post("/name")
                    .body(ElFileBody("name.json"))
                    .check(status().is(200)))
            ;



    HttpProtocolBuilder httpProtocol =
            http.baseUrl("http://localhost:8081/")
                    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .acceptLanguageHeader("en-US,en;q=0.5")
                    .acceptEncodingHeader("gzip, deflate")
                    .userAgentHeader(
                            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0"
                    );


    {
        setUp(
          //      searchByName.injectOpen(atOnceUsers(2000))
                searchByName.injectOpen(nothingFor(10),
                        rampUsers(10).during(10),
                        rampUsers(20).during(10),
                        rampUsers(30).during(10),
                        rampUsers(40).during(10)
                )
         //       searchByName.injectOpen(rampUsers(500).during(200))
        ).protocols(httpProtocol);
    }




        // DEBUG
//        setUp(myScenario.injectOpen(atOnceUsers(1)).protocols(httpProtocol)).maxDuration(1);

        // MAXPERF
//        setUp(myScenario.injectOpen(
//                // Интенсивность на ступень
//                incrementUsersPerSec(10)
//                        // Количество ступеней
//                .times(8)
//                        // Длительность полки
//                .eachLevelLasting(30)
//                        // длительность разгона
//                .separatedByRampsLasting(20)
//                        // Начало нагрузки с rps
//                .startingFrom(0))
//                ).protocols(httpProtocol)
//                //  Общая длительность теста
//                .maxDuration(1);

        // STABILITY
//        setUp(myScenario.injectOpen(
//                // Длительность разгона
//                incrementUsersPerSec(0)
//                        // Количество ступеней
//                .times(8)
//                        // Длительность полки
//                .eachLevelLasting(30)
//                        // длительность разгона
//                .separatedByRampsLasting(20)
//                        // Начало нагрузки с rps
//                .startingFrom(0))
//                ).protocols(httpProtocol)
//                //  Общая длительность теста
//                .maxDuration(1);
  //  }


    @Override
    public void after() {
        System.out.println("Собственный код После теста!");
    }

}
