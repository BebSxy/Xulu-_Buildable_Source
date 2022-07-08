//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.passive.AbstractHorse
 *  net.minecraft.entity.passive.EntityTameable
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.alpha432.oyvey.features.modules.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.util.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;

public class MobOwner
extends Module {
    private final Map<Entity, String> owners = new HashMap<Entity, String>();
    private final Map<Entity, UUID> toLookUp = new ConcurrentHashMap<Entity, UUID>();
    private final List<Entity> lookedUp = new ArrayList<Entity>();

    public MobOwner() {
        super("MobOwner", "Displays The UUID and the Owner Of Mobs", Module.Category.MISC, false, false, false);
    }

    @Override
    public void onUpdate() {
        if (MobOwner.fullNullCheck()) {
            return;
        }
        if (PlayerUtil.timer.passedS(5.0)) {
            for (Map.Entry<Object, UUID> entry : this.toLookUp.entrySet()) {
                Entity entity = (Entity)entry.getKey();
                UUID uuid = entry.getValue();
                if (uuid != null) {
                    EntityPlayer owner = MobOwner.mc.world.getPlayerEntityByUUID(uuid);
                    if (owner != null) {
                        this.owners.put(entity, owner.getName());
                        this.lookedUp.add(entity);
                        continue;
                    }
                    try {
                        String name = PlayerUtil.getNameFromUUID(uuid);
                        if (name != null) {
                            this.owners.put(entity, name);
                            this.lookedUp.add(entity);
                        }
                    }
                    catch (Exception e) {
                        this.lookedUp.add(entity);
                        this.toLookUp.remove(entry);
                    }
                    PlayerUtil.timer.reset();
                    break;
                }
                this.lookedUp.add(entity);
                this.toLookUp.remove(entry);
            }
        }
        for (Entity entity : MobOwner.mc.world.getLoadedEntityList()) {
            AbstractHorse horse;
            if (entity.getAlwaysRenderNameTag()) continue;
            if (entity instanceof EntityTameable) {
                EntityTameable tameableEntity = (EntityTameable)entity;
                if (!tameableEntity.isTamed() || tameableEntity.getOwnerId() == null) continue;
                if (this.owners.get((Object)tameableEntity) != null) {
                    tameableEntity.setAlwaysRenderNameTag(true);
                    tameableEntity.setCustomNameTag(this.owners.get((Object)tameableEntity));
                    continue;
                }
                if (this.lookedUp.contains((Object)entity)) continue;
                this.toLookUp.put((Entity)tameableEntity, tameableEntity.getOwnerId());
                continue;
            }
            if (!(entity instanceof AbstractHorse) || !(horse = (AbstractHorse)entity).isTame() || horse.getOwnerUniqueId() == null) continue;
            if (this.owners.get((Object)horse) != null) {
                horse.setAlwaysRenderNameTag(true);
                horse.setCustomNameTag(this.owners.get((Object)horse));
                continue;
            }
            if (this.lookedUp.contains((Object)entity)) continue;
            this.toLookUp.put((Entity)horse, horse.getOwnerUniqueId());
        }
    }

    @Override
    public void onDisable() {
        for (Entity entity : MobOwner.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityTameable) && !(entity instanceof AbstractHorse)) continue;
            try {
                entity.setAlwaysRenderNameTag(false);
            }
            catch (Exception exception) {}
        }
    }
}

