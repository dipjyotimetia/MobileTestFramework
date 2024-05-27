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

import com.config.AppConfig;
import com.constants.Arg;
import com.typesafe.config.ConfigFactory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.MobileBrowserType;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.apache.commons.exec.OS;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class AppiumController implements Access {

    protected static final AppConfig appConfig = new AppConfig(ConfigFactory.load());
    private static final String nodeJS = System.getenv("NODE_HOME") + "/node.exe";
    private static final String appiumJS = System.getenv("APPIUM_HOME") + "/main.js";
    private static RemoteWebDriver _driver;
    private static BrowserMobProxy server;
    private static DriverService service;
    private final String appiumPort = "4723";
    private final String username = System.getenv("BROWSERSTACK_USERNAME");
    private final String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
    private final String apk_url = System.getenv("APK_URL");
    private final String ipa_url = System.getenv("IPA_URL");
    private final String serverIp = "127.0.0.1";    //Local
    private final String cloudURL = "https://" + username + ":" + accessKey + "@hub.browserstack.com/wd/hub";
    DesiredCapabilities _caps = new DesiredCapabilities();
    private String testName = null;

    @Parameters({"device", "apps"})
    @BeforeClass
    public void setup(String device, String apk) throws Exception {
        testName = this.getClass().getName().substring(24);
        initDriver(device, apk);
    }

    public AppiumDriver getDriver() {
        return (AppiumDriver) _driver;
    }

    /**
     * Initialize Driver
     *
     * @param device device
     * @param apps   apk
     * @throws Exception exception
     */
    private void initDriver(String device, String apps) throws Exception {
        try {
            File appDir = new File("input/app");
            File app = new File(appDir, "***.apk");
            String serverUrl = "http://" + serverIp + ":" + appiumPort + "/wd/hub";
            switch (device) {
                case "NEXUS" -> {
                    log.info("Selected device is NEXUS");
                    if (apps.equals("Y")) {
                        _caps.setCapability(UiAutomator2Options.APP_OPTION, app);
                    }
                    _caps.setCapability(UiAutomator2Options.UDID_OPTION, NEXUS);
                    _caps.setCapability(UiAutomator2Options.DEVICE_NAME_OPTION, "NEXUS");
                    _androidCapabilities(_caps);
                    _createService().start();
                    log.info("Argument to driver object : " + serverUrl);
                    _driver = new AndroidDriver(new URL(serverUrl), _caps);
                }
                case "PIXEL" -> {
                    log.info("Selected device is PIXEL");
                    if (apps.equals("Y")) {
                        _caps.setCapability(UiAutomator2Options.APP_OPTION, app);
                    }
                    _caps.setCapability(UiAutomator2Options.UDID_OPTION, PIXEL);
                    _caps.setCapability(UiAutomator2Options.DEVICE_NAME_OPTION, "PIXEL");
                    _androidCapabilities(_caps);
                    _createService().start();
                    log.info("Argument to driver object : " + serverUrl);
                    _driver = new AndroidDriver(new URL(serverUrl), _caps);
                }
                case "samsung" -> {
                    log.info("Selected device is SAMSUNG");
                    if (apps.equals("Y")) {
                        _caps.setCapability(UiAutomator2Options.APP_OPTION, app);
                    }
                    _browserstackCapabilities(_caps, "samsung");
                    _androidCapabilities(_caps);
                    log.info("Argument to driver object : " + cloudURL);
                    _driver = new RemoteWebDriver(new URL(cloudURL), _caps);
                }
                case "iPhone14" -> {
                    log.info("Selected device is IPHONE");
                    if (apps.equals("Y")) {
                        _caps.setCapability(XCUITestOptions.APP_OPTION, app);
                    }
                    _browserstackCapabilities(_caps, "iPhone14");
                    _iosCapabilities(_caps);
                    log.info("Argument to driver object : " + cloudURL);
                    _driver = new RemoteWebDriver(new URL(cloudURL), _caps);
                }
                case "IPHONE" -> {
                    log.info("Selected device is IPHONE");
                    if (apps.equals("Y")) {
                        _caps.setCapability(XCUITestOptions.APP_OPTION, app);
                    }
                    _caps.setCapability(XCUITestOptions.UDID_OPTION, "iphone");
                    _caps.setCapability(XCUITestOptions.DEVICE_NAME_OPTION, "iphone");
                    _iosCapabilities(_caps);
                    _createService().start();
                    log.info("Argument to driver object : " + serverUrl);
                    _driver = new IOSDriver(new URL(serverUrl), _caps);
                }
                case "WEB" -> {
                    log.info("Selected device is WEB");
                    _caps.setCapability(UiAutomator2Options.UDID_OPTION, NEXUS);
                    _caps.setCapability(UiAutomator2Options.DEVICE_NAME_OPTION, "NEXUS");
                    _createService().start();
                    _browserCapabilities(_caps, "chrome");
                    log.info("Argument to driver object : " + serverUrl);
                    _driver = new AndroidDriver(new URL(serverUrl), _caps);
                }
            }
        } catch (Error |
                 Exception ex) {
            log.error("Appium driver could not be initialised for device", ex);
            throw new Exception("Appium driver could not be initialised for device: " + ex);
        }
        log.info("Driver initialized");
    }

    /**
     * BrowserStack capabilities
     *
     * @param _caps capabilities
     */
    private void _browserstackCapabilities(DesiredCapabilities _caps, String device) {
        switch (device) {
            case "samsung" -> {
                _caps.setCapability("platformName", "android");
                _caps.setCapability("os_version", "13.0");
                _caps.setCapability("device", "Samsung Galaxy S23");
                _caps.setCapability("app", apk_url);
            }
            case "pixel" -> {
                _caps.setCapability("os_version", "9.0");
                _caps.setCapability("device", "Google Pixel 3");
                _caps.setCapability("app", apk_url);
            }
            case "iPhone14" -> {
                _caps.setCapability("platformName", "ios");
                _caps.setCapability("os_version", "16");
                _caps.setCapability("device", "iPhone 14");
                _caps.setCapability("app", ipa_url);
            }
            default -> System.out.println("No device found");
        }
        _caps.setCapability("browserstack.appium_version", appConfig.getAppiumVersion());
        _caps.setCapability("project", appConfig.getApplicationName());
        _caps.setCapability("build", testName + sysTime());
        _caps.setCapability("name", testName);
        _caps.setCapability("isRealMobile", true);
    }

    /**
     * Android capabilities
     *
     * @param _caps capabilities
     */
    private void _androidCapabilities(DesiredCapabilities _caps) {
        _caps.setCapability("platformName", "android");
        _caps.setCapability("platformVersion", "13.0");
        _caps.setCapability(UiAutomator2Options.NO_RESET_OPTION, true);
        _caps.setCapability(UiAutomator2Options.FULL_RESET_OPTION, false);
        _caps.setCapability(UiAutomator2Options.AUTO_WEB_VIEW_OPTION, false);
        _caps.setCapability(UiAutomator2Options.AUTO_GRANT_PERMISSIONS_OPTION, true);
        _caps.setCapability(UiAutomator2Options.ANDROID_INSTALL_TIMEOUT_OPTION, 60);
        _caps.setCapability(UiAutomator2Options.APP_PACKAGE_OPTION, "com.swaglabsmobileapp");
//         _caps.setCapability(UiAutomator2Options.APP_ACTIVITY_OPTION, "com.swaglabsmobileapp.MainActivity");
    }

    /**
     * IOS capabilities
     *
     * @param _caps capabilities
     */
    private void _iosCapabilities(DesiredCapabilities _caps) {
        _caps.setCapability("platformName", "ios");
        _caps.setCapability("platformVersion", "16");
        _caps.setCapability(XCUITestOptions.FULL_RESET_OPTION, false);
        _caps.setCapability(XCUITestOptions.NO_RESET_OPTION, true);
//         _caps.setCapability(XCUITestOptions.XCODE_ORG_ID_OPTION, "");
//         _caps.setCapability(XCUITestOptions.XCODE_SIGNING_ID_OPTION, "");
//         _caps.setCapability(XCUITestOptions.UPDATED_WDA_BUNDLE_ID_OPTION, "");
        _caps.setCapability(XCUITestOptions.AUTO_DISMISS_ALERTS_OPTION, true);
        _caps.setCapability(XCUITestOptions.BUNDLE_ID_OPTION, "com.saucelabs.SwagLabsMobileApp");
        _caps.setCapability(XCUITestOptions.AUTOMATION_NAME_OPTION, "com.saucelabs.SwagLabsMobileApp");
    }

    /**
     * Add Browser capability
     *
     * @param _caps   capabilities
     * @param browser browser
     */
    private void _browserCapabilities(DesiredCapabilities _caps, String browser) {
        if (browser.contains("chrome")) {
            _caps.setCapability(UiAutomator2Options.BROWSER_NAME_OPTION, MobileBrowserType.CHROME);
            _caps.setCapability(UiAutomator2Options.PLATFORM_VERSION_OPTION, "126");
        } else {
            _caps.setCapability(XCUITestOptions.BROWSER_NAME_OPTION, MobileBrowserType.SAFARI);
            _caps.setCapability(XCUITestOptions.PLATFORM_VERSION_OPTION, "8.1");
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
        _caps.setCapability(CapabilityType.PROXY, proxy);
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

    @AfterTest
    public void tearDown() {
        try {
            // Har har = server.getHar();
            // FileOutputStream fos = new FileOutputStream("C:\\temp\\perf.har");
            // har.writeTo(fos);
            // server.stop();
            _driver.quit();
            _createService().stop();
            _stopAppiumServer();
        } catch (Exception e) {
            log.info("Performance test not included");
        }
    }
}
