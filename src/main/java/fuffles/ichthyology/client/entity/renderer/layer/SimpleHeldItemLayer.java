package fuffles.ichthyology.client.entity.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import fuffles.ichthyology.client.entity.model.IHoldItemModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SimpleHeldItemLayer<T extends LivingEntity, U extends EntityModel<T> & IHoldItemModel<T>> extends RenderLayer<T, U>
{
    private final ItemInHandRenderer itemInHandRenderer;

    public SimpleHeldItemLayer(RenderLayerParent<T, U> parent, ItemInHandRenderer itemRenderer)
    {
        super(parent);
        this.itemInHandRenderer = itemRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch)
    {
        ItemStack renderStack = entity.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!renderStack.isEmpty())
        {
            poseStack.pushPose();
            this.getParentModel().translateHeldItem(poseStack, entity, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);
            this.itemInHandRenderer.renderItem(entity, renderStack, ItemDisplayContext.GROUND, false, poseStack, buffer, packedLight);
            poseStack.popPose();
        }
    }
}
