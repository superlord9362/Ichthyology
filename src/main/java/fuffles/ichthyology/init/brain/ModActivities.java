package fuffles.ichthyology.init.brain;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.init.RegistryRelay;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.registries.RegistryObject;

public class ModActivities
{
    @SuppressWarnings("deprecation")
    public static final RegistryRelay<Activity> REGISTRY = new RegistryRelay<>(BuiltInRegistries.ACTIVITY, Ichthyology::id);

    private static Activity registerSimple(String id)
    {
        ResourceLocation resource = REGISTRY.createKey(id);
        return REGISTRY.register(resource, new Activity(resource.toString()));
    }

    public static final Activity EAT = registerSimple("eat");
    public static final Activity EAT_BLOCK = registerSimple("eat_block");
}
