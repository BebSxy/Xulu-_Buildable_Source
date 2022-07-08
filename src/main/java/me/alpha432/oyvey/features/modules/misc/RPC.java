/*
 * Decompiled with CFR 0.150.
 */
package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.DiscordPresence;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;

public class RPC
extends Module {
    public static RPC INSTANCE;
    public Setting<Boolean> showIP = this.register(new Setting<Boolean>("showip", Boolean.valueOf(true), "Professional IP grabber."));
    public Setting<String> state = this.register(new Setting<String>("state", "Xulu+ v1.0.0", "Sets the state of the DiscordRPC."));

    public RPC() {
        super("rpc", "Discord rich presence", Module.Category.MISC, false, false, false);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        DiscordPresence.start();
    }

    @Override
    public void onDisable() {
        DiscordPresence.shutdown();
    }
}

