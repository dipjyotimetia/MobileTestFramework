package com.TestDefinitionLayer;

import com.core.WebActions;
import com.pages.HomePage;
import com.pages.LoginPage;
import com.reporting.ExtentReports.ExtentTestManager;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;

public class TC003_E2E_Web extends WebActions {

    @Link("")
    @Feature("test")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "")
    public void E2E_MTR_638() {

        String Tname = "TC001_E2E_MTR_638";

        LoginPage login = new LoginPage();

        ExtentTestManager.getTest().setDescription("");

        try {
            login.Login();
        } catch (Exception e) {

        } finally {
            ExtentTestManager.endTest();
        }

    }
}
