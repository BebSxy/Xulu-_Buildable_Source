//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiMainMenu
 */
package me.alpha432.oyvey;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.alpha432.oyvey.features.modules.misc.RPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;

public class DiscordPresence {
    public static DiscordRichPresence presence;
    private static final DiscordRPC rpc;
    private static Thread thread;

    public static void start() {
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        rpc.Discord_Initialize("879361669250318389", handlers, true, "");
        DiscordPresence.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        DiscordPresence.presence.details = Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu ? "Vibin As Always" : "Smokin " + (Minecraft.getMinecraft().getCurrentServerData() != null ? (RPC.INSTANCE.showIP.getValue().booleanValue() ? "NoNames ON  " + Minecraft.getMinecraft().getCurrentServerData().serverIP + "." : " MultiSwagger.") : "SadNiggaHours.");
        DiscordPresence.presence.state = "Starting up...";
        DiscordPresence.presence.largeImageKey = "aa";
        DiscordPresence.presence.largeImageText = "Xulu+ v1.0.5";
        rpc.Discord_UpdatePresence(presence);
        thread = new Thread(() -> {
            StringBuilder sb = new StringBuilder();
            while (!Thread.currentThread().isInterrupted()) {
                String ip;
                rpc.Discord_RunCallbacks();
                String string = "";
                sb.setLength(0);
                sb.append("Owning ");
                string = Minecraft.getMinecraft().getCurrentServerData() != null ? (!(ip = Minecraft.getMinecraft().getCurrentServerData().serverIP).isEmpty() ? "Fags On " + ip : " BigBoyMode") : " SadNiggaHours";
                DiscordPresence.presence.details = sb.append(string).toString();
                DiscordPresence.presence.state = "FixedByZocker_160 (^;";
                rpc.Discord_UpdatePresence(presence);
                try {
                    Thread.sleep(2000L);
                }
                catch (InterruptedException interruptedException) {}
            }
        }, "RPC-Callback-Handler");
        thread.start();
    }

    public static void shutdown() {
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }
        rpc.Discord_Shutdown();
    }

    static {
        rpc = DiscordRPC.INSTANCE;
        presence = new DiscordRichPresence();
    }
}

