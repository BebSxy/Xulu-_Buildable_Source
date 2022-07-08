//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.SoundEvent
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.oyvey.features.modules.Flex;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashSet;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.combat.AutoCrystal;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoLag
extends Module {
    private static final HashSet<Object> BLACKLIST = Sets.newHashSet((Object[])((Object[])new SoundEvent[]{SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundEvents.ITEM_ARMOR_EQIIP_ELYTRA, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER}));
    private static NoLag instance;
    public Setting<Boolean> crystals = this.register(new Setting<Boolean>("C-AntiLag", true));
    public Setting<Boolean> armor = this.register(new Setting<Boolean>("A-AntiLag", true));
    public Setting<Float> soundRange = this.register(new Setting<Float>("LagDisableRange", Float.valueOf(12.0f), Float.valueOf(0.0f), Float.valueOf(12.0f)));

    public NoLag() {
        super("-Procrastinate", "Tries to Disable Lags", Module.Category.Flex, true, false, false);
        instance = this;
    }

    public static NoLag getInstance() {
        if (instance == null) {
            instance = new NoLag();
        }
        return instance;
    }

    public static void removeEntities(SPacketSoundEffect packet, float range) {
        BlockPos pos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
        ArrayList<Entity> toRemove = new ArrayList<Entity>();
        if (NoLag.fullNullCheck()) {
            return;
        }
        for (Entity entity : NoLag.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal) || entity.getDistanceSq(pos) > MathUtil.square(range)) continue;
            toRemove.add(entity);
        }
        for (Entity entity : toRemove) {
            entity.setDead();
        }
    }

    @SubscribeEvent
    public void onPacketReceived(PacketEvent.Receive event) {
        if (event != null && event.getPacket() != null && NoLag.mc.player != null && NoLag.mc.world != null && event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
            if (this.crystals.getValue().booleanValue() && packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE && (AutoCrystal.getInstance().isOff() || !AutoCrystal.getInstance().sound.getValue().booleanValue() && AutoCrystal.getInstance().threadMode.getValue() != AutoCrystal.ThreadMode.SOUND)) {
                NoLag.removeEntities(packet, this.soundRange.getValue().floatValue());
            }
            if (BLACKLIST.contains((Object)packet.getSound()) && this.armor.getValue().booleanValue()) {
                event.setCanceled(true);
            }
        }
    }
}

