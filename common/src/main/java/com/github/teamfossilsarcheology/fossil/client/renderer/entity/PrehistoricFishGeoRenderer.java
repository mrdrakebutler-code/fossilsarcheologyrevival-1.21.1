package com.github.teamfossilsarcheology.fossil.client.renderer.entity;

import com.github.teamfossilsarcheology.fossil.client.model.PrehistoricFishGeoModel;
import com.github.teamfossilsarcheology.fossil.entity.prehistoric.base.PrehistoricFish;
import com.github.teamfossilsarcheology.fossil.entity.prehistoric.fish.Nautilus;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PrehistoricFishGeoRenderer<T extends PrehistoricFish> extends GeoEntityRenderer<T> {

    public PrehistoricFishGeoRenderer(EntityRendererProvider.Context renderManager, String model) {
        super(renderManager, new PrehistoricFishGeoModel<>(model));
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        //1.21.1: GeckoLib 4.7.7 applies entity.getScale() to the PoseStack itself (see PrehistoricGeoRenderer note),
        //so we no longer call withScale here. Fish don't override Entity.getScale() (it returns 1.0), so this was
        //already a no-op for them — dropped for consistency with the dino renderer.
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    protected void applyRotations(T animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
        if (animatable instanceof Nautilus) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180));
        }
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.shadowRadius = entity.getBbWidth() * 0.45F;
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }
}