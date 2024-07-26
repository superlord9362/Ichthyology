package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.PerchModel;
import fuffles.ichthyology.common.entity.Perch;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class PerchRenderer extends MobRenderer<Perch, EntityModel<Perch>> {

	private static final ResourceLocation PERCH = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/perch.png");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PerchRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new PerchModel(renderManager.bakeLayer(ClientEvents.PERCH)), 0.25F);
		this.addLayer(new ItemInHandLayer(this, renderManager.getItemInHandRenderer()));
	}

	public ResourceLocation getTextureLocation(Perch entity) {
		return PERCH;
	}


}
