package fuffles.ichthyology.mixin.client.model;

import net.minecraft.client.model.ColorableHierarchicalModel;
import net.minecraft.client.model.TropicalFishModelB;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TropicalFishModelB.class)
public abstract class TropicalFishModelBMixin<T extends Entity> extends ColorableHierarchicalModel<T>
{
    @Unique
    private ModelPart ichthyology$body;

    @Redirect(method = "<init>(Lnet/minecraft/client/model/geom/ModelPart;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;getChild(Ljava/lang/String;)Lnet/minecraft/client/model/geom/ModelPart;"))
    public ModelPart init(ModelPart root, String name)
    {
        this.ichthyology$body = root.getChild("body");
        return this.ichthyology$body.getChild(name);
    }

    @Inject(method = "createBodyLayer(Lnet/minecraft/client/model/geom/builders/CubeDeformation;)Lnet/minecraft/client/model/geom/builders/LayerDefinition;", at = @At("HEAD"), cancellable = true)
    private static void createBodyLayer(CubeDeformation deformation, CallbackInfoReturnable<LayerDefinition> cbr)
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, -3.0F, -3.0F, 2.0F, 6.0F, 6.0F, deformation), PartPose.offset(0.0F, 19.0F, 0.0F));
        body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(21, 16).addBox(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 5.0F, deformation), PartPose.offset(0.0F, 0.0F, 3.0F));
        body.addOrReplaceChild("right_fin", CubeListBuilder.create().texOffs(2, 16).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, deformation), PartPose.offsetAndRotation(-1.0F, 1.0F, 0.0F, 0.0F, ((float)Math.PI / 4F), 0.0F));
        body.addOrReplaceChild("left_fin", CubeListBuilder.create().texOffs(2, 12).addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, deformation), PartPose.offsetAndRotation(1.0F, 1.0F, 0.0F, 0.0F, (-(float)Math.PI / 4F), 0.0F));
        body.addOrReplaceChild("top_fin", CubeListBuilder.create().texOffs(20, 11).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 6.0F, deformation), PartPose.offset(0.0F, -3.0F, -3.0F));
        body.addOrReplaceChild("bottom_fin", CubeListBuilder.create().texOffs(20, 21).addBox(0.0F, 0.0F, 0.0F, 0.0F, 4.0F, 6.0F, deformation), PartPose.offset(0.0F, 3.0F, -3.0F));
        cbr.setReturnValue(LayerDefinition.create(meshdefinition, 32, 32));
    }

    @Inject(method = "setupAnim", at = @At("TAIL"))
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo cb)
    {
        this.ichthyology$body.xRot = headPitch * Mth.DEG_TO_RAD;
        this.ichthyology$body.yRot = netHeadYaw * Mth.DEG_TO_RAD;
    }
}
