package com.github.teamfossilsarcheology.fossil.client.renderer;

import com.github.teamfossilsarcheology.fossil.FossilMod;
import com.github.teamfossilsarcheology.fossil.config.FossilConfig;
import com.github.teamfossilsarcheology.fossil.item.ModItems;
import com.github.teamfossilsarcheology.fossil.material.ModFluids;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class OverlayRenderer {
    private static final ResourceLocation ANCIENT_HELMET = FossilMod.location("textures/gui/ancient_helmet_blur.png");
    private static final ResourceLocation BONE_HELMET = FossilMod.location("textures/gui/bone_helmet_blur.png");
    private static final ResourceLocation TAR = FossilMod.location("textures/block/tar_still.png");

    public static void renderHelmet(int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        if (FossilConfig.isEnabled(FossilConfig.HELMET_OVERLAYS) && mc.options.getCameraType().isFirstPerson()) {
            ItemStack helmet = mc.player.getItemBySlot(EquipmentSlot.HEAD);
            boolean ancient = helmet.is(ModItems.ANCIENT_HELMET.get());
            if (ancient || helmet.is(ModItems.BONE_HELMET.get())) {
                RenderSystem.defaultBlendFunc();
                RenderSystem.enableBlend();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1);
                RenderSystem.setShaderTexture(0, ancient ? ANCIENT_HELMET : BONE_HELMET);
                Tesselator tesselator = Tesselator.getInstance();
                BufferBuilder bufferbuilder;
                bufferbuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                bufferbuilder.addVertex((float)(0.0), (float)(screenHeight), (float)(-90.0)).setUv(0.0f, 1.0f);
                bufferbuilder.addVertex((float)(screenWidth), (float)(screenHeight), (float)(-90.0)).setUv(1.0f, 1.0f);
                bufferbuilder.addVertex((float)(screenWidth), (float)(0.0), (float)(-90.0)).setUv(1.0f, 0.0f);
                bufferbuilder.addVertex((float)(0.0), (float)(0.0), (float)(-90.0)).setUv(0.0f, 0.0f);
                BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
                RenderSystem.disableBlend();
            }
        }
    }

    public static void renderTar(PoseStack poseStack) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player.isEyeInFluid(ModFluids.TAR_FLUID)) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TAR);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.8f);
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder;
            bufferbuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            Matrix4f matrix4f = poseStack.last().pose();
            vertex(bufferbuilder, matrix4f, -1, -1, -0.5f).setUv(0, 1);
            vertex(bufferbuilder, matrix4f, 1, -1, -0.5f).setUv(1, 1);
            vertex(bufferbuilder, matrix4f, 1, 1, -0.5f).setUv(1, 0);
            vertex(bufferbuilder, matrix4f, -1, 1, -0.5f).setUv(0, 0);
            BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    private static VertexConsumer vertex(BufferBuilder bufferBuilder, Matrix4f matrix4f, float x, float y, float z) {
        Vector4f vector4f = new Vector4f(x, y, z, 1);
        matrix4f.transform(vector4f);
        return bufferBuilder.addVertex(vector4f.x(), vector4f.y(), vector4f.z());
    }
}
