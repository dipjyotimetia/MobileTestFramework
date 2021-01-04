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
package com.core;

import com.Common.Property;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApiActions<T> {

    protected static final Faker faker = new Faker();
    private Logger logger = LogManager.getLogger(ApiActions.class);

    private final static String workstation = System.getenv("COMPUTERNAME");
    private final static String domain = System.getenv("USERDOMAIN");
    private final static String baseURI = Property.baseURI.getValue();
    private final static String userName = Property.userName.getValue();
    private final static String userPass = Property.passWord.getValue();
    private final static String tokenURI = Property.tokenURI.getValue();
    private final static String clientUser = Property.clientUser.getValue();
    private final static String clientPass = Property.clientPassword.getValue();

    @SuppressWarnings("unchecked")
    private String authLogin() {
        RestAssured.baseURI = baseURI;
        RequestSpecification httpRequest = RestAssured
                .given()
                .contentType(ContentType.JSON);

        JSONObject requestParams = new JSONObject();
        requestParams.put("Identifier", userName);
        requestParams.put("Password", userPass);

        Response response = httpRequest.body(requestParams.toJSONString()).
                request(Method.POST, "/Test");

        return getHeaders(response, "auth");
    }

    /**
     * Authentication token generate
     *
     * @return auth token
     */
    protected String authToken() {
        String token = "";
        CredentialsProvider credProvider = new BasicCredentialsProvider();
        credProvider.setCredentials(AuthScope.ANY,
                new NTCredentials(clientUser, clientPass, workstation, domain));
        try (CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(credProvider).build()) {
            HttpGet request = new HttpGet(tokenURI);
            HttpResponse response = client.execute(request);
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }
            String[] res = builder.toString().trim().split(",");
            token = res[1].substring(9, res[1].length() - 1);
        } catch (Exception e) {
            logger.error(e);
        }
        return token;
    }

    /**
     * http request with parameter
     *
     * @param params jsonObject
     * @return returns httpRequest
     */
    private RequestSpecification httpRequest(JSONObject params) {
        return RestAssured
                .given()
                .with()
                .header("auth", authLogin())
                .contentType(ContentType.JSON)
                .with()
                .body(params.toJSONString());
    }

    /**
     * http request with parameter and auth token
     *
     * @param params jsonObject
     * @param token  token
     * @return httpRequest
     */
    private RequestSpecification httpRequestLogin(JSONObject params, String token) {
        return RestAssured
                .given()
                .with()
                .header("token", token)
                .contentType(ContentType.JSON)
                .with()
                .body(params.toJSONString());
    }

    /**
     * http request without parameter
     *
     * @return returns httpRequest
     */
    private RequestSpecification httpRequest() {
        return RestAssured
                .given()
                .with()
                .header("auth", authLogin());
    }

    /**
     * http request with token
     *
     * @param token token
     * @return http request
     */
    private RequestSpecification httpLogin(String token) {
        return RestAssured
                .given()
                .with()
                .header("token", token);
    }

    /**
     * Set base uri
     *
     * @param baseURI baseUri
     */
    public void setBaseURI(String baseURI) {
        RestAssured.baseURI = baseURI;
    }

    /**
     * Set base path
     *
     * @param basePathTerm basepath
     */
    public void setBasePath(String basePathTerm) {
        RestAssured.basePath = basePathTerm;
    }

    /**
     * reset base uri
     */
    public void resetBaseURI() {
        RestAssured.baseURI = null;
    }

    /**
     * Reset base path
     */
    public void resetBasePath() {
        RestAssured.basePath = null;
    }

    /**
     * http post
     *
     * @param params params
     * @param path   endpoint
     * @return response
     */
    protected Response httpPost(JSONObject params, String path) {
        return httpRequest(params).request(Method.POST, path);
    }

    /**
     * http post cancel
     *
     * @param params params
     * @param path   endpoint
     * @param token  token
     * @return response
     */
    protected Response httpPostCancelPendingBets(JSONObject params, String path, String token) {
        return httpRequestLogin(params, token).request(Method.POST, path);
    }

    /**
     * http get
     *
     * @param path endpoint
     * @return response
     */
    protected Response httpGet(String path) {
        return httpRequest().request(Method.GET, path);
    }

    /**
     * http get pending
     *
     * @param path  endpoint
     * @param token token
     * @return response
     */
    protected Response httpGetMicroServices(String path, String token) {
        return httpLogin(token).request(Method.GET, path);
    }

    /**
     * http delete
     *
     * @param params params
     * @param path   endpoint
     * @return response
     */
    protected Response httpDelete(JSONObject params, String path) {
        return httpRequest(params).request(Method.DELETE, path);
    }

    /**
     * http put
     *
     * @param params params
     * @param path   endpoint
     * @return response
     */
    protected Response httpPut(JSONObject params, String path) {
        return httpRequest(params).request(Method.PUT, path);
    }

    /**
     * Get Status code
     *
     * @param response response
     * @return status code
     */
    protected int getStatusCode(Response response) {
        return response.getStatusCode();
    }

    /**
     * Get headers
     *
     * @param response response
     * @param header   header
     * @return header value
     */
    private String getHeaders(Response response, String header) {
        return response.getHeaders().getValue(header);
    }

    /**
     * Response Body
     *
     * @param response response
     * @return responseBody
     */
    private ResponseBody responseBody(Response response) {
        return response.getBody();
    }

    /**
     * Json schema validation
     *
     * @param response response
     * @return responseBody
     */
    private ValidatableResponse schemaValidation(Response response, String path) {
        return response.then().body(JsonSchemaValidator.matchesJsonSchema(path));
    }

    /**
     * Get Body
     *
     * @param response response
     * @return preety Print
     */
    protected String getBody(Response response) {
        return responseBody(response).prettyPrint();
    }

    /**
     * JsonPath evaluator
     *
     * @param response response
     * @return jsonPath
     */
    protected T jsonPathEvaluator(Response response, String exp) {
        return response.jsonPath().get(exp);
    }


    protected String sysDateFormat() {
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
