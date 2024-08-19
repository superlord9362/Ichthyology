package fuffles.ichthyology.mixin.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.PufferfishMidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PufferfishMidModel.class)
public abstract class PufferfishMidModelMixin<T extends Entity> extends HierarchicalModel<T>
{
    @Unique
    private ModelPart ichthyology$body;

    @Redirect(method = "<init>(Lnet/minecraft/client/model/geom/ModelPart;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;getChild(Ljava/lang/String;)Lnet/minecraft/client/model/geom/ModelPart;"))
    public ModelPart init(ModelPart root, String name)
    {
        if (this.ichthyology$body == null)
            this.ichthyology$body = root.getChild("body");
        return this.ichthyology$body.getChild(name);
    }

    @Inject(method = "createBodyLayer()Lnet/minecraft/client/model/geom/builders/LayerDefinition;", at = @At("HEAD"), cancellable = true)
    private static void createBodyLayer(CallbackInfoReturnable<LayerDefinition> cbr)
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(12, 22).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F), PartPose.offset(0.0F, 22.0F, 0.0F));
        body.addOrReplaceChild("right_blue_fin", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F), PartPose.offset(-2.5F, -5.0F, -1.5F));
        body.addOrReplaceChild("left_blue_fin", CubeListBuilder.create().texOffs(24, 3).addBox(0.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F), PartPose.offset(2.5F, -5.0F, -1.5F));
        body.addOrReplaceChild("top_front_fin", CubeListBuilder.create().texOffs(15, 16).addBox(-2.5F, -1.0F, 0.0F, 5.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -5.0F, -2.5F, ((float)Math.PI / 4F), 0.0F, 0.0F));
        body.addOrReplaceChild("top_back_fin", CubeListBuilder.create().texOffs(10, 16).addBox(-2.5F, -1.0F, -1.0F, 5.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -5.0F, 2.5F, (-(float)Math.PI / 4F), 0.0F, 0.0F));
        body.addOrReplaceChild("right_front_fin", CubeListBuilder.create().texOffs(8, 16).addBox(-1.0F, -5.0F, 0.0F, 1.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(-2.5F, 0.0F, -2.5F, 0.0F, (-(float)Math.PI / 4F), 0.0F));
        body.addOrReplaceChild("right_back_fin", CubeListBuilder.create().texOffs(8, 16).addBox(-1.0F, -5.0F, 0.0F, 1.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(-2.5F, 0.0F, 2.5F, 0.0F, ((float)Math.PI / 4F), 0.0F));
        body.addOrReplaceChild("left_back_fin", CubeListBuilder.create().texOffs(4, 16).addBox(0.0F, -5.0F, 0.0F, 1.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(2.5F, 0.0F, 2.5F, 0.0F, (-(float)Math.PI / 4F), 0.0F));
        body.addOrReplaceChild("left_front_fin", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -5.0F, 0.0F, 1.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(2.5F, 0.0F, -2.5F, 0.0F, ((float)Math.PI / 4F), 0.0F));
        body.addOrReplaceChild("bottom_back_fin", CubeListBuilder.create().texOffs(8, 22).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(0.5F, 0.0F, 2.5F, ((float)Math.PI / 4F), 0.0F, 0.0F));
        body.addOrReplaceChild("bottom_front_fin", CubeListBuilder.create().texOffs(17, 21).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 1.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -2.5F, (-(float)Math.PI / 4F), 0.0F, 0.0F));
        cbr.setReturnValue(LayerDefinition.create(meshdefinition, 32, 32));
    }

    @Inject(method = "setupAnim", at = @At("TAIL"))
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo cb)
    {
        this.ichthyology$body.xRot = headPitch * Mth.DEG_TO_RAD;
        this.ichthyology$body.yRot = netHeadYaw * Mth.DEG_TO_RAD;
    }
}
