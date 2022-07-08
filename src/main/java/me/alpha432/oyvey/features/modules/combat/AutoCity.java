//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.combat;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BlockUtilPhob;
import me.alpha432.oyvey.util.EntityUtilPhob;
import me.alpha432.oyvey.util.OyPair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoCity
extends Module {
    private static final BlockPos[] surroundOffset = new BlockPos[]{new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0)};
    public Setting<Boolean> raytrace = this.register(new Setting<Boolean>("Raytrace", false));
    public Setting<Integer> range = this.register(new Setting<Integer>("Range", 5, 1, 6));
    public Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotations", true));
    public Setting<Boolean> autodisable = this.register(new Setting<Boolean>("Toggle", true));
    public Setting<Integer> rotations = this.register(new Setting<Integer>("Spoof", 1, 1, 20));

    public AutoCity() {
        super("AutoCity", "Automaticly Cities peoples Feet", Module.Category.COMBAT, true, false, false);
    }

    public static ArrayList<OyPair<EntityPlayer, ArrayList<BlockPos>>> GetPlayersReadyToBeCitied() {
        ArrayList<OyPair<EntityPlayer, ArrayList<BlockPos>>> arrayList = new ArrayList<OyPair<EntityPlayer, ArrayList<BlockPos>>>();
        for (EntityPlayer entity : Objects.requireNonNull(EntityUtilPhob.getNearbyPlayers(6.0)).stream().filter(entityPlayer -> !OyVey.friendManager.isFriend((EntityPlayer)entityPlayer)).collect(Collectors.toList())) {
            ArrayList<BlockPos> arrayList2 = new ArrayList<BlockPos>();
            for (int i = 0; i < 4; ++i) {
                BlockPos blockPos = EntityUtilPhob.GetPositionVectorBlockPos((Entity)entity, surroundOffset[i]);
                if (AutoCity.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) continue;
                boolean bl = false;
                switch (i) {
                    case 0: {
                        bl = BlockUtilPhob.canPlaceCrystal(blockPos.north(2), true, false);
                        break;
                    }
                    case 1: {
                        bl = BlockUtilPhob.canPlaceCrystal(blockPos.east(2), true, false);
                        break;
                    }
                    case 2: {
                        bl = BlockUtilPhob.canPlaceCrystal(blockPos.south(2), true, false);
                        break;
                    }
                    case 3: {
                        bl = BlockUtilPhob.canPlaceCrystal(blockPos.west(2), true, false);
                    }
                }
                if (!bl) continue;
                arrayList2.add(blockPos);
            }
            if (arrayList2.isEmpty()) continue;
            arrayList.add(new OyPair(entity, arrayList2));
        }
        return arrayList;
    }

    @Override
    public void onEnable() {
        ArrayList<OyPair<EntityPlayer, ArrayList<BlockPos>>> arrayList = AutoCity.GetPlayersReadyToBeCitied();
        if (arrayList.isEmpty()) {
            Command.sendMessage("I cant find anyone to city");
            this.toggle();
            return;
        }
        EntityPlayer entityPlayer = null;
        BlockPos blockPos = null;
        double d = 50.0;
        for (OyPair<EntityPlayer, ArrayList<BlockPos>> OyPair2 : arrayList) {
            for (BlockPos blockPos2 : OyPair2.getSecond()) {
                if (blockPos == null) {
                    entityPlayer = OyPair2.getFirst();
                    blockPos = blockPos2;
                    continue;
                }
                double d2 = blockPos2.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                if (!(d2 < d)) continue;
                d = d2;
                blockPos = blockPos2;
                entityPlayer = OyPair2.getFirst();
            }
        }
        if (blockPos == null || entityPlayer == null) {
            Command.sendMessage("there are no blocks to mine");
            this.toggle();
            return;
        }
        BlockUtilPhob.SetCurrentBlock(blockPos);
        Command.sendMessage("Mining A Block From Ur Target: " + entityPlayer.getName());
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent updateWalkingPlayerEvent) {
        boolean bl;
        boolean bl2 = bl = AutoCity.mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_PICKAXE;
        if (!bl) {
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack = AutoCity.mc.player.inventory.getStackInSlot(i);
                if (itemStack.isEmpty() || itemStack.getItem() != Items.DIAMOND_PICKAXE) continue;
                bl = true;
                AutoCity.mc.player.inventory.currentItem = i;
                AutoCity.mc.playerController.updateController();
                break;
            }
        }
        if (!bl) {
            Command.sendMessage("No pickaxe!");
            this.toggle();
            return;
        }
        BlockPos blockPos = BlockUtilPhob.GetCurrBlock();
        if (blockPos == null) {
            if (this.autodisable.getValue().booleanValue()) {
                Command.sendMessage("Done!");
                this.toggle();
            }
            return;
        }
        if (this.rotate.getValue().booleanValue()) {
            OyVey.rotationManager.updateRotations();
            OyVey.rotationManager.lookAtPos(blockPos);
            updateWalkingPlayerEvent.setCanceled(true);
        }
        BlockUtilPhob.Update(this.range.getValue().intValue(), this.raytrace.getValue());
    }
}

