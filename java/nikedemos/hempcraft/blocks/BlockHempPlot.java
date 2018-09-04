package nikedemos.hempcraft.blocks;

import java.util.Random;

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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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

public class BlockHempPlot extends BlockFarmland {
	
	//public static final PropertyInteger BONEMEAL = PropertyInteger.create("bonemeal", 0, 1);
    
    public BlockHempPlot() {
    	super();
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(MOISTURE, Integer.valueOf(0)));
		setCreativeTab(Main.HEMP_TAB);
		this.setSoundType(SoundType.PLANT);
		this.setHardness(0.6F);
    }
    
    public static void turnToCoarseDirt(World p_190970_0_, BlockPos worldIn)
    {
        System.out.print("COARSEY");
    	p_190970_0_.setBlockState(worldIn, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT));
        AxisAlignedBB axisalignedbb = field_194405_c.offset(worldIn);

        for (Entity entity : p_190970_0_.getEntitiesWithinAABBExcludingEntity((Entity)null, axisalignedbb))
        {
            double d0 = Math.min(axisalignedbb.maxY - axisalignedbb.minY, axisalignedbb.maxY - entity.getEntityBoundingBox().minY);
            entity.setPositionAndUpdate(entity.posX, entity.posY + d0 + 0.001D, entity.posZ);
        }
    }
    
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);

        if (worldIn.getBlockState(pos.up()).getMaterial().isSolid())
        {
            turnToCoarseDirt(worldIn, pos);
        }
    }
    
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);

        if (worldIn.getBlockState(pos.up()).getMaterial().isSolid())
        {
            turnToCoarseDirt(worldIn, pos);
        }
    }

	@Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        if (!worldIn.isRemote && entityIn.canTrample(worldIn, this, pos, fallDistance)) // Forge: Move logic to Entity#canTrample
        {
            turnToCoarseDirt(worldIn, pos);
        }
        //from Block.onFallenUpon class
        entityIn.fall(fallDistance, 1.0F);
    }
	
    private boolean hasPlant(World worldIn, BlockPos pos)
    {
    	return (worldIn.getBlockState(pos.up()).getBlock() == ModBlocks.HEMP_STALK);
    }

    public int getMoisture(World worldIn, BlockPos pos)
    {
    	return worldIn.getBlockState(pos).getValue(MOISTURE).intValue();
    }
    
    public void setMoisture(World worldIn, BlockPos pos, int moist)
    {
    	worldIn.setBlockState(pos, getStateFromMeta(moist));
    }
    
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        int water_level = ((Integer)state.getValue(MOISTURE)).intValue();
        boolean has_plant = hasPlant(worldIn, pos);
        
        //first: if there's no plant and the moisture is 0, there's a 1/4 chance you'll turn back into Coarse Dirt.
        //otherwise, proceed as normal
        if (!worldIn.isRainingAt(pos.up()) && water_level==0 && !has_plant && rand.nextInt(3)==0)
        	{
        	turnToCoarseDirt(worldIn, pos);
        	}
        else
        {      
        int neighbour_water_level;
        boolean neighbour_has_plant;
        
        //first, check for rain.
        if (worldIn.isRainingAt(pos.up()))
        	{
        	if (water_level<7)
        		{
            	worldIn.setBlockState(pos, this.getStateFromMeta(7)); //full irrigation instantly        		
        		}
        	}
        
        //pick a random neighbour in a 3 by 3 grid of 1 height
        //BlockPos random_pos = new BlockPos(pos.getX()-1+rand.nextInt(2), pos.getY(), pos.getZ()-1+rand.nextInt(2));
        
        //decide on random pos.add arguments order. basically, start iterating from random corners, so there's no direction bias
        int x1 = -1,
        	z1 = -1,
        	x2 = 1,
        	z2 = 1;
        

        if (rand.nextBoolean() == true)
        {
            if (rand.nextBoolean() == true)
            {
            x1 = 1;
            z1 = 1;
            
            x2 = -1;
            z2 = -1;
            }
            else
            {
            x1 = -1;
            z1 = -1;
                
            x2 = 1;
            z2 = 1;            	
            }
        }
        else
        {
            if (rand.nextBoolean() == true)
                if (rand.nextBoolean() == true)
                {
                x1 = 1;
                z1 = -1;
                
                x2 = -1;
                z2 = 1;
                }
                else
                {
                x1 = 1;
                z1 = -1;
                    
                x2 = -1;
                z2 = 1;            	
                }
        }
        
        for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(x1, 0, z1), pos.add(x2, 0, z2)))
        {
        //if it's you, don't bother
        if (blockpos$mutableblockpos.equals(pos) == false)
        {
		if (worldIn.getBlockState(blockpos$mutableblockpos).getBlock() == ModBlocks.HEMP_PLOT) //only take plots into consideration
            {
            	neighbour_water_level = worldIn.getBlockState(blockpos$mutableblockpos).getValue(MOISTURE).intValue();
                neighbour_has_plant = hasPlant(worldIn, blockpos$mutableblockpos);
                
                //if both of you are in the same plant situation
                if ((has_plant && neighbour_has_plant) || !has_plant && !neighbour_has_plant)
                {
                int water_level_difference = (water_level - neighbour_water_level);
                //try to diffuse the water equally between the two tiles - max difference 1
                //so if you have more, give it out to the neighbour. if you have less, take it.
                    if (Math.abs(water_level_difference)>1)
                    {
                    	if (water_level>neighbour_water_level) //give out, but only if neighbour is not full (7) and you have at least 2
                    	{
                    		if (neighbour_water_level<7 && water_level>1)
                    		{
                    		worldIn.setBlockState(pos, this.getStateFromMeta(water_level - 1)); //-1
                    		worldIn.setBlockState(blockpos$mutableblockpos, this.getStateFromMeta(neighbour_water_level + 1)); //+1                    		
                    		}
                    	}
                    	else //take from neighbour, but only when you're not full (7) and neighbour has at least 2
                    	{
                    		if (water_level<7 && neighbour_water_level>1)
                    		{
                        	worldIn.setBlockState(pos, this.getStateFromMeta(water_level + 1)); //+1
                        	worldIn.setBlockState(blockpos$mutableblockpos, this.getStateFromMeta(neighbour_water_level - 1)); //-1                      			
                    		}                    		
                    	}
                    }
                }
                else //oh no. let's see who has the plant and who hasn't. the plant haver just takes indiscriminately
                {
                    {
                    if (has_plant && !neighbour_has_plant && water_level<7 && neighbour_water_level>0) //take water if yours is less than 7
                		{
                    	worldIn.setBlockState(pos, this.getStateFromMeta(water_level + 1)); //+1
                    	worldIn.setBlockState(blockpos$mutableblockpos, this.getStateFromMeta(neighbour_water_level - 1)); //-1                  			
                    	}
                    else if (!has_plant && neighbour_has_plant && neighbour_water_level<7 && water_level>0) //give water if the neighbours is less than 7
                    	{
                		worldIn.setBlockState(pos, this.getStateFromMeta(water_level - 1)); //-1
                		worldIn.setBlockState(blockpos$mutableblockpos, this.getStateFromMeta(neighbour_water_level + 1)); //+1                       	
                    	}
                    }                	
                }
                

            }
		else if (worldIn.getBlockState(blockpos$mutableblockpos).getBlock() == Blocks.WATER)
			{
			//CAREFUL: if it's a water block, increase yours indiscriminately -
			// - to the point of destruction!
        	if (water_level<7)
        		worldIn.setBlockState(pos, this.getStateFromMeta(water_level + 1)); //+1
        	else
        		{
            	worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());        		
        		}
        	        	
			}
        }
        }
    }
    
    }
	
	@Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable)
    {
		return (plantable instanceof ItemBaseSeeds);
    }
	
	@Override
	public boolean isFertile(World world, BlockPos pos) {
		return true;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        Item item = itemstack.getItem();
        int water_level = ((Integer)state.getValue(MOISTURE)).intValue();
        if (itemstack.isEmpty())
        {
        	return true; //this will be the gui stuff etc
        }
        if (item == Items.WATER_BUCKET)
        {
            if (!worldIn.isRemote)
            {
                if (!playerIn.capabilities.isCreativeMode)
                {
                    playerIn.setHeldItem(hand, new ItemStack(Items.BUCKET));
                }

                if (water_level+6>7) //you overdone irrigation, nice going(!) turn the block into water block.
                	{
                	worldIn.setBlockState(pos, Blocks.WATER.getDefaultState());
                	}
                else
                	{
                	worldIn.setBlockState(pos, this.getStateFromMeta(water_level + 6)); //just irrigate that block
                	}
                
                worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                
            }

            return true;
        }
        else return false;
        
	}
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(MOISTURE, Integer.valueOf(meta & 7));
    }
	
	@Override
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(MOISTURE)).intValue();
    }
	
	@Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {MOISTURE});
    }

}
