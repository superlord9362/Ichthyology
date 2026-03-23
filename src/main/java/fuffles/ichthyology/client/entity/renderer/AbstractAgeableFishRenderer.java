package fuffles.ichthyology.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import fuffles.ichthyology.common.entity.AbstractAgeableFish;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractAgeableFishRenderer<T extends AbstractAgeableFish, M extends EntityModel<T>> extends AbstractFishRenderer<T, M>
{
    private final M adultModel;
    private final M babyModel;
    private final float adultShadowSize;
    private final float babyShadowSize;

    public AbstractAgeableFishRenderer(EntityRendererProvider.Context context, M adultModel, float adultShadowSize, @Nullable M babyModel, float babyShadowSize)
    {
        super(context, adultModel, adultShadowSize);
        this.adultModel = adultModel;
        this.adultShadowSize = adultShadowSize;
        this.babyModel = babyModel;
        this.babyShadowSize = babyShadowSize;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight)
    {
        if (this.babyModel != null && entity.isBaby())
        {
            this.model = this.babyModel;
            this.shadowRadius = this.babyShadowSize;
        }
        else
        {
            this.model = this.adultModel;
            this.shadowRadius = this.adultShadowSize;
        }
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
