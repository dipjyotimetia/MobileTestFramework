## Mobile Test Automation

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6aaf27fdb62e4792ba5a3a9841ce13ee)](https://www.codacy.com/app/dipjyotimetia/MobileTestFramework?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=TestautoDev/MobileTestFramework&amp;utm_campaign=Badge_Grade)
![CI](https://github.com/dipjyotimetia/MobileTestFramework/workflows/CI/badge.svg)
### Introduction:
The main purpose of this framework to add automation capability to mobile applications

<img src="https://github.com/dipjyotimetia/MobileTestFramework/blob/master/docs/FrameworkArchitecture.png" width="700">

### Supported Platforms  
Appium Studio supports app automation across a variety of platforms, like iOS,Android, from Windows platform. Each platform is supported by one or more "drivers",
which know how to automate that particular platform. Choose a driver below for specific information about how that driver works and how to set it up:

* Android
    * The [UiAutomator2 Driver](/docs/en/drivers/android-uiautomator2.md)
* IOS
    * The [XCUITest](http://appium.io/docs/en/drivers/ios-xcuitest/)  
    
### Why [Appium](http://appium.io/docs/en/about-appium/intro/)?
* Appium is an Open source automation tool used for cross platform testing like native, hybrid and web applications on both the platforms IOS and Android.
  Its capability for testing all kinds of tools under one platform, makes it a multipurpose and convenient testing tool.Appium is called as a cross platform
  testing tool because it uses JSON wire protocol internally to interact with native apps of IOS and Android using Selenium Webdriver.

### Setup & Tools
* Download and install [Nodejs](https://nodejs.org/en/download/)   
  ``
  npm install -g appium
  ``  
  ``
  npm install -g appium-doctor
  ``
  verify all appium dependencies  
* [Download Appium Desktop](https://github.com/appium/appium-desktop/releases) download latest release
* [Install InteliJ Community Edition](https://www.jetbrains.com/idea/download/)
* [Java JDK_8](https://docs.aws.amazon.com/corretto/latest/corretto-8-ug/downloads-list.html) install jdk_8 version
* [Gradle](https://gradle.org/next-steps/?version=6.7.1&format=bin)
* [Allure](https://github.com/allure-framework/allure2/archive/2.13.8.zip)
* Set Environment variables
```shell
  * JAVA_HOME: Pointing to the Java SDK folder\bin
  * GRADLE_HOME: Pointing to Gradle directory\bin.
  * ALLURE_HOME: Pointing to allure directory\bin.
  * APPIUM_HOME: Pointing appium main.js from global location.
  * NODE_HOME: Pointing nodejs installation.
```
* For more details navigate to the above [Wiki Page](https://github.com/dipjyotimetia/MobileTestFramework/wiki)

### Connect - Local Devices:
Connect an Android and an iOS Device using a USB cable to your PC
 - Follow documentation for device connection

### Getting Started
```shell
$ git clone 
$ cd 
$ import project from intelij as a gradle project
$ gradle clean
$ gradle build
$ gradle task E2E
$ allureServe
```
### Execution gif
![grab-landing-page](https://github.com/dipjyotimetia/MobileTestFramework/blob/master/docs/gif/videogif.gif)

### Write your first user journey
Create new class and name as the TC00*_E2E_TEST-***
 - Provide jira link in @Link
 - Provide all the api components as @Feature
 - Provide test severity and description
 - Write test
 - Use CatchBlock in try/catch section
