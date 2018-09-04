package nikedemos.hempcraft.init;

import net.minecraft.block.Block;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nikedemos.hempcraft.Main;
import nikedemos.hempcraft.blocks.BlockHempBud;
import nikedemos.hempcraft.blocks.BlockHempFlowerFemale;
import nikedemos.hempcraft.blocks.BlockHempFlowerMale;
import nikedemos.hempcraft.blocks.BlockHempPlot;
import nikedemos.hempcraft.blocks.BlockHempStalk;

@EventBusSubscriber
public final class ModBlocks {

	public static final Block HEMP_STALK = addBlock(new BlockHempStalk(),"hemp_stalk");
	public static final Block HEMP_PLOT = addBlock(new BlockHempPlot(),"hemp_plot");
	public static final Block HEMP_FLOWER_FEMALE = addBlock(new BlockHempFlowerFemale(),"hemp_flower_female");
	public static final Block HEMP_FLOWER_MALE = addBlock(new BlockHempFlowerMale(),"hemp_flower_male");
	public static final Block HEMP_BUD = addBlock(new BlockHempBud(),"hemp_bud");
	
	
	
	public static Block addBlock(Block block, String name) {
		return block.setRegistryName(Main.MODID, name).setUnlocalizedName(Main.MODID + "." + name);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(HEMP_STALK);
		event.getRegistry().register(HEMP_PLOT);
		event.getRegistry().register(HEMP_FLOWER_FEMALE);
		event.getRegistry().register(HEMP_FLOWER_MALE);
		event.getRegistry().register(HEMP_BUD);	
		
	}

}
