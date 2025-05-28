package fuffles.ichthyology.client.entity.renderer;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;

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
    
    protected void scale(Olm olm, PoseStack matrixStackIn, float partialTickTime) {
		if(olm.isBaby()) {
			matrixStackIn.scale(0.5F, 0.5F, 0.5F);
		}
		super.scale(olm, matrixStackIn, partialTickTime);
	}

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(Olm entity)
    {
        return entity.getVariant().getTexture();
    }
}
