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

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Dipjyoti Metia
 */
@Slf4j
public class ADB {

    private String SDK_PATH = System.getenv("ANDROID_HOME");
    private String ADB_PATH = SDK_PATH + "platform-tools" + File.separator + "adb";
    private String EMULATOR_PATH = SDK_PATH + File.separator + "emulator";

    private String ID;

    public ADB(String deviceID) {
        ID = deviceID;
    }

    /**
     * launch android emulator
     *
     * @param avdName emulator name
     */
    public void launchEmulator(String avdName) {
        log.info("Starting emulator for" + avdName + "....");
        String[] aCommand = new String[]{EMULATOR_PATH, "-avd", avdName};
        try {
            Process process = new ProcessBuilder(aCommand).start();
            process.waitFor(180, TimeUnit.SECONDS);
            log.info("Emulator launched successfully");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Close android emulator
     */
    public void closeEmulator() {
        log.info("Closing emulator");
        String[] aCommand = new String[]{EMULATOR_PATH, "emu", "kill"};
        try {
            Process process = new ProcessBuilder(aCommand).start();
            process.waitFor(1, TimeUnit.SECONDS);
            log.info("Emulator closed successfully");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * avd commands
     *
     * @param command command
     * @return output
     */
    public String command(String command) {
        log.debug("Formatting ADB Command: " + command);
        if (command.startsWith("adb"))
            command = command.replace("adb ", ServerManager.getAndroidHome() + "/platform-tools/adb ");
        else throw new RuntimeException("This method is designed to run ADB commands only!");
        log.debug("Formatted ADB Command: " + command);
        String output = ServerManager.runCommand(command);
        log.debug("Output of the ADB Command: " + output);
        if (output == null) return "";
        else return output.trim();
    }

    public void killServer() {
        command("adb kill-server");
    }

    public void startServer() {
        command("adb start-server");
    }

    /**
     * Get list of connected device
     *
     * @return devices
     */
    public List<Object> getConnectedDevices() {
        List<Object> devices = new ArrayList<>();
        String output = command("adb devices");
        for (String line : output.split("\n")) {
            line = line.trim();
            if (line.endsWith("device")) devices.add(line.replace("device", "").trim());
        }
        return devices;
    }

    public String getForegroundActivity() {
        return command("adb -s " + ID + " shell dumpsys window windows | grep mCurrentFocus");
    }

    public String getAndroidVersionAsString() {
        String output = command("adb -s " + ID + " shell getprop ro.build.version.release");
        if (output.length() == 3) output += ".0";
        return output;
    }

    public int getAndroidVersion() {
        return Integer.parseInt(getAndroidVersionAsString().replaceAll("\\.", ""));
    }

    public List<Object> getInstalledPackages() {
        List<Object> packages = new ArrayList<>();
        String[] output = command("adb -s " + ID + " shell pm list packages").split("\n");
        for (String packageID : output) packages.add(packageID.replace("package:", "").trim());
        return packages;
    }

    public void openAppsActivity(String packageID, String activityID) {
        command("adb -s " + ID + " shell am start -c api.android.intent.category.LAUNCHER -a api.android.intent.action.MAIN -n " + packageID + "/" + activityID);
    }

    public void clearAppsData(String packageID) {
        command("adb -s " + ID + " shell pm clear " + packageID);
    }

    public void forceStopApp(String packageID) {
        command("adb -s " + ID + " shell am force-stop " + packageID);
    }

    public void installApp(String apkPath) {
        command("adb -s " + ID + " install " + apkPath);
    }

    public void uninstallApp(String packageID) {
        command("adb -s " + ID + " uninstall " + packageID);
    }

    public void clearLogBuffer() {
        command("adb -s " + ID + " shell -c");
    }

    public void pushFile(String source, String target) {
        command("adb -s " + ID + " push " + source + " " + target);
    }

    public void pullFile(String source, String target) {
        command("adb -s " + ID + " pull " + source + " " + target);
    }

    public void deleteFile(String target) {
        command("adb -s " + ID + " shell rm " + target);
    }

    public void moveFile(String source, String target) {
        command("adb -s " + ID + " shell mv " + source + " " + target);
    }

    public void takeScreenshot(String target) {
        command("adb -s " + ID + " shell screencap " + target);
    }

    public void rebootDevice() {
        command("adb -s " + ID + " reboot");
    }

    public String getDeviceModel() {
        return command("adb -s " + ID + " shell getprop ro.product.model");
    }

    public String getDeviceSerialNumber() {
        return command("adb -s " + ID + " shell getprop ro.serialno");
    }

    public String getDeviceCarrier() {
        return command("adb -s " + ID + " shell getprop gsm.operator.alpha");
    }

    /**
     * Get log process
     *
     * @return process
     */
    public List<Object> getLogcatProcesses() {
        String[] output = command("adb -s " + ID + " shell top -n 1 | grep -i 'logcat'").split("\n");
        List<Object> processes = new ArrayList<>();
        for (String line : output) {
            processes.add(line.split(" ")[0]);
            processes.removeAll(Arrays.asList("", null));
        }
        return processes;
    }
}
