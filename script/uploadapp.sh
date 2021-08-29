echo "**************** PUBLISH APP TO SAUCELABS WITH THIS DATA ******************"

#-F "file=@/path/to/app/file/app-debug.ipa"
echo "Enter the app name: "
# shellcheck disable=SC2162
read app
echo "The Current User Name is $app"

if [ "$app" == "android" ]; then
  curl -u "$SAUCE_USERNAME:$SAUCE_ACCESS_KEY" \
    -X POST "https://api-cloud.browserstack.com/app-automate/upload" \
    -F "url=https://github.com/saucelabs/sample-app-mobile/releases/download/2.7.1/Android.SauceLabs.Mobile.Sample.app.2.7.1.apk"
else
  curl -u "$SAUCE_USERNAME:$SAUCE_ACCESS_KEY" \
    -X POST "https://api-cloud.browserstack.com/app-automate/upload" \
    -F "url=https://github.com/saucelabs/sample-app-mobile/releases/download/2.7.1/iOS.RealDevice.SauceLabs.Mobile.Sample.app.2.7.1.ipa"
fi
