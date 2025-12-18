package fuffles.ichthyology.init;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.blocks.CrabBurrowingBlock;
import fuffles.ichthyology.common.blocks.FishRoeBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks
{
    public static final RegistryRelay<Block> REGISTRY = new RegistryRelay<>(Registries.BLOCK, Ichthyology::id);

    private static FishRoeBlock registerEggs(String id, EntityType<? extends Mob> toSpawn) {
    	return REGISTRY.register(id + "_roe", new FishRoeBlock(() -> toSpawn, Block.Properties.copy(Blocks.FROGSPAWN)));
    }
    
    public static final Block CATFISH_ROE = registerEggs("catfish", ModEntityTypes.CATFISH_BABY);
    public static final Block PEACOCK_BASS_ROE = registerEggs("peacock_bass", ModEntityTypes.PEACOCK_BASS_BABY);
    public static final Block GAR_ROE = registerEggs("gar", ModEntityTypes.GAR_BABY);
    public static final Block STURGEON_ROE = registerEggs("sturgeon", ModEntityTypes.STURGEON_BABY);
    public static final Block OLM_EGGS = REGISTRY.register("olm_eggs", new FishRoeBlock(() -> ModEntityTypes.OLM, Block.Properties.copy(Blocks.FROGSPAWN)));
    
    public static final Block MUD_WITH_CRAB = REGISTRY.register("mud_with_crab", new CrabBurrowingBlock(Blocks.MUD, BlockBehaviour.Properties.copy(Blocks.MUD).randomTicks()));
    public static final Block SAND_WITH_CRAB = REGISTRY.register("sand_with_crab", new CrabBurrowingBlock(Blocks.SAND, BlockBehaviour.Properties.copy(Blocks.SAND).randomTicks()));
    public static final Block RED_SAND_WITH_CRAB = REGISTRY.register("red_sand_with_crab", new CrabBurrowingBlock(Blocks.RED_SAND, BlockBehaviour.Properties.copy(Blocks.RED_SAND).randomTicks()));
    
    public static final class Tags
    {
        public static final TagKey<Block> AMPHIBIOUS_EGGS = tag("amphibious_eggs");
        public static final TagKey<Block> STRIPPED_LOGS = tag("stripped_logs");

        private static TagKey<Block> tag(String path)
        {
            return TagKey.create(Registries.BLOCK, Ichthyology.id(path));
        }
    }
}