package serverTest;

import io.gatling.core.ConfigKeys;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import static io.gatling.javaapi.core.CoreDsl.csv;
import static io.gatling.javaapi.core.CoreDsl.scenario;

public class TestServer extends Simulation {

    @Override
    public void before() {
        System.out.println("Собственный код До теста!");
    }

    ScenarioBuilder scn = scenario("Scenario");

    // Читаем файл с данными

    FeederBuilder<String> feeder = csv("id.csv").random();


//    scenario("StandardUser")
//        .exec(ConfigKeys.http("testGet").get("http://localhost:8099/hello"))
//        .pause(2, 3)
//        .exec(ConfigKeys.http("testGetId").get("http://localhost:8099/hello?q=#{id}"))
//        .pause(2);




    @Override
    public void after() {
        System.out.println("Собственный код После теста!");
    }
}