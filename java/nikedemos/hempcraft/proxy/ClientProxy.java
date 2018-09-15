package nikedemos.hempcraft.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import nikedemos.hempcraft.init.ModBlocks;
import nikedemos.hempcraft.init.ModItems;
import nikedemos.hempcraft.items.ItemWateringCan;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
		//items
		registerItemModel(ModItems.HEMP_SEED);
		registerItemModel(ModItems.HEMP_STEM);
		registerItemModel(ModItems.HEMP_LEAF);
		registerItemModel(ModItems.HEMP_BUD_FRESH);
		registerItemModel(ModItems.HEMP_BUD_DRY);
		registerItemModel(ModItems.HEMP_NUGGET);
		
		//this is how we register items dependent on NBT (or any other factors, like capabilities / stack number)
		//remember, don't use the registerItemModel method for them!
        ((ItemWateringCan) ModItems.WATERING_CAN_CLAY_FIRED).initModel(); 
        ((ItemWateringCan) ModItems.WATERING_CAN_IRON).initModel(); 
        ((ItemWateringCan) ModItems.WATERING_CAN_GOLD).initModel(); 
        ((ItemWateringCan) ModItems.WATERING_CAN_DIAMOND).initModel(); 
        
        //blocks
		registerItemModel(Item.getItemFromBlock(ModBlocks.HEMP_STALK));
		registerItemModel(Item.getItemFromBlock(ModBlocks.HEMP_PLOT));
		registerItemModel(Item.getItemFromBlock(ModBlocks.HEMP_FLOWER_FEMALE));
		registerItemModel(Item.getItemFromBlock(ModBlocks.HEMP_FLOWER_MALE));
		registerItemModel(Item.getItemFromBlock(ModBlocks.HEMP_BUD));

	}

	public static void registerItemModel(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
