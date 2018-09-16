package nikedemos.hempcraft.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nikedemos.hempcraft.Main;
import nikedemos.hempcraft.capabilities.HighnessProvider;

@EventBusSubscriber()
public class CapabilityHandler {
	public static final ResourceLocation HIGHNESS = new ResourceLocation(Main.MODID, "highness");

	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<EntityPlayer> event)
	{
	event.addCapability(HIGHNESS, new HighnessProvider()); 
	}
}
