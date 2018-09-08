package nikedemos.hempcraft.items;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
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


public class ItemWateringCan extends ItemBase {
	public Block containedBlock = Blocks.AIR;
	public ItemWateringCan() {
		super();
		this.setMaxDamage(64);//later, take it from the constructor's TIER argument.
		setCreativeTab(Main.HEMP_TAB);
		/*

Watering can: has moisture capacity, has damage (uses left), has range

clay watering can: moisture capacity 16 (2 water blocks), 64 uses, range 1x1

iron watering can: moisture capacity 48 (6 water blocks), 96 uses, range 3x3
gold watering can: moisture capacity 32 (4 water blocks), 128 uses, range 3x3

diamond watering can: moisture capacity 128 (16 water blocks), 512 uses, range 5x5
		 */
	
	}
		
	public int max_water_level=16; //2 water blocks
	
    @SuppressWarnings("null")
	@SideOnly(Side.CLIENT)
    public void initModel() {
    	ModelResourceLocation[] water_level_model = null; //it's okay, that's what the for loop below is for
    	
    	for (int w=0; w<=8; w++)
    	{
    		water_level_model[w] = new ModelResourceLocation(getRegistryName() + "_level_"+Integer.toString(w), "inventory");
    	}

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


    public void set_water_level(ItemStack stack, int level)
    {
    getTagCompoundSafe(stack).setInteger("water_level", level);
    }
    
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        /*
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
                    else if (blokky == ModBlocks.HEMP_PLOT && this.get_water_level(itemstack)>0 && ((Integer)iblockstate.getValue(BlockHempPlot.MOISTURE)).intValue() < 7) //you'll never overfill by accident using a watering can
                    {
                        int water_level = (Integer)iblockstate.getValue(BlockHempPlot.MOISTURE).intValue();
                        worldIn.setBlockState(blockpos, blokky.getStateFromMeta(water_level + 1), 11);
                        //playerIn.addStat(StatList.getObjectUseStats(this));
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, this.drain_can(itemstack, playerIn, 1));
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
