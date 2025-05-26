package fuffles.ichthyology.init;

import java.util.List;
import java.util.function.Function;

import fuffles.ichthyology.init.brain.ModActivities;
import fuffles.ichthyology.init.brain.ModMemoryModuleTypes;
import fuffles.ichthyology.init.brain.ModSensorTypes;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;

public class RegistryRelay<T>
{
    private final ObjectList<Pair<ResourceLocation, T>> registrar = new ObjectArrayList<>();
    private final Registry<T> registry;
    private final Function<String, ResourceLocation> keyCreator;

    public RegistryRelay(Registry<T> registry, Function<String, ResourceLocation> keyCreator)
    {
        this.registry = registry;
        this.keyCreator = keyCreator;
    }

    public ResourceLocation createKey(String id)
    {
        return this.keyCreator.apply(id);
    }

    public <U extends T> U register(String id, U obj)
    {
        return this.register(this.createKey(id), obj);
    }

    public <U extends T> U register(ResourceLocation id, U obj)
    {
        this.registrar.add(Pair.of(id, obj));
        return obj;
    }

    public List<T> getEntryValues()
    {
        return this.registrar.stream().map(Pair::right).toList();
    }

    private void registerSelf(RegisterEvent event)
    {
        event.register(this.registry.key(), registerHelper -> {
            for (Pair<ResourceLocation, T> pair : this.registrar)
            {
                registerHelper.register(pair.left(), pair.right());
            }
        });
    }

    public static void registerAll(RegisterEvent event)
    {
        ModActivities.REGISTRY.registerSelf(event);
        ModBlocks.REGISTRY.registerSelf(event);
        ModEntityTypes.REGISTRY.registerSelf(event);
        ModItems.ITEM_REGISTRY.registerSelf(event);
        ModItems.SPAWN_EGG_REGISTRY.registerSelf(event);
        ModLootItemFunctions.REGISTRY.registerSelf(event);
        ModMemoryModuleTypes.REGISTRY.registerSelf(event);
        ModSensorTypes.REGISTRY.registerSelf(event);
        ModSoundEvents.REGISTRY.registerSelf(event);
    }
}
