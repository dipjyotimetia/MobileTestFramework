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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

public class Issue {

    private static final Logger logger = LogManager.getLogger();
    private final String issueKey; // Jira Key e.g. KT-123

    public Issue(String issue) {
        this.issueKey = issue;
    }

    /**
     * Create and post a JSON request to JIRA to get issues.
     *
     * @param type         name of issue
     * @param inwardIssue  inward issue key
     * @param outwardIssue outward issue key
     */
    public void linkIssues(String type, String inwardIssue, String outwardIssue) {
        JSONObject obj = new JSONObject();
        JSONObject typeObj = new JSONObject();
        JSONObject inwardIssueObj = new JSONObject();
        JSONObject outwardIssueObj = new JSONObject();

        try {
            obj.put("type", typeObj);
            typeObj.put("name", type);
            obj.put("inwardIssue", inwardIssueObj);
            inwardIssueObj.put("key", inwardIssue);
            obj.put("outwardIssue", outwardIssueObj);
            outwardIssueObj.put("key", outwardIssue);
        } catch (JSONException e) {
            logger.error("Can't create JSON Object for linkIssues", e);
        }

        JiraConfig.getJIRARequestSpec()
                .contentType("application/json")
                .body(obj.toString())
                .when()
                .post(JiraConfig.JIRA_REST_PATH + "issueLink");
    }

    /**
     * Returns list of attachment IDs.
     */
    public List<String> getAttachmentIds() {

        return JiraConfig.getJIRARequestSpec()
                .when()
                .get(JiraConfig.JIRA_REST_PATH + "issue/" + issueKey)
                .thenReturn().jsonPath()
                .getList("fields.attachment.id");
    }

    /**
     * Adds the file attachment to the JIRA issue.
     */
    public void addAttachment(File attachment) {
        String attachmentPath = String.format("issue/%s/attachments", issueKey);

        JiraConfig.getJIRARequestSpec()
                .header("X-Atlassian-Token", "nocheck")
                .multiPart(attachment).and()
                .when()
                .post(JiraConfig.JIRA_REST_PATH + attachmentPath)
                .thenReturn().statusLine();
    }
}