package fuffles.ichthyology.client;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.client.entity.model.*;
import fuffles.ichthyology.client.entity.renderer.*;
import fuffles.ichthyology.init.ModBlocks;
import fuffles.ichthyology.init.ModEntityTypes;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Ichthyology.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
	
	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event) {
		setupBlockRenders();
	}
	
	public static final ModelLayerLocation BLIND_CAVE_TETRA = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "blind_cave_tetra"), "blind_cave_tetra");
	public static final ModelLayerLocation GOLDFISH = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "goldfish"), "goldfish");
	public static final ModelLayerLocation TILAPIA = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "tilapia"), "tilapia");
	public static final ModelLayerLocation AFRICAN_CICHLID = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "african_cichlid"), "african_cichlid");
	public static final ModelLayerLocation CARP = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "carp"), "carp");
	public static final ModelLayerLocation PIRANHA = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "piranha"), "piranha");
	public static final ModelLayerLocation PERCH = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "perch"), "perch");
	public static final ModelLayerLocation DISCUS = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "discus"), "discus");
	public static final ModelLayerLocation ANGELFISH = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "angelfish"), "angelfish");
	public static final ModelLayerLocation NEON_TETRA = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "neon_tetra"), "neon_tetra");
	public static final ModelLayerLocation PLECO = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "pleco"), "pleco");
	public static final ModelLayerLocation ARCHERFISH = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "archerfish"), "archerfish");
	public static final ModelLayerLocation MUDSKIPPER = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "mudskipper"), "mudskipper");
	public static final ModelLayerLocation CRAYFISH = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "crayfish"), "crayfish");
	public static final ModelLayerLocation CATFISH = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "catfish"), "catfish");
	public static final ModelLayerLocation CATFISH_BABY = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "catfish_baby"), "catfish_baby");
	public static final ModelLayerLocation PEACOCK_BASS = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "peacock_bass"), "peacock_bass");
	public static final ModelLayerLocation PEACOCK_BASS_BABY = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "peacock_bass_baby"), "peacock_bass_baby");
	public static final ModelLayerLocation GAR = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "gar"), "gar");
	public static final ModelLayerLocation GAR_BABY = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "gar_baby"), "gar_baby");
	public static final ModelLayerLocation STURGEON = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "sturgeon"), "sturgeon");
	public static final ModelLayerLocation STURGEON_BABY = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "sturgeon_baby"), "sturgeon_baby");
	public static final ModelLayerLocation FIDDLER_CRAB = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "fiddler_crab"), "fiddler_crab");
	public static final ModelLayerLocation OLM = new ModelLayerLocation(new ResourceLocation(Ichthyology.ID, "olm"), "olm");
	
	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntityTypes.BLIND_CAVE_TETRA, BlindCaveTetraRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.GOLDFISH, GoldfishRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.TILAPIA, TilapiaRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.AFRICAN_CICHLID, AfricanCichlidRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.CARP, CarpRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.PIRANHA, PiranhaRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.PERCH, PerchRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.DISCUS, DiscusRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.ANGELFISH, AngelfishRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.NEON_TETRA, NeonTetraRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.PLECO, PlecoRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.ARCHERFISH, ArcherfishRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.ARCHERFISH_SPIT, ArcherfishSpitRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.MUDSKIPPER, MudskipperRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.CRAYFISH, CrayfishRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.CATFISH, CatfishRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.CATFISH_BABY, CatfishBabyRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.PEACOCK_BASS, PeacockBassRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.PEACOCK_BASS_BABY, PeacockBassBabyRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.GAR, GarRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.GAR_BABY, GarBabyRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.STURGEON, SturgeonRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.STURGEON_BABY, SturgeonBabyRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.FIDDLER_CRAB, FiddlerCrabRenderer::new);
		event.registerEntityRenderer(ModEntityTypes.OLM, OlmRenderer::new);
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
		event.registerLayerDefinition(ARCHERFISH, ArcherfishModel::createBodyLayer);
		event.registerLayerDefinition(MUDSKIPPER, MudskipperModel::createBodyLayer);
		event.registerLayerDefinition(CRAYFISH, CrayfishModel::createBodyLayer);
		event.registerLayerDefinition(CATFISH, CatfishModel::createBodyLayer);
		event.registerLayerDefinition(CATFISH_BABY, CatfishBabyModel::createBodyLayer);
		event.registerLayerDefinition(PEACOCK_BASS, PeacockBassModel::createBodyLayer);
		event.registerLayerDefinition(PEACOCK_BASS_BABY, PeacockBassBabyModel::createBodyLayer);
		event.registerLayerDefinition(GAR, GarModel::createBodyLayer);
		event.registerLayerDefinition(GAR_BABY, GarBabyModel::createBodyLayer);
		event.registerLayerDefinition(STURGEON, SturgeonModel::createBodyLayer);
		event.registerLayerDefinition(STURGEON_BABY, SturgeonBabyModel::createBodyLayer);
		event.registerLayerDefinition(FIDDLER_CRAB, FiddlerCrabModel::createBodyLayer);
		event.registerLayerDefinition(OLM, OlmModel::createBodyLayer);
	}
	
	@SuppressWarnings("deprecation")
	public static void setupBlockRenders() {
		RenderType cutoutRenderType = RenderType.cutout();
		RenderType translucentRenderType = RenderType.translucent();
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.CATFISH_ROE, cutoutRenderType);
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.PEACOCK_BASS_ROE, cutoutRenderType);
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.GAR_ROE, cutoutRenderType);
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.STURGEON_ROE, cutoutRenderType);
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.OLM_EGGS, translucentRenderType);
		ItemBlockRenderTypes.setRenderLayer(Blocks.FROGSPAWN, translucentRenderType);
	}

}
