package fuffles.ichthyology.mixin.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.PufferfishSmallModel;
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

@Mixin(PufferfishSmallModel.class)
public abstract class PufferfishSmallModelMixin<T extends Entity> extends HierarchicalModel<T>
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

    @SuppressWarnings("unused")
	@Inject(method = "createBodyLayer()Lnet/minecraft/client/model/geom/builders/LayerDefinition;", at = @At("HEAD"), cancellable = true)
    private static void createBodyLayer(CallbackInfoReturnable<LayerDefinition> cbr)
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        int i = 23;
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 27).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 2.0F, 3.0F), PartPose.offset(0.0F, 23.0F, 0.0F));
        body.addOrReplaceChild("right_eye", CubeListBuilder.create().texOffs(24, 6).addBox(-1.5F, 0.0F, -1.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(0.0F, -3.0F, 0.0F));
        body.addOrReplaceChild("left_eye", CubeListBuilder.create().texOffs(28, 6).addBox(0.5F, 0.0F, -1.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(0.0F, -3.0F, 0.0F));
        body.addOrReplaceChild("back_fin", CubeListBuilder.create().texOffs(-3, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 0.0F, 3.0F), PartPose.offset(0.0F, -1.0F, 1.5F));
        body.addOrReplaceChild("right_fin", CubeListBuilder.create().texOffs(25, 0).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 2.0F), PartPose.offset(-1.5F, -1.0F, -1.5F));
        body.addOrReplaceChild("left_fin", CubeListBuilder.create().texOffs(25, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 2.0F), PartPose.offset(1.5F, -1.0F, -1.5F));
        cbr.setReturnValue(LayerDefinition.create(meshdefinition, 32, 32));
    }

    @Inject(method = "setupAnim", at = @At("TAIL"))
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo cb)
    {
        this.ichthyology$body.xRot = headPitch * Mth.DEG_TO_RAD;
        this.ichthyology$body.yRot = netHeadYaw * Mth.DEG_TO_RAD;
    }
}
