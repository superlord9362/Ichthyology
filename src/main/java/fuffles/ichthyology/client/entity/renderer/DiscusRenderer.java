package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.DiscusModel;
import fuffles.ichthyology.common.entity.Discus;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DiscusRenderer extends MobRenderer<Discus, EntityModel<Discus>> {

	private static final ResourceLocation DISCUS = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/discus.png");

	public DiscusRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new DiscusModel(renderManager.bakeLayer(ClientEvents.DISCUS)), 0.25F);
	}

	public ResourceLocation getTextureLocation(Discus entity) {
		return DISCUS;
	}


}
