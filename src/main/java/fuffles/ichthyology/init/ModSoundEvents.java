package fuffles.ichthyology.init;

import fuffles.ichthyology.Ichthyology;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSoundEvents
{
    public static final RegistryRelay<SoundEvent> REGISTRY = new RegistryRelay<>(Registries.SOUND_EVENT, Ichthyology::id);

    private static SoundEvent registerVariable(String id)
    {
        ResourceLocation resource = REGISTRY.createLocation(id);
        return REGISTRY.register(resource, SoundEvent.createVariableRangeEvent(resource));
    }

    public static final SoundEvent AFRICAN_CICHLID_DEATH = registerVariable("entity.african_cichlid.death");
    public static final SoundEvent AFRICAN_CICHLID_FLOP = registerVariable("entity.african_cichlid.flop");
    public static final SoundEvent AFRICAN_CICHLID_HURT = registerVariable("entity.african_cichlid.hurt");

    public static final SoundEvent TILAPIA_DEATH = registerVariable("entity.tilapia.death");
    public static final SoundEvent TILAPIA_FLOP = registerVariable("entity.tilapia.flop");
    public static final SoundEvent TILAPIA_HURT = registerVariable("entity.tilapia.hurt");

    public static final SoundEvent PERCH_DEATH = registerVariable("entity.perch.death");
    public static final SoundEvent PERCH_EAT = registerVariable("entity.perch.eat");
    public static final SoundEvent PERCH_FLOP = registerVariable("entity.perch.flop");
    public static final SoundEvent PERCH_HURT = registerVariable("entity.perch.hurt");

    public static final SoundEvent ICEFISH_DEATH = registerVariable("entity.icefish.death");
    public static final SoundEvent ICEFISH_FLOP = registerVariable("entity.icefish.flop");
    public static final SoundEvent ICEFISH_HURT = registerVariable("entity.icefish.hurt");

    public static final SoundEvent BLIND_CAVE_TETRA_DEATH = registerVariable("entity.blind_cave_tetra.death");
    public static final SoundEvent BLIND_CAVE_TETRA_FLOP = registerVariable("entity.blind_cave_tetra.flop");
    public static final SoundEvent BLIND_CAVE_TETRA_HURT = registerVariable("entity.blind_cave_tetra.hurt");

    public static final SoundEvent NEON_TETRA_DEATH = registerVariable("entity.neon_tetra.death");
    public static final SoundEvent NEON_TETRA_FLOP = registerVariable("entity.neon_tetra.flop");
    public static final SoundEvent NEON_TETRA_HURT = registerVariable("entity.neon_tetra.hurt");

    public static final SoundEvent GOLDFISH_DEATH = registerVariable("entity.goldfish.death");
    public static final SoundEvent GOLDFISH_FLOP = registerVariable("entity.goldfish.flop");
    public static final SoundEvent GOLDFISH_HURT = registerVariable("entity.goldfish.hurt");

    public static final SoundEvent PIRANHA_BITE = registerVariable("entity.piranha.bite");
    public static final SoundEvent PIRANHA_DEATH = registerVariable("entity.piranha.death");
    public static final SoundEvent PIRANHA_FLOP = registerVariable("entity.piranha.flop");
    public static final SoundEvent PIRANHA_HURT = registerVariable("entity.piranha.hurt");

    public static final SoundEvent ANGELFISH_DEATH = registerVariable("entity.angelfish.death");
    public static final SoundEvent ANGELFISH_FLOP = registerVariable("entity.angelfish.flop");
    public static final SoundEvent ANGELFISH_HURT = registerVariable("entity.angelfish.hurt");

    public static final SoundEvent CARP_DEATH = registerVariable("entity.carp.death");
    public static final SoundEvent CARP_FLOP = registerVariable("entity.carp.flop");
    public static final SoundEvent CARP_HURT = registerVariable("entity.carp.hurt");

    public static final SoundEvent DISCUS_DEATH = registerVariable("entity.discus.death");
    public static final SoundEvent DISCUS_FLOP = registerVariable("entity.discus.flop");
    public static final SoundEvent DISCUS_HURT = registerVariable("entity.discus.hurt");

    public static final SoundEvent PLECO_DEATH = registerVariable("entity.pleco.death");
    public static final SoundEvent PLECO_FLOP = registerVariable("entity.pleco.flop");
    public static final SoundEvent PLECO_HURT = registerVariable("entity.pleco.hurt");

    public static final SoundEvent ARCHERFISH_DEATH = registerVariable("entity.archerfish.death");
    public static final SoundEvent ARCHERFISH_FLOP = registerVariable("entity.archerfish.flop");
    public static final SoundEvent ARCHERFISH_HURT = registerVariable("entity.archerfish.hurt");
    

    public static final SoundEvent MUDSKIPPER_DEATH = registerVariable("entity.mudskipper.death");
    public static final SoundEvent MUDSKIPPER_HURT = registerVariable("entity.mudskipper.hurt");
}