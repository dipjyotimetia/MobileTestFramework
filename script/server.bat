https://www.browserstack.com/guide/selenium-grid-4-tutorial

@REM Standalone
java -jar selenium-server-4.0.0-beta-4.jar standalone

@REM Hub and Node
java -jar selenium-server-4.0.0-beta-4.jar hub

java -jar selenium-server-4.0.0-alpha-7.jar node --detect-drivers true

@REM Distributed
java -jar selenium-server-4.0.0-beta-4.jar sessions

java -jar selenium-server-4.0.0-beta-4.jar distributor --sessions http://localhost:5556

java -jar selenium-server-4.0.0-beta-4.jar router --sessions http://localhost:5556 --distributor http://localhost:5553


@REM # Window 2: the iOS node
appium -p 4723 --nodeconfig /path/to/nodeconfig-ios.json

@REM # Window 3: the Android node
appium -p 4733 --nodeconfig /path/to/nodeconfig-android.json