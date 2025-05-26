package fuffles.ichthyology.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import fuffles.ichthyology.common.entity.Crayfish;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

@SuppressWarnings("unused")
public class CrayfishModel extends EntityModel<Crayfish> {
	private final ModelPart carapace;
	private final ModelPart head;
	private final ModelPart antenna;
	private final ModelPart antenna_1;
	private final ModelPart Head_2;
	private final ModelPart eye;
	private final ModelPart eye_1;
	private final ModelPart abdomen;
	private final ModelPart telson;
	private final ModelPart leg_l_1;
	private final ModelPart leg_r_1;
	private final ModelPart leg_l_2;
	private final ModelPart leg_r_2;
	private final ModelPart leg_l_3;
	private final ModelPart leg_r_3;
	private final ModelPart leg_l_4;
	private final ModelPart leg_r_4;
	private final ModelPart Pincer_r;
	private final ModelPart Pincer_top_r;
	private final ModelPart Pincer_bot_r;
	private final ModelPart Pincer_l;
	private final ModelPart Pincer_top_l;
	private final ModelPart Pincer_bot_l;

	public CrayfishModel(ModelPart root) {
		this.carapace = root.getChild("carapace");
		this.head = this.carapace.getChild("head");
		this.antenna = this.head.getChild("antenna");
		this.antenna_1 = this.head.getChild("antenna_1");
		this.Head_2 = this.head.getChild("Head_2");
		this.eye = this.head.getChild("eye");
		this.eye_1 = this.head.getChild("eye_1");
		this.abdomen = this.carapace.getChild("abdomen");
		this.telson = this.abdomen.getChild("telson");
		this.leg_l_1 = this.carapace.getChild("leg_l_1");
		this.leg_r_1 = this.carapace.getChild("leg_r_1");
		this.leg_l_2 = this.carapace.getChild("leg_l_2");
		this.leg_r_2 = this.carapace.getChild("leg_r_2");
		this.leg_l_3 = this.carapace.getChild("leg_l_3");
		this.leg_r_3 = this.carapace.getChild("leg_r_3");
		this.leg_l_4 = this.carapace.getChild("leg_l_4");
		this.leg_r_4 = this.carapace.getChild("leg_r_4");
		this.Pincer_r = this.carapace.getChild("Pincer_r");
		this.Pincer_top_r = this.Pincer_r.getChild("Pincer_top_r");
		this.Pincer_bot_r = this.Pincer_r.getChild("Pincer_bot_r");
		this.Pincer_l = this.carapace.getChild("Pincer_l");
		this.Pincer_top_l = this.Pincer_l.getChild("Pincer_top_l");
		this.Pincer_bot_l = this.Pincer_l.getChild("Pincer_bot_l");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition carapace = partdefinition.addOrReplaceChild("carapace", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.8F, 0.0F));

		PartDefinition head = carapace.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 7).addBox(-0.5F, -0.5F, -4.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, -0.7F, 0.0873F, 0.0F, 0.0F));

		PartDefinition antenna = head.addOrReplaceChild("antenna", CubeListBuilder.create().texOffs(0, 9).addBox(0.0F, -3.0F, -8.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.5F, -4.0F, 0.0F, 0.2618F, 0.0F));

		PartDefinition antenna_1 = head.addOrReplaceChild("antenna_1", CubeListBuilder.create().texOffs(0, 9).mirror().addBox(0.0F, -3.0F, -8.0F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, 0.5F, -4.0F, 0.0F, -0.2618F, 0.0F));

		PartDefinition Head_2 = head.addOrReplaceChild("Head_2", CubeListBuilder.create().texOffs(0, 27).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.6F, 0.5F));

		PartDefinition eye = head.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(11, 30).mirror().addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.2F, -2.5F, 0.0F, -0.6109F, 0.0F));

		PartDefinition eye_1 = head.addOrReplaceChild("eye_1", CubeListBuilder.create().texOffs(11, 30).addBox(0.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.2F, -2.5F, 0.0F, 0.6109F, 0.0F));

		PartDefinition abdomen = carapace.addOrReplaceChild("abdomen", CubeListBuilder.create().texOffs(14, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.8F, 2.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition telson = abdomen.addOrReplaceChild("telson", CubeListBuilder.create().texOffs(24, 2).mirror().addBox(-1.5F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.0F, 4.0F, -0.5934F, 0.0F, 0.0F));

		PartDefinition leg_l_1 = carapace.addOrReplaceChild("leg_l_1", CubeListBuilder.create().texOffs(0, 21).addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, 0.0F, 0.0F, 0.2618F, 0.0F));

		PartDefinition leg_r_1 = carapace.addOrReplaceChild("leg_r_1", CubeListBuilder.create().texOffs(0, 21).mirror().addBox(-3.0F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 1.0F, 0.0F, 0.0F, -0.2618F, 0.0F));

		PartDefinition leg_l_2 = carapace.addOrReplaceChild("leg_l_2", CubeListBuilder.create().texOffs(0, 21).addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, 0.5F, 0.0F, -0.0873F, 0.0F));

		PartDefinition leg_r_2 = carapace.addOrReplaceChild("leg_r_2", CubeListBuilder.create().texOffs(0, 21).mirror().addBox(-3.0F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 1.0F, 0.5F, 0.0F, 0.0873F, 0.0F));

		PartDefinition leg_l_3 = carapace.addOrReplaceChild("leg_l_3", CubeListBuilder.create().texOffs(0, 21).addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, 1.0F, 0.0F, -0.3491F, 0.0F));

		PartDefinition leg_r_3 = carapace.addOrReplaceChild("leg_r_3", CubeListBuilder.create().texOffs(0, 21).mirror().addBox(-3.0F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 1.0F, 1.0F, 0.0F, 0.3491F, 0.0F));

		PartDefinition leg_l_4 = carapace.addOrReplaceChild("leg_l_4", CubeListBuilder.create().texOffs(0, 21).addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, 1.5F, 0.0F, -0.6981F, 0.0F));

		PartDefinition leg_r_4 = carapace.addOrReplaceChild("leg_r_4", CubeListBuilder.create().texOffs(0, 21).mirror().addBox(-3.0F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 1.0F, 1.5F, 0.0F, 0.6981F, 0.0F));

		PartDefinition Pincer_r = carapace.addOrReplaceChild("Pincer_r", CubeListBuilder.create().texOffs(23, 18).mirror().addBox(-2.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0002F)).mirror(false), PartPose.offsetAndRotation(-1.5F, 1.0F, -2.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition Pincer_top_r = Pincer_r.addOrReplaceChild("Pincer_top_r", CubeListBuilder.create().texOffs(13, 17).mirror().addBox(-0.5F, -1.0F, -3.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0001F)).mirror(false), PartPose.offset(-1.5F, 0.0F, -2.0F));

		PartDefinition Pincer_bot_r = Pincer_r.addOrReplaceChild("Pincer_bot_r", CubeListBuilder.create().texOffs(18, 27).mirror().addBox(-0.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, 1.0F, -2.0F));

		PartDefinition Pincer_l = carapace.addOrReplaceChild("Pincer_l", CubeListBuilder.create().texOffs(23, 18).addBox(0.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0002F)), PartPose.offsetAndRotation(1.5F, 1.0F, -2.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition Pincer_top_l = Pincer_l.addOrReplaceChild("Pincer_top_l", CubeListBuilder.create().texOffs(13, 17).addBox(-0.5F, -1.0F, -3.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0001F)), PartPose.offset(1.5F, 0.0F, -2.0F));

		PartDefinition Pincer_bot_l = Pincer_l.addOrReplaceChild("Pincer_bot_l", CubeListBuilder.create().texOffs(18, 27).addBox(-0.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 1.0F, -2.0F));

		return LayerDefinition.create(meshdefinition, 31, 32);
	}

	@Override
	public void setupAnim(Crayfish entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float partialTick = ageInTicks - entity.tickCount;
		float attackProgress = entity.getMeleeProgress(partialTick);
		this.carapace.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.leg_r_4.zRot = 0;
		this.leg_l_4.zRot = 0;
		this.leg_r_3.zRot = 0;
		this.leg_l_3.zRot = 0;
		this.leg_r_2.zRot = 0;
		this.leg_l_2.zRot = 0;
		this.leg_r_1.zRot = 0;
		this.leg_l_1.zRot = 0;
		this.leg_r_4.yRot = ((float)Math.PI / 4F);
		this.leg_l_4.yRot = (-(float)Math.PI / 4F);
		this.leg_r_3.yRot = ((float)Math.PI / 8F);
		this.leg_l_3.yRot = (-(float)Math.PI / 8F);
		this.leg_r_2.yRot = (-(float)Math.PI / 8F);
		this.leg_l_2.yRot = ((float)Math.PI / 8F);
		this.leg_r_1.yRot = (-(float)Math.PI / 4F);
		this.leg_l_1.yRot = ((float)Math.PI / 4F);
		float f3 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
		float f4 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.4F) * limbSwingAmount;
		float f5 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + ((float)Math.PI / 2F)) * 0.4F) * limbSwingAmount;
		float f6 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + ((float)Math.PI * 1.5F)) * 0.4F) * limbSwingAmount;
		float f7 = Math.abs(Mth.sin(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount;
		float f8 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float)Math.PI) * 0.4F) * limbSwingAmount;
		float f9 = Math.abs(Mth.sin(limbSwing * 0.6662F + ((float)Math.PI / 2F)) * 0.4F) * limbSwingAmount;
		float f10 = Math.abs(Mth.sin(limbSwing * 0.6662F + ((float)Math.PI * 1.5F)) * 0.4F) * limbSwingAmount;
		this.leg_r_4.yRot += f3;
		this.leg_l_4.yRot += -f3;
		this.leg_r_3.yRot += f4;
		this.leg_l_3.yRot += -f4;
		this.leg_r_2.yRot += f5;
		this.leg_l_2.yRot += -f5;
		this.leg_r_1.yRot += f6;
		this.leg_l_1.yRot += -f6;
		this.leg_r_4.zRot += f7;
		this.leg_l_4.zRot += -f7;
		this.leg_r_3.zRot += f8;
		this.leg_l_3.zRot += -f8;
		this.leg_r_2.zRot += f9;
		this.leg_l_2.zRot += -f9;
		this.leg_r_1.zRot += f10;
		this.leg_l_1.zRot += -f10;
		this.abdomen.xRot = -Mth.abs(0.25F * Mth.sin(0.015F * ageInTicks)) + 0.0873F;
		this.antenna.xRot = 0.25F * Mth.sin(0.05F * ageInTicks);
		this.antenna_1.xRot = 0.25F * Mth.sin(0.05F * ageInTicks);
		this.Pincer_top_l.xRot = attackProgress * (float)Math.toRadians(-50);
		this.Pincer_top_r.xRot = attackProgress * (float)Math.toRadians(-50);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		carapace.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
