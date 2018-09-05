package com.core;

import io.appium.java_client.android.AndroidDriver;

public class DriverManager extends AppiumController {

    public AndroidDriver driver;

    public DriverManager() {
        this.driver = super.getDriver();
    }

}
