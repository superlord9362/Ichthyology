package fuffles.ichthyology.client;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.entity.model.*;
import fuffles.ichthyology.client.entity.renderer.*;
import fuffles.ichthyology.init.IEntities;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Ichthyology.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
	
	public static ModelLayerLocation BLIND_CAVE_TETRA = new ModelLayerLocation(new ResourceLocation(Ichthyology.MOD_ID, "blind_cave_tetra"), "blind_cave_tetra");
	public static ModelLayerLocation GOLDFISH = new ModelLayerLocation(new ResourceLocation(Ichthyology.MOD_ID, "goldfish"), "goldfish");
	public static ModelLayerLocation TILAPIA = new ModelLayerLocation(new ResourceLocation(Ichthyology.MOD_ID, "tilapia"), "tilapia");
	public static ModelLayerLocation AFRICAN_CICHLID = new ModelLayerLocation(new ResourceLocation(Ichthyology.MOD_ID, "african_cichlid"), "african_cichlid");
	public static ModelLayerLocation CARP = new ModelLayerLocation(new ResourceLocation(Ichthyology.MOD_ID, "carp"), "carp");
	public static ModelLayerLocation PIRANHA = new ModelLayerLocation(new ResourceLocation(Ichthyology.MOD_ID, "piranha"), "piranha");
	public static ModelLayerLocation PERCH = new ModelLayerLocation(new ResourceLocation(Ichthyology.MOD_ID, "perch"), "perch");
	public static ModelLayerLocation DISCUS = new ModelLayerLocation(new ResourceLocation(Ichthyology.MOD_ID, "discus"), "discus");
	public static ModelLayerLocation ANGELFISH = new ModelLayerLocation(new ResourceLocation(Ichthyology.MOD_ID, "angelfish"), "angelfish");
	public static ModelLayerLocation NEON_TETRA = new ModelLayerLocation(new ResourceLocation(Ichthyology.MOD_ID, "neon_tetra"), "neon_tetra");
	public static ModelLayerLocation PLECO = new ModelLayerLocation(new ResourceLocation(Ichthyology.MOD_ID, "pleco"), "pleco");
	
	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(IEntities.BLIND_CAVE_TETRA.get(), BlindCaveTetraRenderer::new);
		event.registerEntityRenderer(IEntities.GOLDFISH.get(), GoldfishRenderer::new);
		event.registerEntityRenderer(IEntities.TILAPIA.get(), TilapiaRenderer::new);
		event.registerEntityRenderer(IEntities.PRINCESS_CICHLID.get(), PrincessCichlidRenderer::new);
		event.registerEntityRenderer(IEntities.SAULOSI_CICHLID.get(), SaulosiCichlidRenderer::new);
		event.registerEntityRenderer(IEntities.KASANGA_CICHLID.get(), KasangaCichlidRenderer::new);
		event.registerEntityRenderer(IEntities.CARP.get(), CarpRenderer::new);
		event.registerEntityRenderer(IEntities.PIRANHA.get(), PiranhaRenderer::new);
		event.registerEntityRenderer(IEntities.PERCH.get(), PerchRenderer::new);
		event.registerEntityRenderer(IEntities.DISCUS.get(), DiscusRenderer::new);
		event.registerEntityRenderer(IEntities.ANGELFISH.get(), AngelfishRenderer::new);
		event.registerEntityRenderer(IEntities.NEON_TETRA.get(), NeonTetraRenderer::new);
		event.registerEntityRenderer(IEntities.PLECO.get(), PlecoRenderer::new);
	}
	
	@SubscribeEvent
	public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(BLIND_CAVE_TETRA, BlindCaveTetraModel::createBodyLayer);
		event.registerLayerDefinition(GOLDFISH, GoldfishModel::createBodyLayer);
		event.registerLayerDefinition(TILAPIA, TilapiaModel::createBodyLayer);
		event.registerLayerDefinition(AFRICAN_CICHLID, AfricanCichlidModel::createBodyLayer);
		event.registerLayerDefinition(CARP, CarpModel::createBodyLayer);
		event.registerLayerDefinition(PIRANHA, PiranhaModel::createBodyLayer);
		event.registerLayerDefinition(PERCH, PerchModel::createBodyLayer);
		event.registerLayerDefinition(DISCUS, DiscusModel::createBodyLayer);
		event.registerLayerDefinition(ANGELFISH, AngelfishModel::createBodyLayer);
		event.registerLayerDefinition(NEON_TETRA, NeonTetraModel::createBodyLayer);
		event.registerLayerDefinition(PLECO, PlecoModel::createBodyLayer);
	}

}
