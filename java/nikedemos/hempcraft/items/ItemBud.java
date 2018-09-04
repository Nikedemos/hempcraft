package nikedemos.hempcraft.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nikedemos.hempcraft.blocks.BlockHempBud;
import nikedemos.hempcraft.init.ModBlocks;

public class ItemBud extends ItemBlockBase {
    protected final Block block = ModBlocks.HEMP_BUD;
    
	public ItemBud() {
		super(ModBlocks.HEMP_BUD);

	}

@Override
public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
    {
	if (!BlockHempBud.check_your_staying_privilege(world, pos)) return false;
        else if (!world.setBlockState(pos, newState, 11)) return false;
        else
        {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block)
        {
            setTileEntityNBT(world, player, pos, stack);
            this.block.onBlockPlacedBy(world, pos, state, player, stack);

            if (player instanceof EntityPlayerMP)
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
        }

        return true;
        }
    }
	
	
}
