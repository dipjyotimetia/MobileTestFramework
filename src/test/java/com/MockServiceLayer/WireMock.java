/*
MIT License

Copyright (c) 2021 Dipjyoti Metia

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.MockServiceLayer;

import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.stubbing.Scenario;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


public class WireMock {

    public WireMock() {
    }

    public void setupExampleStub() {

        stubFor(post(urlEqualTo("/pingpong"))
                .withRequestBody(matching("<input>PING</input>"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/xml")
                        .withBody("<output>PONG</output>")));
    }

    public void setupStubURLMatching() {

        stubFor(get(urlEqualTo("/urlmatching"))
                .willReturn(aResponse()
                        .withBody("URL matching")
                ));
    }

    public void setupStubRequestBodyMatching() {

        stubFor(post(urlEqualTo("/requestbodymatching"))
                .withRequestBody(containing("RequestBody"))
                .willReturn(aResponse()
                        .withBody("Request body matching")
                ));
    }

    public void setupStubHeaderMatching() {

        stubFor(get(urlEqualTo("/headermatching"))
                .withHeader("Content-Type", containing("application/json"))
                .withHeader("DoesntExist", absent())
                .willReturn(aResponse()
                        .withBody("Header matching")
                ));
    }

    public void setupStubAuthorizationMatching() {

        stubFor(get(urlEqualTo("/authorizationmatching"))
                .withBasicAuth("username", "password")
                .willReturn(aResponse()
                        .withBody("Authorization matching")
                ));
    }

    public void setupStubReturningErrorCode() {

        stubFor(get(urlEqualTo("/errorcode"))
                .willReturn(aResponse()
                        .withStatus(500)
                ));
    }

    public void setupStubFixedDelay() {

        stubFor(get(urlEqualTo("/fixeddelay"))
                .willReturn(aResponse()
                        .withFixedDelay(2000)
                ));
    }

    public void setupStubBadResponse() {

        stubFor(get(urlEqualTo("/badresponse"))
                .willReturn(aResponse()
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK)
                ));
    }

    public void setupStubStateful() {

        stubFor(get(urlEqualTo("/order")).inScenario("Order processing")
                .whenScenarioStateIs(Scenario.STARTED)
                .willReturn(aResponse()
                        .withBody("Your shopping cart is empty")
                ));

        stubFor(post(urlEqualTo("/order")).inScenario("Order processing")
                .whenScenarioStateIs(Scenario.STARTED)
                .withRequestBody(equalTo("Ordering 1 item"))
                .willReturn(aResponse()
                        .withBody("Item placed in shopping cart")
                )
                .willSetStateTo("ORDER_PLACED"));

        stubFor(get(urlEqualTo("/order")).inScenario("Order processing")
                .whenScenarioStateIs("ORDER_PLACED")
                .willReturn(aResponse()
                        .withBody("There is 1 item in your shopping cart")
                ));
    }

}
