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
import org.testng.annotations.AfterTest;
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

    protected static final AppConfig appConfig = new AppConfig(ConfigFactory.load());
    private static final String nodeJS = System.getenv("NODE_HOME") + "/node.exe";
    private static final String appiumJS = System.getenv("APPIUM_HOME") + "/main.js";
    private static AppiumDriver _driver;
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
        return _driver;
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
                case "NEXUS":
                    log.info("Selected device is NEXUS");
                    if (apps.equals("Y")) {
                        _caps.setCapability(MobileCapabilityType.APP, app);
                    }
                    _caps.setCapability(MobileCapabilityType.UDID, NEXUS);
                    _caps.setCapability(MobileCapabilityType.DEVICE_NAME, "NEXUS");
                    _androidCapabilities(_caps);
                    _createService().start();
                    log.info("Argument to driver object : " + serverUrl);
                    _driver = new AndroidDriver(new URL(serverUrl), _caps);
                    break;
                case "PIXEL":
                    log.info("Selected device is PIXEL");
                    if (apps.equals("Y")) {
                        _caps.setCapability(MobileCapabilityType.APP, app);
                    }
                    _caps.setCapability(MobileCapabilityType.UDID, PIXEL);
                    _caps.setCapability(MobileCapabilityType.DEVICE_NAME, "PIXEL");
                    _androidCapabilities(_caps);
                    _createService().start();
                    log.info("Argument to driver object : " + serverUrl);
                    _driver = new AndroidDriver(new URL(serverUrl), _caps);
                    break;
                case "samsung":
                    log.info("Selected device is SAMSUNG");
                    if (apps.equals("Y")) {
                        _caps.setCapability(MobileCapabilityType.APP, app);
                    }
                    _browserstackCapabilities(_caps, "samsung");
                    _androidCapabilities(_caps);
                    log.info("Argument to driver object : " + cloudURL);
                    _driver = new AndroidDriver(new URL(cloudURL), _caps);
                    break;
                case "iPhone12":
                    log.info("Selected device is IPHONE");
                    if (apps.equals("Y")) {
                        _caps.setCapability(MobileCapabilityType.APP, app);
                    }
                    _browserstackCapabilities(_caps, "iPhone12");
                    _iosCapabilities(_caps);
                    log.info("Argument to driver object : " + cloudURL);
                    _driver = new IOSDriver(new URL(cloudURL), _caps);
                    break;
                case "IPHONE":
                    log.info("Selected device is IPHONE");
                    if (apps.equals("Y")) {
                        _caps.setCapability(MobileCapabilityType.APP, app);
                    }
                    _caps.setCapability(MobileCapabilityType.UDID, "iphone");
                    _caps.setCapability(MobileCapabilityType.DEVICE_NAME, "iphone");
                    _iosCapabilities(_caps);
                    _createService().start();
                    log.info("Argument to driver object : " + serverUrl);
                    _driver = new IOSDriver(new URL(serverUrl), _caps);
                    break;
                case "WEB":
                    log.info("Selected device is WEB");
                    _caps.setCapability(MobileCapabilityType.UDID, NEXUS);
                    _caps.setCapability(MobileCapabilityType.DEVICE_NAME, "NEXUS");
                    _createService().start();
                    _browserCapabilities(_caps, "chrome");
                    log.info("Argument to driver object : " + serverUrl);
                    _driver = new AndroidDriver(new URL(serverUrl), _caps);
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
        switch (device) {
            case "samsung":
                _caps.setCapability("os_version", "10.0");
                _caps.setCapability("device", "Samsung Galaxy S20");
                _caps.setCapability("app", apk_url);
                break;
            case "pixel":
                _caps.setCapability("os_version", "9.0");
                _caps.setCapability("device", "Google Pixel 3");
                _caps.setCapability("app", apk_url);
                break;
            case "iPhone12":
                _caps.setCapability("os_version", "14");
                _caps.setCapability("device", "iPhone 12");
                _caps.setCapability("app", ipa_url);
                break;
            default:
                System.out.println("No device found");
                break;
        }
        _caps.setCapability("browserstack.appium_version", appConfig.getAppiumVersion());
        _caps.setCapability("project", appConfig.getApplicationName());
        _caps.setCapability("build", testName + sysTime());
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
        _caps.setCapability(AndroidMobileCapabilityType.ANDROID_INSTALL_TIMEOUT, 60);
        _caps.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.swaglabsmobileapp");
        // _caps.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "com.swaglabsmobileapp.MainActivity");
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
        _caps.setCapability(MobileCapabilityType.NO_RESET, true);
        // _caps.setCapability(IOSMobileCapabilityType.XCODE_ORG_ID, "");
        // _caps.setCapability(IOSMobileCapabilityType.XCODE_SIGNING_ID, "");
        // _caps.setCapability(IOSMobileCapabilityType.UPDATE_WDA_BUNDLEID, "");
        _caps.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.saucelabs.SwagLabsMobileApp");
        _caps.setCapability(IOSMobileCapabilityType.APP_NAME, "com.saucelabs.SwagLabsMobileApp");
    }

    /**
     * Add Browser capability
     *
     * @param _caps   capabilities
     * @param browser browser
     */
    private void _browserCapabilities(DesiredCapabilities _caps, String browser) {
        if (browser.contains("chrome")) {
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
