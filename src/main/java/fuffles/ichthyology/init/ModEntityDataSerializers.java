package fuffles.ichthyology.init;

import fuffles.ichthyology.common.entity.AfricanCichlid;
import fuffles.ichthyology.common.entity.Carp;
import fuffles.ichthyology.common.entity.Crayfish;
import fuffles.ichthyology.common.entity.Goldfish;
import fuffles.ichthyology.common.entity.Olm;
import fuffles.ichthyology.common.entity.perch.Perch;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

public class ModEntityDataSerializers
{
    public static final EntityDataSerializer<AfricanCichlid.Variant> AFRICAN_CICHLID_VARIANT = EntityDataSerializer.simpleEnum(AfricanCichlid.Variant.class);
    public static final EntityDataSerializer<Carp.Variant> CARP_VARIANT = EntityDataSerializer.simpleEnum(Carp.Variant.class);
    public static final EntityDataSerializer<Goldfish.Variant> GOLDFISH_VARIANT = EntityDataSerializer.simpleEnum(Goldfish.Variant.class);
    public static final EntityDataSerializer<Crayfish.Variant> CRAYFISH_VARIANT = EntityDataSerializer.simpleEnum(Crayfish.Variant.class);
    public static final EntityDataSerializer<Perch.Variant> PERCH_VARIANT = EntityDataSerializer.simpleEnum(Perch.Variant.class);
    public static final EntityDataSerializer<Olm.Variant> OLM_VARIANT = EntityDataSerializer.simpleEnum(Olm.Variant.class);

    public static void register()
    {
        EntityDataSerializers.registerSerializer(AFRICAN_CICHLID_VARIANT);
        EntityDataSerializers.registerSerializer(CARP_VARIANT);
        EntityDataSerializers.registerSerializer(GOLDFISH_VARIANT);
        EntityDataSerializers.registerSerializer(CRAYFISH_VARIANT);
        EntityDataSerializers.registerSerializer(PERCH_VARIANT);
        EntityDataSerializers.registerSerializer(OLM_VARIANT);
    }
}
