//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 */
package me.alpha432.oyvey.manager;

import java.util.ArrayList;
import java.util.List;
import me.alpha432.oyvey.features.Feature;
import net.minecraft.network.Packet;

public class PacketManager
extends Feature {
    private final List<Packet<?>> noEventPackets = new ArrayList();

    public void sendPacketNoEvent(Packet<?> packet) {
        if (packet != null && !PacketManager.nullCheck()) {
            this.noEventPackets.add(packet);
            PacketManager.mc.player.connection.sendPacket(packet);
        }
    }

    public boolean shouldSendPacket(Packet<?> packet) {
        if (this.noEventPackets.contains(packet)) {
            this.noEventPackets.remove(packet);
            return false;
        }
        return true;
    }
}

