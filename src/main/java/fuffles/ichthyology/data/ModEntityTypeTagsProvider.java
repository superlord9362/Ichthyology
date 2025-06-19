package fuffles.ichthyology.data;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.init.ModEntityTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {

    public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
    	super(output, provider, Ichthyology.ID, existingFileHelper);
    }
    
    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
    	this.tag(ModEntityTypes.Tags.CAVE_FISH).add(ModEntityTypes.CRAYFISH).add(ModEntityTypes.BLIND_CAVE_TETRA);
    	this.tag(ModEntityTypes.Tags.FOREST_FISH).add(ModEntityTypes.GOLDFISH).add(ModEntityTypes.CARP);
    	this.tag(ModEntityTypes.Tags.CHERY_FISH).add(ModEntityTypes.GOLDFISH).add(ModEntityTypes.CARP);
    	this.tag(ModEntityTypes.Tags.PLAINS_FISH).add(ModEntityTypes.GOLDFISH).add(ModEntityTypes.CARP);
    	this.tag(ModEntityTypes.Tags.SAVANNA_FISH).add(ModEntityTypes.TILAPIA).add(ModEntityTypes.AFRICAN_CICHLID);
    	this.tag(ModEntityTypes.Tags.DESERT_FISH).add(ModEntityTypes.AFRICAN_CICHLID);
    	this.tag(ModEntityTypes.Tags.JUNGLE_FISH).add(ModEntityTypes.PIRANHA).add(ModEntityTypes.DISCUS).add(ModEntityTypes.ANGELFISH).add(ModEntityTypes.NEON_TETRA).add(ModEntityTypes.PLECO).add(ModEntityTypes.PEACOCK_BASS);
    	this.tag(ModEntityTypes.Tags.RIVER_FISH).add(ModEntityTypes.PERCH).add(ModEntityTypes.CRAYFISH).add(ModEntityTypes.CATFISH).add(ModEntityTypes.STURGEON);
    	this.tag(ModEntityTypes.Tags.FROZEN_RIVER_FISH).add(ModEntityTypes.PERCH).add(ModEntityTypes.STURGEON);
    	this.tag(ModEntityTypes.Tags.SWAMP_FISH).add(ModEntityTypes.PERCH).add(ModEntityTypes.PLECO).add(ModEntityTypes.CRAYFISH).add(ModEntityTypes.GAR);
    	this.tag(ModEntityTypes.Tags.MANGROVE_FISH).add(ModEntityTypes.ARCHERFISH).add(ModEntityTypes.MUDSKIPPER);
    	this.tag(ModEntityTypes.Tags.DRIPSTONE_FISH).add(ModEntityTypes.OLM);
    	this.tag(ModEntityTypes.Tags.DEEP_COLD_OCEAN_FISH).add(ModEntityTypes.STURGEON);
    }
	
}
