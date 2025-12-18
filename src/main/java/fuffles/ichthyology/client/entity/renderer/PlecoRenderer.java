package fuffles.ichthyology.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.PlecoModel;
import fuffles.ichthyology.common.entity.Pleco;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class PlecoRenderer extends MobRenderer<Pleco, EntityModel<Pleco>> {

	private static final ResourceLocation PLECO = new ResourceLocation(Ichthyology.ID, "textures/entity/plecostomus.png");

	public PlecoRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new PlecoModel(renderManager.bakeLayer(ClientEvents.PLECO)), 0.25F);
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	protected void setupRotations(Pleco entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
		float progresso = 1F - (entityLiving.prevAttachChangeProgress + (entityLiving.attachChangeProgress - entityLiving.prevAttachChangeProgress) * partialTicks);

		float trans = entityLiving.isBaby() ? 0.25F : 0.1F;
		if(entityLiving.getAttachmentFacing() == Direction.DOWN){
			matrixStackIn.mulPose(Axis.YP.rotationDegrees (180.0F - rotationYaw));
			matrixStackIn.translate(0.0D, trans, 0.0D);
			if(entityLiving.yo < entityLiving.getY()){
				matrixStackIn.mulPose(Axis.XP.rotationDegrees(90 * (1 - progresso)));
			}else{
				matrixStackIn.mulPose(Axis.XP.rotationDegrees(-90 * (1 - progresso)));
			}
			matrixStackIn.translate(0.0D, -trans, 0.0D);
		}else if(entityLiving.getAttachmentFacing() == Direction.UP){
			matrixStackIn.mulPose(Axis.YP.rotationDegrees (180.0F - rotationYaw));
			matrixStackIn.mulPose(Axis.XP.rotationDegrees(180));
			matrixStackIn.mulPose(Axis.YP.rotationDegrees(180));
			matrixStackIn.translate(0.0D, -trans * 2.5, 0.0D);

		}else{
			matrixStackIn.translate(0.0D, trans, 0.0D);
			switch (entityLiving.getAttachmentFacing()){
			case NORTH:
				matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F * progresso));
				matrixStackIn.mulPose(Axis.ZP.rotationDegrees(0));
				break;
			case SOUTH:
				matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
				matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F * progresso ));
				break;
			case WEST:
				matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
				matrixStackIn.mulPose(Axis.YP.rotationDegrees(90F - 90.0F * progresso));
				matrixStackIn.mulPose(Axis.ZP.rotationDegrees(-90.0F));
				break;
			case EAST:
				matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F ));
				matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F * progresso - 90F));
				matrixStackIn.mulPose(Axis.ZP.rotationDegrees(90.0F));
				break;
			}
			if(entityLiving.getDeltaMovement().y <= -0.001F){
				matrixStackIn.mulPose(Axis.YP.rotationDegrees(-180.0F));
			}
			matrixStackIn.translate(0.0D, -trans, 0.0D);
		}
	}


	public ResourceLocation getTextureLocation(Pleco entity) {
		return PLECO;
	}


}
