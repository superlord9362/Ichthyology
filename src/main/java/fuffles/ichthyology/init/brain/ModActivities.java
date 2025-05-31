package fuffles.ichthyology.init.brain;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.init.RegistryRelay;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.schedule.Activity;

public class ModActivities
{
    public static final RegistryRelay<Activity> REGISTRY = new RegistryRelay<>(Registries.ACTIVITY, Ichthyology::id);

    private static Activity registerSimple(String id)
    {
        ResourceLocation resource = REGISTRY.createLocation(id);
        return REGISTRY.register(resource, new Activity(resource.toString()));
    }

    public static final Activity EAT = registerSimple("eat");
    public static final Activity EAT_BLOCK = registerSimple("eat_block");
}