package com.ServiceLayer;

import com.Common.Property;
import com.core.ApiActions;
import com.reporting.ExtentReports.ExtentTestManager;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
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

public class TC007_ST_TransactionHistoryDepositByDate<T> extends ApiActions<T> {
    private Logger logger = LogManager.getLogger(ApiActions.class);

    @Feature("")
    @Test(description = "")
    public void TransactionHistoryDepositByDate() {
        ExtentTestManager.getTest().setDescription("");


        try {
            String DateTo = "2018-10-30T13:59:59Z";
            String DateFrom = "2018-03-31T13:00:00Z";

            RestAssured.baseURI = Property.baseURI.getValue();

            JSONObject params = new JSONObject();
            params.put("CurrentPage", "0");
            params.put("DateTo", DateTo);
            params.put("RowsPerPage", "200");
            params.put("SearchOption", "2");
            params.put("DateFrom", DateFrom);
            params.put("Detail", "true");

            Response response = httpPost(params, "/Account/TransactionSearch");
            Assert.assertEquals(getStatusCode(response) /*actual value*/, 200 /*expected value*/, "Correct status code returned");
            logger.info("Response Body is =>  " + getBody(response));
            T AcTransactionId = jsonPathEvaluator(response, "Results.ACTransactionID");
            T Amount = jsonPathEvaluator(response, "Results.Amount");
            log("AcTransactionId: " + AcTransactionId);
            log("Amount: " + Amount);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            ExtentTestManager.endTest();
        }

    }
}
