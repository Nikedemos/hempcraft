package nikedemos.hempcraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.RegistryManager;
import nikedemos.hempcraft.handlers.BreedingHandler;
import nikedemos.hempcraft.handlers.FollowingHandler;
import nikedemos.hempcraft.handlers.HoeHandler;
import nikedemos.hempcraft.init.ModItems;
import nikedemos.hempcraft.proxy.CommonProxy;
import nikedemos.hempcraft.util.HempCraftConfig;
import nikedemos.hempcraft.util.HempCraftCreativeTab;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)

public class Main {	
	public static final CreativeTabs HEMP_TAB = new HempCraftCreativeTab("hemp");
	
	public static final String MODID = "hemp";
	public static final String NAME = "HempCraft";
	public static final String VERSION = "0.1";
	public static final int GUI_PLOT = 0;
	
	public Configuration config;
	
	@Instance(MODID)
	public static Main instance;
	
	@SidedProxy(clientSide = "nikedemos.hempcraft.proxy.ClientProxy", serverSide = "nikedemos.hempcraft.proxy.ServerProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){

		config = new Configuration(event.getSuggestedConfigurationFile());
		HempCraftConfig.load(config);
		
		//Let's register some event handlers over here
		
		//chickens eat seeds
		MinecraftForge.EVENT_BUS.register(new FollowingHandler(EntityChicken.class,ModItems.HEMP_SEED));
		MinecraftForge.EVENT_BUS.register(new BreedingHandler(EntityChicken.class,ModItems.HEMP_SEED));

		//sheep eat leaves, fresh buds and stalks
		MinecraftForge.EVENT_BUS.register(new FollowingHandler(EntitySheep.class,ModItems.HEMP_LEAF));
		MinecraftForge.EVENT_BUS.register(new BreedingHandler(EntitySheep.class,ModItems.HEMP_LEAF));

		MinecraftForge.EVENT_BUS.register(new FollowingHandler(EntitySheep.class,ModItems.HEMP_BUD_FRESH));
		MinecraftForge.EVENT_BUS.register(new BreedingHandler(EntitySheep.class,ModItems.HEMP_BUD_FRESH));
		
		MinecraftForge.EVENT_BUS.register(new FollowingHandler(EntitySheep.class,ModItems.HEMP_STEM));
		MinecraftForge.EVENT_BUS.register(new BreedingHandler(EntitySheep.class,ModItems.HEMP_STEM));		
		
		//cows eat leaves
		MinecraftForge.EVENT_BUS.register(new FollowingHandler(EntityCow.class,ModItems.HEMP_LEAF));
		MinecraftForge.EVENT_BUS.register(new BreedingHandler(EntityCow.class,ModItems.HEMP_LEAF));
		
		//pigs eat leaves, seeds, fresh buds and stalks
		MinecraftForge.EVENT_BUS.register(new FollowingHandler(EntityPig.class,ModItems.HEMP_LEAF));
		MinecraftForge.EVENT_BUS.register(new BreedingHandler(EntityPig.class,ModItems.HEMP_LEAF));

		MinecraftForge.EVENT_BUS.register(new FollowingHandler(EntityPig.class,ModItems.HEMP_BUD_FRESH));
		MinecraftForge.EVENT_BUS.register(new BreedingHandler(EntityPig.class,ModItems.HEMP_BUD_FRESH));
		
		MinecraftForge.EVENT_BUS.register(new FollowingHandler(EntityPig.class,ModItems.HEMP_STEM));
		MinecraftForge.EVENT_BUS.register(new BreedingHandler(EntityPig.class,ModItems.HEMP_STEM));
		
		MinecraftForge.EVENT_BUS.register(new FollowingHandler(EntityPig.class,ModItems.HEMP_SEED));
		MinecraftForge.EVENT_BUS.register(new BreedingHandler(EntityPig.class,ModItems.HEMP_SEED));
		
		//some handlers
		
		MinecraftForge.EVENT_BUS.register(nikedemos.hempcraft.handlers.HoeHandler.class);
		MinecraftForge.EVENT_BUS.register(nikedemos.hempcraft.handlers.HighHandler.class);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		
		proxy.registerOreDicts();
		
	}
	
	@EventHandler
	public void onConstructionEvent(FMLConstructionEvent event) {

	}
	
	
	
}
