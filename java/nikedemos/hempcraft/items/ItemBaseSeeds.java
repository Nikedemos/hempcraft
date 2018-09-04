package nikedemos.hempcraft.items;

import java.util.Collection;

import com.google.common.collect.ImmutableMap;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.properties.IProperty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import nikedemos.hempcraft.Main;
import nikedemos.hempcraft.init.ModBlocks;

public class ItemBaseSeeds extends ItemSeeds {
    private final Block crops;
    /** BlockID of the block the seeds can be planted on. */
    private final Block soilBlockID;
	
	public ItemBaseSeeds(Block crops, Block soil) {
		super(crops, soil);
		this.crops = crops;
        this.soilBlockID = soil;

		setCreativeTab(Main.HEMP_TAB);		
	}

@Override
public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        net.minecraft.block.state.IBlockState state = worldIn.getBlockState(pos);
        if (facing == EnumFacing.UP && player.canPlayerEdit(pos.offset(facing), facing, itemstack) && worldIn.isAirBlock(pos.up()))
        {
            //if it's not hemp_plot, make it into one - but only if there's max moisture
        	if (state.getBlock() == ModBlocks.HEMP_PLOT) //ready plot, just plant
        	{
        	if (!worldIn.isRainingAt(pos.up()))
        		{
        		worldIn.setBlockState(pos.up(), this.crops.getDefaultState());
                
                
                if (player instanceof EntityPlayerMP)
                {
                    CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos.up(), itemstack);
                }

                itemstack.shrink(1);
                return EnumActionResult.SUCCESS;
        		}
        	else
        		{
                if (player instanceof EntityPlayerMP)
                {
        		player.sendMessage(new TextComponentString("It's raining, please shield the plot before planting."));
                }
        		
        		return EnumActionResult.FAIL;
        		}
        	}
        	else
        	{
                return EnumActionResult.FAIL;        	
        	}
        	
        	
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }
}
