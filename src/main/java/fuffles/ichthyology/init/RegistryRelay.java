package fuffles.ichthyology.init;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import fuffles.ichthyology.init.brain.ModActivities;
import fuffles.ichthyology.init.brain.ModMemoryModuleTypes;
import fuffles.ichthyology.init.brain.ModSensorTypes;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;

public class RegistryRelay<T>
{
    private final ResourceKey<? extends Registry<T>> registryKey;
    private final Function<String, ResourceLocation> idGen;
    private final ObjectList<Pair<ResourceLocation, T>> registrar = new ObjectArrayList<>();

    public RegistryRelay(ResourceKey<? extends Registry<T>> registry, Function<String, ResourceLocation> keyCreator)
    {
        this.registryKey = registry;
        this.idGen = keyCreator;
    }

    public <U extends T> U register(String id, U obj)
    {
        return this.register(this.createLocation(id), obj);
    }

    public <U extends T> U register(ResourceLocation id, U obj)
    {
        this.registrar.add(Pair.of(id, obj));
        return obj;
    }

    public ResourceKey<? extends Registry<T>> registryKey()
    {
        return this.registryKey;
    }

    public ResourceLocation createLocation(String id)
    {
        return this.idGen.apply(id);
    }

    public ResourceKey<T> createKey(String id)
    {
        return this.createKey(this.createLocation(id));
    }

    public ResourceKey<T> createKey(ResourceLocation location)
    {
        return ResourceKey.create(this.registryKey(), location);
    }

    public void registerContents(RegisterEvent event)
    {
        if (Objects.equals(event.getRegistryKey(), this.registryKey()))
        {
            for (Pair<ResourceLocation, T> entry : this.registrar)
            {
                event.register(this.registryKey(), entry.key(), entry::value);
            }
        }
    }

    public List<T> getEntries()
    {
        return this.registrar.stream().map(Pair::right).toList();
    }

    public static void registerAll(RegisterEvent event)
    {
        ModActivities.REGISTRY.registerContents(event);
        ModBlocks.REGISTRY.registerContents(event);
        ModEntityTypes.REGISTRY.registerContents(event);
        ModItems.ITEM_REGISTRY.registerContents(event);
        ModItems.SPAWN_EGG_REGISTRY.registerContents(event);
        ModLootItemFunctions.REGISTRY.registerContents(event);
        ModMemoryModuleTypes.REGISTRY.registerContents(event);
        ModSensorTypes.REGISTRY.registerContents(event);
        ModSoundEvents.REGISTRY.registerContents(event);
    }
}