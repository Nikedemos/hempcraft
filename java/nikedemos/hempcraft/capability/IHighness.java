package nikedemos.hempcraft.capability;

//import nikedemos.hempcraft.states.RegenType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public interface IHighness extends INBTSerializable<NBTTagCompound> {

    void update();

    int getTicksHigh();

    //Regen Ticks
    void setTicksHigh(int ticks);

    //Returns the player
    EntityPlayer getPlayer();

    //Lives
    //int getLivesLeft();

    //void setLivesLeft(int left);

    int getTimesHigh();

    void setTimesHigh(int times);

    NBTTagCompound getStyle();

    void setStyle(NBTTagCompound nbt);

    //Sync
    void sync();

    //RegenType getType();

    boolean isCapable();

    void setCapable(boolean capable);

    boolean isHigh();

    void setHigh(boolean regenerating);
}
