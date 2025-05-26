package fuffles.ichthyology.init;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.entity.AfricanCichlid;
import fuffles.ichthyology.common.entity.Angelfish;
import fuffles.ichthyology.common.entity.Archerfish;
import fuffles.ichthyology.common.entity.ArcherfishSpit;
import fuffles.ichthyology.common.entity.BlindCaveTetra;
import fuffles.ichthyology.common.entity.Carp;
import fuffles.ichthyology.common.entity.Catfish;
import fuffles.ichthyology.common.entity.CatfishBaby;
import fuffles.ichthyology.common.entity.Crayfish;
import fuffles.ichthyology.common.entity.Discus;
import fuffles.ichthyology.common.entity.FiddlerCrab;
import fuffles.ichthyology.common.entity.Gar;
import fuffles.ichthyology.common.entity.GarBaby;
import fuffles.ichthyology.common.entity.Goldfish;
import fuffles.ichthyology.common.entity.Mudskipper;
import fuffles.ichthyology.common.entity.NeonTetra;
import fuffles.ichthyology.common.entity.Olm;
import fuffles.ichthyology.common.entity.PeacockBass;
import fuffles.ichthyology.common.entity.PeacockBassBaby;
import fuffles.ichthyology.common.entity.Piranha;
import fuffles.ichthyology.common.entity.Pleco;
import fuffles.ichthyology.common.entity.Sturgeon;
import fuffles.ichthyology.common.entity.SturgeonBaby;
import fuffles.ichthyology.common.entity.Tilapia;
import fuffles.ichthyology.common.entity.perch.Perch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntityTypes
{
	@SuppressWarnings("deprecation")
	public static final RegistryRelay<EntityType<?>> REGISTRY = new RegistryRelay<>(BuiltInRegistries.ENTITY_TYPE, Ichthyology::id);

	private static <T extends Entity> EntityType<T> registerSimple(String id, EntityType.Builder<T> builder)
	{
		ResourceLocation resource = REGISTRY.createKey(id);
		return REGISTRY.register(resource, builder.build(resource.toString()));
	}

	private static <T extends Entity> EntityType<T> registerFish(String id, EntityType.EntityFactory<T> factory, float width, float height)
	{
		return registerSimple(id, EntityType.Builder.of(factory, MobCategory.WATER_AMBIENT).sized(width, height));
	}

	public static final EntityType<AfricanCichlid> AFRICAN_CICHLID = registerFish("african_cichlid", AfricanCichlid::new, 0.25F, 0.1875F);
	public static final EntityType<Tilapia> TILAPIA = registerFish("tilapia", Tilapia::new, 0.25F, 0.3125F);
	public static final EntityType<Perch> PERCH = registerFish("perch", Perch::new, 0.4F, 0.275F);

	public static final EntityType<BlindCaveTetra> BLIND_CAVE_TETRA = registerFish("blind_cave_tetra", BlindCaveTetra::new, 0.25F, 0.1875F);
	public static final EntityType<NeonTetra> NEON_TETRA = registerFish("neon_tetra", NeonTetra::new, 0.25F, 0.125F);
	public static final EntityType<Goldfish> GOLDFISH = registerFish("goldfish", Goldfish::new, 0.25F, 0.1875F);
	public static final EntityType<Piranha> PIRANHA = registerFish("piranha", Piranha::new, 0.25F, 0.25F);
	public static final EntityType<Angelfish> ANGELFISH = registerFish("angelfish", Angelfish::new, 0.25F, 0.4375F);
	public static final EntityType<Carp> CARP = registerFish("carp", Carp::new, 0.45F, 0.3925F);
	public static final EntityType<Discus> DISCUS = registerFish("discus", Discus::new, 0.25F, 0.4375F);
	public static final EntityType<Pleco> PLECO = registerFish("pleco", Pleco::new, 0.25F, 0.125F);
	public static final EntityType<Archerfish> ARCHERFISH = registerFish("archerfish", Archerfish::new, 0.25F, 0.25F);
	public static final EntityType<Mudskipper> MUDSKIPPER = registerFish("mudskipper", Mudskipper::new, 0.25F, 0.25F);
	public static final EntityType<Crayfish> CRAYFISH = registerFish("crayfish", Crayfish::new, 0.3F, 0.25F);
	public static final EntityType<Catfish> CATFISH = registerFish("catfish", Catfish::new, 0.375F, 0.375F);
	public static final EntityType<CatfishBaby> CATFISH_BABY = registerFish("catfish_baby", CatfishBaby::new, 0.25F, 0.25F);
	public static final EntityType<PeacockBass> PEACOCK_BASS = registerFish("peacock_bass", PeacockBass::new, 0.375F, 0.5625F);
	public static final EntityType<PeacockBassBaby> PEACOCK_BASS_BABY = registerFish("peacock_bass_baby", PeacockBassBaby::new, 0.25F, 0.25F);
	public static final EntityType<Gar> GAR = registerFish("gar", Gar::new, 0.3125F, 0.3125F);
	public static final EntityType<GarBaby> GAR_BABY = registerFish("gar_baby", GarBaby::new, 0.25F, 0.25F);
	public static final EntityType<FiddlerCrab> FIDDLER_CRAB = registerFish("fiddler_crab", FiddlerCrab::new, 0.375F, 0.375F);
	public static final EntityType<Sturgeon> STURGEON = registerFish("sturgeon", Sturgeon::new, 0.375F, 0.375F);
	public static final EntityType<SturgeonBaby> STURGEON_BABY = registerFish("sturgeon_baby", SturgeonBaby::new, 0.25F, 0.25F);
	public static final EntityType<Olm> OLM = registerFish("olm", Olm::new, 0.25F, 0.25F);

	public static final EntityType<ArcherfishSpit> ARCHERFISH_SPIT = registerSimple("archerfish_spit", EntityType.Builder.<ArcherfishSpit>of(ArcherfishSpit::new, MobCategory.MISC).sized(0.125F, 0.125F));
	
}

