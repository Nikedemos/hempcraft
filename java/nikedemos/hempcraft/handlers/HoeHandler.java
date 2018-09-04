package nikedemos.hempcraft.handlers;

import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nikedemos.hempcraft.init.ModBlocks;

public class HoeHandler {
    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public static void usedHoe(UseHoeEvent event) {
    //check if player is has just right clicked COARSE DIRT while holding a diamond hoe - in such case,
    //it's not gonna turn coarse dirt into normal dirt. instead, it's gonna make it into a hemp plot
		if (!event.getWorld().isRemote && event.getEntityPlayer().isSneaking())
			{
			if (event.getWorld().getBlockState(event.getPos()) == Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT))
				{
				//okay the dirt sure is coarse, now is the player holding a hoe
				ItemStack held_hoe = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);
				
				if (held_hoe.getItem() == Items.DIAMOND_HOE || held_hoe.getItem() == Items.GOLDEN_HOE || held_hoe.getItem() == Items.IRON_HOE || held_hoe.getItem() == Items.STONE_HOE || held_hoe.getItem() == Items.WOODEN_HOE )
				{
		            if (event.getWorld().isAirBlock(event.getPos().up())) //great. now, is space air above you free?
		            {
		            event.getWorld().setBlockState(event.getPos(), ModBlocks.HEMP_PLOT.getDefaultState()); //change the block
		            event.getWorld().playSound(event.getEntityPlayer(), event.getPos(),
		            		SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F); //play the sound
		            held_hoe.damageItem(4, event.getEntityPlayer()); //quadruple the damage, hexadecimal the fun																	//damage the hoe
					event.setCanceled(true); //only case of true	
		            }
		            else
		            {
					event.setCanceled(false);
		            }
				}
				else event.setCanceled(false);
				}
			else
				{
				event.setCanceled(false);
				}
			}
		else
			event.setCanceled(false);
    }
}