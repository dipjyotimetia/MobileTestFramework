## Mobile Test Automation

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6aaf27fdb62e4792ba5a3a9841ce13ee)](https://www.codacy.com/app/dipjyotimetia/MobileTestFramework?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=TestautoDev/MobileTestFramework&amp;utm_campaign=Badge_Grade)

### Introduction:
The main purpose of this framework to add automation capability to mobile applications

<img src="https://github.com/TestautoDev/Tricks-And-Tips/blob/master/architecture.png" width="600">

### Supported Platforms  
Appium Studio supports app automation across a variety of platforms, like iOS,Android, from Windows platform. Each platform is supported by one or more "drivers",
which know how to automate that particular platform. Choose a driver below for specific information about how that driver works and how to set it up:

* Android
    * The [UiAutomator2 Driver](/docs/en/drivers/android-uiautomator2.md)
* IOS
    * The [XCUITest](http://appium.io/docs/en/drivers/ios-xcuitest/)  
    
### Why AppiumStudio?
* Appium Studio is an IDE designed for mobile test automation development and
  execution using the Appium/Selenium WebDriver API.

* It allows you to start developing on Android\iOS devices using advanced features such as
  Device\Application management & provisioning, Advanced Element detection (Object Spy) and an embedded recorder.

* The tool eliminates a large majority of Appium's rigid dependencies model and prerequisites such as the requirement to
  develop iOS tests on MAC OSX machines or the inability to run tests in parallel on real/simulated iOS Devices.

### Setup & Tools
* Download and install [Nodejs](https://nodejs.org/en/download/)   
  ``
  npm install -g appium
  ``  
  ``
  npm install -g appium-doctor
  ``
* Download Appium Studio Community Edition
  http://experitest.com/mobile-test-automation/appium-studio/
* Install InteliJ Community Edition
  https://www.jetbrains.com/idea/download/
* Java SDK  
  http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html or any latest version.
* Gradle
  https://gradle.org/next-steps/?version=4.7&format=bin
* Allure
  https://github.com/allure-framework/allure2/archive/2.4.1.zip    
* Set Environment variables      
    * JAVA_HOME: Pointing to the Java SDK folder\bin
    * GRADLE_HOME: Pointing to Gradle directory\bin.
    * ALLURE_HOME: Pointing to allure directory\bin.
    * APPIUM_HOME: Pointing appium main.js from global location.
    * NODE_HOME: Pointing nodejs installation.

### Connect - Local Devices:
Connect an Android and an iOS Device using a USB cable to your PC
 - Follow documentation for device connection

### Getting Started
```sh
$ git clone 
$ cd 
$ import project from intelij as a gradle project
$ gradle clean
$ gradle build
$ gradle task E2E
$ allureServe
```

### Write your first user journey
Create new class and name as the TC00*_E2E_TEST-***
 - Provide jira link in @Link
 - Provide all the api components as @Feature
 - Provide test severity and description
 - Write test
 - Use CatchBlock in try/catch section

Error Handle for dynamic classpath:
Search and modify the below line in .idea workspace.xml
<component name="PropertiesComponent">
    <property name="dynamic.classpath" value="true" />
</component>