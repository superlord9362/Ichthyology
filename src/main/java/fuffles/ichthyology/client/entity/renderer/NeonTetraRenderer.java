package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.NeonTetraModel;
import fuffles.ichthyology.client.entity.renderer.layer.NeonTetraShineLayer;
import fuffles.ichthyology.common.entity.NeonTetra;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class NeonTetraRenderer extends MobRenderer<NeonTetra, EntityModel<NeonTetra>> {

	private static final ResourceLocation NEON_TETRA = new ResourceLocation(Ichthyology.ID, "textures/entity/neon_tetra/neon_tetra.png");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public NeonTetraRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new NeonTetraModel(renderManager.bakeLayer(ClientEvents.NEON_TETRA)), 0.25F);
		this.addLayer(new NeonTetraShineLayer(this));
	}

	public ResourceLocation getTextureLocation(NeonTetra entity) {
		return NEON_TETRA;
	}


}
