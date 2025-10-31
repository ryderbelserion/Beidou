package com.badbones69.weebs;

import com.ryderbelserion.fusion.addons.interfaces.IAddon;

public class WeebsAddon extends IAddon {

    @Override
    public void onEnable() {
        getLogger().info("Guten Tag!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Guten Nacht!");
    }
}