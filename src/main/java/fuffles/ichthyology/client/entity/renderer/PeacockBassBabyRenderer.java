package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.PeacockBassBabyModel;
import fuffles.ichthyology.common.entity.PeacockBassBaby;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PeacockBassBabyRenderer extends MobRenderer<PeacockBassBaby, EntityModel<PeacockBassBaby>> {

	private static final ResourceLocation PEACOCK_BASS = new ResourceLocation(Ichthyology.ID, "textures/entity/peacock_bass/peacock_bass_baby.png");

	public PeacockBassBabyRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new PeacockBassBabyModel(renderManager.bakeLayer(ClientEvents.PEACOCK_BASS_BABY)), 0.25F);
	}

	public ResourceLocation getTextureLocation(PeacockBassBaby entity) {
		return PEACOCK_BASS;
	}

}
