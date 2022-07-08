//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.BlockWeb
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumHand
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.input.Mouse
 */
package me.alpha432.oyvey.features.modules.Flex;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.ProcessRightClickBlockEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockWeb;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

public class Offhandimprove
extends Module {
    private static Offhandimprove instance;
    private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<InventoryUtil.Task>();
    private final Timer timer = new Timer();
    private final Timer secondTimer = new Timer();
    public Setting<Boolean> crystal = this.register(new Setting<Boolean>("Crystal", true));
    public Setting<Float> crystalHealth = this.register(new Setting<Float>("CrystalHP", Float.valueOf(13.0f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Float> crystalHoleHealth = this.register(new Setting<Float>("CrystalHoleHP", Float.valueOf(3.5f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Boolean> gapple = this.register(new Setting<Boolean>("Gapple", true));
    public Setting<Boolean> armorCheck = this.register(new Setting<Boolean>("ArmorCheck", true));
    public Setting<Integer> actions = this.register(new Setting<Integer>("Actions", 4, 1, 4));
    public Mode2 currentMode = Mode2.TOTEMS;
    public int totems;
    public int crystals;
    public int gapples;
    public int lastTotemSlot = -1;
    public int lastGappleSlot = -1;
    public int lastCrystalSlot = -1;
    public int lastObbySlot = -1;
    public int lastWebSlot = -1;
    public boolean holdingCrystal;
    public boolean holdingTotem;
    public boolean holdingGapple;
    public boolean didSwitchThisTick;
    private boolean second;
    private boolean switchedForHealthReason;

    public Offhandimprove() {
        super("-AlternativeHand(2b)", "Allows you to switch up your Offhand.", Module.Category.Flex, true, false, false);
        instance = this;
    }

    public static Offhandimprove getInstance() {
        if (instance == null) {
            instance = new Offhandimprove();
        }
        return instance;
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(ProcessRightClickBlockEvent event) {
        if (event.hand == EnumHand.MAIN_HAND && event.stack.getItem() == Items.END_CRYSTAL && Offhandimprove.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && Offhandimprove.mc.objectMouseOver != null && event.pos == Offhandimprove.mc.objectMouseOver.getBlockPos()) {
            event.setCanceled(true);
            Offhandimprove.mc.player.setActiveHand(EnumHand.OFF_HAND);
            Offhandimprove.mc.playerController.processRightClick((EntityPlayer)Offhandimprove.mc.player, (World)Offhandimprove.mc.world, EnumHand.OFF_HAND);
        }
    }

    @Override
    public void onUpdate() {
        if (this.timer.passedMs(50L)) {
            if (Offhandimprove.mc.player != null && Offhandimprove.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && Offhandimprove.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL && Mouse.isButtonDown((int)1)) {
                Offhandimprove.mc.player.setActiveHand(EnumHand.OFF_HAND);
                Offhandimprove.mc.gameSettings.keyBindUseItem.pressed = Mouse.isButtonDown((int)1);
            }
        } else if (Offhandimprove.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && Offhandimprove.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
            Offhandimprove.mc.gameSettings.keyBindUseItem.pressed = false;
        }
        if (Offhandimprove.nullCheck()) {
            return;
        }
        this.doOffhand();
        if (this.secondTimer.passedMs(50L) && this.second) {
            this.second = false;
            this.timer.reset();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (!Offhandimprove.fullNullCheck() && Offhandimprove.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && Offhandimprove.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL && Offhandimprove.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                CPacketPlayerTryUseItemOnBlock packet2 = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
                if (packet2.getHand() == EnumHand.MAIN_HAND) {
                    if (this.timer.passedMs(50L)) {
                        Offhandimprove.mc.player.setActiveHand(EnumHand.OFF_HAND);
                        Offhandimprove.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
                    }
                    event.setCanceled(true);
                }
            } else if (event.getPacket() instanceof CPacketPlayerTryUseItem && ((CPacketPlayerTryUseItem)event.getPacket()).getHand() == EnumHand.OFF_HAND && !this.timer.passedMs(50L)) {
                event.setCanceled(true);
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        if (Offhandimprove.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            return "Crystals";
        }
        if (Offhandimprove.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            return "Totems";
        }
        if (Offhandimprove.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
            return "Gapples";
        }
        return null;
    }

    public void doOffhand() {
        this.didSwitchThisTick = false;
        this.holdingCrystal = Offhandimprove.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
        this.holdingTotem = Offhandimprove.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING;
        this.holdingGapple = Offhandimprove.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE;
        this.totems = Offhandimprove.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (this.holdingTotem) {
            this.totems += Offhandimprove.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        }
        this.crystals = Offhandimprove.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
        if (this.holdingCrystal) {
            this.crystals += Offhandimprove.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
        }
        this.gapples = Offhandimprove.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
        if (this.holdingGapple) {
            this.gapples += Offhandimprove.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
        }
        this.doSwitch();
    }

    public void doSwitch() {
        this.currentMode = Mode2.TOTEMS;
        if (this.gapple.getValue().booleanValue() && Offhandimprove.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && Offhandimprove.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            this.currentMode = Mode2.GAPPLES;
        } else if (this.currentMode != Mode2.CRYSTALS && this.crystal.getValue().booleanValue() && (EntityUtil.isSafe((Entity)Offhandimprove.mc.player) && EntityUtil.getHealth((Entity)Offhandimprove.mc.player, true) > this.crystalHoleHealth.getValue().floatValue() || EntityUtil.getHealth((Entity)Offhandimprove.mc.player, true) > this.crystalHealth.getValue().floatValue())) {
            this.currentMode = Mode2.CRYSTALS;
        }
        if (this.currentMode == Mode2.CRYSTALS && this.crystals == 0) {
            this.setMode(Mode2.TOTEMS);
        }
        if (this.currentMode == Mode2.CRYSTALS && (!EntityUtil.isSafe((Entity)Offhandimprove.mc.player) && EntityUtil.getHealth((Entity)Offhandimprove.mc.player, true) <= this.crystalHealth.getValue().floatValue() || EntityUtil.getHealth((Entity)Offhandimprove.mc.player, true) <= this.crystalHoleHealth.getValue().floatValue())) {
            if (this.currentMode == Mode2.CRYSTALS) {
                this.switchedForHealthReason = true;
            }
            this.setMode(Mode2.TOTEMS);
        }
        if (this.switchedForHealthReason && (EntityUtil.isSafe((Entity)Offhandimprove.mc.player) && EntityUtil.getHealth((Entity)Offhandimprove.mc.player, true) > this.crystalHoleHealth.getValue().floatValue() || EntityUtil.getHealth((Entity)Offhandimprove.mc.player, true) > this.crystalHealth.getValue().floatValue())) {
            this.setMode(Mode2.CRYSTALS);
            this.switchedForHealthReason = false;
        }
        if (this.currentMode == Mode2.CRYSTALS && this.armorCheck.getValue().booleanValue() && (Offhandimprove.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.AIR || Offhandimprove.mc.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == Items.AIR || Offhandimprove.mc.player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == Items.AIR || Offhandimprove.mc.player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == Items.AIR)) {
            this.setMode(Mode2.TOTEMS);
        }
        if (Offhandimprove.mc.currentScreen instanceof GuiContainer && !(Offhandimprove.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        Item currentOffhandTolonEditionItem = Offhandimprove.mc.player.getHeldItemOffhand().getItem();
        switch (this.currentMode) {
            case TOTEMS: {
                if (this.totems <= 0 || this.holdingTotem) break;
                this.lastTotemSlot = InventoryUtil.findItemInventorySlot(Items.TOTEM_OF_UNDYING, false);
                int lastSlot = this.getLastSlot(currentOffhandTolonEditionItem, this.lastTotemSlot);
                this.putItemInOffhandTolonEdition(this.lastTotemSlot, lastSlot);
                break;
            }
            case GAPPLES: {
                if (this.gapples <= 0 || this.holdingGapple) break;
                this.lastGappleSlot = InventoryUtil.findItemInventorySlot(Items.GOLDEN_APPLE, false);
                int lastSlot = this.getLastSlot(currentOffhandTolonEditionItem, this.lastGappleSlot);
                this.putItemInOffhandTolonEdition(this.lastGappleSlot, lastSlot);
                break;
            }
            default: {
                if (this.crystals <= 0 || this.holdingCrystal) break;
                this.lastCrystalSlot = InventoryUtil.findItemInventorySlot(Items.END_CRYSTAL, false);
                int lastSlot = this.getLastSlot(currentOffhandTolonEditionItem, this.lastCrystalSlot);
                this.putItemInOffhandTolonEdition(this.lastCrystalSlot, lastSlot);
            }
        }
        for (int i = 0; i < this.actions.getValue(); ++i) {
            InventoryUtil.Task task = this.taskList.poll();
            if (task == null) continue;
            task.run();
            if (!task.isSwitching()) continue;
            this.didSwitchThisTick = true;
        }
    }

    private int getLastSlot(Item item, int slotIn) {
        if (item == Items.END_CRYSTAL) {
            return this.lastCrystalSlot;
        }
        if (item == Items.GOLDEN_APPLE) {
            return this.lastGappleSlot;
        }
        if (item == Items.TOTEM_OF_UNDYING) {
            return this.lastTotemSlot;
        }
        if (InventoryUtil.isBlock(item, BlockObsidian.class)) {
            return this.lastObbySlot;
        }
        if (InventoryUtil.isBlock(item, BlockWeb.class)) {
            return this.lastWebSlot;
        }
        if (item == Items.AIR) {
            return -1;
        }
        return slotIn;
    }

    private void putItemInOffhandTolonEdition(int slotIn, int slotOut) {
        if (slotIn != -1 && this.taskList.isEmpty()) {
            this.taskList.add(new InventoryUtil.Task(slotIn));
            this.taskList.add(new InventoryUtil.Task(45));
            this.taskList.add(new InventoryUtil.Task(slotOut));
            this.taskList.add(new InventoryUtil.Task());
        }
    }

    public void setMode(Mode2 mode2) {
        this.currentMode = this.currentMode == mode2 ? Mode2.TOTEMS : mode2;
    }

    public static enum Mode2 {
        TOTEMS,
        GAPPLES,
        CRYSTALS;

    }
}

