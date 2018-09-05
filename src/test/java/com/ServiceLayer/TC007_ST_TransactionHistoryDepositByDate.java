package com.ServiceLayer;

import com.core.ApiActions;
import com.reporting.ExtentReports.ExtentTestManager;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class TC007_ST_TransactionHistoryDepositByDate extends ApiActions {
    private Logger logger = LogManager.getLogger(ApiActions.class);

    @Test
    @Description("")
    public void TransactionHistoryDepositByDate() {
        ExtentTestManager.getTest().setDescription("");

        try {
            String DateTo = "2018-06-30T13:59:59Z";
            String DateFrom = "2018-03-31T13:00:00Z";

            RestAssured.baseURI = "";

            JSONObject params = new JSONObject();
            params.put("CurrentPage", "0");
            params.put("DateTo", DateTo);
            params.put("RowsPerPage", "200");
            params.put("SearchOption", "2");
            params.put("DateFrom", DateFrom);
            params.put("Detail", "true");

            RequestSpecification httpRequest = RestAssured
                    .given()
                    .with()
                    .header("ClientAuth", AuthLogin())
                    .contentType(ContentType.JSON)
                    .with()
                    .body(params.toJSONString());

            Response response = httpRequest.request(Method.POST, "/Account/TransactionSearch");
            ResponseBody responseBody = response.getBody();
            Assert.assertEquals(response.getStatusCode() /*actual value*/, 200 /*expected value*/, "Correct status code returned");

            logger.info("Response Body is =>  " + responseBody.prettyPrint());

            JsonPath jsonPathEvaluator = response.jsonPath();
            ArrayList<Integer> AcTransactionId = jsonPathEvaluator.get("Results.ACTransactionID");
            ArrayList<String> Amount = jsonPathEvaluator.get("Results.Amount");

            log("AcTransactionId: " + AcTransactionId);
            log("Amount: " + Amount);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            ExtentTestManager.endTest();
        }

    }
}
