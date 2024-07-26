package fuffles.ichthyology.client.entity.renderer;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.GoldfishModel;
import fuffles.ichthyology.common.entity.Goldfish;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GoldfishRenderer extends MobRenderer<Goldfish, EntityModel<Goldfish>> {

	private static final ResourceLocation WILD = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/goldfish/wild.png");
	private static final ResourceLocation ORANGE = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/goldfish/orange.png");
	private static final ResourceLocation COMET = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/goldfish/comet.png");
	private static final ResourceLocation PEARLSCALE = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/goldfish/pearlscale.png");
	private static final ResourceLocation RANCHU = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/goldfish/ranchu.png");
	private static final ResourceLocation REDCAP = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/goldfish/redcap.png");
	private static final ResourceLocation TELESCOPE = new ResourceLocation(Ichthyology.MOD_ID, "textures/entity/goldfish/telescope.png");

	public GoldfishRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new GoldfishModel(renderManager.bakeLayer(ClientEvents.GOLDFISH)), 0.25F);
	}

	public ResourceLocation getTextureLocation(Goldfish entity) {
		if (entity.getColor() == 1) return ORANGE;
		if (entity.getColor() == 2) return COMET;
		if (entity.getColor() == 3) return PEARLSCALE;
		if (entity.getColor() == 4) return RANCHU;
		if (entity.getColor() == 5) return REDCAP;
		if (entity.getColor() == 6) return TELESCOPE;
		return WILD;
	}


}
