package fuffles.ichthyology.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Mob;

public abstract class AbstractFishRenderer<T extends Mob, M extends EntityModel<T>> extends MobRenderer<T, M>
{
    public AbstractFishRenderer(EntityRendererProvider.Context context, M model, float shadowSize)
    {
        super(context, model, shadowSize);
    }

    protected float getYRotation(T abstractFish, float ageInTicks)
    {
        return 0;
    }

    @Override
    protected void setupRotations(T abstractFish, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks)
    {
        super.setupRotations(abstractFish, poseStack, ageInTicks, rotationYaw, partialTicks);
        poseStack.mulPose(Axis.YP.rotationDegrees(this.getYRotation(abstractFish, ageInTicks)));
        if (!abstractFish.isInWater()) {
            poseStack.translate(0.1F, 0.1F, -0.1F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }
    }
}
