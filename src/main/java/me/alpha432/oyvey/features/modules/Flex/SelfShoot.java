//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.potion.PotionUtils
 */
package me.alpha432.oyvey.features.modules.Flex;

import java.util.List;
import java.util.Objects;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.SeconddaryInventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.PotionUtils;

public class SelfShoot
extends Module {
    private final Setting<Integer> tickDelay = this.register(new Setting<Integer>("TickDelay", 3, 0, 8));

    public SelfShoot() {
        super("SelfShoot", "shoots arrows at you", Module.Category.Flex, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (SelfShoot.mc.player != null) {
            List<Integer> arrowSlots;
            if (SelfShoot.mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && SelfShoot.mc.player.isHandActive() && SelfShoot.mc.player.getItemInUseMaxCount() >= this.tickDelay.getValue()) {
                SelfShoot.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(SelfShoot.mc.player.cameraYaw, -90.0f, SelfShoot.mc.player.onGround));
                SelfShoot.mc.playerController.onStoppedUsingItem((EntityPlayer)SelfShoot.mc.player);
            }
            if ((arrowSlots = SeconddaryInventoryUtil.getItemInventory(Items.TIPPED_ARROW)).get(0) == -1) {
                return;
            }
            int speedSlot = -1;
            int strengthSlot = -1;
            for (Integer slot : arrowSlots) {
                if (PotionUtils.getPotionFromItem((ItemStack)SelfShoot.mc.player.inventory.getStackInSlot(slot.intValue())).getRegistryName().getPath().contains("swiftness")) {
                    speedSlot = slot;
                    continue;
                }
                if (!Objects.requireNonNull(PotionUtils.getPotionFromItem((ItemStack)SelfShoot.mc.player.inventory.getStackInSlot(slot.intValue())).getRegistryName()).getPath().contains("strength")) continue;
                strengthSlot = slot;
            }
        }
    }

    @Override
    public void onEnable() {
    }

    private int findBow() {
        return SeconddaryInventoryUtil.getItemHotbar((Item)Items.BOW);
    }
}

