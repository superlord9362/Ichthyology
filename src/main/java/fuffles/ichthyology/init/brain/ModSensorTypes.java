package fuffles.ichthyology.init.brain;

import fuffles.ichthyology.Ichthyology;
import fuffles.ichthyology.common.entity.ai.sensing.AdvancedNearestItemSensor;
import fuffles.ichthyology.common.entity.ai.sensing.InHandSensor;
import fuffles.ichthyology.common.entity.ai.sensing.PerchSpecificSensor;
import fuffles.ichthyology.init.RegistryRelay;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.sensing.SensorType;

public class ModSensorTypes
{
    @SuppressWarnings("deprecation")
    public static final RegistryRelay<SensorType<?>> REGISTRY = new RegistryRelay<>(BuiltInRegistries.SENSOR_TYPE, Ichthyology::id);

    public static final SensorType<InHandSensor> HAS_INHANDITEM = REGISTRY.register("has_inhanditem", new SensorType<>(InHandSensor::new));
    public static final SensorType<AdvancedNearestItemSensor> ADVANCED_NEAREST_ITEMS = REGISTRY.register("advanced_nearest_items", new SensorType<>(AdvancedNearestItemSensor::new));
    public static final SensorType<PerchSpecificSensor> PERCH_SPECIFIC_SENSOR = REGISTRY.register("perch_specific_sensor", new SensorType<>(PerchSpecificSensor::new));
}
