package nikedemos.hempcraft.status;

import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;
import nikedemos.hempcraft.Main;

public class PseudoPotionBase extends Potion
{
	
	public static ResourceLocation EFFECTS = new ResourceLocation("hemp:textures/gui/effects.png");

	protected PseudoPotionBase(boolean isBadEffectIn, int liquidColorIn, String effect_name) {
		super(isBadEffectIn, liquidColorIn);
		setPotionName("effect.hemp." + effect_name + ".name");
		setRegistryName(Main.MODID, effect_name);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public int getStatusIconIndex()
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(EFFECTS);
		//
		return (super.getStatusIconIndex());
	}
	
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier)
    {
        entity.setVelocity(0D, 0D, 0D);
        entity.velocityChanged = true;
        entity.moveVertical = 0;
        entity.moveStrafing = 0;
        entity.motionX      = 0;
        entity.motionZ      = 0;
    }	
}

