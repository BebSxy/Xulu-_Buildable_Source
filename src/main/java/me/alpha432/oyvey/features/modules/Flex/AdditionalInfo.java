//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.client.renderer.DestroyBlockProgress
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.MobEffects
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.GL11
 */
package me.alpha432.oyvey.features.modules.Flex;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.combat.AutoCrystal;
import me.alpha432.oyvey.features.modules.combat.Killaura;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.util.XuluRenderUtilBoss;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class AdditionalInfo
extends Module {
    private static final ResourceLocation box = new ResourceLocation("textures/gui/container/shulker_box.png");
    private static final double HALF_PI = 1.5707963267948966;
    private Map<EntityPlayer, Map<Integer, ItemStack>> hotbarMap = new HashMap<EntityPlayer, Map<Integer, ItemStack>>();
    public Setting<Boolean> inventory = this.register(new Setting<Boolean>("Inventory", false));
    public Setting<Integer> invX = this.register(new Setting<Object>("InvX", Integer.valueOf(564), Integer.valueOf(0), Integer.valueOf(1000), v -> this.inventory.getValue()));
    public Setting<Integer> invY = this.register(new Setting<Object>("InvY", Integer.valueOf(467), Integer.valueOf(0), Integer.valueOf(1000), v -> this.inventory.getValue()));
    public Setting<Integer> fineinvX = this.register(new Setting<Object>("InvFineX", Integer.valueOf(0), v -> this.inventory.getValue()));
    public Setting<Integer> fineinvY = this.register(new Setting<Object>("InvFineY", Integer.valueOf(0), v -> this.inventory.getValue()));
    public Setting<Boolean> renderXCarry = this.register(new Setting<Object>("RenderXCarry", Boolean.valueOf(false), v -> this.inventory.getValue()));
    public Setting<Integer> invH = this.register(new Setting<Object>("InvH", Integer.valueOf(3), v -> this.inventory.getValue()));
    public Setting<Boolean> holeHud = this.register(new Setting<Boolean>("HoleHUD", false));
    public Setting<Integer> holeX = this.register(new Setting<Object>("HoleX", Integer.valueOf(279), Integer.valueOf(0), Integer.valueOf(1000), v -> this.holeHud.getValue()));
    public Setting<Integer> holeY = this.register(new Setting<Object>("HoleY", Integer.valueOf(485), Integer.valueOf(0), Integer.valueOf(1000), v -> this.holeHud.getValue()));
    public Setting<Boolean> playerViewer = this.register(new Setting<Boolean>("PlayerViewer", false));
    public Setting<Integer> playerViewerX = this.register(new Setting<Object>("PlayerX", Integer.valueOf(752), Integer.valueOf(0), Integer.valueOf(1000), v -> this.playerViewer.getValue()));
    public Setting<Integer> playerViewerY = this.register(new Setting<Object>("PlayerY", Integer.valueOf(497), Integer.valueOf(0), Integer.valueOf(1000), v -> this.playerViewer.getValue()));
    public Setting<Float> playerScale = this.register(new Setting<Object>("PlayerScale", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(2.0f), v -> this.playerViewer.getValue()));
    public Setting<Boolean> targetHud = this.register(new Setting<Boolean>("TargetHud", false));
    public Setting<Boolean> targetHudBackground = this.register(new Setting<Object>("TargetHudBackground", Boolean.valueOf(true), v -> this.targetHud.getValue()));
    public Setting<Integer> targetHudX = this.register(new Setting<Object>("TargetHudX", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(1000), v -> this.targetHud.getValue()));
    public Setting<Integer> targetHudY = this.register(new Setting<Object>("TargetHudY", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(1000), v -> this.targetHud.getValue()));
    public Setting<TargetHudDesign> design = this.register(new Setting<Object>("Design", (Object)TargetHudDesign.NORMAL, v -> this.targetHud.getValue()));

    public AdditionalInfo() {
        super("AdditionalInfos", "HudInfos", Module.Category.Flex, false, false, true);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (AdditionalInfo.fullNullCheck()) {
            return;
        }
        if (this.playerViewer.getValue().booleanValue()) {
            this.drawPlayer();
        }
        if (this.holeHud.getValue().booleanValue()) {
            this.drawOverlay(event.partialTicks);
        }
        if (this.inventory.getValue().booleanValue()) {
            this.renderInventory();
        }
        if (this.targetHud.getValue().booleanValue()) {
            this.drawTargetHud(event.partialTicks);
        }
    }

    public void drawTargetHud(float partialTicks) {
        if (this.design.getValue() == TargetHudDesign.NORMAL) {
            EntityPlayer target;
            EntityPlayer entityPlayer = AutoCrystal.target != null ? AutoCrystal.target : (target = Killaura.target instanceof EntityPlayer ? (EntityPlayer)Killaura.target : AdditionalInfo.getClosestEnemy());
            if (target == null) {
                return;
            }
            if (this.targetHudBackground.getValue().booleanValue()) {
                RenderUtil.drawRectangleCorrectly(this.targetHudX.getValue(), this.targetHudY.getValue(), 210, 100, ColorUtil.toRGBA(20, 20, 20, 160));
            }
            GlStateManager.disableRescaleNormal();
            GlStateManager.setActiveTexture((int)OpenGlHelper.lightmapTexUnit);
            GlStateManager.disableTexture2D();
            GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            try {
                GuiInventory.drawEntityOnScreen((int)(this.targetHudX.getValue() + 30), (int)(this.targetHudY.getValue() + 90), (int)45, (float)0.0f, (float)0.0f, (EntityLivingBase)target);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
            this.renderer.drawStringWithShadow(target.getName(), this.targetHudX.getValue() + 60, this.targetHudY.getValue() + 10, ColorUtil.toRGBA(255, 0, 0, 255));
            float health = target.getHealth() + target.getAbsorptionAmount();
            int healthColor = health >= 16.0f ? ColorUtil.toRGBA(0, 255, 0, 255) : (health >= 10.0f ? ColorUtil.toRGBA(255, 255, 0, 255) : ColorUtil.toRGBA(255, 0, 0, 255));
            DecimalFormat df = new DecimalFormat("##.#");
            this.renderer.drawStringWithShadow(df.format(target.getHealth() + target.getAbsorptionAmount()), this.targetHudX.getValue() + 60 + this.renderer.getStringWidth(target.getName() + "  "), this.targetHudY.getValue() + 10, healthColor);
            this.renderer.drawStringWithShadow("Pops: " + OyVey.totemPopManager.getTotemPops(target), this.targetHudX.getValue() + 60, this.targetHudY.getValue() + this.renderer.getFontHeight() * 2 + 30, ColorUtil.toRGBA(255, 0, 0, 255));
            GlStateManager.enableTexture2D();
            int iteration = 0;
            int i = this.targetHudX.getValue() + 50;
            int y = this.targetHudY.getValue() + this.renderer.getFontHeight() * 3 + 44;
            for (ItemStack is : target.inventory.armorInventory) {
                ++iteration;
                if (is.isEmpty()) continue;
                int x = i - 90 + (9 - iteration) * 20 + 2;
                GlStateManager.enableDepth();
                RenderUtil.itemRender.zLevel = 200.0f;
                RenderUtil.itemRender.renderItemAndEffectIntoGUI(is, x, y);
                RenderUtil.itemRender.renderItemOverlayIntoGUI(AdditionalInfo.mc.fontRenderer, is, x, y, "");
                RenderUtil.itemRender.zLevel = 0.0f;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                String s = is.getCount() > 1 ? is.getCount() + "" : "";
                this.renderer.drawStringWithShadow(s, x + 19 - 2 - this.renderer.getStringWidth(s), y + 9, 0xFFFFFF);
                int dmg = 0;
                int itemDurability = is.getMaxDamage() - is.getItemDamage();
                float green = ((float)is.getMaxDamage() - (float)is.getItemDamage()) / (float)is.getMaxDamage();
                float red = 1.0f - green;
                dmg = 100 - (int)(red * 100.0f);
                this.renderer.drawStringWithShadow(dmg + "", (float)(x + 8) - (float)this.renderer.getStringWidth(dmg + "") / 2.0f, y - 5, ColorUtil.toRGBA((int)(red * 255.0f), (int)(green * 255.0f), 0));
            }
            this.drawOverlay(partialTicks, (Entity)target, this.targetHudX.getValue() + 150, this.targetHudY.getValue() + 6);
            this.renderer.drawStringWithShadow("Strength", this.targetHudX.getValue() + 150, this.targetHudY.getValue() + 60, target.isPotionActive(MobEffects.STRENGTH) ? ColorUtil.toRGBA(0, 255, 0, 255) : ColorUtil.toRGBA(255, 0, 0, 255));
            this.renderer.drawStringWithShadow("Weakness", this.targetHudX.getValue() + 150, this.targetHudY.getValue() + this.renderer.getFontHeight() + 70, target.isPotionActive(MobEffects.WEAKNESS) ? ColorUtil.toRGBA(0, 255, 0, 255) : ColorUtil.toRGBA(255, 0, 0, 255));
        } else if (this.design.getValue() == TargetHudDesign.COMPACT) {
            // empty if block
        }
    }

    @SubscribeEvent
    public void onReceivePacket(PacketEvent.Receive event) {
    }

    public static EntityPlayer getClosestEnemy() {
        EntityPlayer closestPlayer = null;
        for (EntityPlayer player : AdditionalInfo.mc.world.playerEntities) {
            if (player == AdditionalInfo.mc.player || OyVey.friendManager.isFriend(player)) continue;
            if (closestPlayer == null) {
                closestPlayer = player;
                continue;
            }
            if (!(AdditionalInfo.mc.player.getDistanceSq((Entity)player) < AdditionalInfo.mc.player.getDistanceSq((Entity)closestPlayer))) continue;
            closestPlayer = player;
        }
        return closestPlayer;
    }

    public void drawPlayer(EntityPlayer player, int x, int y) {
        EntityPlayer ent = player;
        GlStateManager.pushMatrix();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel((int)7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.rotate((float)0.0f, (float)0.0f, (float)5.0f, (float)0.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(this.playerViewerX.getValue() + 25), (float)(this.playerViewerY.getValue() + 25), (float)50.0f);
        GlStateManager.scale((float)(-50.0f * this.playerScale.getValue().floatValue()), (float)(50.0f * this.playerScale.getValue().floatValue()), (float)(50.0f * this.playerScale.getValue().floatValue()));
        GlStateManager.rotate((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.rotate((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(-((float)Math.atan((float)this.playerViewerY.getValue().intValue() / 40.0f)) * 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.translate((float)0.0f, (float)0.0f, (float)0.0f);
        RenderManager rendermanager = mc.getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        try {
            rendermanager.renderEntity((Entity)ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        }
        catch (Exception exception) {
            // empty catch block
        }
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture((int)OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
        GlStateManager.depthFunc((int)515);
        GlStateManager.resetColor();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }

    public void drawPlayer() {
        EntityPlayerSP ent = AdditionalInfo.mc.player;
        GlStateManager.pushMatrix();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel((int)7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.rotate((float)0.0f, (float)0.0f, (float)5.0f, (float)0.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(this.playerViewerX.getValue() + 25), (float)(this.playerViewerY.getValue() + 25), (float)50.0f);
        GlStateManager.scale((float)(-50.0f * this.playerScale.getValue().floatValue()), (float)(50.0f * this.playerScale.getValue().floatValue()), (float)(50.0f * this.playerScale.getValue().floatValue()));
        GlStateManager.rotate((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.rotate((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(-((float)Math.atan((float)this.playerViewerY.getValue().intValue() / 40.0f)) * 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.translate((float)0.0f, (float)0.0f, (float)0.0f);
        RenderManager rendermanager = mc.getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        try {
            rendermanager.renderEntity((Entity)ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        }
        catch (Exception exception) {
            // empty catch block
        }
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture((int)OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture((int)OpenGlHelper.defaultTexUnit);
        GlStateManager.depthFunc((int)515);
        GlStateManager.resetColor();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }

    public void drawOverlay(float partialTicks) {
        BlockPos westPos;
        Block west;
        BlockPos eastPos;
        Block east;
        BlockPos southPos;
        Block south;
        int damage;
        float yaw = 0.0f;
        int dir = MathHelper.floor((double)((double)(AdditionalInfo.mc.player.rotationYaw * 4.0f / 360.0f) + 0.5)) & 3;
        switch (dir) {
            case 1: {
                yaw = 90.0f;
                break;
            }
            case 2: {
                yaw = -180.0f;
                break;
            }
            case 3: {
                yaw = -90.0f;
            }
        }
        BlockPos northPos = this.traceToBlock(partialTicks, yaw);
        Block north = this.getBlock(northPos);
        if (north != null && north != Blocks.AIR) {
            damage = this.getBlockDamage(northPos);
            if (damage != 0) {
                RenderUtil.drawRect(this.holeX.getValue() + 16, this.holeY.getValue().intValue(), this.holeX.getValue() + 32, this.holeY.getValue() + 16, 0x60FF0000);
            }
            this.drawBlock(north, this.holeX.getValue() + 16, this.holeY.getValue().intValue());
        }
        if ((south = this.getBlock(southPos = this.traceToBlock(partialTicks, yaw - 180.0f))) != null && south != Blocks.AIR) {
            damage = this.getBlockDamage(southPos);
            if (damage != 0) {
                RenderUtil.drawRect(this.holeX.getValue() + 16, this.holeY.getValue() + 32, this.holeX.getValue() + 32, this.holeY.getValue() + 48, 0x60FF0000);
            }
            this.drawBlock(south, this.holeX.getValue() + 16, this.holeY.getValue() + 32);
        }
        if ((east = this.getBlock(eastPos = this.traceToBlock(partialTicks, yaw + 90.0f))) != null && east != Blocks.AIR) {
            damage = this.getBlockDamage(eastPos);
            if (damage != 0) {
                RenderUtil.drawRect(this.holeX.getValue() + 32, this.holeY.getValue() + 16, this.holeX.getValue() + 48, this.holeY.getValue() + 32, 0x60FF0000);
            }
            this.drawBlock(east, this.holeX.getValue() + 32, this.holeY.getValue() + 16);
        }
        if ((west = this.getBlock(westPos = this.traceToBlock(partialTicks, yaw - 90.0f))) != null && west != Blocks.AIR) {
            damage = this.getBlockDamage(westPos);
            if (damage != 0) {
                RenderUtil.drawRect(this.holeX.getValue().intValue(), this.holeY.getValue() + 16, this.holeX.getValue() + 16, this.holeY.getValue() + 32, 0x60FF0000);
            }
            this.drawBlock(west, this.holeX.getValue().intValue(), this.holeY.getValue() + 16);
        }
    }

    public void drawOverlay(float partialTicks, Entity player, int x, int y) {
        BlockPos westPos;
        Block west;
        BlockPos eastPos;
        Block east;
        BlockPos southPos;
        Block south;
        int damage;
        float yaw = 0.0f;
        int dir = MathHelper.floor((double)((double)(player.rotationYaw * 4.0f / 360.0f) + 0.5)) & 3;
        switch (dir) {
            case 1: {
                yaw = 90.0f;
                break;
            }
            case 2: {
                yaw = -180.0f;
                break;
            }
            case 3: {
                yaw = -90.0f;
            }
        }
        BlockPos northPos = this.traceToBlock(partialTicks, yaw, player);
        Block north = this.getBlock(northPos);
        if (north != null && north != Blocks.AIR) {
            damage = this.getBlockDamage(northPos);
            if (damage != 0) {
                RenderUtil.drawRect(x + 16, y, x + 32, y + 16, 0x60FF0000);
            }
            this.drawBlock(north, x + 16, y);
        }
        if ((south = this.getBlock(southPos = this.traceToBlock(partialTicks, yaw - 180.0f, player))) != null && south != Blocks.AIR) {
            damage = this.getBlockDamage(southPos);
            if (damage != 0) {
                RenderUtil.drawRect(x + 16, y + 32, x + 32, y + 48, 0x60FF0000);
            }
            this.drawBlock(south, x + 16, y + 32);
        }
        if ((east = this.getBlock(eastPos = this.traceToBlock(partialTicks, yaw + 90.0f, player))) != null && east != Blocks.AIR) {
            damage = this.getBlockDamage(eastPos);
            if (damage != 0) {
                RenderUtil.drawRect(x + 32, y + 16, x + 48, y + 32, 0x60FF0000);
            }
            this.drawBlock(east, x + 32, y + 16);
        }
        if ((west = this.getBlock(westPos = this.traceToBlock(partialTicks, yaw - 90.0f, player))) != null && west != Blocks.AIR) {
            damage = this.getBlockDamage(westPos);
            if (damage != 0) {
                RenderUtil.drawRect(x, y + 16, x + 16, y + 32, 0x60FF0000);
            }
            this.drawBlock(west, x, y + 16);
        }
    }

    private int getBlockDamage(BlockPos pos) {
        for (DestroyBlockProgress destBlockProgress : AdditionalInfo.mc.renderGlobal.damagedBlocks.values()) {
            if (destBlockProgress.getPosition().getX() != pos.getX() || destBlockProgress.getPosition().getY() != pos.getY() || destBlockProgress.getPosition().getZ() != pos.getZ()) continue;
            return destBlockProgress.getPartialBlockDamage();
        }
        return 0;
    }

    private BlockPos traceToBlock(float partialTicks, float yaw) {
        Vec3d pos = XuluRenderUtilBoss.interpolateEntity((Entity)AdditionalInfo.mc.player, partialTicks);
        Vec3d dir = MathUtil.direction(yaw);
        return new BlockPos(pos.x + dir.x, pos.y, pos.z + dir.z);
    }

    private BlockPos traceToBlock(float partialTicks, float yaw, Entity player) {
        Vec3d pos = XuluRenderUtilBoss.interpolateEntity(player, partialTicks);
        Vec3d dir = MathUtil.direction(yaw);
        return new BlockPos(pos.x + dir.x, pos.y, pos.z + dir.z);
    }

    private Block getBlock(BlockPos pos) {
        Block block = AdditionalInfo.mc.world.getBlockState(pos).getBlock();
        if (block == Blocks.BEDROCK || block == Blocks.OBSIDIAN) {
            return block;
        }
        return Blocks.AIR;
    }

    private void drawBlock(Block block, float x, float y) {
        ItemStack stack = new ItemStack(block);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.translate((float)x, (float)y, (float)0.0f);
        AdditionalInfo.mc.getRenderItem().zLevel = 501.0f;
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
        AdditionalInfo.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableBlend();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.popMatrix();
    }

    public void renderInventory() {
        this.boxrender(this.invX.getValue() + this.fineinvX.getValue(), this.invY.getValue() + this.fineinvY.getValue());
        this.itemrender((NonNullList<ItemStack>)AdditionalInfo.mc.player.inventory.mainInventory, this.invX.getValue() + this.fineinvX.getValue(), this.invY.getValue() + this.fineinvY.getValue());
    }

    private static void preboxrender() {
        GL11.glPushMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear((int)256);
        GlStateManager.enableBlend();
        GlStateManager.color((float)255.0f, (float)255.0f, (float)255.0f, (float)255.0f);
    }

    private static void postboxrender() {
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
        GL11.glPopMatrix();
    }

    private static void preitemrender() {
        GL11.glPushMatrix();
        GL11.glDepthMask((boolean)true);
        GlStateManager.clear((int)256);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.scale((float)1.0f, (float)1.0f, (float)0.01f);
    }

    private static void postitemrender() {
        GlStateManager.scale((float)1.0f, (float)1.0f, (float)1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale((double)0.5, (double)0.5, (double)0.5);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale((float)2.0f, (float)2.0f, (float)2.0f);
        GL11.glPopMatrix();
    }

    private void boxrender(int x, int y) {
        AdditionalInfo.preboxrender();
        AdditionalInfo.mc.renderEngine.bindTexture(box);
        RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
        RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 54 + this.invH.getValue(), 500);
        RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
        AdditionalInfo.postboxrender();
    }

    private void itemrender(NonNullList<ItemStack> items, int x, int y) {
        int iX;
        int i;
        for (i = 0; i < items.size() - 9; ++i) {
            iX = x + i % 9 * 18 + 8;
            int iY = y + i / 9 * 18 + 18;
            ItemStack itemStack = (ItemStack)items.get(i + 9);
            AdditionalInfo.preitemrender();
            AdditionalInfo.mc.getRenderItem().zLevel = 501.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(itemStack, iX, iY);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(AdditionalInfo.mc.fontRenderer, itemStack, iX, iY, null);
            AdditionalInfo.mc.getRenderItem().zLevel = 0.0f;
            AdditionalInfo.postitemrender();
        }
        if (this.renderXCarry.getValue().booleanValue()) {
            for (i = 1; i < 5; ++i) {
                iX = x + (i + 4) % 9 * 18 + 8;
                ItemStack itemStack = ((Slot)AdditionalInfo.mc.player.inventoryContainer.inventorySlots.get(i)).getStack();
                if (itemStack == null || itemStack.isEmpty) continue;
                AdditionalInfo.preitemrender();
                AdditionalInfo.mc.getRenderItem().zLevel = 501.0f;
                RenderUtil.itemRender.renderItemAndEffectIntoGUI(itemStack, iX, y + 1);
                RenderUtil.itemRender.renderItemOverlayIntoGUI(AdditionalInfo.mc.fontRenderer, itemStack, iX, y + 1, null);
                AdditionalInfo.mc.getRenderItem().zLevel = 0.0f;
                AdditionalInfo.postitemrender();
            }
        }
    }

    public static void drawCompleteImage(int posX, int posY, int width, int height) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)posX, (float)posY, (float)0.0f);
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex3f((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex3f((float)0.0f, (float)height, (float)0.0f);
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex3f((float)width, (float)height, (float)0.0f);
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex3f((float)width, (float)0.0f, (float)0.0f);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    private static enum Direction {
        N,
        W,
        S,
        E;

    }

    public static enum Compass {
        NONE,
        CIRCLE,
        LINE;

    }

    public static enum TargetHudDesign {
        NORMAL,
        COMPACT;

    }
}

