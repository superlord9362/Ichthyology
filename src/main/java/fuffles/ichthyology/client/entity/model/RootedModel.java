package fuffles.ichthyology.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

public abstract class RootedModel<E extends Entity> extends HierarchicalModel<E>
{
    protected final ModelPart root;

    public RootedModel(ModelPart root)
    {
        this(RenderType::entityCutoutNoCull, root);
    }

    public RootedModel(Function<ResourceLocation, RenderType> renderType, ModelPart root)
    {
        super(renderType);
        this.root = root;
    }

    @Override
    public ModelPart root()
    {
        return this.root;
    }
}
