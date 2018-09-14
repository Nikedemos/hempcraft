package nikedemos.hempcraft.status;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class PseudoPotionBase extends Potion{
	
	public static ResourceLocation EFFECTS = new ResourceLocation("hemp:textures/gui/effects.png");

	protected PseudoPotionBase(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn);
		// TODO Auto-generated constructor stub
	}

	
	
}
