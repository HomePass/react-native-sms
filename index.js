//@flow
"use strict";

import { NativeModules, PermissionsAndroid, Platform } from "react-native";

async function send(
  options: Object,
  callback: (boolean, boolean, ?Error) => void
) {
  if (Platform.OS === "android") {
    try {
      /**
       * We had a user using a Nokia phone requiring READ_PHONE_STATE permission
       * This permission is not needed based on Android's Documentation
       *
       * primary permissions checks are only done on SMS because
       * READ_PHONE_STATE returns inconsistent results as most phone doesnt require
       * the permission and is often ignored
       **/

      await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.READ_PHONE_STATE
      );

      const smsPermissionsGranted = await PermissionsAndroid.check(
        PermissionsAndroid.PERMISSIONS.SEND_SMS
      );
      if (!smsPermissionsGranted) {
        const request = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.SEND_SMS
        );
        if (request !== PermissionsAndroid.RESULTS.GRANTED) {
          callback(false, true);
          return;
        }
      }
    } catch (error) {
      callback(false, false, error);
      return;
    }
  }

  NativeModules.SendSMS.send(options, callback);
}

let SendSMS = {
  send
};

module.exports = SendSMS;
