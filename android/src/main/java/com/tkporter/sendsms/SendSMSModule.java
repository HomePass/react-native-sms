package com.tkporter.sendsms;

import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.text.TextUtils;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.Callback;

import java.util.ArrayList;

public class SendSMSModule extends ReactContextBaseJavaModule {
    private final ReactApplicationContext reactContext;

    public SendSMSModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "SendSMS";
    }

    @ReactMethod
    public void send(ReadableMap options, final Callback callback) {
        try {
            SmsManager manager = SmsManager.getDefault();
            String message = options.hasKey("body") ? options.getString("body") : "";
            ReadableArray recipients = options.hasKey("recipients") ? options.getArray("recipients") : null;

            if(!isSmsFeatureSupported(reactContext) || !isSmsMessageValid(message)) {
                callback.invoke(false, false, true);
                return;
            }

            ArrayList<String> messageParts = manager.divideMessage(message);

            //if recipients specified
            if (recipients != null) {
                for (int i = 0; i < recipients.size(); i++) {
                    String recipient = recipients.getString(i);
                    if(!TextUtils.isEmpty(recipient)) {
                        manager.sendMultipartTextMessage(recipient, null, messageParts, null, null);
                    }
                }
            }

            callback.invoke(true, false, false);
        } catch (UnsupportedOperationException e) {
            //error!
            callback.invoke(false, false, true);
            throw e;
        }
    }

    public boolean isSmsFeatureSupported(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
    }

    public boolean isSmsMessageValid(String message) {
        return !TextUtils.isEmpty(message);
    }

}
