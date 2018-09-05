package com.core;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApiActions {

    protected static final Faker faker = new Faker();
    private Logger logger = LogManager.getLogger(ApiActions.class);

    protected String AuthLogin() {
        RestAssured.baseURI = "";
        RequestSpecification httpRequest = RestAssured
                .given()
                .contentType(ContentType.JSON);

        JSONObject requestParams = new JSONObject();
        requestParams.put("", ""); // Cast
        requestParams.put("", "");

        Response response = httpRequest.body(requestParams.toJSONString()).
                request(Method.POST, "/Account/Login");

        return response.getHeaders().getValue("ClientAuth");
    }

    protected String SystemDateFormat() {
        try {
            DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
            Date date1 = new Date();
            String abc1 = date.format(date1);
            return abc1;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    @Step("{0}")
    protected void log(String message) {
        logger.info(message);
    }

}
