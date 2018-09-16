package nikedemos.hempcraft.handlers;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nikedemos.hempcraft.status.EffectHigh;

@EventBusSubscriber()
public class HighHandler {

	public static final EffectHigh BUZZED = new EffectHigh(1, "buzzed");
	public static final EffectHigh STRUNK = new EffectHigh(2, "strunk");
	public static final EffectHigh LIT_UP = new EffectHigh(3, "lit_up");
	public static final EffectHigh STONED = new EffectHigh(4, "stoned");
	public static final EffectHigh RIPPED = new EffectHigh(5, "ripped");
	
	public static final EffectHigh BLITZED = new EffectHigh(6, "blitzed");
	public static final EffectHigh FADED = new EffectHigh(7, "faded");
	public static final EffectHigh BLAZED = new EffectHigh(8, "blazed");
	public static final EffectHigh KRUNKED = new EffectHigh(9, "krunked");
	public static final EffectHigh ZONED = new EffectHigh(10, "zoned");
	
	public static final EffectHigh I_AND_I = new EffectHigh(11, "i_and_i");
	
	
    @SubscribeEvent
    public static void addPotions(RegistryEvent.Register<Potion> e) {
        e.getRegistry().registerAll(BUZZED);
        e.getRegistry().registerAll(STRUNK);
        e.getRegistry().registerAll(LIT_UP);
        e.getRegistry().registerAll(STONED);
        e.getRegistry().registerAll(RIPPED);
        
        e.getRegistry().registerAll(BLITZED);
        e.getRegistry().registerAll(FADED);
        e.getRegistry().registerAll(BLAZED);
        e.getRegistry().registerAll(KRUNKED);
        e.getRegistry().registerAll(ZONED);
        
        e.getRegistry().registerAll(I_AND_I);
        
    }
}
