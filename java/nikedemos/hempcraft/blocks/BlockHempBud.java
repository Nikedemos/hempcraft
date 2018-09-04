package nikedemos.hempcraft.blocks;

import java.util.Random;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nikedemos.hempcraft.Main;
import nikedemos.hempcraft.init.ModBlocks;
import nikedemos.hempcraft.items.ItemBaseSeeds;

public class BlockHempBud extends Block {
    public static final PropertyInteger DRY = PropertyInteger.create("dry", 0, 1);
    protected static final AxisAlignedBB BUD_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D);
	
	//public static final PropertyInteger BONEMEAL = PropertyInteger.create("bonemeal", 0, 1);
    
    public BlockHempBud() {
    	super(Material.PLANTS);
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(DRY, Integer.valueOf(0)));
		setCreativeTab(Main.HEMP_TAB);
		this.setSoundType(SoundType.PLANT);
		this.setHardness(0.3F);
    }
    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }   

    
    public static boolean check_your_staying_privilege(World worldIn, BlockPos pos)
    {
    	boolean can_stay=true;
    	
    	IBlockState state = worldIn.getBlockState(pos);
    	
    	for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-1, -1, -1), pos.add(1, 1, 1)))
        {
        //if it's you, don't bother. also only check blocks neighboring sides (von neumann's neighbourhood)
    		if (blockpos$mutableblockpos.equals(pos) == false &&
    				(
    				blockpos$mutableblockpos.equals(pos.up()) ||
    				blockpos$mutableblockpos.equals(pos.down()) ||
    				blockpos$mutableblockpos.equals(pos.east()) ||
    				blockpos$mutableblockpos.equals(pos.west()) ||
    				blockpos$mutableblockpos.equals(pos.south()) ||
    				blockpos$mutableblockpos.equals(pos.north())
    				))
    		{
    		IBlockState statey = worldIn.getBlockState(blockpos$mutableblockpos);
    		Block blockey = statey.getBlock();
    		
    			if(blockpos$mutableblockpos.equals(pos.up())) //check the neighbor on top, has to be solid
    			{

    				if (!statey.getMaterial().isSolid())
    				{
    				can_stay=false; break;
    				}
    			
    			}
    			else //check any other neighbor blocks, have to be either non-solid or hemp bud
    			{
    			if (!(statey.getBlock()==ModBlocks.HEMP_BUD || !statey.getMaterial().isSolid()))
    				{
    				can_stay=false; break;				
    				}
    			}
    		}
        }
    	
    	return can_stay;
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BUD_AABB;
    }    

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(DRY);
	}
    
	@Override
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }


    public boolean is_dry(World worldIn, BlockPos pos)
    {
    	return worldIn.getBlockState(pos).getValue(DRY).intValue()==1;
    }
    
    public void set_dry(World worldIn, BlockPos pos, int moist)
    {
    	worldIn.setBlockState(pos, getStateFromMeta(moist));
    }
    
	
	@Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {DRY});
    }

}
