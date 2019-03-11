package com.pages;

import com.core.UserActions;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidBy;
import io.appium.java_client.pagefactory.iOSXCUITBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends UserActions {

    @AndroidBy(xpath = "")
    @iOSXCUITBy(xpath = "")
    private MobileElement username;

    public HomePage(){
        super();
        PageFactory.initElements(this.driver,HomePage.class);
    }

    public void Login(){
        click(username);

    }
}
