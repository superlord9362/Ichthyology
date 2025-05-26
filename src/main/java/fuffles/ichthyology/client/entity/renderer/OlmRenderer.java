package fuffles.ichthyology.client.entity.renderer;

import org.jetbrains.annotations.NotNull;

import fuffles.ichthyology.client.ClientEvents;
import fuffles.ichthyology.client.entity.model.OlmModel;
import fuffles.ichthyology.common.entity.Olm;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class OlmRenderer extends MobRenderer<Olm, OlmModel>
{
    public OlmRenderer(EntityRendererProvider.Context context)
    {
        super(context, new OlmModel(context.bakeLayer(ClientEvents.OLM)), 0.2F);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(Olm entity)
    {
        return entity.getVariant().getTexture();
    }
}
