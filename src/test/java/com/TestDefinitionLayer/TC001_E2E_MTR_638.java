package com.TestDefinitionLayer;

import com.core.MobileActions;
import com.pages.*;
import com.reporting.ExtentReports.ExtentTestManager;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;

public class TC001_E2E_MTR_638 extends MobileActions {

    @Link("")
    @Feature("test")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "")
    public void E2E_MTR_638() {

        String Tname = "TC001_E2E_MTR_638";

        HomePage homePage = new HomePage();

        ExtentTestManager.getTest().setDescription("");

        try {
            CreateImageDoc(Tname);
        } catch (Exception e) {
            catchBlock(e);
        } finally {
            ExtentTestManager.endTest();
        }

    }
}
