//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.material.Material
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package me.alpha432.oyvey.features.modules.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.BlockUtil;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import me.alpha432.oyvey.util.TestUtil;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class HoleFiller
extends Module {
    private static final BlockPos[] surroundOffset;
    private static HoleFiller INSTANCE;
    private final Setting<Integer> range = this.register(new Setting<Integer>("Placerange", 8, 0, 10));
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 50, 0, 250));
    private final Setting<Integer> blocksPerTick = this.register(new Setting<Integer>("Blockspertick", 20, 8, 30));
    private final Timer offTimer = new Timer();
    private final Timer timer = new Timer();
    private final Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();
    private final Timer retryTimer = new Timer();
    private int blocksThisTick = 0;
    private ArrayList<BlockPos> holes = new ArrayList();
    private int trie;

    public HoleFiller() {
        super("Holefiller", "Fills em holes (^;", Module.Category.COMBAT, true, false, true);
        this.setInstance();
    }

    public static HoleFiller getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HoleFiller();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (HoleFiller.fullNullCheck()) {
            this.disable();
        }
        this.offTimer.reset();
        this.trie = 0;
    }

    @Override
    public void onTick() {
        if (this.isOn()) {
            this.doHoleFill();
        }
    }

    @Override
    public void onDisable() {
        this.retries.clear();
    }

    private void doHoleFill() {
        if (this.check()) {
            return;
        }
        this.holes = new ArrayList();
        Iterable blocks = BlockPos.getAllInBox((BlockPos)HoleFiller.mc.player.getPosition().add(-this.range.getValue().intValue(), -this.range.getValue().intValue(), -this.range.getValue().intValue()), (BlockPos)HoleFiller.mc.player.getPosition().add(this.range.getValue().intValue(), this.range.getValue().intValue(), this.range.getValue().intValue()));
        for (BlockPos pos : blocks) {
            boolean solidNeighbours;
            if (HoleFiller.mc.world.getBlockState(pos).getMaterial().blocksMovement() || HoleFiller.mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial().blocksMovement()) continue;
            boolean bl = HoleFiller.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK | HoleFiller.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN && HoleFiller.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK | HoleFiller.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN && HoleFiller.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK | HoleFiller.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN && HoleFiller.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK | HoleFiller.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN && HoleFiller.mc.world.getBlockState(pos.add(0, 0, 0)).getMaterial() == Material.AIR && HoleFiller.mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial() == Material.AIR && HoleFiller.mc.world.getBlockState(pos.add(0, 2, 0)).getMaterial() == Material.AIR ? true : (solidNeighbours = false);
            if (!solidNeighbours) continue;
            this.holes.add(pos);
        }
        this.holes.forEach(this::placeBlock);
        this.toggle();
    }

    private void placeBlock(BlockPos pos) {
        for (Entity entity : HoleFiller.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityLivingBase)) continue;
            return;
        }
        if (this.blocksThisTick < this.blocksPerTick.getValue()) {
            int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
            if (obbySlot == -1 && eChestSot == -1) {
                this.toggle();
            }
            int originalSlot = HoleFiller.mc.player.inventory.currentItem;
            HoleFiller.mc.player.inventory.currentItem = obbySlot == -1 ? eChestSot : obbySlot;
            HoleFiller.mc.playerController.updateController();
            TestUtil.placeBlock(pos);
            if (HoleFiller.mc.player.inventory.currentItem != originalSlot) {
                HoleFiller.mc.player.inventory.currentItem = originalSlot;
                HoleFiller.mc.playerController.updateController();
            }
            this.timer.reset();
            ++this.blocksThisTick;
        }
    }

    private boolean check() {
        if (HoleFiller.fullNullCheck()) {
            this.disable();
            return true;
        }
        this.blocksThisTick = 0;
        if (this.retryTimer.passedMs(2000L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        return !this.timer.passedMs(this.delay.getValue().intValue());
    }

    static {
        INSTANCE = new HoleFiller();
        surroundOffset = BlockUtil.toBlockPos(EntityUtil.getOffsets(0, true));
    }
}

