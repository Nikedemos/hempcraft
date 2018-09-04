package nikedemos.hempcraft.blocks;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import nikedemos.hempcraft.init.ModBlocks;
import nikedemos.hempcraft.init.ModItems;
import nikedemos.hempcraft.util.HempCraftConfig;
import nikedemos.hempcraft.util.HempCraftVaria;

public class BlockHempStalk extends BlockCrops implements IGrowable{
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
	
	private static final AxisAlignedBB[] HEMP_AABB = new AxisAlignedBB[]
	{
	new AxisAlignedBB(0.4375D, 0.0D, 0.4375D,  0.5625D,  0.0625D,  0.5625D), //0
	new AxisAlignedBB(0.4375D, 0.0D, 0.4375D,  0.5625D,  0.0625D*2,  0.5625D), //1
	new AxisAlignedBB(0.4375D, 0.0D, 0.4375D,  0.5625D,  0.0625D*3,  0.5625D), //2
	new AxisAlignedBB(0.4375D, 0.0D, 0.4375D,  0.5625D,  0.0625D*4,  0.5625D), //3
	new AxisAlignedBB(0.4375D, 0.0D, 0.4375D,  0.5625D,  0.0625D*5,  0.5625D), //4
	new AxisAlignedBB(0.4375D, 0.0D, 0.4375D,  0.5625D,  0.0625D*6,  0.5625D), //5
	new AxisAlignedBB(0.4375D, 0.0D, 0.4375D,  0.5625D,  0.0625D*7,  0.5625D), //6
	new AxisAlignedBB(0.4375D, 0.0D, 0.4375D,  0.5625D,  0.0625D*8,  0.5625D), //7
	new AxisAlignedBB(0.4375D, 0.0D, 0.4375D,  0.5625D,  0.0625D*9,  0.5625D), //8
	new AxisAlignedBB(0.4375D, 0.0D, 0.4375D,  0.5625D,  0.0625D*10,  0.5625D), //9
	new AxisAlignedBB(0.4375D, 0.0D, 0.4375D,  0.5625D,  0.0625D*11,  0.5625D), //10
	new AxisAlignedBB(0.4375D, 0.0D, 0.4375D,  0.5625D,  0.0625D*12,  0.5625D), //11
	new AxisAlignedBB(0.4375D, 0.0D, 0.4375D,  0.5625D,  0.0625D*13,  0.5625D), //12
	new AxisAlignedBB(0.4375D, 0.0D, 0.4375D,  0.5625D,  0.0625D*14,  0.5625D), //13
	new AxisAlignedBB(0.4375D, 0.0D, 0.4375D,  0.5625D,  0.0625D*15,  0.5625D), //14
	new AxisAlignedBB(0.4375D, 0.0D, 0.4375D,  0.5625D,  0.0625D*16,  0.5625D)  //15
	
	
	};
	 
	public BlockHempStalk()
	{
		setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
		setTickRandomly(true);
		setHardness(0.5F);  
		this.setSoundType(SoundType.PLANT);
		this.disableStats();
	}
	
	/*
	@Override
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return HEMP_AABB[state.getValue(this.getAgeProperty())];
    } */
	
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return HEMP_AABB[state.getValue(this.getAgeProperty())].offset(state.getOffset(source, pos));
    }
    
	@Override
    public Block.EnumOffsetType getOffsetType()
    {
        return Block.EnumOffsetType.XZ;
    }
	
	
	@Override
	protected PropertyInteger getAgeProperty()
    {
        return AGE;
    }

    @Override
	public int getMaxAge()
    {
        return 15;
    }

    @Override
	protected int getAge(IBlockState state)
    {
        return state.getValue(this.getAgeProperty());
    }

    @Override
	public IBlockState withAge(int age)
    {
        return this.getDefaultState().withProperty(this.getAgeProperty(), age);
    }

    @Override
	public boolean isMaxAge(IBlockState state)
    {
        return state.getValue(this.getAgeProperty()).intValue() >= this.getMaxAge();
    }
   
    //returns how many blocks down to the hemp plot
    public int get_level(World worldIn, BlockPos pos) {
    	int level=1;
    	
    	for (int i=1; i<=5; i++)
    	{
        	if (worldIn.getBlockState(pos.down(i)).getBlock() instanceof BlockHempPlot) //is there a plot underneath you?
        		{
        		level = i;
        		break;
        		}
    	}
    	
    	return level;
    }
    //simple, yet useful. give it your (or any other) hemp stalk position and it will tell you the position of the plot.
    public BlockPos get_plot_pos(World worldIn, BlockPos pos) {
    	return pos.down(this.get_level(worldIn, pos));
    }
    
    //returns the overall height of the whole plant
    public int get_total_height(int level, World worldIn, BlockPos pos) {
    	int height=1;
    	
    	for (int h=1; h<=5; h++)
    	{
    		if  (
    			worldIn.getBlockState(pos.up(h-(level-1))).getBlock() != ModBlocks.HEMP_STALK
    			&& worldIn.getBlockState(pos.up(h-(level-1))).getBlock() != ModBlocks.HEMP_FLOWER_FEMALE
    			&& worldIn.getBlockState(pos.up(h-(level-1))).getBlock() != ModBlocks.HEMP_FLOWER_MALE
    			)
    			{
        		height = h;
        		break;
        		}
    	}
    	
    	return height;
    }
    
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    	int age = state.getValue(AGE);
    	
    	//default:
    	
	    int block_type=0; //0 = stalk, 1 = female flower, 2 = male flower
    	
    	if (worldIn.getBlockState(pos).getBlock() instanceof BlockHempFlowerFemale)
    	{
    		block_type=1;
    	
    	}
    	else if (worldIn.getBlockState(pos).getBlock() instanceof BlockHempFlowerMale)
    	{
    		block_type=2;
    	}
    	
    	boolean is_topmost = false;
    	int current_level = get_level(worldIn, pos);
    	int overall_height = get_total_height(current_level, worldIn, pos);

    	if (block_type!=0 || overall_height == current_level)
    		{
    		is_topmost = true;
    		}
    	

    	if((worldIn.getBlockState(pos.down()).getBlock() instanceof BlockHempStalk || canBlockStay(worldIn, pos,state))
    			&& worldIn.getLightFromNeighbors(pos.up()) >= 9) {

    		
    		boolean canGrow = true;//(rand.nextInt(HempSettings.growChance) == 0); //growChance is currently 4
    		
    		if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, canGrow)) {
    			//check if there's enough water underneath you. if there is, consume 1. if there isn't...

    			//only the topmost can check for growth and consumes water
    			if (is_topmost == true)
    			{
   				PropertyInteger moisty = BlockHempPlot.MOISTURE;
    			int under = worldIn.getBlockState(pos.down(current_level)).getValue(moisty);
    			
    			float stage_survival_chance=1.0F; //default
    			
    			if (block_type==0 && current_level==1) //only matters for small seedlings, for now, at least
    			{
    				float i_divisor;
    			
    				if (worldIn.isRainingAt(pos))
    				{
    					i_divisor=1.0F;
    				}
    				else
    				{
    					i_divisor=0.623F;//wait, what? why?	
    				}
    			
    				                //this is the magic number. when you multiply all the probabilities for age(0 to 15),
    								//you get a number very, very close to 0.5 (actually 0.49933655748832, according to PHP)
    			 					//believe it or not, I kinda bruteforced that number by playing "higher" or "lower"
									//with digits at consecutive decimal places until the result matched 0.5 closely.
									//
									// Here's a poem to explain it.
									// ..don't ask. Just... just don't.
									//
									//         
									//    	  ~~~~~~~~~~~~~~~~~~
									//A myriad seeds would ought be sown,
									//A thousand plots would need be digged,
									//A million crops will have been grown,
									//To see that game of life was rigged.
									//
									//And rigged are dice, and cards as well.
									//Beware the house! It always wins.
									//The scythe? It's fair! For it can't tell
									//The lowly peasants from the kings.
    								//
    								//One sows, it reaps - that's how it goes;
    								//A vicious cycle, there's no doubt.
    								//A harvest means the end of woes
    								//And it's the only one way out.
    								//
    								//The Mower's weary. Scythe is dull.
    								//The earth is barren. Help the man
    								//And separate the grain from hull.
    								//But leave the corn... and take the bran.
    								
    			stage_survival_chance=((100-(16-(age/i_divisor))))/100;
    			}
    			
    			
    			if (rand.nextFloat()<stage_survival_chance) //keep on livin
    			{
    			

    			//Basic dying chance, happening regardless of the level of moisture etc.
    			//dying chance is based on several factors.
    			
    			
    			//First, crowdedness factor is calculated. Just take your 8 immediate neighbours and divide that value by 8.
    			//So with eight neighbours, that float is 1. Maximum "capacity".
    			//The weight of this factor is 0.25
    			
    			//Second, your growth level + growth stage.
    			//It only matters for the first level and the flowering level. So height doesn't play a role here.
    			//Basically, as a stalk, there's roughly 0.5 chance of you dying in the first stage.
    			//So only half of planted seeds will ever see level 2.
    			//So here's a little formula:
    			//Here, rain doubles the chances of dying, maxing this factor out to 1, so for young plants, it's deadly.
    			
    			
    		
    			//
    			//The weight of this factor is 0.5.
    			
    			if (worldIn.getBlockState(pos.up()).getBlock() == Blocks.AIR )
    			{
    			if (under > 0 || worldIn.isRainingAt(pos)) //if it's raining, grow anyway, even if it
    				{
    				if(((block_type==0 || block_type==2) && age < 15) || (block_type==1 && age < 15 && age!=7)) //because if you're a girl, you stop on stage 7
    					//pretty same stuff for flowers and stalks going on, nothing special, move on
    					{
    					worldIn.setBlockState(pos, this.getStateFromMeta(age + 1));
    					}
    				else
    					{
    					
    						{
    						if (block_type==0) //stalks
    	    					{
    							int level_factor;
    							/*
    							Now, in order to get equal distribution of the 4 different height factors,
    							we need to take the level into account. Basically, if you have 4 possible
    							heights, there's a 1/4 chance that you will stay that height. If you don't,
    							now you have 3 possibilities - so there's a 1 in 3 chance that you will stay
    							on level 2. And so on. If there's only 2 states left to choose from, the chance
    							is 1/2. Pretty straight-forward, huh.
    							
    							*/
    							level_factor = 5-current_level;
    							
    							boolean trigger_premature_flower = (rand.nextInt(level_factor) == 0);
    	        				
    							if (current_level<4 && !trigger_premature_flower)
    	    						{
    								worldIn.setBlockState(pos.up(), this.getStateFromMeta(0));
    	    						}
    							else if ((current_level==4 || trigger_premature_flower))
    	   							{
    								//time to decide on gender now.
    								float female_chance=0.5F;
    								float randy = rand.nextFloat();
    								
    								if (randy<female_chance)
    									{
    									worldIn.setBlockState(pos.up(), ModBlocks.HEMP_FLOWER_FEMALE.getStateFromMeta(0));
    									}
    								else
    									{
    									worldIn.setBlockState(pos.up(), ModBlocks.HEMP_FLOWER_MALE.getStateFromMeta(0));
    									}
    	   							}
    	    					}
    						else //pollination
    	    					{
    							if (block_type==2 && age>0)
    								{
    								//your age is your effective radius
    								for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-age, -age, -age), pos.add(age, age, age)))
    									{
    									BlockPos cur_pos = blockpos$mutableblockpos;
    									
    									if (pos.equals(cur_pos) == false) //no point in self-pollinating males, lol, skip that
    										{
    										//now, check if the spherical radius checks out
    										if (HempCraftVaria.get_distance(pos, cur_pos)<=age)
    											{
    											//great. let's see if there's a female flower there...
    											if (worldIn.getBlockState(cur_pos).getBlock() == ModBlocks.HEMP_FLOWER_FEMALE)
    												{ //there is! let's take it's maturity and this will determine pollination chance
    												int female_age = worldIn.getBlockState(cur_pos).getValue(AGE).intValue();
    												//whoa, easy, tiger. is the lady Sin Semilla?
    												if (female_age<=6 && female_age>0)
    													{
    													float pollination_chance = (float)(female_age/6);
    													if (rand.nextFloat()<=pollination_chance)
    														{ //YES. POLLINATE. POLLINATE THE SHIT OUT OF HER
    														worldIn.setBlockState(cur_pos, ModBlocks.HEMP_FLOWER_FEMALE.getDefaultState().withProperty(AGE,female_age+8));
    														}
    													}
    												}
    											}
    										}
    									}
    								}
    	    					}
    						}    					}
    					
    				//consume 1 moisture, unless it's raining
    				if (!worldIn.isRainingAt(pos))
    					worldIn.setBlockState(pos.down(current_level), ModBlocks.HEMP_PLOT.getStateFromMeta(under-1));
    				
    				//System.out.print("MNIAM!");

    				}
    			else //no water? no growth. chance of dying, then. 1/4
    				{
    				if (rand.nextInt(3) == 0)
    					{
    					harvest(worldIn, pos, state);
        				//System.out.print("YES DIE");
        				}
    				}
    			}
    			else //stage_survival_chance has failed. die.
    			{
					harvest(worldIn, pos, state);	
    			}
    			} //oops, there's something directly above you. die.
    			else
    				{
					harvest(worldIn, pos, state);
    				}
    			}


	    		net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
    		}
    	}
    }
    
    public void harvest(World worldIn, BlockPos pos, IBlockState state){
    	//System.out.print("HARVEST MOON");
		ItemStack out_stem1;
		ItemStack out_stem2;
		ItemStack out_stem3;
		
		ItemStack out_leaves1;
		ItemStack out_leaves2;
		ItemStack out_leaves3;
		ItemStack out_leaves4;

		int s = state.getValue(AGE);

		{
		
		boolean chance_stem1 = worldIn.rand.nextInt(15)<s;
		boolean chance_stem2 = worldIn.rand.nextInt(15)<s;
		boolean chance_stem3 = worldIn.rand.nextInt(15)<s;
		
		boolean chance_leaves1 = worldIn.rand.nextInt(15)<s;
		boolean chance_leaves2 = worldIn.rand.nextInt(15)<s;
		boolean chance_leaves3 = worldIn.rand.nextInt(15)<s;
		boolean chance_leaves4 = worldIn.rand.nextInt(15)<s;
		
		if (chance_stem1)
		{
			out_stem1 = new ItemStack(ModItems.HEMP_STEM,1);
			Block.spawnAsEntity(worldIn, pos, out_stem1);
		}
		if (chance_stem2)
		{
			out_stem2 = new ItemStack(ModItems.HEMP_STEM,1);
			Block.spawnAsEntity(worldIn, pos, out_stem2);
		}
		if (chance_stem3)
		{
			out_stem3 = new ItemStack(ModItems.HEMP_STEM,1);
			Block.spawnAsEntity(worldIn, pos, out_stem3);
		}
		if (chance_leaves1)
		{
			out_leaves1 = new ItemStack(ModItems.HEMP_LEAF,1+worldIn.rand.nextInt(3));
			Block.spawnAsEntity(worldIn, pos, out_leaves1);
		}
		if (chance_leaves2)
		{
			out_leaves2 = new ItemStack(ModItems.HEMP_LEAF,1+worldIn.rand.nextInt(3));
			Block.spawnAsEntity(worldIn, pos, out_leaves2);
		}

		if (chance_leaves3)
		{
			out_leaves3 = new ItemStack(ModItems.HEMP_LEAF,1+worldIn.rand.nextInt(3));
			Block.spawnAsEntity(worldIn, pos, out_leaves3);
		}
		if (chance_leaves4)
		{
			out_leaves4 = new ItemStack(ModItems.HEMP_LEAF,1+worldIn.rand.nextInt(3));
			Block.spawnAsEntity(worldIn, pos, out_leaves4);
		}
		
		}
		worldIn.setBlockToAir(pos);
	}

    
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {

    	boolean chance_seed;
		int s = state.getValue(AGE);
		
		//if you are 2 or less, you ALWAYS drop the seed if you're only.
		
		if (s<=2)
			{
			chance_seed=true;
			}
		else
			{
			chance_seed = worldIn.rand.nextInt(15)>s;
			}
		
		//if you're the only one, the younger you are, the bigger chance you're gonna drop seed, yo.
		
		boolean is_only = (worldIn.getBlockState(pos.up()).getBlock() == Blocks.AIR && worldIn.getBlockState(pos.down()).getBlock() == ModBlocks.HEMP_PLOT);

		
		if (is_only && chance_seed)
			{
			ItemStack out_seed = new ItemStack(this.getSeed(),1);
			Block.spawnAsEntity(worldIn, pos, out_seed);
			}
		else
    	harvest(worldIn, pos, state);
    }
    
	
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		int s = state.getValue(AGE);
		//System.out.print("AUAAAAAAAAAAAAAAAAAAAAAAAAA");
		if(s > 2){
			return this.getCrop();
		}

		return ModItems.HEMP_SEED;
	} 
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) 
	{
		//first, determine current level and overall height
		//from that, you can calculate where the plot is located
		//and iterate up from it
    	int current_level = get_level(worldIn, pos);
    	int overall_height = get_total_height(current_level, worldIn, pos);
		
    	BlockPos plot_pos = get_plot_pos(worldIn, pos);
    	
    	//now we iterate >overall_height< blocks up
    	for (int u=1; u<=overall_height; u++)
    	{
    		//so bummed out we can't use a switch statement for that... just... looks messy
    		//make sure you're using the proper harvesting method for a particular block
    		//as the harvest method is the one that defines actual yields.
    		if (worldIn.getBlockState(plot_pos.up(u)).getBlock() == ModBlocks.HEMP_STALK)
    		{
    			((BlockHempStalk) ModBlocks.HEMP_STALK).harvest(worldIn,  (plot_pos.up(u)), state);
    		}
    		else if (worldIn.getBlockState(plot_pos.up(u)).getBlock() == ModBlocks.HEMP_FLOWER_FEMALE) //there should be a "get_hemp_type" method, really!
    		{
    			((BlockHempFlowerFemale) ModBlocks.HEMP_FLOWER_FEMALE).harvest(worldIn,  (plot_pos.up(u)), state);    			
    		}
    		else if (worldIn.getBlockState(plot_pos.up(u)).getBlock() == ModBlocks.HEMP_FLOWER_MALE)
    		{
    			((BlockHempFlowerMale) ModBlocks.HEMP_FLOWER_MALE).harvest(worldIn,  (plot_pos.up(u)), state);    			
    		}
    	}

	}

	@Override
	protected Item getSeed()
	{
		return ModItems.HEMP_STEM;
	}

	@Override
	protected Item getCrop()
	{
		return ModItems.HEMP_STEM;
	}
	
	@Override
	public boolean isFertile(World world, BlockPos pos)
    {
     return false;
    }

	@Override
	public boolean canBlockStay(World world, BlockPos pos,IBlockState state)
	{
		if (world.getBlockState(pos.down()).getBlock() == ModBlocks.HEMP_STALK || world.getBlockState(pos.down()).getBlock() == ModBlocks.HEMP_PLOT  || world.getBlockState(pos.down()).getBlock().isFertile(world, pos))
		{
			return true;
		}
		else
		{
			return this.canPlaceBlockAt(world, pos);
		}
	}

	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient)
	{
		return state.getValue(AGE).intValue() < 15;
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Crop;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(AGE, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(AGE);
	}

	@Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, AGE);
    }

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		Block block = world.getBlockState(pos.down()).getBlock();
		return block.canSustainPlant(world.getBlockState(pos.down()), world, pos, EnumFacing.UP, this) || block == this && world.getBlockState(pos).getMaterial().isReplaceable();
	}  

/*
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		if(!HempSettings.useeasyharvesting){
			return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
		}
		
		int s = state.getValue(AGE);
		 //if corn is ripe
		if(s > 8 ){
			breakBlock(worldIn,pos,state);
				worldIn.setBlockState(pos.down(s-9), this.getDefaultState());
			return true;
		}
		
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	} */

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos,IBlockState state) {
		return false;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		this.updateTick(worldIn, pos, state, rand);
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		int j = state.getValue(AGE);

		if(j > 8){
			entityIn.motionX *= 0.1D;
			entityIn.motionZ *= 0.1D;  
		}
	}

}
