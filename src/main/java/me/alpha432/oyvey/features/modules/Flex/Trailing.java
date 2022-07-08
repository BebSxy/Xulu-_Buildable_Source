//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderPearl
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.GL11
 */
package me.alpha432.oyvey.features.modules.Flex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.event.events.UpdateWalkingPlayerEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Trailing
extends Module {
    private final Setting<Float> lineWidth = this.register(new Setting<Float>("LineThickness", Float.valueOf(1.5f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    private final Setting<Integer> red = this.register(new Setting<Integer>("Red", 0, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<Integer>("Green", 255, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 0, 0, 255));
    private final Setting<Integer> alpha = this.register(new Setting<Integer>("Saturation", 255, 0, 255));
    private final Map<Entity, List<Vec3d>> renderMap = new HashMap<Entity, List<Vec3d>>();

    public Trailing() {
        super("-Trailing", "Draws trails on projectiles.", Module.Category.Flex, true, false, false);
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        for (Entity entity : Trailing.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderPearl)) continue;
            Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, mc.getRenderPartialTicks());
            ArrayList<Vec3d> vectors = this.renderMap.get((Object)entity) != null ? this.renderMap.get((Object)entity) : new ArrayList<Vec3d>();
            vectors.add(new Vec3d(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z));
            this.renderMap.put(entity, vectors);
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        for (Entity entity : Trailing.mc.world.loadedEntityList) {
            if (!this.renderMap.containsKey((Object)entity)) continue;
            GlStateManager.pushMatrix();
            RenderUtil.GLPre(this.lineWidth.getValue().floatValue());
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask((boolean)false);
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
            GL11.glColor4f((float)((float)this.red.getValue().intValue() / 255.0f), (float)((float)this.green.getValue().intValue() / 255.0f), (float)((float)this.blue.getValue().intValue() / 255.0f), (float)((float)this.alpha.getValue().intValue() / 255.0f));
            GL11.glLineWidth((float)this.lineWidth.getValue().floatValue());
            GL11.glBegin((int)1);
            for (int i = 0; i < this.renderMap.get((Object)entity).size() - 1; ++i) {
                GL11.glVertex3d((double)this.renderMap.get((Object)entity).get((int)i).x, (double)this.renderMap.get((Object)entity).get((int)i).y, (double)this.renderMap.get((Object)entity).get((int)i).z);
                GL11.glVertex3d((double)this.renderMap.get((Object)entity).get((int)(i + 1)).x, (double)this.renderMap.get((Object)entity).get((int)(i + 1)).y, (double)this.renderMap.get((Object)entity).get((int)(i + 1)).z);
            }
            GL11.glEnd();
            GlStateManager.resetColor();
            GlStateManager.enableDepth();
            GlStateManager.depthMask((boolean)true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            RenderUtil.GlPost();
            GlStateManager.popMatrix();
        }
    }
}

