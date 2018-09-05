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
import net.minecraft.world.biome.Biome.TempCategory;
import net.minecraft.world.biome.BiomeProvider;
import nikedemos.hempcraft.Main;
import nikedemos.hempcraft.init.ModBlocks;
import nikedemos.hempcraft.init.ModItems;
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

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    	//0th: check the config-specific updateTick chance. default: 3
    	//but also check if you can still stay! if not, break yourself
    	if (!check_your_staying_privilege(worldIn, pos))
    		{
    		this.dropBlockAsItem(worldIn, pos, state, 0);
    		worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
    		}
    	else
    	if (!is_dry(worldIn, pos) && worldIn.rand.nextInt(3)==0)
    	{
    	
    	//first, perform a privilege check - and drop if you can't stay anymore
    	//second, check if you're dry. if not, drying chance will be calculated.
    	/*
    	 * BIGGEST FACTOR: weather/time of day
    	 If it's raining - globally - the buds won't dry. Too much moisture in the air.
    	 If it's not raining, check if it's day. The buds won't dry at night.
    	 
    	 In drying speed, an important factor is biome temperature and 
    	 
    	 it works in this way: first, take the +2 moore neighboorhood cuboid around you.
    	 for every block, check what kind of block that is. We have several options here 
    	 
    	 */
    	
    	//all the factors - temperature, humidity, time of day
    	
    	int temperature_factor=1;
    	
    	switch(worldIn.getBiome(pos).getTempCategory())
    	{
		case COLD: temperature_factor=1;
			break;
		case MEDIUM: temperature_factor=2;
			break;
		case OCEAN: temperature_factor=1;
			break;
		case WARM: temperature_factor=3;
			break;
		default:
			break;
    	}
    	    	
    	boolean can_rain = worldIn.getBiome(pos).canRain(); //bonus if it can't
    	boolean high_humidity = worldIn.getBiome(pos).isHighHumidity(); //handicap if it is
    	
    	int humidity_factor=1;
    	
    	if (!can_rain)
    	{
    		humidity_factor=3;
    	}
    	else
    	{
    	if (!high_humidity)
    	{
    		humidity_factor=2;
    	}
    	else humidity_factor=1;
    	
    	boolean raining_now = worldIn.isRaining();
    	boolean raining_now_at = worldIn.isRainingAt(pos.up(2));
    	
    	if (raining_now)
    		{
    		if (raining_now_at)
    			{
    			humidity_factor=0;
    			}
    		else
    			{
    			humidity_factor=1;
    			}
    		}
    	
    	}
    	
    	//that's it for the factors that the players can't affect. now, some blocks placed nearby
    	//that might affect the drying speed.
    	int heat_sources=0;
    	
    	for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-2, -2, -2), pos.add(2, 2, 2)))
        {
    		//if it's you, don't bother. also only check blocks neighboring sides (von neumann's neighbourhood).
    		//Also, ignore blockhempbud, it doesn't affect anything
    		if (blockpos$mutableblockpos.equals(pos) == false)
    		{
    		Block blockey = worldIn.getBlockState(blockpos$mutableblockpos).getBlock();
    		
    		if (blockey.isFireSource(worldIn, pos, EnumFacing.UP) || blockey == Blocks.LIT_FURNACE || blockey == Blocks.LAVA || blockey == Blocks.FLOWING_LAVA) //lit netherrack, magma, lit furnace, lava
    			{
    			heat_sources+=3;
    			}
    		//other stuff like furnaces / torches
    		//later, remember to provide the ability to add custom heat sources in the config
    		else if (blockey == Blocks.FURNACE)
			{
			heat_sources+=1;
			}
    		else if (blockey == Blocks.TORCH)
			{
			heat_sources+=2;
			}
    		}
        if (heat_sources>15)
        	{
        	heat_sources=15; break; //capping at 15
        	}
        }
    //we have everything now. these are the weights against max values. We all multiply them together
    /* HUMIDITY FACTOR - goes from 0 to 3. The weight is 0.3
     * TEMPERATURE_FACTOR - goes from 0 to 3. The weight is 0.3
     * HEAT SOURCES - go from 0 to 15. The weight is 0.4
     */
    
    float final_luck=0F;
    	
    if (humidity_factor!=0) //0 means it will never dry, not even in theory
    {
    final_luck = (((float)humidity_factor/3.0F)*0.3F)+(((float)temperature_factor/3.0F)*0.3F+(((float)heat_sources/15.0F)*0.4F));
    }
    if (worldIn.rand.nextFloat()<=final_luck)
    {
    set_dry(worldIn, pos, 1);
    }
    
    }
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
    
    public void set_dry(World worldIn, BlockPos pos, int dry)
    {
    	worldIn.setBlockState(pos,this.blockState.getBaseState().withProperty(DRY, Integer.valueOf(dry))); 
    }
    
	
	@Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {DRY});
    }
	
	@Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        if (state.getValue(DRY) == 0)
        	return ModItems.HEMP_BUD_FRESH;
        else
        	return ModItems.HEMP_BUD_DRY;
    }


}
