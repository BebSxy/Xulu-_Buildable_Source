/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.alpha432.oyvey.event.events;

import me.alpha432.oyvey.event.EventStage;
import net.minecraft.entity.player.EntityPlayer;

public class TotemPopEvent
extends EventStage {
    private EntityPlayer entity;

    public TotemPopEvent(EntityPlayer entity) {
        this.entity = entity;
    }

    public EntityPlayer getEntity() {
        return this.entity;
    }
}

