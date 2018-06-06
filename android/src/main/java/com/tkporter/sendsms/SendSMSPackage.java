package com.tkporter.sendsms;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SendSMSPackage implements ReactPackage {

    private SendSMSModule sendSms = null;

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactApplicationContext) {
        sendSms = new SendSMSModule(reactApplicationContext);
        return Arrays.<NativeModule>asList(sendSms);
    }

    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
