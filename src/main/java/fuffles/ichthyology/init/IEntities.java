package fuffles.ichthyology.init;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.entity.Angelfish;
import fuffles.ichthyology.common.entity.BlindCaveTetra;
import fuffles.ichthyology.common.entity.Carp;
import fuffles.ichthyology.common.entity.Discus;
import fuffles.ichthyology.common.entity.Goldfish;
import fuffles.ichthyology.common.entity.KasangaCichlid;
import fuffles.ichthyology.common.entity.NeonTetra;
import fuffles.ichthyology.common.entity.Perch;
import fuffles.ichthyology.common.entity.Piranha;
import fuffles.ichthyology.common.entity.Pleco;
import fuffles.ichthyology.common.entity.PrincessCichlid;
import fuffles.ichthyology.common.entity.SaulosiCichlid;
import fuffles.ichthyology.common.entity.Tilapia;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class IEntities {

	public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Ichthyology.MOD_ID);

	public static final RegistryObject<EntityType<BlindCaveTetra>> BLIND_CAVE_TETRA = REGISTER.register("blind_cave_tetra", () -> EntityType.Builder.<BlindCaveTetra>of(BlindCaveTetra::new, MobCategory.WATER_AMBIENT).sized(0.25F, 0.1875F).build(new ResourceLocation(Ichthyology.MOD_ID, "blind_cave_tetra").toString()));
	public static final RegistryObject<EntityType<Goldfish>> GOLDFISH = REGISTER.register("goldfish", () -> EntityType.Builder.<Goldfish>of(Goldfish::new, MobCategory.WATER_AMBIENT).sized(0.25F, 0.1875F).build(new ResourceLocation(Ichthyology.MOD_ID, "goldfish").toString()));
	public static final RegistryObject<EntityType<Tilapia>> TILAPIA = REGISTER.register("tilapia", () -> EntityType.Builder.<Tilapia>of(Tilapia::new, MobCategory.WATER_AMBIENT).sized(0.25F, 0.3125F).build(new ResourceLocation(Ichthyology.MOD_ID, "tilapia").toString()));
	public static final RegistryObject<EntityType<PrincessCichlid>> PRINCESS_CICHLID = REGISTER.register("princess_cichlid", () -> EntityType.Builder.<PrincessCichlid>of(PrincessCichlid::new, MobCategory.WATER_AMBIENT).sized(0.25F, 0.1875F).build(new ResourceLocation(Ichthyology.MOD_ID, "princess_cichlid").toString()));
	public static final RegistryObject<EntityType<SaulosiCichlid>> SAULOSI_CICHLID = REGISTER.register("saulosi_cichlid", () -> EntityType.Builder.<SaulosiCichlid>of(SaulosiCichlid::new, MobCategory.WATER_AMBIENT).sized(0.25F, 0.1875F).build(new ResourceLocation(Ichthyology.MOD_ID, "saulosi_cichlid").toString()));
	public static final RegistryObject<EntityType<KasangaCichlid>> KASANGA_CICHLID = REGISTER.register("kasanga_cichlid", () -> EntityType.Builder.<KasangaCichlid>of(KasangaCichlid::new, MobCategory.WATER_AMBIENT).sized(0.25F, 0.1875F).build(new ResourceLocation(Ichthyology.MOD_ID, "kasanga_cichlid").toString()));
	public static final RegistryObject<EntityType<Carp>> CARP = REGISTER.register("carp", () -> EntityType.Builder.<Carp>of(Carp::new, MobCategory.WATER_AMBIENT).sized(0.375F, 0.25F).build(new ResourceLocation(Ichthyology.MOD_ID, "carp").toString()));
	public static final RegistryObject<EntityType<Piranha>> PIRANHA = REGISTER.register("piranha", () -> EntityType.Builder.<Piranha>of(Piranha::new, MobCategory.WATER_AMBIENT).sized(0.25F, 0.25F).build(new ResourceLocation(Ichthyology.MOD_ID, "piranha").toString()));
	public static final RegistryObject<EntityType<Perch>> PERCH = REGISTER.register("perch", () -> EntityType.Builder.<Perch>of(Perch::new, MobCategory.WATER_AMBIENT).sized(0.375F, 0.25F).build(new ResourceLocation(Ichthyology.MOD_ID, "perch").toString()));
	public static final RegistryObject<EntityType<Discus>> DISCUS = REGISTER.register("discus", () -> EntityType.Builder.<Discus>of(Discus::new, MobCategory.WATER_AMBIENT).sized(0.25F, 0.4375F).build(new ResourceLocation(Ichthyology.MOD_ID, "discus").toString()));
	public static final RegistryObject<EntityType<Angelfish>> ANGELFISH = REGISTER.register("angelfish", () -> EntityType.Builder.<Angelfish>of(Angelfish::new, MobCategory.WATER_AMBIENT).sized(0.25F, 0.4375F).build(new ResourceLocation(Ichthyology.MOD_ID, "angelfish").toString()));
	public static final RegistryObject<EntityType<NeonTetra>> NEON_TETRA = REGISTER.register("neon_tetra", () -> EntityType.Builder.<NeonTetra>of(NeonTetra::new, MobCategory.WATER_AMBIENT).sized(0.25F, 0.125F).build(new ResourceLocation(Ichthyology.MOD_ID, "neon_tetra").toString()));
	public static final RegistryObject<EntityType<Pleco>> PLECO = REGISTER.register("pleco", () -> EntityType.Builder.<Pleco>of(Pleco::new, MobCategory.WATER_AMBIENT).sized(0.25F, 0.125F).build(new ResourceLocation(Ichthyology.MOD_ID, "plecostomus").toString()));

}

