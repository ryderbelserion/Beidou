package com.ryderbelserion.tebex;

import com.ryderbelserion.fusion.addons.api.Extension;
import com.ryderbelserion.tebex.config.ConfigManager;

public class TebexAddon extends Extension {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        getLogger().warn("Guten Tag!");

        this.configManager = new ConfigManager(getDataDirectory());
        this.configManager.init();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onReload() {
        this.configManager.reload();
    }
}