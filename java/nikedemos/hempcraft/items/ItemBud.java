package nikedemos.hempcraft.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nikedemos.hempcraft.Main;
import nikedemos.hempcraft.blocks.BlockHempBud;

public class ItemBud extends ItemBase {
	public ItemBud() {
		super();

	}
	
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        IBlockState state = worldIn.getBlockState(pos);
        
        //this is where you check NBT to see if the bud is fresh (placeable)
        System.out.print("USE");
        
        if (BlockHempBud.check_your_staying_privilege(worldIn, pos))
        {
        System.out.print("PLANT");
        }
        
        return EnumActionResult.FAIL;
    }
}
