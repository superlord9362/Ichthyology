package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.AfricanCichlidModel;
import fuffles.ichthyology.common.entity.SaulosiCichlid;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SaulosiCichlidRenderer extends MobRenderer<SaulosiCichlid, AfricanCichlidModel<SaulosiCichlid>> {

	private static final ResourceLocation MALE = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/cichlid/saulosi_m.png");
	private static final ResourceLocation FEMALE = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/cichlid/saulosi_f.png");

	public SaulosiCichlidRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new AfricanCichlidModel<>(renderManager.bakeLayer(ClientEvents.AFRICAN_CICHLID)), 0.25F);
	}

	public ResourceLocation getTextureLocation(SaulosiCichlid entity) {
		if (entity.getColor() == 0) return MALE;
		else return FEMALE;
	}


}
