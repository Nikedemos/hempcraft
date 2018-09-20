package nikedemos.hempcraft.capability;

import nikedemos.hempcraft.Main;
import nikedemos.hempcraft.items.ItemFobWatch;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Main.MODID)
public class RObjects {

    //Items
    public static List<Item> ITEMS = new ArrayList<>();

    @SubscribeEvent
    public static void addItems(RegistryEvent.Register<Item> e) {
        IForgeRegistry<Item> reg = e.getRegistry();
        reg.registerAll(setUpItem(new ItemFobWatch(), "fob_watch"));
    }

    private static Item setUpItem(Item item, String name) {
        item.setRegistryName(Main.MODID, name);
        item.setUnlocalizedName(name);
        ITEMS.add(item);
        return item;
    }

    //Sounds
    @SubscribeEvent
    public static void addSounds(RegistryEvent.Register<SoundEvent> e) {
        IForgeRegistry<SoundEvent> reg = e.getRegistry();
        reg.registerAll(setUpSound("regen_1"), setUpSound("fob_watch"));
    }

    private static SoundEvent setUpSound(String soundName) {
        return new SoundEvent(new ResourceLocation(Main.MODID, soundName)).setRegistryName(soundName);
    }

    @GameRegistry.ObjectHolder(Main.MODID)
    public static class Items {
        public static final Item FOB_WATCH = null;
    }

    @GameRegistry.ObjectHolder(Main.MODID)
    public static class Sounds {
        public static final SoundEvent FOB_WATCH = null;
        public static final SoundEvent REGEN_1 = null;
    }
}
