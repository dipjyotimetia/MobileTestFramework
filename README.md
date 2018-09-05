## Mobile Test Automation

### Introduction:
The main purpose of this framework to add automation capability to mobile applications

### Supported Platforms

AppiumStudio supports app automation across a variety of platforms, like iOS,Android, from Windows platform. Each platform is supported by one or more "drivers",
which know how to automate that particular platform. Choose a driver below for specific information about how that driver works and how to set it up:

* Android
    * The [UiAutomator2 Driver](/docs/en/drivers/android-uiautomator2.md)

### Why AppiumStudio?
* Appium Studio is an IDE designed for mobile test automation development and
  execution using the Appium/Selenium WebDriver API.

* It allows you to start developing on Android\iOS devices using advanced features such as
  Device\Application management & provisioning, Advanced Element detection (Object Spy) and an embedded recorder.

* The tool eliminates a large majority of Appium's rigid dependencies model and prerequisites such as the requirement to
  develop iOS tests on MAC OSX machines or the inability to run tests in parallel on real/simulated iOS Devices.

### Setup
* download AppiumStudio Community Edition
  http://experitest.com/mobile-test-automation/appium-studio/
* Install InteliJ Community Edition
  https://www.jetbrains.com/idea/download/
* Java SDK  
  http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html or any latest version.
* Gradle
  https://gradle.org/next-steps/?version=4.7&format=bin
* Allure
  https://github.com/allure-framework/allure2/archive/2.4.1.zip
* JAVA_HOME: Pointing to the Java SDK folder\bin
* GRADLE_HOME: Pointing to Gradle directory.
* ALLURE_HOME: Pointing to allure directory

### Connect - Local Devices:
Connect an Android and an iOS Device using a USB cable to your PC
 - Follow documentation for device connection
 - https://docs.experitest.com/display/AS/Android+-+Build+your+first+test
 - https://docs.experitest.com/display/AS/iOS+-+Build+your+first+test

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
Create new class and name as the TC00*_E2E_MTR-***
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