package nikedemos.hempcraft.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nikedemos.hempcraft.Main;
import nikedemos.hempcraft.capability.CapabilityHighness;
import nikedemos.hempcraft.capability.IHighness;

public class ItemTokable extends ItemFood {
	public ItemTokable() {
		super(0,0,false); //this puts it into creative/foods aswell!
		this.setMaxDamage(8); //"puffs" available
		this.setAlwaysEdible();
		this.setMaxStackSize(1);
		setCreativeTab(Main.HEMP_TAB);
		
		//
	}
	
    @SideOnly(Side.CLIENT)
    public void initModel() {
    	ModelResourceLocation[] spliff_stages = 
    			{
    			new ModelResourceLocation(getRegistryName() + "_unlit", "inventory"),
    			new ModelResourceLocation(getRegistryName() + "_lit", "inventory"),
    			};

    	ModelBakery.registerItemVariants(this,
    			spliff_stages[0],
    			spliff_stages[1]
    		);
        ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
        	@Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
        		return spliff_stages[get_is_lit(stack)==false? 0 : 1];
            }
        });
    }
	
	@Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }
	
	public void puff(EntityPlayer player, ItemStack stack)
	{
		IHighness high =  CapabilityHighness.get((EntityPlayer) player);
		
		//let's find out 
	}
	
	
	@Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BREATH, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            //this.onFoodEaten(stack, worldIn, entityplayer);
            //entityplayer.addStat(StatList.getObjectUseStats(this));

            if (entityplayer instanceof EntityPlayerMP)
            {
                CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityplayer, stack);
            }
            puff(entityplayer, stack);
        }

        stack.damageItem(1, entityLiving); //not destroy!
        return stack;
    }
	
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
	tooltip.add(TextFormatting.DARK_GREEN+"Strength: "+TextFormatting.GREEN+this.get_strength(stack)+TextFormatting.RESET);
    }

    public void set_strength(ItemStack stack, int high_units_per_puff)
    {
    	getTagCompoundSafe(stack).setInteger("strength", high_units_per_puff);
    }
    
    public int get_strength(ItemStack stack)
    {
        int rettie=0;
        NBTTagCompound taggy=getTagCompoundSafe(stack);
        
        //check if it has a tag, if not, you're the default strength - 0
        if (!taggy.hasNoTags() && taggy.hasKey("strength"))
        {
        	return taggy.getInteger("strength");
        }
        else return rettie;
    }
    
    public boolean get_is_lit(ItemStack stack)
    {
    	return stack.isItemDamaged();
    }

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
        if (this.isInCreativeTab(tab))
        {
		ItemStack strength_1 = new ItemStack(this);
		
		this.set_strength(strength_1, 1);
		
		items.add(strength_1);
        }
	}
	
    private NBTTagCompound getTagCompoundSafe(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
            stack.setTagCompound(tagCompound);
        }
        return tagCompound;
        }
}
