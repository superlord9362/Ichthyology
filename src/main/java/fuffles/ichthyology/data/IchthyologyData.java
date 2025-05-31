package fuffles.ichthyology.data;

import fuffles.ichthyology.Ichthyology;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class IchthyologyData
{
    private static final RegistrySetBuilder BUILTIN_REGISTRIES = new RegistrySetBuilder()
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifierProvider::bootstrap);

    @SuppressWarnings("deprecation")
	public static void onGatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        PackOutput out = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        boolean isServerSide = event.includeServer();

        CompletableFuture<HolderLookup.Provider> lookupProvider = generator.addProvider(isServerSide, new DatapackBuiltinEntriesProvider(out, event.getLookupProvider(), BUILTIN_REGISTRIES, Set.of(Ichthyology.ID))).getRegistryProvider();

        generator.addProvider(isServerSide, new LootTableProvider(out, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(ModEntityLootProvider::new, LootContextParamSets.ENTITY)
        )));
        generator.addProvider(isServerSide, new ModRecipeProvider(out));
        ModBlockTagsProvider blockTagsAccess = generator.addProvider(isServerSide, new ModBlockTagsProvider(out, lookupProvider, helper));
        generator.addProvider(isServerSide, new ModItemTagsProvider(out, lookupProvider, blockTagsAccess.contentsGetter(), helper));
        generator.addProvider(isServerSide, new ModBiomeTagsProvider(out, lookupProvider, helper));
    }
}