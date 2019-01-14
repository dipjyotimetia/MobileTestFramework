package com.MockServiceLayer;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TC001_MockService {
    private WireMock wme = new WireMock();

    private WireMockServer server = new WireMockServer(WireMockConfiguration.wireMockConfig().port(9876));

    @Test
    public void testPingPongPositive() {

        server.start();

        wme.setupExampleStub();

        RestAssured.given().
                body("<input>PING</input>").
                when().
                post("http://localhost:9876/pingpong").
                then().
                log().
                body().
                and().
                assertThat().
                statusCode(200).
                and().
                body("output", Matchers.equalTo("PONG"));
    }

    @Test
    public void testURLMatching() {

        wme.setupStubURLMatching();

        RestAssured.given().
                when().
                get("http://localhost:9876/urlmatching").
                then().
                assertThat().
                body(Matchers.equalTo("URL matching"));
    }

    @Test
    public void testRequestBodyMatching() {

        wme.setupStubRequestBodyMatching();

        RestAssured.given().
                body("TestRequestBodyMatching").
                when().
                post("http://localhost:9876/requestbodymatching").
                then().
                assertThat().
                body(Matchers.equalTo("Request body matching"));
    }

    @Test
    public void testHeaderMatching() {

        wme.setupStubHeaderMatching();

        RestAssured.given().
                contentType("application/json").
                when().
                get("http://localhost:9876/headermatching").
                then().
                assertThat().
                body(Matchers.equalTo("Header matching"));
    }

    @Test
    public void testAuthorizationMatching() {

        wme.setupStubAuthorizationMatching();

        RestAssured.given().
                auth().
                preemptive().
                basic("username", "password").
                when().
                get("http://localhost:9876/authorizationmatching").
                then().
                assertThat().
                body(Matchers.equalTo("Authorization matching"));
    }

    @Test
    public void testErrorCode() {

        wme.setupStubReturningErrorCode();

        RestAssured.given().
                when().
                get("http://localhost:9876/errorcode").
                then().
                assertThat().
                statusCode(500);
    }

    @Test
    public void testFixedDelay() {

        wme.setupStubFixedDelay();

        RestAssured.given().
                when().
                get("http://localhost:9876/fixeddelay").
                then().
                assertThat().
                time(Matchers.greaterThan(2000L), TimeUnit.MILLISECONDS);

    }

    @Test
    public void testStatefulStub() {

        wme.setupStubStateful();

        RestAssured.given().
                when().
                get("http://localhost:9876/order").
                then().
                assertThat().
                body(Matchers.equalTo("Your shopping cart is empty"));

        RestAssured.given().
                body("Ordering 1 item").
                when().
                post("http://localhost:9876/order").
                then().
                assertThat().
                body(Matchers.equalTo("Item placed in shopping cart"));

        RestAssured.given().
                when().
                get("http://localhost:9876/order").
                then().
                assertThat().
                body(Matchers.equalTo("There is 1 item in your shopping cart"));
    }
}
