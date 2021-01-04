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

import io.restassured.path.json.JsonPath;

import java.util.List;

public class SearchIssues {

    private static final int MAX_SEARCH_RESULTS = 1000;
    private final JsonPath jsonPath;

    /**
     * Search all issues that match a string.
     */
    public SearchIssues(String query) {
        try {
            jsonPath = JiraConfig.getJIRARequestSpec()
                    .param("jql", query)
                    .param("maxResults", MAX_SEARCH_RESULTS)
                    .when()
                    .get(JiraConfig.JIRA_REST_PATH + "search")
                    .thenReturn().jsonPath();
        } catch (Exception e) {
            throw new IllegalArgumentException("Problem with JIRA or JQL.", e);
        }
        if (jsonPath == null || jsonPath.getList("issues") == null) {
            throw new IllegalStateException(
                    String.format("No JIRA issues returned by specified JQL '%s'", query));
        }
    }

    public List<String> getKeys() {
        return jsonPath.getList("issues.key");
    }

    public List<String> getSummaries() {
        return jsonPath.getList("issues.fields.summary");
    }

    public String getKeyForSummary(final String summary) {
        return jsonPath.getString(
                String.format("issues.find {it.fields.summary == '%s'}.key", summary));
    }
}