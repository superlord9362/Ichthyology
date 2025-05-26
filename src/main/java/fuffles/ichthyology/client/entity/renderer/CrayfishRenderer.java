package fuffles.ichthyology.client.entity.renderer;

import org.jetbrains.annotations.NotNull;

import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.CrayfishModel;
import fuffles.ichthyology.common.entity.Crayfish;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CrayfishRenderer extends MobRenderer<Crayfish, CrayfishModel>
{
    public CrayfishRenderer(EntityRendererProvider.Context context)
    {
        super(context, new CrayfishModel(context.bakeLayer(ClientEvents.CRAYFISH)), 0.2F);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(Crayfish entity)
    {
        return entity.getVariant().getTexture();
    }
}