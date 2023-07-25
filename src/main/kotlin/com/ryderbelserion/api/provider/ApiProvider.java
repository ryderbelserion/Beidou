package com.ryderbelserion.api.provider;

import com.ryderbelserion.api.ModuleApplication;
import com.ryderbelserion.api.exceptions.ModuleInitializeFailure;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public final class ApiProvider {

    private static ModuleApplication instance = null;

    public static @NotNull ModuleApplication get() {
        ModuleApplication instance = ApiProvider.instance;
        if (instance == null) throw new ModuleInitializeFailure("Failed to bind to method.");

        return instance;
    }

    @ApiStatus.Internal
    public static void register(ModuleApplication instance) {
        ApiProvider.instance = instance;
    }

    @ApiStatus.Internal
    public static void unregister() {
        ApiProvider.instance = null;
    }
}