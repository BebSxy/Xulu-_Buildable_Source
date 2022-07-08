//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockPortal
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package me.alpha432.oyvey.features.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PortalViewerYippie
extends Module {
    private int cooldownTicks;
    private final ArrayList<BlockPos> blockPosArrayList = new ArrayList();
    private final Setting<Integer> distance = this.register(new Setting<Integer>("RenderRange", 60, 10, 100));
    private final Setting<Boolean> box = this.register(new Setting<Boolean>("CubeRender", false));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Object>("Saturation", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> this.box.getValue()));
    private final Setting<Boolean> outline = this.register(new Setting<Boolean>("Wireframe", true));
    private final Setting<Float> lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.outline.getValue()));

    public PortalViewerYippie() {
        super("PortalViewerYippie", "Highlights Portals", Module.Category.RENDER, true, false, false);
    }

    @SubscribeEvent
    public void onTickEvent(TickEvent.ClientTickEvent event) {
        if (PortalViewerYippie.mc.world == null) {
            return;
        }
        if (this.cooldownTicks < 1) {
            this.blockPosArrayList.clear();
            this.compileDL();
            this.cooldownTicks = 80;
        }
        --this.cooldownTicks;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (PortalViewerYippie.mc.world == null) {
            return;
        }
        for (BlockPos pos : this.blockPosArrayList) {
            RenderUtil.drawBoxESP(pos, new Color(204, 0, 153, 255), false, new Color(204, 0, 153, 255), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
        }
    }

    private void compileDL() {
        if (PortalViewerYippie.mc.world == null || PortalViewerYippie.mc.player == null) {
            return;
        }
        for (int x = (int)PortalViewerYippie.mc.player.posX - this.distance.getValue(); x <= (int)PortalViewerYippie.mc.player.posX + this.distance.getValue(); ++x) {
            for (int y = (int)PortalViewerYippie.mc.player.posY - this.distance.getValue(); y <= (int)PortalViewerYippie.mc.player.posY + this.distance.getValue(); ++y) {
                int z = (int)Math.max(PortalViewerYippie.mc.player.posZ - (double)this.distance.getValue().intValue(), 0.0);
                while ((double)z <= Math.min(PortalViewerYippie.mc.player.posZ + (double)this.distance.getValue().intValue(), 255.0)) {
                    BlockPos pos = new BlockPos(x, y, z);
                    Block block = PortalViewerYippie.mc.world.getBlockState(pos).getBlock();
                    if (block instanceof BlockPortal) {
                        this.blockPosArrayList.add(pos);
                    }
                    ++z;
                }
            }
        }
    }
}

