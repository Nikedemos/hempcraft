package nikedemos.hempcraft.proxy;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.oredict.OreDictionary;
import nikedemos.hempcraft.init.ModBlocks;
import nikedemos.hempcraft.init.ModItems;

public class CommonProxy {

    public static void registerModels(ModelRegistryEvent event) {}

    
    public void registerOreDicts(){
    	OreDictionary.registerOre("cropHemp", ModItems.HEMP_STEM);
    	OreDictionary.registerOre("listAllseed", ModItems.HEMP_SEED);
    }
}

