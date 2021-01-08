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
package com.pages;

import com.core.Constants;
import com.core.UserActions;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITBy;
import org.openqa.selenium.support.PageFactory;

/**
 * @author Dipjyoti Metia
 */
public class HomePage extends UserActions implements Constants {

    @AndroidFindBy(id = "com.booking:id/search_details_text")
    @iOSXCUITBy(xpath = "")
    private MobileElement destination;

    @AndroidFindBy(id = "com.booking:id/disam_search")
    private MobileElement search;

    @AndroidFindBy(id = "com.booking:id/bt_accept")
    private MobileElement acceptCookie;

    @AndroidFindBy(xpath = "//android.widget.ImageButton[@content-desc=\"Navigate up\"]")
    private MobileElement closeButton;

    @AndroidFindBy(id = "com.booking:id/disambiguation_search_edittext")
    private MobileElement searchEdit;

    @AndroidFindBy(id = "com.booking:id/button_positive")
    private MobileElement gotIt;

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
        click(acceptCookie); // comment for local tests
        click(closeButton); // comment for local tests
        waitForElement(destination);
        click(destination);
        enter(searchEdit, "Paris");
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
