package nikedemos.hempcraft.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class HighnessStorage implements Capability.IStorage<IHighness> {

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IHighness> capability, IHighness instance, EnumFacing side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<IHighness> capability, IHighness instance, EnumFacing side, NBTBase nbt) {
        instance.deserializeNBT(nbt instanceof NBTTagCompound ? (NBTTagCompound) nbt : new NBTTagCompound());
    }
}
