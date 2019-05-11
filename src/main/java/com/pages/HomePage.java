package com.pages;

import com.core.UserActions;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends UserActions {

    @AndroidFindBy(id = "com.booking:id/search_details_text")
    @iOSXCUITBy(xpath = "")
    private MobileElement destination;

    @AndroidFindBy(id = "com.booking:id/disam_search")
    private MobileElement search;

    @AndroidFindBy(xpath = "(//android.widget.TextView[contains(@text,'Paris')])[1]")
    private MobileElement select;

    @AndroidFindBy(id = "com.booking:id/calendar_confirm")
    private MobileElement selectDate;

    @AndroidFindBy(id = "com.booking:id/search_search")
    private MobileElement searchButton;

    public HomePage() {
        super();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public HomePage searchDestination() {
        waitForElement(destination);
        click(destination);
        enter(search, "Paris");
        click(select);
        return this;
    }

    public HomePage selectDate() {
        click(selectDate);
        return this;
    }

    public void search() {
        click(searchButton);
    }

}
