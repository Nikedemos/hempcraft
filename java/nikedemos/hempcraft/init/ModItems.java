package nikedemos.hempcraft.init;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import nikedemos.hempcraft.Main;
import nikedemos.hempcraft.items.ItemBase;
import nikedemos.hempcraft.items.ItemBaseSeeds;
import nikedemos.hempcraft.items.ItemBlockBase;
import nikedemos.hempcraft.items.ItemBud;
import nikedemos.hempcraft.items.ItemWateringCan;
import nikedemos.hempcraft.util.HempCraftConfig;


@EventBusSubscriber
public final class ModItems {

	public static final Item HEMP_SEED = addItem(new ItemBaseSeeds(ModBlocks.HEMP_STALK, ModBlocks.HEMP_PLOT),"hemp_seed");
	public static final Item HEMP_STEM = addItem(new ItemBase(),"hemp_stem");
	public static final Item HEMP_LEAF = addItem(new ItemBase(),"hemp_leaf");
	public static final Item HEMP_BUD_FRESH = addItem(new ItemBud(),"hemp_bud_fresh");
	public static final Item HEMP_BUD_DRY = addItem(new ItemBase(),"hemp_bud_dry");
	public static final Item WATERING_CAN_CLAY_FIRED = addItem(new ItemWateringCan(),"watering_can_clay_fired");	
	
	
	
	private static Item addItem(Item item, String name) { 
		return item.setRegistryName(Main.MODID, name).setUnlocalizedName(Main.MODID + "." + name);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		
		registry.register(HEMP_SEED);
		registry.register(HEMP_STEM);
		registry.register(HEMP_LEAF);
		registry.register(HEMP_BUD_FRESH);
		registry.register(HEMP_BUD_DRY);
		registry.register(WATERING_CAN_CLAY_FIRED);
		


		registry.register(new ItemBlock(ModBlocks.HEMP_STALK).setRegistryName( ModBlocks.HEMP_STALK.getRegistryName()));
		registry.register(new ItemBlock(ModBlocks.HEMP_PLOT).setRegistryName( ModBlocks.HEMP_PLOT.getRegistryName()));
		registry.register(new ItemBlock(ModBlocks.HEMP_FLOWER_FEMALE).setRegistryName( ModBlocks.HEMP_FLOWER_FEMALE.getRegistryName()));
		registry.register(new ItemBlock(ModBlocks.HEMP_FLOWER_MALE).setRegistryName( ModBlocks.HEMP_FLOWER_MALE.getRegistryName()));
		registry.register(new ItemBlock(ModBlocks.HEMP_BUD).setRegistryName( ModBlocks.HEMP_BUD.getRegistryName()));
		
		
	}
	
}
