//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.init.MobEffects
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.item.ItemEndCrystal
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.network.play.server.SPacketSpawnObject
 *  net.minecraft.util.CombatRules
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.event.events.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.features.modules.combat.AutoCrystal;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.EntityUtil;
import me.alpha432.oyvey.util.InventoryUtil;
import me.alpha432.oyvey.util.MathUtil;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.util.Timer;
import me.alpha432.oyvey.util.Util;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SecondAutoCrystal
extends Module {
    private final Timer placeTimer = new Timer();
    private final Timer breakTimer = new Timer();
    private final Timer preditTimer = new Timer();
    private final Timer manualTimer = new Timer();
    private final Setting<Integer> attackFactor = this.register(new Setting<Integer>("PredictDelay", 0, 0, 200));
    private final Setting<Integer> red = this.register(new Setting<Integer>("Red", 0, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<Integer>("Green", 255, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 0, 0, 255));
    private final Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Integer>("Boxalpha", 125, 0, 255));
    private final Setting<Float> lineWidth = this.register(new Setting<Float>("Linewidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    public Setting<Boolean> sound = this.register(new Setting<Boolean>("Sequential", true));
    public Setting<Boolean> place = this.register(new Setting<Boolean>("Place", true));
    public Setting<Float> placeDelay = this.register(new Setting<Float>("Delays", Float.valueOf(4.0f), Float.valueOf(0.0f), Float.valueOf(300.0f)));
    public Setting<Float> placeRange = this.register(new Setting<Float>("Range", Float.valueOf(4.0f), Float.valueOf(0.1f), Float.valueOf(7.0f)));
    public Setting<Boolean> explode = this.register(new Setting<Boolean>("Break", true));
    public Setting<Boolean> packetBreak = this.register(new Setting<Boolean>("BreakPackets[MAX]", true));
    public Setting<Boolean> predicts = this.register(new Setting<Boolean>("Infered", true));
    public Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    public Setting<Float> breakDelay = this.register(new Setting<Float>("Delays", Float.valueOf(4.0f), Float.valueOf(0.0f), Float.valueOf(300.0f)));
    public Setting<Float> breakRange = this.register(new Setting<Float>("Brange", Float.valueOf(4.0f), Float.valueOf(0.1f), Float.valueOf(7.0f)));
    public Setting<Float> breakWallRange = this.register(new Setting<Float>("Breakwallrange", Float.valueOf(4.0f), Float.valueOf(0.1f), Float.valueOf(7.0f)));
    public Setting<Boolean> opPlace = this.register(new Setting<Boolean>("1.13 place", true));
    public Setting<Boolean> suicide = this.register(new Setting<Boolean>("AntiSelfKill", true));
    public Setting<Boolean> autoswitch = this.register(new Setting<Boolean>("Autoswitch", true));
    public Setting<SwitchMode> switchmode = this.register(new Setting<SwitchMode>("Awitchmode", SwitchMode.Normal, v -> this.autoswitch.getValue()));
    public Setting<Boolean> silentSwitch = this.register(new Setting<Boolean>("Silent", Boolean.valueOf(true), v -> this.switchmode.getValue() == SwitchMode.Silent));
    public Setting<Boolean> ignoreUseAmount = this.register(new Setting<Boolean>("UseAmount[OverLook]", true));
    public Setting<Integer> wasteAmount = this.register(new Setting<Integer>("UseAmount", 4, 1, 5));
    public Setting<Boolean> facePlaceSword = this.register(new Setting<Boolean>("SwordFP", true));
    public Setting<Float> targetRange = this.register(new Setting<Float>("EnemyRange", Float.valueOf(4.0f), Float.valueOf(1.0f), Float.valueOf(12.0f)));
    public Setting<Float> minDamage = this.register(new Setting<Float>("MinDMG", Float.valueOf(4.0f), Float.valueOf(0.1f), Float.valueOf(20.0f)));
    public Setting<Float> facePlace = this.register(new Setting<Float>("FaceHP", Float.valueOf(4.0f), Float.valueOf(0.0f), Float.valueOf(36.0f)));
    public Setting<Float> breakMaxSelfDamage = this.register(new Setting<Float>("Breakmaxself", Float.valueOf(4.0f), Float.valueOf(0.1f), Float.valueOf(12.0f)));
    public Setting<Float> breakMinDmg = this.register(new Setting<Float>("Breakmindmg", Float.valueOf(4.0f), Float.valueOf(0.1f), Float.valueOf(7.0f)));
    public Setting<Float> minArmor = this.register(new Setting<Float>("MinArmor", Float.valueOf(4.0f), Float.valueOf(0.1f), Float.valueOf(80.0f)));
    public Setting<SwingMode> swingMode = this.register(new Setting<SwingMode>("Swing", SwingMode.MainHand));
    public Setting<Boolean> render = this.register(new Setting<Boolean>("Render", true));
    public Setting<Boolean> renderDmg = this.register(new Setting<Boolean>("renderdmg", true));
    public Setting<Boolean> box = this.register(new Setting<Boolean>("box", true));
    public Setting<Boolean> outline = this.register(new Setting<Boolean>("outline", true));
    private final Setting<Integer> cRed = this.register(new Setting<Object>("8-red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue()));
    private final Setting<Integer> cGreen = this.register(new Setting<Object>("8.8-green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue()));
    private final Setting<Integer> cBlue = this.register(new Setting<Object>("8-blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue()));
    private final Setting<Integer> cAlpha = this.register(new Setting<Object>("8-alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue()));
    EntityEnderCrystal crystal;
    private EntityLivingBase target;
    private BlockPos pos;
    private int hotBarSlot;
    private boolean armor;
    private boolean armorTarget;
    private int crystalCount;
    private int predictWait;
    private int predictPackets;
    private boolean packetCalc;
    private float yaw = 0.0f;
    private EntityLivingBase realTarget;
    private int predict;
    private float pitch = 0.0f;
    private boolean rotating = false;

    public SecondAutoCrystal() {
        super("CrystalAura[Simple]", "Slightly Improved[Choose your fav]", Module.Category.COMBAT, true, false, false);
    }

    public static List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                int y = sphere ? cy - (int)r : cy;
                while (true) {
                    float f = sphere ? (float)cy + r : (float)(cy + h);
                    float f2 = f;
                    if (!((float)y < f)) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (!(!(dist < (double)(r * r)) || hollow && dist < (double)((r - 1.0f) * (r - 1.0f)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && this.rotate.getValue().booleanValue() && this.rotating && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            packet.yaw = this.yaw;
            packet.pitch = this.pitch;
            this.rotating = false;
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGH, receiveCanceled=true)
    public void onSoundPacket(PacketEvent.Receive event) {
        SPacketSoundEffect packet2;
        if (AutoCrystal.fullNullCheck()) {
            return;
        }
        if (event.getPacket() instanceof SPacketSoundEffect && this.sound.getValue().booleanValue() && (packet2 = (SPacketSoundEffect)event.getPacket()).getCategory() == SoundCategory.BLOCKS && packet2.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
            ArrayList entities = new ArrayList(SecondAutoCrystal.mc.world.loadedEntityList);
            int size = entities.size();
            for (int i = 0; i < size; ++i) {
                Entity entity = (Entity)entities.get(i);
                if (!(entity instanceof EntityEnderCrystal) || !(entity.getDistanceSq(packet2.getX(), packet2.getY(), packet2.getZ()) < 36.0)) continue;
                entity.setDead();
            }
        }
    }

    private void rotateTo(Entity entity) {
        if (this.rotate.getValue().booleanValue()) {
            float[] angle = MathUtil.calcAngle(SecondAutoCrystal.mc.player.getPositionEyes(Util.mc.getRenderPartialTicks()), entity.getPositionVector());
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }

    private void rotateToPos(BlockPos pos) {
        if (this.rotate.getValue().booleanValue()) {
            float[] angle = MathUtil.calcAngle(SecondAutoCrystal.mc.player.getPositionEyes(Util.mc.getRenderPartialTicks()), new Vec3d((double)((float)pos.getX() + 0.5f), (double)((float)pos.getY() - 0.5f), (double)((float)pos.getZ() + 0.5f)));
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }

    @Override
    public void onEnable() {
        this.placeTimer.reset();
        this.breakTimer.reset();
        this.predictWait = 0;
        this.hotBarSlot = -1;
        this.pos = null;
        this.crystal = null;
        this.predict = 0;
        this.predictPackets = 1;
        this.target = null;
        this.packetCalc = false;
        this.realTarget = null;
        this.armor = false;
        this.armorTarget = false;
    }

    @Override
    public void onDisable() {
        this.rotating = false;
    }

    @Override
    public void onTick() {
        this.onCrystal();
    }

    @Override
    public String getDisplayInfo() {
        if (this.realTarget != null) {
            return this.realTarget.getName();
        }
        return null;
    }

    public void onCrystal() {
        if (SecondAutoCrystal.mc.world == null || SecondAutoCrystal.mc.player == null) {
            return;
        }
        this.realTarget = null;
        this.manualBreaker();
        this.crystalCount = 0;
        if (!this.ignoreUseAmount.getValue().booleanValue()) {
            for (Entity crystal : SecondAutoCrystal.mc.world.loadedEntityList) {
                if (!(crystal instanceof EntityEnderCrystal) || !this.IsValidCrystal(crystal)) continue;
                boolean count = false;
                double damage = this.calculateDamage((double)this.target.getPosition().getX() + 0.5, (double)this.target.getPosition().getY() + 1.0, (double)this.target.getPosition().getZ() + 0.5, (Entity)this.target);
                if (damage >= (double)this.minDamage.getValue().floatValue()) {
                    count = true;
                }
                if (!count) continue;
                ++this.crystalCount;
            }
        }
        this.hotBarSlot = -1;
        if (SecondAutoCrystal.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            int crystalSlot;
            int n = crystalSlot = SecondAutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL ? SecondAutoCrystal.mc.player.inventory.currentItem : -1;
            if (crystalSlot == -1) {
                for (int l = 0; l < 9; ++l) {
                    if (SecondAutoCrystal.mc.player.inventory.getStackInSlot(l).getItem() != Items.END_CRYSTAL) continue;
                    crystalSlot = l;
                    this.hotBarSlot = l;
                    break;
                }
            }
            if (crystalSlot == -1) {
                this.pos = null;
                this.target = null;
                return;
            }
        }
        if (SecondAutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && SecondAutoCrystal.mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL) {
            this.pos = null;
            this.target = null;
            return;
        }
        if (this.target == null) {
            this.target = this.getTarget();
        }
        if (this.target == null) {
            this.crystal = null;
            return;
        }
        if (this.target.getDistance((Entity)SecondAutoCrystal.mc.player) > 12.0f) {
            this.crystal = null;
            this.target = null;
        }
        this.crystal = SecondAutoCrystal.mc.world.loadedEntityList.stream().filter(this::IsValidCrystal).map(p_Entity -> (EntityEnderCrystal)p_Entity).min(Comparator.comparing(p_Entity -> Float.valueOf(this.target.getDistance((Entity)p_Entity)))).orElse(null);
        if (this.crystal != null && this.explode.getValue().booleanValue() && this.breakTimer.passedMs(this.breakDelay.getValue().longValue())) {
            this.breakTimer.reset();
            if (this.packetBreak.getValue().booleanValue()) {
                this.rotateTo((Entity)this.crystal);
                SecondAutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketUseEntity((Entity)this.crystal));
            } else {
                this.rotateTo((Entity)this.crystal);
                SecondAutoCrystal.mc.playerController.attackEntity((EntityPlayer)SecondAutoCrystal.mc.player, (Entity)this.crystal);
            }
            if (this.swingMode.getValue() == SwingMode.MainHand) {
                SecondAutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
            } else if (this.swingMode.getValue() == SwingMode.OffHand) {
                SecondAutoCrystal.mc.player.swingArm(EnumHand.OFF_HAND);
            }
        }
        if (this.placeTimer.passedMs(this.placeDelay.getValue().longValue()) && this.place.getValue().booleanValue()) {
            this.placeTimer.reset();
            double damage = 0.5;
            for (BlockPos blockPos : this.placePostions(this.placeRange.getValue().floatValue())) {
                double d;
                double targetRange;
                if (blockPos == null || this.target == null || !SecondAutoCrystal.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos)).isEmpty() || (targetRange = this.target.getDistance((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ())) > (double)this.targetRange.getValue().floatValue() || this.target.isDead || this.target.getHealth() + this.target.getAbsorptionAmount() <= 0.0f) continue;
                double targetDmg = this.calculateDamage((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 1.0, (double)blockPos.getZ() + 0.5, (Entity)this.target);
                this.armor = false;
                for (ItemStack is : this.target.getArmorInventoryList()) {
                    float green = ((float)is.getMaxDamage() - (float)is.getItemDamage()) / (float)is.getMaxDamage();
                    float red = 1.0f - green;
                    int dmg = 100 - (int)(red * 100.0f);
                    if (!((float)dmg <= this.minArmor.getValue().floatValue())) continue;
                    this.armor = true;
                }
                if (targetDmg < (double)this.minDamage.getValue().floatValue() && (this.facePlaceSword.getValue() != false ? this.target.getAbsorptionAmount() + this.target.getHealth() > this.facePlace.getValue().floatValue() : SecondAutoCrystal.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword || this.target.getAbsorptionAmount() + this.target.getHealth() > this.facePlace.getValue().floatValue()) && (this.facePlaceSword.getValue() == false ? SecondAutoCrystal.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword || !this.armor : !this.armor)) continue;
                double selfDmg = this.calculateDamage((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 1.0, (double)blockPos.getZ() + 0.5, (Entity)SecondAutoCrystal.mc.player);
                if (d + (this.suicide.getValue() != false ? 2.0 : 0.5) >= (double)(SecondAutoCrystal.mc.player.getHealth() + SecondAutoCrystal.mc.player.getAbsorptionAmount()) && selfDmg >= targetDmg && targetDmg < (double)(this.target.getHealth() + this.target.getAbsorptionAmount()) || !(damage < targetDmg)) continue;
                this.pos = blockPos;
                damage = targetDmg;
            }
            if (damage == 0.5) {
                this.pos = null;
                this.target = null;
                this.realTarget = null;
                return;
            }
            this.realTarget = this.target;
            if (this.hotBarSlot != -1 && this.autoswitch.getValue().booleanValue() && !SecondAutoCrystal.mc.player.isPotionActive(MobEffects.WEAKNESS) && this.switchmode.getValue() == SwitchMode.Normal && !this.silentSwitch.getValue().booleanValue()) {
                SecondAutoCrystal.mc.player.inventory.currentItem = this.hotBarSlot;
            }
            int slot = InventoryUtil.findHotbarBlock(ItemEndCrystal.class);
            int old = Util.mc.player.inventory.currentItem;
            EnumHand hand = null;
            if (this.switchmode.getValue() == SwitchMode.Silent && slot != -1) {
                if (Util.mc.player.isHandActive() && this.silentSwitch.getValue().booleanValue()) {
                    hand = Util.mc.player.getActiveHand();
                }
                Util.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
            }
            if (!this.ignoreUseAmount.getValue().booleanValue()) {
                int crystalLimit = this.wasteAmount.getValue();
                if (this.crystalCount >= crystalLimit) {
                    return;
                }
                if (damage < (double)this.minDamage.getValue().floatValue()) {
                    crystalLimit = 1;
                }
                if (this.crystalCount < crystalLimit && this.pos != null) {
                    this.rotateToPos(this.pos);
                    SecondAutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos, EnumFacing.UP, SecondAutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                }
            } else if (this.pos != null) {
                this.rotateToPos(this.pos);
                SecondAutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos, EnumFacing.UP, SecondAutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            }
            if (this.switchmode.getValue() == SwitchMode.Silent && slot != -1) {
                if (this.silentSwitch.getValue().booleanValue() && hand != null) {
                    Util.mc.player.setActiveHand(hand);
                }
                Util.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(old));
            }
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST, receiveCanceled=true)
    public void onPacketReceive(PacketEvent.Receive event) {
        SPacketSpawnObject packet;
        if (event.getPacket() instanceof SPacketSpawnObject && (packet = (SPacketSpawnObject)event.getPacket()).getType() == 51 && this.predicts.getValue().booleanValue() && this.preditTimer.passedMs(this.attackFactor.getValue().longValue()) && this.predicts.getValue().booleanValue() && this.explode.getValue().booleanValue() && this.packetBreak.getValue().booleanValue() && this.target != null) {
            if (!this.isPredicting(packet)) {
                return;
            }
            CPacketUseEntity predict = new CPacketUseEntity();
            predict.entityId = packet.getEntityID();
            predict.action = CPacketUseEntity.Action.ATTACK;
            SecondAutoCrystal.mc.player.connection.sendPacket((Packet)predict);
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.pos != null && this.render.getValue().booleanValue() && this.target != null) {
            RenderUtil.drawBoxESP(this.pos, ClickGui.getInstance().rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.outline.getValue(), ClickGui.getInstance().rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true);
            if (this.renderDmg.getValue().booleanValue()) {
                double renderDamage = this.calculateDamage((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 1.0, (double)this.pos.getZ() + 0.5, (Entity)this.target);
                RenderUtil.drawText(this.pos, (Math.floor(renderDamage) == renderDamage ? Integer.valueOf((int)renderDamage) : String.format("%.1f", renderDamage)) + "");
            }
        }
    }

    private boolean isPredicting(SPacketSpawnObject packet) {
        double d;
        BlockPos packPos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
        if (SecondAutoCrystal.mc.player.getDistance(packet.getX(), packet.getY(), packet.getZ()) > (double)this.breakRange.getValue().floatValue()) {
            return false;
        }
        if (!this.canSeePos(packPos) && SecondAutoCrystal.mc.player.getDistance(packet.getX(), packet.getY(), packet.getZ()) > (double)this.breakWallRange.getValue().floatValue()) {
            return false;
        }
        double targetDmg = this.calculateDamage(packet.getX() + 0.5, packet.getY() + 1.0, packet.getZ() + 0.5, (Entity)this.target);
        if (EntityUtil.isInHole((Entity)SecondAutoCrystal.mc.player) && targetDmg >= 1.0) {
            return true;
        }
        double selfDmg = this.calculateDamage(packet.getX() + 0.5, packet.getY() + 1.0, packet.getZ() + 0.5, (Entity)SecondAutoCrystal.mc.player);
        double d2 = d = this.suicide.getValue() != false ? 2.0 : 0.5;
        if (selfDmg + d < (double)(SecondAutoCrystal.mc.player.getHealth() + SecondAutoCrystal.mc.player.getAbsorptionAmount()) && targetDmg >= (double)(this.target.getAbsorptionAmount() + this.target.getHealth())) {
            return true;
        }
        this.armorTarget = false;
        for (ItemStack is : this.target.getArmorInventoryList()) {
            float green = ((float)is.getMaxDamage() - (float)is.getItemDamage()) / (float)is.getMaxDamage();
            float red = 1.0f - green;
            int dmg = 100 - (int)(red * 100.0f);
            if (!((float)dmg <= this.minArmor.getValue().floatValue())) continue;
            this.armorTarget = true;
        }
        if (targetDmg >= (double)this.breakMinDmg.getValue().floatValue() && selfDmg <= (double)this.breakMaxSelfDamage.getValue().floatValue()) {
            return true;
        }
        return EntityUtil.isInHole((Entity)this.target) && this.target.getHealth() + this.target.getAbsorptionAmount() <= this.facePlace.getValue().floatValue();
    }

    private boolean IsValidCrystal(Entity p_Entity) {
        double d;
        if (p_Entity == null) {
            return false;
        }
        if (!(p_Entity instanceof EntityEnderCrystal)) {
            return false;
        }
        if (this.target == null) {
            return false;
        }
        if (p_Entity.getDistance((Entity)SecondAutoCrystal.mc.player) > this.breakRange.getValue().floatValue()) {
            return false;
        }
        if (!SecondAutoCrystal.mc.player.canEntityBeSeen(p_Entity) && p_Entity.getDistance((Entity)SecondAutoCrystal.mc.player) > this.breakWallRange.getValue().floatValue()) {
            return false;
        }
        if (this.target.isDead || this.target.getHealth() + this.target.getAbsorptionAmount() <= 0.0f) {
            return false;
        }
        double targetDmg = this.calculateDamage((double)p_Entity.getPosition().getX() + 0.5, (double)p_Entity.getPosition().getY() + 1.0, (double)p_Entity.getPosition().getZ() + 0.5, (Entity)this.target);
        if (EntityUtil.isInHole((Entity)SecondAutoCrystal.mc.player) && targetDmg >= 1.0) {
            return true;
        }
        double selfDmg = this.calculateDamage((double)p_Entity.getPosition().getX() + 0.5, (double)p_Entity.getPosition().getY() + 1.0, (double)p_Entity.getPosition().getZ() + 0.5, (Entity)SecondAutoCrystal.mc.player);
        double d2 = d = this.suicide.getValue() != false ? 2.0 : 0.5;
        if (selfDmg + d < (double)(SecondAutoCrystal.mc.player.getHealth() + SecondAutoCrystal.mc.player.getAbsorptionAmount()) && targetDmg >= (double)(this.target.getAbsorptionAmount() + this.target.getHealth())) {
            return true;
        }
        this.armorTarget = false;
        for (ItemStack is : this.target.getArmorInventoryList()) {
            float green = ((float)is.getMaxDamage() - (float)is.getItemDamage()) / (float)is.getMaxDamage();
            float red = 1.0f - green;
            int dmg = 100 - (int)(red * 100.0f);
            if (!((float)dmg <= this.minArmor.getValue().floatValue())) continue;
            this.armorTarget = true;
        }
        if (targetDmg >= (double)this.breakMinDmg.getValue().floatValue() && selfDmg <= (double)this.breakMaxSelfDamage.getValue().floatValue()) {
            return true;
        }
        return EntityUtil.isInHole((Entity)this.target) && this.target.getHealth() + this.target.getAbsorptionAmount() <= this.facePlace.getValue().floatValue();
    }

    EntityPlayer getTarget() {
        EntityPlayer closestPlayer = null;
        for (EntityPlayer entity : SecondAutoCrystal.mc.world.playerEntities) {
            if (SecondAutoCrystal.mc.player == null || SecondAutoCrystal.mc.player.isDead || entity.isDead || entity == SecondAutoCrystal.mc.player || OyVey.friendManager.isFriend(entity.getName()) || entity.getDistance((Entity)SecondAutoCrystal.mc.player) > 12.0f) continue;
            this.armorTarget = false;
            for (ItemStack is : entity.getArmorInventoryList()) {
                float green = ((float)is.getMaxDamage() - (float)is.getItemDamage()) / (float)is.getMaxDamage();
                float red = 1.0f - green;
                int dmg = 100 - (int)(red * 100.0f);
                if (!((float)dmg <= this.minArmor.getValue().floatValue())) continue;
                this.armorTarget = true;
            }
            if (EntityUtil.isInHole((Entity)entity) && entity.getAbsorptionAmount() + entity.getHealth() > this.facePlace.getValue().floatValue() && !this.armorTarget && this.minDamage.getValue().floatValue() > 2.2f) continue;
            if (closestPlayer == null) {
                closestPlayer = entity;
                continue;
            }
            if (!(closestPlayer.getDistance((Entity)SecondAutoCrystal.mc.player) > entity.getDistance((Entity)SecondAutoCrystal.mc.player))) continue;
            closestPlayer = entity;
        }
        return closestPlayer;
    }

    private void manualBreaker() {
        RayTraceResult result;
        if (this.manualTimer.passedMs(200L) && SecondAutoCrystal.mc.gameSettings.keyBindUseItem.isKeyDown() && SecondAutoCrystal.mc.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE && SecondAutoCrystal.mc.player.inventory.getCurrentItem().getItem() != Items.GOLDEN_APPLE && SecondAutoCrystal.mc.player.inventory.getCurrentItem().getItem() != Items.BOW && SecondAutoCrystal.mc.player.inventory.getCurrentItem().getItem() != Items.EXPERIENCE_BOTTLE && (result = SecondAutoCrystal.mc.objectMouseOver) != null) {
            if (result.typeOfHit.equals((Object)RayTraceResult.Type.ENTITY)) {
                Entity entity = result.entityHit;
                if (entity instanceof EntityEnderCrystal) {
                    if (this.packetBreak.getValue().booleanValue()) {
                        SecondAutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity));
                    } else {
                        SecondAutoCrystal.mc.playerController.attackEntity((EntityPlayer)SecondAutoCrystal.mc.player, entity);
                    }
                    this.manualTimer.reset();
                }
            } else if (result.typeOfHit.equals((Object)RayTraceResult.Type.BLOCK)) {
                BlockPos mousePos = new BlockPos((double)SecondAutoCrystal.mc.objectMouseOver.getBlockPos().getX(), (double)SecondAutoCrystal.mc.objectMouseOver.getBlockPos().getY() + 1.0, (double)SecondAutoCrystal.mc.objectMouseOver.getBlockPos().getZ());
                for (Entity target : SecondAutoCrystal.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(mousePos))) {
                    if (!(target instanceof EntityEnderCrystal)) continue;
                    if (this.packetBreak.getValue().booleanValue()) {
                        SecondAutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(target));
                    } else {
                        SecondAutoCrystal.mc.playerController.attackEntity((EntityPlayer)SecondAutoCrystal.mc.player, target);
                    }
                    this.manualTimer.reset();
                }
            }
        }
    }

    private boolean canSeePos(BlockPos pos) {
        return SecondAutoCrystal.mc.world.rayTraceBlocks(new Vec3d(SecondAutoCrystal.mc.player.posX, SecondAutoCrystal.mc.player.posY + (double)SecondAutoCrystal.mc.player.getEyeHeight(), SecondAutoCrystal.mc.player.posZ), new Vec3d((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), false, true, false) == null;
    }

    private NonNullList<BlockPos> placePostions(float placeRange) {
        NonNullList positions = NonNullList.create();
        positions.addAll((Collection)SecondAutoCrystal.getSphere(new BlockPos(Math.floor(SecondAutoCrystal.mc.player.posX), Math.floor(SecondAutoCrystal.mc.player.posY), Math.floor(SecondAutoCrystal.mc.player.posZ)), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> this.canPlaceCrystal((BlockPos)pos, true)).collect(Collectors.toList()));
        return positions;
    }

    private boolean canPlaceCrystal(BlockPos blockPos, boolean specialEntityCheck) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        try {
            if (!this.opPlace.getValue().booleanValue()) {
                if (SecondAutoCrystal.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && SecondAutoCrystal.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                    return false;
                }
                if (SecondAutoCrystal.mc.world.getBlockState(boost).getBlock() != Blocks.AIR || SecondAutoCrystal.mc.world.getBlockState(boost2).getBlock() != Blocks.AIR) {
                    return false;
                }
                if (!specialEntityCheck) {
                    return SecondAutoCrystal.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && SecondAutoCrystal.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
                }
                for (Entity entity : SecondAutoCrystal.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
                for (Entity entity : SecondAutoCrystal.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
            } else {
                if (SecondAutoCrystal.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && SecondAutoCrystal.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                    return false;
                }
                if (SecondAutoCrystal.mc.world.getBlockState(boost).getBlock() != Blocks.AIR) {
                    return false;
                }
                if (!specialEntityCheck) {
                    return SecondAutoCrystal.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty();
                }
                for (Entity entity : SecondAutoCrystal.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
            }
        }
        catch (Exception ignored) {
            return false;
        }
        return true;
    }

    private float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 12.0f;
        double distancedsize = entity.getDistance(posX, posY, posZ) / 12.0;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        }
        catch (Exception exception) {
            // empty catch block
        }
        double v = (1.0 - distancedsize) * blockDensity;
        float damage = (int)((v * v + v) / 2.0 * 7.0 * 12.0 + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = this.getBlastReduction((EntityLivingBase)entity, this.getDamageMultiplied(damage), new Explosion((World)SecondAutoCrystal.mc.world, null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }

    private float getBlastReduction(EntityLivingBase entity, float damageI, Explosion explosion) {
        float damage = damageI;
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer)entity;
            DamageSource ds = DamageSource.causeExplosionDamage((Explosion)explosion);
            damage = CombatRules.getDamageAfterAbsorb((float)damage, (float)ep.getTotalArmorValue(), (float)((float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue()));
            int k = 0;
            try {
                k = EnchantmentHelper.getEnchantmentModifierDamage((Iterable)ep.getArmorInventoryList(), (DamageSource)ds);
            }
            catch (Exception exception) {
                // empty catch block
            }
            float f = MathHelper.clamp((float)k, (float)0.0f, (float)20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(MobEffects.RESISTANCE)) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb((float)damage, (float)entity.getTotalArmorValue(), (float)((float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue()));
        return damage;
    }

    private float getDamageMultiplied(float damage) {
        int diff = SecondAutoCrystal.mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0.0f : (diff == 2 ? 1.0f : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static enum SwitchMode {
        Normal,
        Silent;

    }

    public static enum SwingMode {
        MainHand,
        OffHand,
        None;

    }
}

