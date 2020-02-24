package com.TestDefinitionLayer;

import com.core.UserActions;
import com.pages.HomePage;
import com.reporting.ExtentReports.ExtentTestManager;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;

public class TC_Test extends UserActions {

    @Link("")
    @Feature("test")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "")
    public void E2E_MTR_638() {

        String Tname = "TC_Test";

        HomePage homePage = new HomePage();

        ExtentTestManager.getTest().setDescription("");

        try {
            homePage.searchDestination()
                    .selectDate()
                    .search();
            CreateImageDoc(Tname);
        } catch (Exception e) {
            catchBlock(e);
        } finally {
            ExtentTestManager.endTest();
        }

    }
}
