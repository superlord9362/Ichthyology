package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.PeacockBassModel;
import fuffles.ichthyology.common.entity.PeacockBass;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PeacockBassRenderer extends MobRenderer<PeacockBass, EntityModel<PeacockBass>> {

	private static final ResourceLocation PEACOCK_BASS = new ResourceLocation(Ichthyology.ID, "textures/entity/peacock_bass/peacock_bass.png");

	public PeacockBassRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new PeacockBassModel(renderManager.bakeLayer(ClientEvents.PEACOCK_BASS)), 0.375F);
	}

	public ResourceLocation getTextureLocation(PeacockBass entity) {
		return PEACOCK_BASS;
	}

}
