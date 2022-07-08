//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.monster.EntityGhast
 *  net.minecraft.entity.passive.EntityDonkey
 *  net.minecraft.entity.passive.EntityLlama
 *  net.minecraft.entity.passive.EntityMule
 *  net.minecraft.init.SoundEvents
 */
package me.alpha432.oyvey.features.modules.misc;

import java.util.HashSet;
import java.util.Set;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.init.SoundEvents;

public class DonkeyNotifier
extends Module {
    private final Set<Entity> ghasts = new HashSet<Entity>();
    private final Set<Entity> donkeys = new HashSet<Entity>();
    private final Set<Entity> mules = new HashSet<Entity>();
    private final Set<Entity> llamas = new HashSet<Entity>();
    public Setting<Boolean> Chat = this.register(new Setting<Boolean>("Chat", true));
    public Setting<Boolean> Sound = this.register(new Setting<Boolean>("Sound", true));
    public Setting<Boolean> Ghasts = this.register(new Setting<Boolean>("Ghasts", true));
    public Setting<Boolean> Donkeys = this.register(new Setting<Boolean>("Donkeys", true));
    public Setting<Boolean> Mules = this.register(new Setting<Boolean>("Mules", true));
    public Setting<Boolean> Llamas = this.register(new Setting<Boolean>("Llamas", true));

    public DonkeyNotifier() {
        super("Entitynotifier", "Notifies you about entities such as...", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onEnable() {
        this.ghasts.clear();
        this.donkeys.clear();
        this.mules.clear();
        this.llamas.clear();
    }

    @Override
    public void onUpdate() {
        if (this.Ghasts.getValue().booleanValue()) {
            for (Entity entity : DonkeyNotifier.mc.world.getLoadedEntityList()) {
                if (!(entity instanceof EntityGhast) || this.ghasts.contains((Object)entity)) continue;
                if (this.Chat.getValue().booleanValue()) {
                    Command.sendMessage("Ghast at: " + Math.round(entity.getPosition().getX()) + "x, " + Math.round(entity.getPosition().getY()) + "y, " + Math.round(entity.getPosition().getZ()) + "z.");
                }
                this.ghasts.add(entity);
                if (!this.Sound.getValue().booleanValue()) continue;
                DonkeyNotifier.mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f);
            }
        }
        if (this.Donkeys.getValue().booleanValue()) {
            for (Entity entity : DonkeyNotifier.mc.world.getLoadedEntityList()) {
                if (!(entity instanceof EntityDonkey) || this.donkeys.contains((Object)entity)) continue;
                if (this.Chat.getValue().booleanValue()) {
                    Command.sendMessage("Donkey at: " + Math.round(entity.getPosition().getX()) + "x, " + Math.round(entity.getPosition().getY()) + "y, " + Math.round(entity.getPosition().getZ()) + "z.");
                }
                this.donkeys.add(entity);
                if (!this.Sound.getValue().booleanValue()) continue;
                DonkeyNotifier.mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f);
            }
        }
        if (this.Mules.getValue().booleanValue()) {
            for (Entity entity : DonkeyNotifier.mc.world.getLoadedEntityList()) {
                if (!(entity instanceof EntityMule) || this.mules.contains((Object)entity)) continue;
                if (this.Chat.getValue().booleanValue()) {
                    Command.sendMessage("Mule at: " + Math.round(entity.getPosition().getX()) + "x, " + Math.round(entity.getPosition().getY()) + "y, " + Math.round(entity.getPosition().getZ()) + "z.");
                }
                this.mules.add(entity);
                if (!this.Sound.getValue().booleanValue()) continue;
                DonkeyNotifier.mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f);
            }
        }
        if (this.Llamas.getValue().booleanValue()) {
            for (Entity entity : DonkeyNotifier.mc.world.getLoadedEntityList()) {
                if (!(entity instanceof EntityLlama) || this.llamas.contains((Object)entity)) continue;
                if (this.Chat.getValue().booleanValue()) {
                    Command.sendMessage("Llama at: " + Math.round(entity.getPosition().getX()) + "x, " + Math.round(entity.getPosition().getY()) + "y, " + Math.round(entity.getPosition().getZ()) + "z.");
                }
                this.llamas.add(entity);
                if (!this.Sound.getValue().booleanValue()) continue;
                DonkeyNotifier.mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f);
            }
        }
    }
}

