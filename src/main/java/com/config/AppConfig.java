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
package com.config;

import com.typesafe.config.Config;
import lombok.Getter;

/**
 * @author Dipjyoti Metia
 */
@Getter
public class AppConfig {
    private final String applicationName;
    private final int appiumPort;
    private final int proxyPort;
    private final String appiumVersion;
    private final boolean debug;
    private final boolean deviceLogs;
    private final boolean networkLogs;
    private final boolean acceptInsecureCerts;
    private final boolean appiumLogs;
    private final boolean video;
    private final String gpsLocation;
    private final boolean disableAnimations;

    public AppConfig(Config config) {
        this.applicationName = config.getString("application.name");
        this.appiumPort = config.getInt("appium.appiumport");
        this.proxyPort = config.getInt("appium.proxyport");
        this.appiumVersion = config.getString("browserStack.appium_version");
        this.debug = config.getBoolean("browserStack.debug");
        this.deviceLogs = config.getBoolean("browserStack.devicelogs");
        this.networkLogs = config.getBoolean("browserStack.networkLogs");
        this.acceptInsecureCerts = config.getBoolean("browserStack.acceptInsecureCerts");
        this.appiumLogs = config.getBoolean("browserStack.appiumLogs");
        this.video = config.getBoolean("browserStack.video");
        this.gpsLocation = config.getString("browserStack.gpsLocation");
        this.disableAnimations = config.getBoolean("browserStack.disableAnimations");
    }

}
