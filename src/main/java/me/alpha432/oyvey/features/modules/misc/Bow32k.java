//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemEgg
 *  net.minecraft.item.ItemEnderPearl
 *  net.minecraft.item.ItemSnowball
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.EnumHand
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Bow32k
extends Module {
    private boolean shooting;
    private long lastShootTime;
    public Setting<Boolean> Bows = this.register(new Setting<Boolean>("Bows", true));
    public Setting<Boolean> pearls = this.register(new Setting<Boolean>("Pearls", true));
    public Setting<Boolean> eggs = this.register(new Setting<Boolean>("Eggs", true));
    public Setting<Boolean> snowballs = this.register(new Setting<Boolean>("SnowBalls", true));
    public Setting<Integer> Timeout = this.register(new Setting<Integer>("Timeout", 5000, 100, 20000));
    public Setting<Integer> spoofs = this.register(new Setting<Integer>("Spoofs", 10, 1, 300));
    public Setting<Boolean> bypass = this.register(new Setting<Boolean>("Bypasses", false));
    public Setting<Boolean> debug = this.register(new Setting<Boolean>("DebugOnToggle", false));

    public Bow32k() {
        super("Bow32k", "One Shots Players", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        if (this.isEnabled()) {
            this.shooting = false;
            this.lastShootTime = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        ItemStack handStack;
        CPacketPlayerTryUseItem packet2;
        if (event.getStage() != 0) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayerDigging) {
            ItemStack handStack2;
            CPacketPlayerDigging packet = (CPacketPlayerDigging)event.getPacket();
            if (packet.getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM && !(handStack2 = Bow32k.mc.player.getHeldItem(EnumHand.MAIN_HAND)).isEmpty() && handStack2.getItem() != null && handStack2.getItem() instanceof ItemBow && this.Bows.getValue().booleanValue()) {
                this.doSpoofs();
                if (this.debug.getValue().booleanValue()) {
                    Command.sendMessage("Spoof ATTEMPT");
                }
            }
        } else if (event.getPacket() instanceof CPacketPlayerTryUseItem && (packet2 = (CPacketPlayerTryUseItem)event.getPacket()).getHand() == EnumHand.MAIN_HAND && !(handStack = Bow32k.mc.player.getHeldItem(EnumHand.MAIN_HAND)).isEmpty() && handStack.getItem() != null) {
            if (handStack.getItem() instanceof ItemEgg && this.eggs.getValue().booleanValue()) {
                this.doSpoofs();
            } else if (handStack.getItem() instanceof ItemEnderPearl && this.pearls.getValue().booleanValue()) {
                this.doSpoofs();
            } else if (handStack.getItem() instanceof ItemSnowball && this.snowballs.getValue().booleanValue()) {
                this.doSpoofs();
            }
        }
    }

    private void doSpoofs() {
        if (System.currentTimeMillis() - this.lastShootTime >= (long)this.Timeout.getValue().intValue()) {
            this.shooting = true;
            this.lastShootTime = System.currentTimeMillis();
            Bow32k.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Bow32k.mc.player, CPacketEntityAction.Action.START_SPRINTING));
            for (int index = 0; index < this.spoofs.getValue(); ++index) {
                if (this.bypass.getValue().booleanValue()) {
                    Bow32k.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Bow32k.mc.player.posX, Bow32k.mc.player.posY + 1.0E-10, Bow32k.mc.player.posZ, false));
                    Bow32k.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Bow32k.mc.player.posX, Bow32k.mc.player.posY - 1.0E-10, Bow32k.mc.player.posZ, true));
                    continue;
                }
                Bow32k.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Bow32k.mc.player.posX, Bow32k.mc.player.posY - 1.0E-10, Bow32k.mc.player.posZ, true));
                Bow32k.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Bow32k.mc.player.posX, Bow32k.mc.player.posY + 1.0E-10, Bow32k.mc.player.posZ, false));
            }
            if (this.debug.getValue().booleanValue()) {
                Command.sendMessage("Spoofed");
            }
            this.shooting = false;
        }
    }
}

