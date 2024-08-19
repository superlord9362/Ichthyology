package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.PerchModel;
import fuffles.ichthyology.client.entity.renderer.layer.SimpleHeldItemLayer;
import fuffles.ichthyology.common.entity.perch.Perch;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PerchRenderer extends AbstractFishRenderer<Perch, PerchModel<Perch>>
{
	private static final ResourceLocation PERCH = new ResourceLocation(Ichthyology.ID, "textures/entity/perch.png");

	public PerchRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new PerchModel<>(renderManager.bakeLayer(ClientEvents.PERCH)), 0.2F);
		this.addLayer(new SimpleHeldItemLayer<>(this, renderManager.getItemInHandRenderer()));
	}

	public ResourceLocation getTextureLocation(Perch entity) {
		return PERCH;
	}
}
