package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.SturgeonModel;
import fuffles.ichthyology.common.entity.Sturgeon;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SturgeonRenderer extends MobRenderer<Sturgeon, EntityModel<Sturgeon>> {

	private static final ResourceLocation STURGEON = new ResourceLocation(Ichthyology.ID, "textures/entity/sturgeon/sturgeon.png");

	public SturgeonRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new SturgeonModel(renderManager.bakeLayer(ClientEvents.STURGEON)), 0.375F);
	}

	public ResourceLocation getTextureLocation(Sturgeon entity) {
		return STURGEON;
	}

}
