package nikedemos.hempcraft.capability;

import nikedemos.hempcraft.Main;
import nikedemos.hempcraft.states.RegenType;
import nikedemos.hempcraft.network.NetworkHandler;
import nikedemos.hempcraft.network.packets.MessageUpdateHigh;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(modid = Main.MODID)
public class CapabilityHighness implements IHighness {

    public static final ResourceLocation HIGHNESS_ID = new ResourceLocation(Main.MODID, "highness");
    @CapabilityInject(IHighness.class)
    public static final Capability<IHighness> CAPABILITY = null;
    private int timesHigh = 0, highTicks = 0;
    
    private EntityPlayer player;
    private boolean isHigh = false, isCapable = true;
    //private String typeName = RegenType.FIERY.getType().getName();

    public CapabilityHighness() {
    }

    public CapabilityHighness(EntityPlayer player) {
        this.player = player;
    }

    public static void init() {
        CapabilityManager.INSTANCE.register(IHighness.class, new HighnessStorage(), CapabilityHighness::new);
    }

    public static IHighness get(EntityPlayer player) {
        if (player.hasCapability(CAPABILITY, null)) {
            return player.getCapability(CAPABILITY, null);
        }
        throw new IllegalStateException("Missing Highness capability: " + player + ", this is a very bad thing.");
    }

    @Override
    public void update() {

        //if (isRegenerating()) {
            //startRegenerating();
            sync();
    
    }

    @Override
    public boolean isHigh() {
        return isHigh;
    }

    @Override
    public void setHigh(boolean high) {
        isHigh = high;
    }

    @Override
    public boolean isCapable() {
        return isCapable;
    }

    @Override
    public void setCapable(boolean capable) {
        isCapable = capable;
    }

    @Override
    public int getTicksHigh() {
        return highTicks;
    }

    @Override
    public void setTicksHigh(int ticks) {
        highTicks = ticks;
    }

    @Override
    public EntityPlayer getPlayer() {
        return player;
    }

    @Override
    public int getTimesHigh() {
        return timesHigh;
    }

    @Override
    public void setTimesHigh(int times) {
        timesHigh = times;
    }

    @Override
    public NBTTagCompound getStyle() {
        return getDefaultStyle();
    }

    @Override
    public void setStyle(NBTTagCompound nbt) {

    }

    @Override
    public void sync() {
        NetworkHandler.INSTANCE.sendToAll(new MessageUpdateHigh(player, serializeNBT()));
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("isHigh", isHigh);
        nbt.setInteger("timesHigh", timesHigh);
        nbt.setBoolean("isCapable", isCapable);
        nbt.setInteger("highTicks", highTicks);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        setHigh(nbt.getBoolean("isHigh"));
        setTimesHigh(nbt.getInteger("timesHigh"));
        setCapable(nbt.getBoolean("isCapable"));
        setTicksHigh(nbt.getInteger("highTicks"));
    }

    private void startHighness() {
    	
        setTicksHigh(getTicksHigh() + 1);
        /*
        if (getTicksHigh() == 1) {
            player.world.playSound(null, player.posX, player.posY, player.posZ, getType().getType().getSound(), SoundCategory.PLAYERS, 0.5F, 1.0F);
        }
        
        
        if (getTicksRegenerating() > 0 && getTicksRegenerating() < 100)
            getType().getType().onInitial(player);

        if (getTicksRegenerating() >= 100 && getTicksRegenerating() < 200)
            getType().getType().onMidRegen(player);

        if (player.getHealth() < player.getMaxHealth()) {
            player.setHealth(player.getHealth() + 1);
        }

        if (getTicksRegenerating() == 200) {
            getType().getType().onFinish(player);
            setTicksRegenerating(0);
            setRegenerating(false);
            setLivesLeft(getLivesLeft() - 1);
            setTimesRegenerated(getTimesRegenerated() + 1);
        } */
    }

    public NBTTagCompound getDefaultStyle() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setFloat("PrimaryRed", 1.0f);
        nbt.setFloat("PrimaryGreen", 0.78f);
        nbt.setFloat("PrimaryBlue", 0.0f);
        nbt.setFloat("SecondaryRed", 1.0f);
        nbt.setFloat("SecondaryGreen", 0.47f);
        nbt.setFloat("SecondaryBlue", 0.0f);
        nbt.setBoolean("textured", false);
        return nbt;
    }

}
