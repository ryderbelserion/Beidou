package com.ryderbelserion.api.provider;

import com.ryderbelserion.api.ModuleApplication;
import java.lang.reflect.Method;

public class ApiRegistration {

    private static final Method register;
    private static final Method unregister;

    static {
        try {
            register = ApiProvider.class.getDeclaredMethod("register", ModuleApplication.class);
            register.setAccessible(true);

            unregister = ApiProvider.class.getDeclaredMethod("unregister");
            unregister.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void registerProvider(ModuleApplication moduleApplication) {
        try {
            register.invoke(null, moduleApplication);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unregisterProvider() {
        try {
            unregister.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}