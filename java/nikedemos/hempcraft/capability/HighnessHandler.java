package nikedemos.hempcraft.capability;

import nikedemos.hempcraft.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

import static nikedemos.hempcraft.capability.CapabilityHighness.HIGHNESS_ID;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(modid = Main.MODID)
public class HighnessHandler {

    @SubscribeEvent
    public static void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            IHighness handler = CapabilityHighness.get(player);
            handler.update();
        }
    }


    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof EntityPlayer) {
            event.addCapability(HIGHNESS_ID, new HighnessProvider(new CapabilityHighness((EntityPlayer) event.getObject())));
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        NBTTagCompound nbt = (NBTTagCompound) CapabilityHighness.CAPABILITY.getStorage().writeNBT(CapabilityHighness.CAPABILITY, event.getOriginal().getCapability(CapabilityHighness.CAPABILITY, null), null);
        CapabilityHighness.CAPABILITY.getStorage().readNBT(CapabilityHighness.CAPABILITY, event.getEntityPlayer().getCapability(CapabilityHighness.CAPABILITY, null), null, nbt);
    }

    @SubscribeEvent
    public static void playerTracking(PlayerEvent.StartTracking event) {
        if (event.getEntityPlayer().getCapability(CapabilityHighness.CAPABILITY, null) != null) {
            event.getEntityPlayer().getCapability(CapabilityHighness.CAPABILITY, null).sync();
        }
    }
    
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerRespawnEvent event) {
        //CapabilityHighness.get(event.player).soberUp(); //TODO
        CapabilityHighness.get(event.player).sync();
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
        CapabilityHighness.get(event.player).sync();
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        CapabilityHighness.get(event.player).sync();
    }
    /*
    @SubscribeEvent(priority = EventPriority.HIGH) //lol... HIGH. that's just a coinky-dinky tho
    public static void onHurt(LivingHurtEvent e) {
        if (!(e.getEntity() instanceof EntityPlayer))
            return;

        EntityPlayer player = (EntityPlayer) e.getEntity();
        if (player.getHealth() + player.getAbsorptionAmount() - e.getAmount() > 0 || !e.getEntity().hasCapability(CapabilityHighness.CAPABILITY, null) || !e.getEntity().getCapability(CapabilityHighness.CAPABILITY, null).isCapable())
            return;

        IHighness handler = CapabilityHighness.get(player);
        e.setCanceled(true);
        handler.setHigh(true);
    } */
}




