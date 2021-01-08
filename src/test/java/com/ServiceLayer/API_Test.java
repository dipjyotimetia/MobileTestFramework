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
package com.ServiceLayer;

import com.common.Property;
import com.core.ApiActions;
import com.reporting.ExtentReports.ExtentTestManager;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class API_Test<T> extends ApiActions<T> {
    private final Logger logger = LogManager.getLogger(ApiActions.class);

    @Feature("")
    @Test(description = "")
    public void ServiceDemo() {
        ExtentTestManager.getTest().setDescription("Demo Service");

        try {
            String DateTo = "2018-10-30T13:59:59Z";
            String DateFrom = "2018-03-31T13:00:00Z";

            RestAssured.baseURI = Property.baseURI.getValue();

            JSONObject params = new JSONObject();
            params.put("DateTo", DateTo);
            params.put("DateFrom", DateFrom);

            Response response = httpPost(params, "/Demo/Service");
            Assert.assertEquals(getStatusCode(response) /*actual value*/, 200 /*expected value*/, "Correct status code returned");
            logger.info("Response Body is =>  " + getBody(response));
            T AcId = jsonPathEvaluator(response, "Results.ID");
            T Demo = jsonPathEvaluator(response, "Results.Demo");
            log("AcId: " + AcId);
            log("Demo: " + Demo);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            ExtentTestManager.endTest();
        }

    }
}
