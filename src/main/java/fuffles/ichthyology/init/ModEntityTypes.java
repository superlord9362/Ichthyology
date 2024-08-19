package fuffles.ichthyology.init;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.entity.*;
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
}

