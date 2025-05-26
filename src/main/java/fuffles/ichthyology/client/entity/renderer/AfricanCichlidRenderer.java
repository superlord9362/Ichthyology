package fuffles.ichthyology.client.entity.renderer;

import org.jetbrains.annotations.NotNull;

import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.AfricanCichlidModel;
import fuffles.ichthyology.common.entity.AfricanCichlid;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class AfricanCichlidRenderer extends AbstractFishRenderer<AfricanCichlid, AfricanCichlidModel<AfricanCichlid>>
{
    public AfricanCichlidRenderer(EntityRendererProvider.Context context)
    {
        super(context, new AfricanCichlidModel<>(context.bakeLayer(ClientEvents.AFRICAN_CICHLID)), 0.2F);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(AfricanCichlid entity)
    {
        return entity.getVariant().getTexture();
    }
}