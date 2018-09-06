package nikedemos.hempcraft.items;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nikedemos.hempcraft.Main;

public class ItemWateringCan extends ItemBase {
	public ItemWateringCan() {
		super();
		setCreativeTab(Main.HEMP_TAB);
		/*

Watering can: has moisture capacity, has damage (uses left), has range

clay watering can: moisture capacity 16 (2 water blocks), 64 uses, range 1x1

iron watering can: moisture capacity 48 (6 water blocks), 96 uses, range 3x3
gold watering can: moisture capacity 32 (4 water blocks), 128 uses, range 3x3

diamond watering can: moisture capacity 128 (16 water blocks), 512 uses, range 5x5
		 */
	
	}
	
	public int max_capacity=16; //2 water blocks
	public int max_uses=64; //this will be the item damage
	
	
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
        		return water_level_model[get_water_level(stack)];
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
        
    public int get_water_level(ItemStack stack)
    {
    int rettie=0;
    NBTTagCompound taggy=getTagCompoundSafe(stack);
    
    //check if it has a tag, if not, you're the default level - 0
    if (!taggy.hasNoTags() && taggy.hasKey("water_level"))
    {
    	return Math.round((float)(taggy.getInteger("water_level")/(float)max_capacity)*8.0F);
    }
    else return rettie;
    }
    
    public void set_water_level(ItemStack stack, int level)
    {
    getTagCompoundSafe(stack).setInteger("water_level", level);
    }

}
