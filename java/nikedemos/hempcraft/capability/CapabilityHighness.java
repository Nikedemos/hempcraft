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
    public int timesHigh = 0; //only when the player is sober and tokes does this increase
    public int highTicks = 0;
    
    public int unitsConsumed; //convert into absorbed
    public int unitsAbsorbed; //convert into metabolised. this produces status effects related to getting high
    public int unitsMetabolised; //dissipate over time/exercise/sleep - the more units, the slower the process
    
    public int smokeAcute; //how many units of smoke in the system now. dissipate over time in a linear fashion
    public int smokeChronic; //dissipate over time - the more damage, the slower the process
    public int smokeTolerance;//timesHigh increases this by a certain amount - to a point.
    
    //HOW DOES INTOXICATION AND SMOKING WORK?
    /* - units absorbed The main variable that determines how "high" the Player is
     *   Each tick update, a certain number of highness units is absorbed (converted into metabolites) during update, depending on highness itself.
     * - units consumed - not-yet-absorbed units, a certain number is converted into absorbed, depending on highness and the buffer 
     * - units metabolised - converted units over time, they add up. Passive stuff, determines the tolerance and probability of
     *   developing dependence. Other factor in developing dependence is timesHigh.
     *   Metabolites diminish over time. Physical exercise helps, as does sleeping.
     * 
     * Let's take an example:
     * Standard Joint with the weakest possible "puff" - 16. That's the mean value. Check out BinomialRand for details.
     * About 1/4th of it is "instant high", added directly to highness. The rest goes to highness_buffer, to be "released" over time.
     * The more "toking" experience the player has, the more "instantaneous" the high is,
     * but in future releases, it will also depend on the strain's THC to CBD ratio.
     * CBD does not cause any effects, but it negates the negative ones
     * caused by THC (which produces both positive and negative effects).
     * 
     * There is also one factor to consider: smoke itself.
     * In short: the "weaker" the strain is, the more of it
     * the player has to smoke to achieve the "same high".
     * Smoke causes negative effects, both acute and chronic.
     * 
     * Smoke itself, over time, increases smoke tolerance. 
     * 
     * Some negative acute effects include:
     * - Headache
     * - Light aversion
     * - Nausea (vanilla)
     * 
     * Some negative chronic effects include:
     * - Morning cough
     * - Shortness of breath
     * - Fatigue
     * 
     * Joints have 16 puffs. And made from 1 Hemp Nugget.
     * Thus, 1 Hemp Nugget with Strength 1 has 256 units, on average. Strength 2 has 512 units. Strength 3 Has 768 units, etc.
     */
    
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
        nbt.setInteger("unitsConsumed", unitsConsumed);
        nbt.setInteger("unitsAbsorbed", unitsAbsorbed);
        nbt.setInteger("unitsMetabolised", unitsMetabolised);
        nbt.setInteger("smokeAcute", smokeAcute);
        nbt.setInteger("smokeChronic", smokeChronic);
        nbt.setInteger("smokeTolerance", smokeTolerance);
        
        
/*
 *     private int units_consumed; //convert into absorbed
    private int units_absorbed; //convert into metabolised. this produces status effects related to getting high
    private int units_metabolised; //dissipate over time/exercise/sleep - the more units, the slower the process
    
    private int smoke_acute; //how many units of smoke in the system now. dissipate over time in a linear fashion
    private int smoke_chronic; //dissipate over time - the more damage, the slower the process
    private int smoke_tolerance;
 */	
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        setHigh(nbt.getBoolean("isHigh"));
        setTimesHigh(nbt.getInteger("timesHigh"));
        setCapable(nbt.getBoolean("isCapable"));
        setTicksHigh(nbt.getInteger("highTicks"));
        
        //no setters yet
        unitsConsumed = nbt.getInteger("unitsConsumed");
        unitsAbsorbed = nbt.getInteger("unitsAbsorbed");
        unitsMetabolised = nbt.getInteger("unitsMetabolised");
        
        smokeAcute = nbt.getInteger("smokeAcute");
        smokeChronic = nbt.getInteger("smokeChronic");
        smokeTolerance = nbt.getInteger("smokeTolerance");
        
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
