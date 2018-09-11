package nikedemos.hempcraft.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJModel.Material;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nikedemos.hempcraft.Main;
import nikedemos.hempcraft.blocks.BlockHempPlot;
import nikedemos.hempcraft.init.ModBlocks;
import nikedemos.hempcraft.init.ModItems;
import nikedemos.hempcraft.util.HempCraftVaria;


public class ItemWateringCan extends ItemBase {
	public Block containedBlock = Blocks.AIR;
	public ItemWateringCan(int _max_water_level, int _max_range, int _max_damage) {
		super();
		this.max_water_level=_max_water_level; //2 water blocks capacity
		this.max_range=_max_range;
		this.setMaxDamage(_max_damage);//later, take it from the constructor's TIER argument.
		setCreativeTab(Main.HEMP_TAB);
		/*

Watering can: has moisture capacity, has damage (uses left), has range

clay watering can: moisture capacity 16 (2 water blocks), 64 uses, range 1x1 (0)

iron watering can: moisture capacity 48 (6 water blocks), 96 uses, range 3x3 (1)
gold watering can: moisture capacity 32 (4 water blocks), 128 uses, range 3x3 (1)

diamond watering can: moisture capacity 256 (32 water blocks), 512 uses, range 5x5 (2)
		 */
	
	}
		
	public int max_water_level; //2 water blocks capacity
	public int max_range; //range 1: 3x3, range 2: 5x5
	public int cur_range=0; //0 means we're only directly watering the block, 1 - 3x3 grid, 2 - 5x5 grid
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
	tooltip.add(TextFormatting.BLUE+"Water: "+TextFormatting.AQUA+TextFormatting.BOLD+get_water_level(stack)+"/"+this.max_water_level+TextFormatting.RESET);
	String maybe_max = "";
	
	if (this.get_max_range(stack)>0)
		maybe_max = " (max "+((get_max_range(stack)*2)+1)+"×"+((get_max_range(stack)*2)+1)+")";
	
	tooltip.add(TextFormatting.GOLD+"Range: "+TextFormatting.YELLOW+TextFormatting.BOLD+((get_cur_range(stack)*2)+1)+"×"+((get_cur_range(stack)*2)+1)+maybe_max+TextFormatting.RESET);
	
	String full_blast_mode="SINGLE";
	
	if (get_full_blast(stack))
		full_blast_mode="FULL BLAST";
	
	tooltip.add(TextFormatting.DARK_GREEN+"Irrigation mode: "+TextFormatting.GREEN+TextFormatting.BOLD+full_blast_mode+TextFormatting.RESET);
	
    }
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
        if (this.isInCreativeTab(tab))
        {
		ItemStack empty_type = new ItemStack(this);
		ItemStack full_type = new ItemStack(this);
		
		this.set_water_level(empty_type, 0);
		this.set_water_level(full_type, this.max_water_level);
		
		this.set_full_blast(empty_type, false);
		this.set_full_blast(full_type, false);
		
		this.set_max_range(empty_type, this.max_range);
		this.set_max_range(full_type, this.max_range);
		
		this.set_cur_range(empty_type, 0);
		this.set_cur_range(full_type, 0);		
		
		items.add(empty_type);
		items.add(full_type);
        }
	}
	
    @SideOnly(Side.CLIENT)
    public void initModel() {
    	ModelResourceLocation[] water_level_model = 
    			{
    			new ModelResourceLocation(getRegistryName() + "_level_0", "inventory"),
    			new ModelResourceLocation(getRegistryName() + "_level_1", "inventory"),
    			new ModelResourceLocation(getRegistryName() + "_level_2", "inventory"),
    			new ModelResourceLocation(getRegistryName() + "_level_3", "inventory"),
    			new ModelResourceLocation(getRegistryName() + "_level_4", "inventory"),
    			new ModelResourceLocation(getRegistryName() + "_level_5", "inventory"),
    			new ModelResourceLocation(getRegistryName() + "_level_6", "inventory"),
    			new ModelResourceLocation(getRegistryName() + "_level_7", "inventory"),
    			new ModelResourceLocation(getRegistryName() + "_level_8", "inventory")
    			};

    	ModelBakery.registerItemVariants(this,
    		water_level_model[0],
    		water_level_model[1],
    		water_level_model[2],
    		water_level_model[3],
    		water_level_model[4],
    		water_level_model[5],
    		water_level_model[6],
    		water_level_model[7],
    		water_level_model[8]
    		);
        ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
        	@Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
        		return water_level_model[get_water_level_model(stack)];
            }
        });
    }
    
    private NBTTagCompound getTagCompoundSafe(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
            stack.setTagCompound(tagCompound);
        }
        return tagCompound;
        }
    
    
    public int get_water_level_model(ItemStack stack)
    {
    int rettie=0;
    NBTTagCompound taggy=getTagCompoundSafe(stack);
    
    //check if it has a tag, if not, you're the default level - 0
    if (!taggy.hasNoTags() && taggy.hasKey("water_level"))
    {
    	return Math.round((float)(taggy.getInteger("water_level")/(float)max_water_level)*8.0F);
    }
    else return rettie;
    }
    
    public void set_full_blast(ItemStack stack, boolean blast)
    {
    getTagCompoundSafe(stack).setBoolean("full_blast", blast);
    }
    
    public boolean get_full_blast(ItemStack stack)
    {
    boolean rettie=false;
    NBTTagCompound taggy=getTagCompoundSafe(stack);
    
    //check if it has a tag, if not, you're the default level - 0
    if (!taggy.hasNoTags() && taggy.hasKey("full_blast"))
    {
    	return taggy.getBoolean("full_blast");
    }
    else return rettie;
    }
    
    public void set_max_range(ItemStack stack, int range)
    {
    getTagCompoundSafe(stack).setInteger("max_range", range);
    }
    
    public int get_max_range(ItemStack stack)
    {
    int rettie=0;
    NBTTagCompound taggy=getTagCompoundSafe(stack);
    
    //check if it has a tag, if not, you're the default level - 0
    if (!taggy.hasNoTags() && taggy.hasKey("max_range"))
    {
    	return taggy.getInteger("max_range");
    }
    else return rettie;
    }
    
    public void set_cur_range(ItemStack stack, int range)
    {
    getTagCompoundSafe(stack).setInteger("cur_range", range);
    }
    
    public int get_cur_range(ItemStack stack)
    {
    int rettie=0;
    NBTTagCompound taggy=getTagCompoundSafe(stack);
    
    //check if it has a tag, if not, you're the default level - 0
    if (!taggy.hasNoTags() && taggy.hasKey("cur_range"))
    {
    	return taggy.getInteger("cur_range");
    }
    else return rettie;
    }
    
    public void set_water_level(ItemStack stack, int level)
    {
    getTagCompoundSafe(stack).setInteger("water_level", level);
    }
    
    public int get_water_level(ItemStack stack)
    {
    int rettie=0;
    NBTTagCompound taggy=getTagCompoundSafe(stack);
    
    //check if it has a tag, if not, you're the default level - 0
    if (!taggy.hasNoTags() && taggy.hasKey("water_level"))
    {
    	return taggy.getInteger("water_level");
    }
    else return rettie;
    }
    
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        /*
         * Check if you're sneaking. If so, just set the full blast to its negative.
        The general algorithm
        
        Once you right-click, we have two possibilities.
        We're gonna do a raytrace result...
                RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);
        if we encounter water, we check if the watering can is full.
        If it's not full, we remove the raytraced block, replacing it with air, and we add 8 (or however smaller number to max out)
        Then, water_level++ to NBT. If it's full, we do nothing.
        
        If we encounter a hemp plot, we transfer 1 water_level to hemp_plot's moisture.
        Then, we take 1 water_level and damage the held item by 1.
        */
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        
    	if (playerIn.isSneaking()) //changing mode
    	{
    		ItemStack altered = itemstack;
    		
    		//first, take the "metamode":
    		
    		int metamode=3*(this.get_full_blast(altered) ? 1 : 0)+this.get_cur_range(altered);
    		/*
    		0+0 = 0 single, range 0
    		0+1 = 1 single, range 1
    		0+2 = 2 single, range 2
    		
    		3+0 = 3 blast, range 0
    		3+1 = 4 blast, range 1
    		3+2 = 5 blast, range 2
    		*/
    		
    		//take different max ranges into account
    		switch (this.get_max_range(altered))
    		{
    		case 0: //just go back and forth between 0 and 3
    			{
    			if (metamode==0)
    				metamode=3;
    			else if (metamode==3)
    				metamode=0;
    			} break;
    		case 1:
    			{
    			metamode++;
    			if (metamode==2 || metamode==4)
    				{//add 1 more to skip
    				
    				}
    			
    			if (metamode>5) metamode=0; //back to start	
    			} break;
    		case 2: //change freely from 0 to 5
    			{
    	    		metamode++;
    	    		if (metamode>5) metamode=0; //back to start
    			} break;
    		}

    		
    		//now we have the metamode, we added 1 (to get to the next mode).
    		//decode the new metamode.
    		
    		boolean next_blast=false;
    		int next_cur_range;
    		
    		if (metamode>2) //that means full blast is true
    		{
    		next_blast=true;
    		metamode-=3;
    		}
    		
    		//now take the "remainder" to see the range
    		next_cur_range=metamode;
    		
    		//make the changes
    		this.set_full_blast(altered, next_blast);
    		this.set_cur_range(altered, next_cur_range);
    		
    		//notify the player
            if (playerIn instanceof EntityPlayerMP)
            	{
            	String new_mode = "SINGLE";
            	TextFormatting new_mode_color=TextFormatting.DARK_GREEN;
            	
                String new_range = ""+((next_cur_range*2)+1)+"×"+((next_cur_range*2)+1)+"";
                TextFormatting new_range_color=TextFormatting.DARK_BLUE;
            	
            	if (next_blast==true)
            	{
            		new_mode = "FULL BLAST";
            		new_mode_color=TextFormatting.GREEN;
            	}
            	
            	switch (next_cur_range)
            	{
            	case 1: new_range_color=TextFormatting.BLUE; break;
            	case 2: new_range_color=TextFormatting.AQUA; break;
            	}
            	
            	playerIn.sendMessage(new TextComponentString("Irrigation mode: "+TextFormatting.BOLD+new_mode_color+new_mode+TextFormatting.RESET+", range: "+TextFormatting.BOLD+new_range_color+new_range+""));
            	}
    		
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, altered);
    	}
    	else
    	{
        RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true); //arg3=true: take liquids into account
        if (raytraceresult == null) //nothing
        {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
        else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) //entities
        {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
        else
        {
            BlockPos blockpos = raytraceresult.getBlockPos();

            if (!worldIn.isBlockModifiable(playerIn, blockpos))
            {
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
            }
            else
            {
                if (!playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                }
                else
                {
                    IBlockState iblockstate = worldIn.getBlockState(blockpos);
                    Block blokky = iblockstate.getBlock();

                    if (blokky == Blocks.WATER && this.get_water_level(itemstack)<this.max_water_level && ((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0) //0 - only still water
                    {
                        worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
                        //playerIn.addStat(StatList.getObjectUseStats(this));

                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, this.fill_can(itemstack, playerIn, 8));
                    }
                    else if (blokky == ModBlocks.HEMP_PLOT && this.get_water_level(itemstack)>0) //you'll never overfill by accident using a watering can
                    {
                    //first, check if you're dealing with a grid (3x3 or 5x5) or just this particular block.
                    //if it's just this block, make sure you check this condition:
                    //((Integer)iblockstate.getValue(BlockHempPlot.MOISTURE)).intValue() < 7
                    //okay, let's make it into a nice range of blocks - based on range.
                    //ItemStack final_stack = itemstack;
                    
                    if ((this.get_cur_range(itemstack)==0 && ((Integer)iblockstate.getValue(BlockHempPlot.MOISTURE).intValue() < 7)) || (this.get_cur_range(itemstack)>0)) //for ranged, ignore plot's moisture level	
                    	{
                    	int x1=0;
                    	int x2=0;
                    	int z1=0;
                    	int z2=0;
                    	
                    	int rng=this.get_cur_range(itemstack);
                    	boolean blastey=this.get_full_blast(itemstack);
                    	
                    	if (worldIn.rand.nextBoolean() == true)
                        {
                            if (worldIn.rand.nextBoolean() == true)
                            {
                            x1 = rng;
                            z1 = rng;
                            
                            x2 = -rng;
                            z2 = -rng;
                            }
                            else
                            {
                            x1 = -rng;
                            z1 = -rng;
                                
                            x2 = rng;
                            z2 = rng;            	
                            }
                        }
                        else
                        {
                            if (worldIn.rand.nextBoolean() == true)
                                if (worldIn.rand.nextBoolean() == true)
                                {
                                x1 = rng;
                                z1 = -rng;
                                
                                x2 = -rng;
                                z2 = rng;
                                }
                                else
                                {
                                x1 = rng;
                                z1 = -rng;
                                    
                                x2 = -rng;
                                z2 = rng;            	
                                }
                        }
                        
                        //now, do a grid - 1 by 1 by 1 IS also a grid, yo.
                        //but remember - if cur_range>0, 
                        for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(blockpos.add(x1, 0, z1), blockpos.add(x2, 0, z2)))
                        {                   	
                        //first, check if we're still dealing with hemp plot at this location:
                        IBlockState statey = worldIn.getBlockState(blockpos$mutableblockpos);
                        Block block_type = statey.getBlock();
                        boolean is_hemp_plot = (block_type==ModBlocks.HEMP_PLOT);
                        
                        if (is_hemp_plot==true)
                        {
                        //and this is the actual moisture at that position
                        int water_level = (Integer)statey.getValue(BlockHempPlot.MOISTURE).intValue();
                            
                        	
                        //now check if that block will accept any more water - and for FULL BLAST mode - how MUCH more.
                        int this_many_waters_please=0;
                        
                        if (blastey==true)
                        {
                        this_many_waters_please=7-water_level; //max 7
                        }
                        else if (water_level<7)
                        {
                        this_many_waters_please=1; //just 1 if not full blast - but only if you're not full.
                        }
                        
                        //okay, how much water can I give you, dear hemp plot?
                        int stack_durability = itemstack.getMaxDamage()-itemstack.getItemDamage();
                        
                        int i_can_give_you_this_many_waters=Math.min(Math.min(this.get_water_level(itemstack), stack_durability), 7); //max 7
                        
                        int agreed_waters=Math.min(this_many_waters_please, i_can_give_you_this_many_waters);
                        
                        //now - if we're in the full blast mode, take as much water as you can.
                        //bottlenecks:
                        // this_many_waters_please, i_can_give_you_this_many, itemstack remaining durability
                        
                        if (agreed_waters>0)
                        {
                        worldIn.setBlockState(blockpos$mutableblockpos, block_type.getStateFromMeta(water_level + agreed_waters), 11);
                        //playerIn.addStat(StatList.getObjectUseStats(this));
                        this.drain_can(itemstack, playerIn, agreed_waters); //damage by agreed_waters
                        //a few particles to make it nice
                        HempCraftVaria.water_splash(worldIn, blockpos$mutableblockpos.up(), agreed_waters*4);
                        }
                        
                        }
                        
                        } //that's it for each block
                        

                        }
                        
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
                    }
                    else
                    {
                    	//but in the future, you might just use it to place water blocks
                        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                    }
                }
            }
        }
    }
    }
    
    private ItemStack fill_can(ItemStack canny, EntityPlayer player, int levels)
    {
        int cur_level = this.get_water_level(canny);
        
        if (player.capabilities.isCreativeMode)
        {
            if (cur_level<this.max_water_level)
            	{
            	this.set_water_level(canny, this.max_water_level);
                player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
            	}
        	return canny;
        }
        else
        {
            if (cur_level<this.max_water_level)
            {
                this.set_water_level(canny, Math.min(this.max_water_level, cur_level+levels));
                player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
            }
            return canny;
        }
    }
    
    private ItemStack drain_can(ItemStack canny, EntityPlayer player, int levels)
    {
        int cur_level = this.get_water_level(canny);
                
        if (player.capabilities.isCreativeMode) //don't use up any water on Creative
        {
            player.playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1.0F, 1.0F);
        	return canny;
        }
        else
        {
            if (cur_level>=levels && cur_level!=0)
            {
                this.set_water_level(canny, Math.max(0, cur_level-levels));
                canny.damageItem(1, player);
                player.playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1.0F, 1.0F);
            	return canny;
            }
            else
            {
                return canny;
            }
        }
    }

    public boolean tryPlaceContainedLiquid(@Nullable EntityPlayer player, World worldIn, BlockPos posIn)
    {
        if (this.containedBlock == Blocks.AIR)
        {
            return false;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(posIn);
            net.minecraft.block.material.Material material = iblockstate.getMaterial();
            boolean flag = !material.isSolid();
            boolean flag1 = iblockstate.getBlock().isReplaceable(worldIn, posIn);

            if (!worldIn.isAirBlock(posIn) && !flag && !flag1)
            {
                return false;
            }
            else
            {
                if (worldIn.provider.doesWaterVaporize() && this.containedBlock == Blocks.FLOWING_WATER)
                {
                    int l = posIn.getX();
                    int i = posIn.getY();
                    int j = posIn.getZ();
                    worldIn.playSound(player, posIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

                    for (int k = 0; k < 8; ++k)
                    {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)l + Math.random(), (double)i + Math.random(), (double)j + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
                }
                else
                {
                    if (!worldIn.isRemote && (flag || flag1) && !material.isLiquid())
                    {
                        worldIn.destroyBlock(posIn, true);
                    }

                    SoundEvent soundevent = this.containedBlock == Blocks.FLOWING_LAVA ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY;
                    worldIn.playSound(player, posIn, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    worldIn.setBlockState(posIn, this.containedBlock.getDefaultState(), 11);
                }

                return true;
            }
        }
    }
}
