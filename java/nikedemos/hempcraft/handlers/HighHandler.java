package nikedemos.hempcraft.handlers;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nikedemos.hempcraft.status.EffectHigh;

@EventBusSubscriber
public class HighHandler {
	public static final EffectHigh HIGH = new EffectHigh();
	
    @SubscribeEvent
    public static void addPotions(RegistryEvent.Register<Potion> e) {
        e.getRegistry().registerAll(HIGH);
    }
}
