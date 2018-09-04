package nikedemos.hempcraft.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import nikedemos.hempcraft.init.ModItems;

public class HempCraftCreativeTab extends CreativeTabs
{
	public HempCraftCreativeTab(String label) { super("hemp"); }
	public ItemStack getTabIconItem() { return new ItemStack(ModItems.HEMP_LEAF);}
}
