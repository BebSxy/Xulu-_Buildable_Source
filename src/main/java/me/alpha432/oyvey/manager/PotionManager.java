//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 */
package me.alpha432.oyvey.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.alpha432.oyvey.features.Feature;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionManager
extends Feature {
    private final Map<EntityPlayer, PotionList> potions = new ConcurrentHashMap<EntityPlayer, PotionList>();

    public List<PotionEffect> getOwnPotions() {
        return this.getPlayerPotions((EntityPlayer)PotionManager.mc.player);
    }

    public List<PotionEffect> getPlayerPotions(EntityPlayer player) {
        PotionList list = this.potions.get((Object)player);
        List<PotionEffect> potions = new ArrayList<PotionEffect>();
        if (list != null) {
            potions = list.getEffects();
        }
        return potions;
    }

    public PotionEffect[] getImportantPotions(EntityPlayer player) {
        PotionEffect[] array = new PotionEffect[3];
        for (PotionEffect effect : this.getPlayerPotions(player)) {
            Potion potion = effect.getPotion();
            switch (I18n.format((String)potion.getName(), (Object[])new Object[0]).toLowerCase()) {
                case "Strength": {
                    array[0] = effect;
                }
                case "Weakness": {
                    array[1] = effect;
                }
                case "Speed": {
                    array[2] = effect;
                }
            }
        }
        return array;
    }

    public String getPotionString(PotionEffect effect) {
        Potion potion = effect.getPotion();
        return I18n.format((String)potion.getName(), (Object[])new Object[0]) + " " + (effect.getAmplifier() + 1) + " " + (Object)ChatFormatting.WHITE + Potion.getPotionDurationString((PotionEffect)effect, (float)1.0f);
    }

    public String getColoredPotionString(PotionEffect effect) {
        return this.getPotionString(effect);
    }

    public String getTextRadarPotionWithDuration(EntityPlayer player) {
        PotionEffect[] array = this.getImportantPotions(player);
        PotionEffect strength = array[0];
        PotionEffect weakness = array[1];
        PotionEffect speed = array[2];
        return "" + (strength != null ? "\u00a7c S" + (strength.getAmplifier() + 1) + " " + Potion.getPotionDurationString((PotionEffect)strength, (float)1.0f) : "") + (weakness != null ? "\u00a78 W " + Potion.getPotionDurationString((PotionEffect)weakness, (float)1.0f) : "") + (speed != null ? "\u00a7b S" + (speed.getAmplifier() + 1) + " " + Potion.getPotionDurationString((PotionEffect)weakness, (float)1.0f) : "");
    }

    public String getTextRadarPotion(EntityPlayer player) {
        PotionEffect[] array = this.getImportantPotions(player);
        PotionEffect strength = array[0];
        PotionEffect weakness = array[1];
        PotionEffect speed = array[2];
        return "" + (strength != null ? "\u00a7c S" + (strength.getAmplifier() + 1) + " " : "") + (weakness != null ? "\u00a78 W " : "") + (speed != null ? "\u00a7b S" + (speed.getAmplifier() + 1) + " " : "");
    }

    public static class PotionList {
        private final List<PotionEffect> effects = new ArrayList<PotionEffect>();

        public void addEffect(PotionEffect effect) {
            if (effect != null) {
                this.effects.add(effect);
            }
        }

        public List<PotionEffect> getEffects() {
            return this.effects;
        }
    }
}

