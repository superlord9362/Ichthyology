package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.SturgeonBabyModel;
import fuffles.ichthyology.common.entity.SturgeonBaby;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SturgeonBabyRenderer extends MobRenderer<SturgeonBaby, EntityModel<SturgeonBaby>> {

	private static final ResourceLocation STURGEON = new ResourceLocation(Ichthyology.ID, "textures/entity/sturgeon/sturgeon_baby.png");

	public SturgeonBabyRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new SturgeonBabyModel(renderManager.bakeLayer(ClientEvents.STURGEON_BABY)), 0.25F);
	}

	public ResourceLocation getTextureLocation(SturgeonBaby entity) {
		return STURGEON;
	}

}
