//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockShulkerBox
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.inventory.GuiCrafting
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Enchantments
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemAir
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 */
package me.alpha432.oyvey.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InvUtil
implements Util {
    public static void switchToHotbarSlot(int slot, boolean silent) {
        if (InvUtil.mc.player.inventory.currentItem == slot || slot < 0) {
            return;
        }
        if (silent) {
            InvUtil.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
            InvUtil.mc.playerController.updateController();
        } else {
            InvUtil.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
            InvUtil.mc.player.inventory.currentItem = slot;
            InvUtil.mc.playerController.updateController();
        }
    }

    public static void switchToHotbarSlot(Class clazz, boolean silent) {
        int slot = InvUtil.findHotbarBlock(clazz);
        if (slot > -1) {
            InvUtil.switchToHotbarSlot(slot, silent);
        }
    }

    public static boolean isNull(ItemStack stack) {
        return stack == null || stack.getItem() instanceof ItemAir;
    }

    public static int findHotbarBlock(Class clazz) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = InvUtil.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY) continue;
            if (clazz.isInstance((Object)stack.getItem())) {
                return i;
            }
            if (!(stack.getItem() instanceof ItemBlock) || !clazz.isInstance((Object)((ItemBlock)stack.getItem()).getBlock())) continue;
            return i;
        }
        return -1;
    }

    public static int findAnyBlock() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = InvUtil.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) continue;
            return i;
        }
        return -1;
    }

    public static int findHotbarBlock(Block blockIn) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = InvUtil.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock) || ((ItemBlock)stack.getItem()).getBlock() != blockIn) continue;
            return i;
        }
        return -1;
    }

    public static int getItemHotbar(Item input) {
        for (int i = 0; i < 9; ++i) {
            Item item = InvUtil.mc.player.inventory.getStackInSlot(i).getItem();
            if (Item.getIdFromItem((Item)item) != Item.getIdFromItem((Item)input)) continue;
            return i;
        }
        return -1;
    }

    public static int findStackInventory(Item input) {
        return InvUtil.findStackInventory(input, false);
    }

    public static int findStackInventory(Item input, boolean withHotbar) {
        int i;
        int n = i = withHotbar ? 0 : 9;
        while (i < 36) {
            Item item = InvUtil.mc.player.inventory.getStackInSlot(i).getItem();
            if (Item.getIdFromItem((Item)input) == Item.getIdFromItem((Item)item)) {
                return i + (i < 9 ? 36 : 0);
            }
            ++i;
        }
        return -1;
    }

    public static int findItemInventorySlot(Item item, boolean offHand) {
        AtomicInteger slot = new AtomicInteger();
        slot.set(-1);
        for (Map.Entry<Integer, ItemStack> entry : InvUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getValue().getItem() != item || entry.getKey() == 45 && !offHand) continue;
            slot.set(entry.getKey());
            return slot.get();
        }
        return slot.get();
    }

    public static List<Integer> findEmptySlots(boolean withXCarry) {
        ArrayList<Integer> outPut = new ArrayList<Integer>();
        for (Map.Entry<Integer, ItemStack> entry : InvUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (!entry.getValue().isEmpty && entry.getValue().getItem() != Items.AIR) continue;
            outPut.add(entry.getKey());
        }
        if (withXCarry) {
            for (int i = 1; i < 5; ++i) {
                Slot craftingSlot = (Slot)InvUtil.mc.player.inventoryContainer.inventorySlots.get(i);
                ItemStack craftingStack = craftingSlot.getStack();
                if (!craftingStack.isEmpty() && craftingStack.getItem() != Items.AIR) continue;
                outPut.add(i);
            }
        }
        return outPut;
    }

    public static int findInventoryBlock(Class clazz, boolean offHand) {
        AtomicInteger slot = new AtomicInteger();
        slot.set(-1);
        for (Map.Entry<Integer, ItemStack> entry : InvUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (!InvUtil.isBlock(entry.getValue().getItem(), clazz) || entry.getKey() == 45 && !offHand) continue;
            slot.set(entry.getKey());
            return slot.get();
        }
        return slot.get();
    }

    public static int findInventoryWool(boolean offHand) {
        AtomicInteger slot = new AtomicInteger();
        slot.set(-1);
        for (Map.Entry<Integer, ItemStack> entry : InvUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (!(entry.getValue().getItem() instanceof ItemBlock)) continue;
            ItemBlock wool = (ItemBlock)entry.getValue().getItem();
            if (wool.getBlock().material != Material.CLOTH || entry.getKey() == 45 && !offHand) continue;
            slot.set(entry.getKey());
            return slot.get();
        }
        return slot.get();
    }

    public static int findEmptySlot() {
        AtomicInteger slot = new AtomicInteger();
        slot.set(-1);
        for (Map.Entry<Integer, ItemStack> entry : InvUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (!entry.getValue().isEmpty()) continue;
            slot.set(entry.getKey());
            return slot.get();
        }
        return slot.get();
    }

    public static boolean isBlock(Item item, Class clazz) {
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock)item).getBlock();
            return clazz.isInstance((Object)block);
        }
        return false;
    }

    public static void confirmSlot(int slot) {
        InvUtil.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
        InvUtil.mc.player.inventory.currentItem = slot;
        InvUtil.mc.playerController.updateController();
    }

    public static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        if (InvUtil.mc.currentScreen instanceof GuiCrafting) {
            return InvUtil.fuckYou3arthqu4kev2(10, 45);
        }
        return InvUtil.getInventorySlots(9, 44);
    }

    private static Map<Integer, ItemStack> getInventorySlots(int currentI, int last) {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        for (int current = currentI; current <= last; ++current) {
            fullInventorySlots.put(current, (ItemStack)InvUtil.mc.player.inventoryContainer.getInventory().get(current));
        }
        return fullInventorySlots;
    }

    private static Map<Integer, ItemStack> fuckYou3arthqu4kev2(int currentI, int last) {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        for (int current = currentI; current <= last; ++current) {
            fullInventorySlots.put(current, (ItemStack)InvUtil.mc.player.openContainer.getInventory().get(current));
        }
        return fullInventorySlots;
    }

    public static boolean[] switchItem(boolean back, int lastHotbarSlot, boolean switchedItem, Switch mode2, Class clazz) {
        boolean[] switchedItemSwitched = new boolean[]{switchedItem, false};
        switch (mode2) {
            case NORMAL: {
                if (!back && !switchedItem) {
                    InvUtil.switchToHotbarSlot(InvUtil.findHotbarBlock(clazz), false);
                    switchedItemSwitched[0] = true;
                } else if (back && switchedItem) {
                    InvUtil.switchToHotbarSlot(lastHotbarSlot, false);
                    switchedItemSwitched[0] = false;
                }
                switchedItemSwitched[1] = true;
                break;
            }
            case SILENT: {
                if (!back && !switchedItem) {
                    InvUtil.switchToHotbarSlot(InvUtil.findHotbarBlock(clazz), true);
                    switchedItemSwitched[0] = true;
                } else if (back && switchedItem) {
                    switchedItemSwitched[0] = false;
                    OyVey.inventoryManager.recoverSilent(lastHotbarSlot);
                }
                switchedItemSwitched[1] = true;
                break;
            }
            case NONE: {
                switchedItemSwitched[1] = back || InvUtil.mc.player.inventory.currentItem == InvUtil.findHotbarBlock(clazz);
            }
        }
        return switchedItemSwitched;
    }

    public static boolean[] switchItemToItem(boolean back, int lastHotbarSlot, boolean switchedItem, Switch mode2, Item item) {
        boolean[] switchedItemSwitched = new boolean[]{switchedItem, false};
        switch (mode2) {
            case NORMAL: {
                if (!back && !switchedItem) {
                    InvUtil.switchToHotbarSlot(InvUtil.getItemHotbar(item), false);
                    switchedItemSwitched[0] = true;
                } else if (back && switchedItem) {
                    InvUtil.switchToHotbarSlot(lastHotbarSlot, false);
                    switchedItemSwitched[0] = false;
                }
                switchedItemSwitched[1] = true;
                break;
            }
            case SILENT: {
                if (!back && !switchedItem) {
                    InvUtil.switchToHotbarSlot(InvUtil.getItemHotbar(item), true);
                    switchedItemSwitched[0] = true;
                } else if (back && switchedItem) {
                    switchedItemSwitched[0] = false;
                    OyVey.inventoryManager.recoverSilent(lastHotbarSlot);
                }
                switchedItemSwitched[1] = true;
                break;
            }
            case NONE: {
                switchedItemSwitched[1] = back || InvUtil.mc.player.inventory.currentItem == InvUtil.getItemHotbar(item);
            }
        }
        return switchedItemSwitched;
    }

    public static boolean holdingItem(Class clazz) {
        ItemStack stack = InvUtil.mc.player.getHeldItemMainhand();
        boolean result = InvUtil.isInstanceOf(stack, clazz);
        if (!result) {
            InvUtil.mc.player.getHeldItemOffhand();
            result = InvUtil.isInstanceOf(stack, clazz);
        }
        return result;
    }

    public static boolean isInstanceOf(ItemStack stack, Class clazz) {
        if (stack == null) {
            return false;
        }
        Item item = stack.getItem();
        if (clazz.isInstance((Object)item)) {
            return true;
        }
        if (item instanceof ItemBlock) {
            Block block = Block.getBlockFromItem((Item)item);
            return clazz.isInstance((Object)block);
        }
        return false;
    }

    public static int getEmptyXCarry() {
        for (int i = 1; i < 5; ++i) {
            Slot craftingSlot = (Slot)InvUtil.mc.player.inventoryContainer.inventorySlots.get(i);
            ItemStack craftingStack = craftingSlot.getStack();
            if (!craftingStack.isEmpty() && craftingStack.getItem() != Items.AIR) continue;
            return i;
        }
        return -1;
    }

    public static boolean isSlotEmpty(int i) {
        Slot slot = (Slot)InvUtil.mc.player.inventoryContainer.inventorySlots.get(i);
        ItemStack stack = slot.getStack();
        return stack.isEmpty();
    }

    public static int convertHotbarToInv(int input) {
        return 36 + input;
    }

    public static boolean areStacksCompatible(ItemStack stack1, ItemStack stack2) {
        if (!stack1.getItem().equals((Object)stack2.getItem())) {
            return false;
        }
        if (stack1.getItem() instanceof ItemBlock && stack2.getItem() instanceof ItemBlock) {
            Block block1 = ((ItemBlock)stack1.getItem()).getBlock();
            Block block2 = ((ItemBlock)stack2.getItem()).getBlock();
            if (!block1.material.equals((Object)block2.material)) {
                return false;
            }
        }
        if (!stack1.getDisplayName().equals(stack2.getDisplayName())) {
            return false;
        }
        return stack1.getItemDamage() == stack2.getItemDamage();
    }

    public static EntityEquipmentSlot getEquipmentFromSlot(int slot) {
        if (slot == 5) {
            return EntityEquipmentSlot.HEAD;
        }
        if (slot == 6) {
            return EntityEquipmentSlot.CHEST;
        }
        if (slot == 7) {
            return EntityEquipmentSlot.LEGS;
        }
        return EntityEquipmentSlot.FEET;
    }

    public static int findArmorSlot(EntityEquipmentSlot type2, boolean binding) {
        int slot = -1;
        float damage = 0.0f;
        for (int i = 9; i < 45; ++i) {
            boolean cursed;
            ItemStack s = Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getStack();
            if (s.getItem() == Items.AIR || !(s.getItem() instanceof ItemArmor)) continue;
            ItemArmor armor = (ItemArmor)s.getItem();
            if (armor.armorType != type2) continue;
            float currentDamage = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.PROTECTION, (ItemStack)s);
            boolean bl = cursed = binding && EnchantmentHelper.hasBindingCurse((ItemStack)s);
            if (!(currentDamage > damage) || cursed) continue;
            damage = currentDamage;
            slot = i;
        }
        return slot;
    }

    public static int findArmorSlot(EntityEquipmentSlot type2, boolean binding, boolean withXCarry) {
        int slot = InvUtil.findArmorSlot(type2, binding);
        if (slot == -1 && withXCarry) {
            float damage = 0.0f;
            for (int i = 1; i < 5; ++i) {
                boolean cursed;
                Slot craftingSlot = (Slot)InvUtil.mc.player.inventoryContainer.inventorySlots.get(i);
                ItemStack craftingStack = craftingSlot.getStack();
                if (craftingStack.getItem() == Items.AIR || !(craftingStack.getItem() instanceof ItemArmor)) continue;
                ItemArmor armor = (ItemArmor)craftingStack.getItem();
                if (armor.armorType != type2) continue;
                float currentDamage = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.PROTECTION, (ItemStack)craftingStack);
                boolean bl = cursed = binding && EnchantmentHelper.hasBindingCurse((ItemStack)craftingStack);
                if (!(currentDamage > damage) || cursed) continue;
                damage = currentDamage;
                slot = i;
            }
        }
        return slot;
    }

    public static int findItemInventorySlot(Item item, boolean offHand, boolean withXCarry) {
        int slot = InvUtil.findItemInventorySlot(item, offHand);
        if (slot == -1 && withXCarry) {
            for (int i = 1; i < 5; ++i) {
                Slot craftingSlot = (Slot)InvUtil.mc.player.inventoryContainer.inventorySlots.get(i);
                ItemStack craftingStack = craftingSlot.getStack();
                if (craftingStack.getItem() == Items.AIR || craftingStack.getItem() != item) continue;
                slot = i;
            }
        }
        return slot;
    }

    public static int findBlockSlotInventory(Class<BlockShulkerBox> clazz, boolean offHand, boolean withXCarry) {
        int slot = InvUtil.findInventoryBlock(clazz, offHand);
        if (slot == -1 && withXCarry) {
            for (int i = 1; i < 5; ++i) {
                Slot craftingSlot = (Slot)InvUtil.mc.player.inventoryContainer.inventorySlots.get(i);
                ItemStack craftingStack = craftingSlot.getStack();
                if (craftingStack.getItem() == Items.AIR) continue;
                Item craftingStackItem = craftingStack.getItem();
                if (clazz.isInstance((Object)craftingStackItem)) {
                    slot = i;
                    continue;
                }
                if (!(craftingStackItem instanceof ItemBlock) || !clazz.isInstance((Object)((ItemBlock)craftingStackItem).getBlock())) continue;
                slot = i;
            }
        }
        return slot;
    }

    public static class Task {
        private final int slot;
        private final boolean update;
        private final boolean quickClick;

        public Task() {
            this.update = true;
            this.slot = -1;
            this.quickClick = false;
        }

        public Task(int slot) {
            this.slot = slot;
            this.quickClick = false;
            this.update = false;
        }

        public Task(int slot, boolean quickClick) {
            this.slot = slot;
            this.quickClick = quickClick;
            this.update = false;
        }

        public void run() {
            if (this.update) {
                Util.mc.playerController.updateController();
            }
            if (this.slot != -1) {
                Util.mc.playerController.windowClick(Util.mc.player.inventoryContainer.windowId, this.slot, 0, this.quickClick ? ClickType.QUICK_MOVE : ClickType.PICKUP, (EntityPlayer)Util.mc.player);
            }
        }

        public boolean isSwitching() {
            return !this.update;
        }
    }

    public static enum Switch {
        NORMAL,
        SILENT,
        NONE;

    }
}

