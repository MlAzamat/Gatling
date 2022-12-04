package serverAnyPath;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class TestServerAnyPath extends Simulation {

    @Override
    public void before() {
        System.out.println("Собственный код До теста!!");
    }


    // Читаем файл с данными

    //FeederBuilder<String> feeder1 = csv("id.csv").random();
    FeederBuilder<String> feeder = csv("id.csv").eager().random();


    // ЗАПРОС № 1

    ChainBuilder helloGet =
            exec()      // создал пустой exec чтобы сработал .feed(feeder)
            .feed(feeder)
            .exec(http("helloGet").get("/hello?id=#{id}").check(status().is(200)))
            .pause(1);


    // ЗАПРОС № 2

    ChainBuilder helloPOST =
            exec(
                    http("helloPOST")
                            .post("/hello")
                            .check(status().is(200))
                            //.pause(1)
                            //.feed(feeder)
                    );
    //.exitHereIfFailed();

    // header и url

    HttpProtocolBuilder httpProtocol =
            http.baseUrl("http://localhost:8099/")
                    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .acceptLanguageHeader("en-US,en;q=0.5")
                    .acceptEncodingHeader("gzip, deflate")
                    .userAgentHeader(
                            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0"
                    );

    // сценарий

    ScenarioBuilder myScenario = scenario("hello").exec(helloGet, helloPOST);
    {
        setUp(
                myScenario.injectOpen(rampUsers(2000).during(100))
        ).protocols(httpProtocol);
    }
    @Override
    public void after() {
        System.out.println("Собственный код После теста!");
    }


}
