//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Enchantments
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemElytra
 *  net.minecraft.item.ItemStack
 */
package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;

public class AutoElytra
extends Module {
    public final Setting<Boolean> PreferElytra = new Setting<Boolean>("PreferElytra", true);
    public final Setting<Boolean> Curse = new Setting<Boolean>("Curse of binding", false);

    public AutoElytra() {
        super("AutoElytra", "will switch your chestplate", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (AutoElytra.mc.player == null) {
            return;
        }
        ItemStack l_ChestSlot = AutoElytra.mc.player.inventoryContainer.getSlot(6).getStack();
        if (l_ChestSlot.isEmpty()) {
            int l_Slot = this.FindChestItem(this.PreferElytra.getValue());
            if (!this.PreferElytra.getValue().booleanValue() && l_Slot == -1) {
                l_Slot = this.FindChestItem(true);
            }
            if (l_Slot != -1) {
                AutoElytra.mc.playerController.windowClick(AutoElytra.mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, (EntityPlayer)AutoElytra.mc.player);
                AutoElytra.mc.playerController.windowClick(AutoElytra.mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, (EntityPlayer)AutoElytra.mc.player);
                AutoElytra.mc.playerController.windowClick(AutoElytra.mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, (EntityPlayer)AutoElytra.mc.player);
            }
            this.toggle();
            return;
        }
        int l_Slot = this.FindChestItem(l_ChestSlot.getItem() instanceof ItemArmor);
        if (l_Slot != -1) {
            AutoElytra.mc.playerController.windowClick(AutoElytra.mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, (EntityPlayer)AutoElytra.mc.player);
            AutoElytra.mc.playerController.windowClick(AutoElytra.mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, (EntityPlayer)AutoElytra.mc.player);
            AutoElytra.mc.playerController.windowClick(AutoElytra.mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, (EntityPlayer)AutoElytra.mc.player);
        }
        this.toggle();
    }

    private int FindChestItem(boolean p_Elytra) {
        int slot = -1;
        float damage = 0.0f;
        for (int i = 0; i < AutoElytra.mc.player.inventoryContainer.getInventory().size(); ++i) {
            ItemStack s;
            if (i == 0 || i == 5 || i == 6 || i == 7 || i == 8 || (s = (ItemStack)AutoElytra.mc.player.inventoryContainer.getInventory().get(i)) == null || s.getItem() == Items.AIR) continue;
            if (s.getItem() instanceof ItemArmor) {
                boolean cursed;
                ItemArmor armor = (ItemArmor)s.getItem();
                if (armor.armorType != EntityEquipmentSlot.CHEST) continue;
                float currentDamage = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.PROTECTION, (ItemStack)s);
                boolean bl = cursed = this.Curse.getValue() != false ? EnchantmentHelper.hasBindingCurse((ItemStack)s) : false;
                if (!(currentDamage > damage) || cursed) continue;
                damage = currentDamage;
                slot = i;
                continue;
            }
            if (!p_Elytra || !(s.getItem() instanceof ItemElytra)) continue;
            return i;
        }
        return slot;
    }
}

