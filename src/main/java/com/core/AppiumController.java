package com.core;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


public class AppiumController implements Access {

    private DesiredCapabilities _caps = new DesiredCapabilities();
    private static WebDriver _driver = null;
    private Logger logger = LogManager.getLogger(AppiumController.class);
    private String appiumPort = "4723";
    private static BrowserMobProxy server;
    //    private String serverIp = "127.0.0.1";    //Local
    //    private String serverIp = "172.23.126.97";  //Jenkins

    @Parameters({"device", "apk", "serverIp"})
    @BeforeClass
    public void setup(String device, String apk, String serverIp) {
        initDriver(device, apk, serverIp);
    }

    public WebDriver getDriver() {
        return _driver;
    }

    private void initDriver(String device, String apk, String serverIp) {
        try {
            File appDir = new File("AndroidApp");
            File app = new File(appDir, "***.apk");
            String serverUrl = "http://" + serverIp + ":" + appiumPort + "/wd/hub";
            if (device.equals("S8")) {
                logger.info("Selected device is S8");
                if (apk.equals("Y")) {
                    _caps.setCapability(MobileCapabilityType.APP, app);
                }
                _caps.setCapability(MobileCapabilityType.UDID, "9889d6324131325a34");
                _caps.setCapability(MobileCapabilityType.DEVICE_NAME, "9889d6324131325a34");
                _androidCapabilities(_caps);
                logger.info("Argument to driver object : " + serverUrl);
                _driver = new AndroidDriver<>(new URL(serverUrl), _caps);
            } else if (device.equals("Iphone8")) {
                logger.info("Selected device is iphone8");
                if (apk.equals("Y")) {
                    _caps.setCapability(MobileCapabilityType.APP, app);
                }
                _caps.setCapability(MobileCapabilityType.UDID, "1cb4062d0b037ece");
                _caps.setCapability(MobileCapabilityType.DEVICE_NAME, "1cb4062d0b037ece");
                _iosCapabilities(_caps);
                logger.info("Argument to driver object : " + serverUrl);
                _driver = new IOSDriver<>(new URL(serverUrl), _caps);
            }
        } catch (NullPointerException |
                MalformedURLException ex) {
            logger.error("Appium driver could not be initialised for device", ex);
            throw new RuntimeException("Appium driver could not be initialised for device: " + device);
        }
        logger.info("Driver initialized");

    }

    private void _androidCapabilities(DesiredCapabilities _caps) {
        _caps.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        _caps.setCapability("report.disable", "true");
        _caps.setCapability(MobileCapabilityType.NO_RESET, true);
        _caps.setCapability("gpsEnabled", "true");
        _caps.setCapability(AndroidMobileCapabilityType.INTENT_ACTION, "android.intent.action.MAIN");
        _caps.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "au.com.test.uat");
        _caps.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "au.com.test.ui.SplashActivity");
    }

    private void _iosCapabilities(DesiredCapabilities _caps) {
        _caps.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
        _caps.setCapability("report.disable", "true");
        _caps.setCapability(MobileCapabilityType.NO_RESET, true);
        _caps.setCapability("gpsEnabled", "true");
        _caps.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "ios.intent.action.MAIN");
        _caps.setCapability(IOSMobileCapabilityType.APP_NAME, "");
    }

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

    @AfterClass
    public void tearDown() {
        _driver.quit();
    }

}
