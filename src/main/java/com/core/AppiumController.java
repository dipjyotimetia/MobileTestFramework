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
package com.core;

import com.constants.Arg;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.*;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.apache.commons.exec.OS;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class AppiumController implements Access {

    DesiredCapabilities _caps = new DesiredCapabilities();
    private static AppiumDriver _driver;
    private final String appiumPort = "4723";
    private static BrowserMobProxy server;
    private final String username = System.getenv("BROWSERSTACK_USERNAME");
    private final String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
    private static final String nodeJS = System.getenv("NODE_HOME") + "/node.exe";
    private static final String appiumJS = System.getenv("APPIUM_HOME") + "/main.js";
    private static DriverService service;
    private String testName = null;

    private final String serverIp = "127.0.0.1";    //Local
    private final String cloudURL = "https://" + username + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub";

    @Parameters({"device", "apk"})
    @BeforeClass
    public void setup(String device, String apk) throws Exception {
        testName = this.getClass().getName().substring(24);
        initDriver(device, apk);
    }

    public AppiumDriver getDriver() {
        return _driver;
    }

    /**
     * Initialize Driver
     *
     * @param device device
     * @param apk    apk
     * @throws Exception exception
     */
    private void initDriver(String device, String apk) throws Exception {
        try {
            File appDir = new File("input/app");
            File app = new File(appDir, "***.apk");
            String serverUrl = "http://" + serverIp + ":" + appiumPort + "/wd/hub";
            switch (device) {
                case "NEXUS":
                    log.info("Selected device is NEXUS");
                    if (apk.equals("Y")) {
                        _caps.setCapability(MobileCapabilityType.APP, app);
                    }
                    _caps.setCapability(MobileCapabilityType.UDID, NEXUS);
                    _caps.setCapability(MobileCapabilityType.DEVICE_NAME, "NEXUS");
                    _androidCapabilities(_caps);
                    _createService().start();
                    log.info("Argument to driver object : " + serverUrl);
                    _driver = new AndroidDriver<>(new URL(serverUrl), _caps);
                    break;
                case "PIXEL":
                    log.info("Selected device is PIXEL");
                    if (apk.equals("Y")) {
                        _caps.setCapability(MobileCapabilityType.APP, app);
                    }
                    _caps.setCapability(MobileCapabilityType.UDID, PIXEL);
                    _caps.setCapability(MobileCapabilityType.DEVICE_NAME, "PIXEL");
                    _androidCapabilities(_caps);
                    _createService().start();
                    log.info("Argument to driver object : " + serverUrl);
                    _driver = new AndroidDriver<>(new URL(serverUrl), _caps);
                    break;
                case "samsung":
                    log.info("Selected device is NEXUS");
                    if (apk.equals("Y")) {
                        _caps.setCapability(MobileCapabilityType.APP, app);
                    }
                    _browserstackCapabilities(_caps, "samsung");
                    _androidCapabilities(_caps);
                    log.info("Argument to driver object : " + cloudURL);
                    _driver = new AndroidDriver<>(new URL(cloudURL), _caps);
                    break;
                case "IPHONE":
                    log.info("Selected device is IPHONE");
                    if (apk.equals("Y")) {
                        _caps.setCapability(MobileCapabilityType.APP, app);
                    }
                    _caps.setCapability(MobileCapabilityType.UDID, "iphone");
                    _caps.setCapability(MobileCapabilityType.DEVICE_NAME, "iphone");
                    _iosCapabilities(_caps);
                    _createService().start();
                    log.info("Argument to driver object : " + serverUrl);
                    _driver = new IOSDriver<>(new URL(serverUrl), _caps);
                    break;
                case "WEB":
                    log.info("Selected device is WEB");
                    _caps.setCapability(MobileCapabilityType.UDID, NEXUS);
                    _caps.setCapability(MobileCapabilityType.DEVICE_NAME, "NEXUS");
                    _createService().start();
                    _browserCapabilities(_caps, "chrome");
                    log.info("Argument to driver object : " + serverUrl);
                    _driver = new AndroidDriver<>(new URL(serverUrl), _caps);
                    break;
            }
        } catch (NullPointerException |
                MalformedURLException ex) {
            log.error("Appium driver could not be initialised for device", ex);
            throw new RuntimeException("Appium driver could not be initialised for device: " + device);
        }
        log.info("Driver initialized");
    }

    /**
     * BrowserStack capabilities
     *
     * @param _caps capabilities
     */
    private void _browserstackCapabilities(DesiredCapabilities _caps, String device) {
        _caps.setCapability("app", "bs://3bfb9c9216290cc210b46fb6f64c6482a4a2a6b7");
        switch (device) {
            case "samsung":
                _caps.setCapability("os_version", "10.0");
                _caps.setCapability("device", "Samsung Galaxy S20");
                break;
            case "pixel":
                _caps.setCapability("os_version", "9.0");
                _caps.setCapability("device", "Google Pixel 3");
                break;
            default:
                System.out.println("No device found");
        }
        _caps.setCapability("browserstack.appium_version", "1.19.1");
        _caps.setCapability("project", "MobileTestFramework");
        _caps.setCapability("build", "AndroidTest");
        _caps.setCapability("name", testName);
    }

    /**
     * Android capabilities
     *
     * @param _caps capabilities
     */
    private void _androidCapabilities(DesiredCapabilities _caps) {
        _caps.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        _caps.setCapability(MobileCapabilityType.NO_RESET, true);
        _caps.setCapability(MobileCapabilityType.FULL_RESET, false);
        _caps.setCapability(MobileCapabilityType.AUTO_WEBVIEW, false);
        _caps.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
        _caps.setCapability(AndroidMobileCapabilityType.APPLICATION_NAME, "UiAutomator2");
        _caps.setCapability(AndroidMobileCapabilityType.ANDROID_INSTALL_TIMEOUT, 60);
        _caps.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.booking");
        _caps.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".startup.HomeActivity");
    }

    /**
     * IOS capabilities
     *
     * @param _caps capabilities
     */
    private void _iosCapabilities(DesiredCapabilities _caps) {
        _caps.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
        _caps.setCapability(MobileCapabilityType.FULL_RESET, false);
        _caps.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
        _caps.setCapability(AndroidMobileCapabilityType.APPLICATION_NAME, "XCUITest");
        _caps.setCapability(MobileCapabilityType.NO_RESET, true);
        _caps.setCapability(IOSMobileCapabilityType.XCODE_ORG_ID, "");
        _caps.setCapability(IOSMobileCapabilityType.XCODE_SIGNING_ID, "");
        _caps.setCapability(IOSMobileCapabilityType.UPDATE_WDA_BUNDLEID, "");
        _caps.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "ios.intent.action.MAIN");
        _caps.setCapability(IOSMobileCapabilityType.APP_NAME, "");
    }

    /**
     * Add Browser capability
     *
     * @param _caps   capabilities
     * @param browser browser
     */
    private void _browserCapabilities(DesiredCapabilities _caps, String browser) {
        if (browser.contains("chrome")) {
            _caps.setCapability(AndroidMobileCapabilityType.APPLICATION_NAME, "UiAutomator2");
            _caps.setCapability(MobileCapabilityType.BROWSER_NAME, MobileBrowserType.CHROME);
            _caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, "69");
        } else {
            _caps.setCapability(MobileCapabilityType.BROWSER_NAME, MobileBrowserType.SAFARI);
            _caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, "8.1");
        }
    }

    /**
     * Add Performance capability
     *
     * @param _caps capability
     */
    private void _performanceCapability(DesiredCapabilities _caps) {
        server = new BrowserMobProxyServer();
        server.setTrustAllServers(true);
        server.start(PROXY_Port);
        server.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
        server.enableHarCaptureTypes(CaptureType.REQUEST_HEADERS, CaptureType.RESPONSE_HEADERS);
        Proxy proxy = ClientUtil.createSeleniumProxy(server);
        _caps.setCapability(MobileCapabilityType.PROXY, proxy);
        server.newHar("appiumPerf.har");
    }

    /**
     * Create appium driver service
     *
     * @return service
     */
    private DriverService _createService() {
        service = new AppiumServiceBuilder()
                .usingDriverExecutable(new File(nodeJS))
                .withAppiumJS(new File(appiumJS))
                .withIPAddress(serverIp)
                .usingPort(APPIUM_Port)
                .withArgument(Arg.TIMEOUT, "120")
                .withArgument(Arg.LOG_LEVEL, "warn")
                .build();
        return service;
    }

    /**
     * Stop appium server
     */
    public void _stopAppiumServer() {
        if (OS.isFamilyWindows()) {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("taskkill /F /IM node.exe");
                runtime.exec("taskkill /F /IM cmd.exe");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return sysDateTime
     */
    private String sysTime() {
        try {
            DateFormat dates = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = new Date();
            return dates.format(date);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @AfterClass
    public void tearDown() {
        try {
//            Har har = server.getHar();
//            FileOutputStream fos = new FileOutputStream("C:\\temp\\perf.har");
//            har.writeTo(fos);
//            server.stop();
            _driver.quit();
            _createService().stop();
            _stopAppiumServer();
        } catch (Exception e) {
            log.info("Performance test not included");
        }
    }
}
