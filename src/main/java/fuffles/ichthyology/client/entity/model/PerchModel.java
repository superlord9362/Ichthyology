package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.math.Axis;
import fuffles.ichthyology.common.entity.perch.Perch;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

@SuppressWarnings("unused")
public class PerchModel<T extends Perch> extends RootedModel<T> implements IHoldItemModel<T>
{
	private final ModelPart Body;
	private final ModelPart Tail;
	private final ModelPart Tail_f;
	private final ModelPart mouth;
	private final ModelPart Dorsal_f;
	private final ModelPart Anal_f;
	private final ModelPart Pelvic_f;
	private final ModelPart Pectoral_f_r;
	private final ModelPart Pectoral_f_l;

	public PerchModel(ModelPart root) {
		super(root);
		this.Body = root.getChild("Body");
		this.Tail = this.Body.getChild("Tail");
		this.Tail_f = this.Tail.getChild("Tail_f");
		this.mouth = this.Body.getChild("mouth");
		this.Dorsal_f = this.Body.getChild("Dorsal_f");
		this.Anal_f = this.Body.getChild("Anal_f");
		this.Pelvic_f = this.Body.getChild("Pelvic_f");
		this.Pectoral_f_r = this.Body.getChild("Pectoral_f_r");
		this.Pectoral_f_l = this.Body.getChild("Pectoral_f_l");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 4.0F, 7.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, 21.0F, -3.75F));

		PartDefinition Tail = Body.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(11, 0).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(26, -2).addBox(0.0F, -1.0F, 1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 5.0F));

		PartDefinition Tail_f = Tail.addOrReplaceChild("Tail_f", CubeListBuilder.create().texOffs(17, -4).addBox(0.0F, 0.0F, 0.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));

		PartDefinition mouth = Body.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(0, 11).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.0F));

		PartDefinition Dorsal_f = Body.addOrReplaceChild("Dorsal_f", CubeListBuilder.create().texOffs(22, -1).addBox(0.0F, -2.0F, 0.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition Anal_f = Body.addOrReplaceChild("Anal_f", CubeListBuilder.create().texOffs(0, -2).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 4.0F));

		PartDefinition Pelvic_f = Body.addOrReplaceChild("Pelvic_f", CubeListBuilder.create().texOffs(9, 12).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));

		PartDefinition Pectoral_f_r = Body.addOrReplaceChild("Pectoral_f_r", CubeListBuilder.create().texOffs(0, 4).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition Pectoral_f_l = Body.addOrReplaceChild("Pectoral_f_l", CubeListBuilder.create().texOffs(0, 3).addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 16);
	}


	@Override
	public void translateHeldItem(PoseStack poseStack, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch)
	{
		//-F: translate to body
		poseStack.translate(this.Body.x / 16F, this.Body.y / 16F, this.Body.z / 16F);
		poseStack.mulPose(Axis.XP.rotationDegrees(headPitch));
		poseStack.mulPose(Axis.YP.rotationDegrees(netHeadYaw));
		//-F: apply custom offset
		poseStack.translate(0F, -1F / 16F, -0.425F);
		poseStack.mulPose(Axis.YP.rotationDegrees(22.5F));
		poseStack.mulPose(Axis.XP.rotationDegrees(90F));
	}

	@Override
	public void setupAnim(Perch entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 1F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.Body.xRot = headPitch * Mth.DEG_TO_RAD;
		this.Body.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		this.Tail.yRot = f * 0.45F * Mth.sin(0.6F * ageInTicks) * 0.5F;
		this.Tail_f.yRot = f * 0.45F * Mth.sin(0.6F * ageInTicks);
		this.Pectoral_f_r.yRot = -Mth.HALF_PI / 2F; // Mth.abs(-0.85F * Mth.sin(0.15F * ageInTicks));
		this.Pectoral_f_l.yRot = Mth.HALF_PI / 2F; // -Mth.abs(0.85F * Mth.sin(0.15F * ageInTicks));
	}
}