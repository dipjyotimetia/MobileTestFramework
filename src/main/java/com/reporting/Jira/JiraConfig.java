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
package com.reporting.Jira;

import com.Common.Property;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class JiraConfig {
    public static final String JIRA_REST_PATH = "/rest/api/latest/";
    public static final String REST_ZAPI_PATH = "/rest/zapi/latest/";

    private JiraConfig() {
        // hide default constructor for this util class
    }

    /**
     * Basic request to send to JIRA and authenticate successfully.
     */
    public static RequestSpecification getJIRARequestSpec() {
        return RestAssured.given()
                .baseUri(Property.JIRA_URL.getValue())
                .relaxedHTTPSValidation()
                .auth().preemptive().basic(
                        Property.JIRA_USERNAME.getValue(),
                        Property.JIRA_PASSWORD.getValue());
    }

    /**
     * These should correspond to your ZAPI result IDs and
     * are only used if logging to Zephyr for JIRA.
     */
    public static class ZapiStatus {

        public static final int ZAPI_STATUS_PASS = 1;
        public static final int ZAPI_STATUS_FAIL = 2;
        public static final int ZAPI_STATUS_WIP = 3;
        public static final int ZAPI_STATUS_BLOCKED = 4;
    }

    /**
     * These should correspond to your field options
     * if logging a test result to a field.
     */
    public static class JiraFieldStatus {
        public static final String JIRA_STATUS_PASS = "Pass";
        public static final String JIRA_STATUS_FAIL = "Fail";
        public static final String JIRA_STATUS_WIP = "WIP";
        public static final String JIRA_STATUS_BLOCKED = "Blocked";
    }

    /**
     * These should correspond to the workflow transition names required to mark
     * the result if using a customised jira issue type & workflow to manage
     * tests NB - put all required transitions to get between statuses
     * (e.g. restart, then mark result) - each will be tried & ignored if not possible
     */
    public static class JiraTransition {
        public static final String[] JIRA_TRANSITION_PASS = {"Done"};
        public static final String[] JIRA_TRANSITION_FAIL = {"Done"};
        public static final String[] JIRA_TRANSITION_WIP = {"Reopen", "Start Progress"};
        public static final String[] JIRA_TRANSITION_BLOCKED = {"Done"};
    }

}
