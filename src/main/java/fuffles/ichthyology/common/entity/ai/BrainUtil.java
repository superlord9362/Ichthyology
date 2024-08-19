package fuffles.ichthyology.common.entity.ai;

import fuffles.ichthyology.Ichthyology;
import net.minecraft.world.entity.ai.Brain;

public class BrainUtil
{
    @SuppressWarnings("deprecation")
    public static void dumpBrain(Brain<?> brain)
    {
        Ichthyology.LOG.info("========== BRAIN DUMP ==========");
        brain.getActiveActivities().forEach(activity -> {
            Ichthyology.LOG.info("[Act] " + activity.toString());
        });
        brain.getRunningBehaviors().forEach(behaviorControl -> {
            Ichthyology.LOG.info("[Behav] " + behaviorControl.debugString());
        });
        brain.getMemories().forEach((memory, value) -> {
            Ichthyology.LOG.info("[Mem] " + memory.toString() + ": " + value.toString());
        });
        Ichthyology.LOG.info("========== ========== ==========");
    }
}
