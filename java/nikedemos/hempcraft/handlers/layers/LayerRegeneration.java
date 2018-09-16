package nikedemos.hempcraft.handlers.layers;

import nikedemos.hempcraft.Main;
import nikedemos.hempcraft.capability.CapabilityRegeneration;
import nikedemos.hempcraft.capability.IRegeneration;
import nikedemos.hempcraft.util.RenderUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class LayerRegeneration implements LayerRenderer<EntityPlayer> {

    private static final ModelPlayer playerModelLargeArms = new ModelPlayer(0.1F, false);
    private static final ModelPlayer playerModelSmallArms = new ModelPlayer(0.1F, true);
    private static final ResourceLocation REGEN_TEXTURE = new ResourceLocation(Main.MODID, "textures/entity/regen.png");
    private RenderPlayer playerRenderer;

    public LayerRegeneration(RenderPlayer playerRenderer) {
        this.playerRenderer = playerRenderer;
    }

    @Override
    public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        IRegeneration capability = CapabilityRegeneration.get(player);
        if (capability.isCapable() && capability.isRegenerating()) {
            Main.LOG.info(capability.getTicksRegenerating());
            renderEffect(playerRenderer, capability, player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }


    private void renderEffect(RenderLivingBase<?> renderLivingBase, IRegeneration capability, EntityPlayer entityPlayer, float v, float v1, float v2, float v3, float v4, float v5, float v6) {
        ModelBiped model = (ModelBiped) renderLivingBase.getMainModel();

        // State manager changes
        GlStateManager.pushAttrib();
        GlStateManager.disableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GlStateManager.depthMask(false);
        RenderUtil.setLightmapTextureCoords(65, 65);

        NBTTagCompound style = capability.getStyle();
        Color primaryColor = new Color(style.getFloat("PrimaryRed"), style.getFloat("PrimaryGreen"), style.getFloat("PrimaryBlue"));
        Color secondaryColor = new Color(style.getFloat("SecondaryRed"), style.getFloat("SecondaryGreen"), style.getFloat("SecondaryBlue"));

        float primaryScale = capability.getTimesRegenerated() * 40f;
        float secondaryScale = capability.getTimesRegenerated() * 70f;

        // Render right cone
        GlStateManager.pushMatrix();
        model.postRenderArm(0.0625F, EnumHandSide.RIGHT);
        GlStateManager.translate(0f, -0.2f, 0f);
        renderCone(entityPlayer, primaryScale, primaryScale, primaryColor);
        renderCone(entityPlayer, secondaryScale, secondaryScale * 1.5f, secondaryColor);
        GlStateManager.popMatrix();

        // Render left cone
        GlStateManager.pushMatrix();
        model.postRenderArm(0.0625F, EnumHandSide.LEFT);
        GlStateManager.translate(0f, -0.2f, 0f);
        renderCone(entityPlayer, primaryScale, primaryScale, primaryColor);
        renderCone(entityPlayer, secondaryScale, secondaryScale * 1.5f, secondaryColor);
        GlStateManager.popMatrix();

        // Render head cone
        GlStateManager.pushMatrix();
        GlStateManager.translate(0f, 0.3f, 0f);
        GlStateManager.rotate(180, 1.0f, 0.0f, 0.0f);
        renderCone(entityPlayer, primaryScale, primaryScale, primaryColor);
        renderCone(entityPlayer, secondaryScale, secondaryScale * 1.5f, secondaryColor);
        GlStateManager.popMatrix();

        // Check which slightly larger model to use
        ModelPlayer playerModel = ((AbstractClientPlayer) entityPlayer).getSkinType().equals("slim") ? playerModelSmallArms : playerModelLargeArms;

        // Define which parts are glowing
        playerModel.bipedBody.isHidden = playerModel.bipedLeftLeg.isHidden = playerModel.bipedRightLeg.isHidden = playerModel.bipedBodyWear.isHidden = playerModel.bipedHeadwear.isHidden = playerModel.bipedLeftLegwear.isHidden = playerModel.bipedRightLegwear.isHidden = false;

        // Copy model attributes from the real player model
        playerModel.setModelAttributes(model);

        // Render glowing overlay
        GlStateManager.color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), 1);
        playerModel.render(entityPlayer, v, v1, v3, v4, v5, v6);

        // Undo state manager changes
        RenderUtil.restoreLightMap();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(255, 255, 255, 255);
        GlStateManager.enableTexture2D();
        GlStateManager.popAttrib();
    }


    public void renderCone(EntityPlayer entityPlayer, float scale, float scale2, Color color) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        for (int i = 0; i < 8; i++) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(entityPlayer.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(1.0f, 1.0f, 0.65f);
            vertexBuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
            vertexBuffer.pos(0.0D, 0.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
            vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
            vertexBuffer.pos(0.266D * scale, scale, -0.5F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
            vertexBuffer.pos(0.0D, scale2, 1.0F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
            vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
