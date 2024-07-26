package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.PiranhaModel;
import fuffles.ichthyology.common.entity.Piranha;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class PiranhaRenderer extends MobRenderer<Piranha, EntityModel<Piranha>> {

	private static final ResourceLocation PIRANHA = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/piranha.png");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PiranhaRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new PiranhaModel(renderManager.bakeLayer(ClientEvents.PIRANHA)), 0.25F);
		this.addLayer(new ItemInHandLayer(this, renderManager.getItemInHandRenderer()));
	}

	public ResourceLocation getTextureLocation(Piranha entity) {
		return PIRANHA;
	}


}
