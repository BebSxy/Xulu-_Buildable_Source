//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  org.lwjgl.util.glu.Project
 */
package me.alpha432.oyvey.mixin.mixins;

import me.alpha432.oyvey.event.events.PerspectiveEvent;
import me.alpha432.oyvey.features.modules.render.NoRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.lwjgl.util.glu.Project;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={EntityRenderer.class}, priority=1001)
public class MixinEntityRenderer {
    @Shadow
    public float farPlaneDistance;
    @Shadow
    public double cameraZoom;
    @Shadow
    public double cameraYaw;
    @Shadow
    public double cameraPitch;
    @Shadow
    public int frameCount;
    @Shadow
    public int debugViewDirection;
    @Shadow
    public int rendererUpdateCount;
    @Shadow
    public boolean debugView;
    Minecraft mc = Minecraft.getMinecraft();

    @Shadow
    public void orientCamera(float partialTicks) {
    }

    @Shadow
    public void hurtCameraEffect(float partialTicks) {
    }

    @Shadow
    public void applyBobbing(float partialTicks) {
    }

    @Shadow
    public void enableLightmap() {
    }

    @Shadow
    public void disableLightmap() {
    }

    @Shadow
    public void updateFogColor(float partialTicks) {
    }

    @Shadow
    public void setupFog(int startCoords, float partialTicks) {
    }

    protected MixinEntityRenderer(RenderManager renderManager) {
    }

    @Inject(method={"updateLightmap"}, at={@At(value="HEAD")}, cancellable=true)
    private void updateLightmap(float partialTicks, CallbackInfo info) {
        if (NoRender.getInstance().isOn() && (NoRender.getInstance().skylight.getValue() == NoRender.Skylight.ENTITY || NoRender.getInstance().skylight.getValue() == NoRender.Skylight.ALL)) {
            info.cancel();
        }
    }

    @Inject(method={"hurtCameraEffect"}, at={@At(value="HEAD")}, cancellable=true)
    public void hurtCameraEffect(float ticks, CallbackInfo info) {
        if (NoRender.getInstance().hurtcam.getValue().booleanValue() && NoRender.getInstance().isOn()) {
            info.cancel();
        }
    }

    @Redirect(method={"setupCameraTransform"}, at=@At(value="INVOKE", target="Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V"))
    private void onSetupCameraTransform(float fovy, float aspect, float zNear, float zFar) {
        PerspectiveEvent event = new PerspectiveEvent((float)this.mc.displayWidth / (float)this.mc.displayHeight);
        MinecraftForge.EVENT_BUS.post((Event)event);
        Project.gluPerspective((float)fovy, (float)event.getAspect(), (float)zNear, (float)zFar);
    }

    @Redirect(method={"renderWorldPass"}, at=@At(value="INVOKE", target="Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V"))
    private void onRenderWorldPass(float fovy, float aspect, float zNear, float zFar) {
        PerspectiveEvent event = new PerspectiveEvent((float)this.mc.displayWidth / (float)this.mc.displayHeight);
        MinecraftForge.EVENT_BUS.post((Event)event);
        Project.gluPerspective((float)fovy, (float)event.getAspect(), (float)zNear, (float)zFar);
    }

    @Redirect(method={"renderCloudsCheck"}, at=@At(value="INVOKE", target="Lorg/lwjgl/util/glu/Project;gluPerspective(FFFF)V"))
    private void onRenderCloudsCheck(float fovy, float aspect, float zNear, float zFar) {
        PerspectiveEvent event = new PerspectiveEvent((float)this.mc.displayWidth / (float)this.mc.displayHeight);
        MinecraftForge.EVENT_BUS.post((Event)event);
        Project.gluPerspective((float)fovy, (float)event.getAspect(), (float)zNear, (float)zFar);
    }
}

