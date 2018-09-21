package nikedemos.hempcraft.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class HighnessProvider implements ICapabilitySerializable<NBTTagCompound> {

    private IHighness capability;

    public HighnessProvider(IHighness capability) {
        this.capability = capability;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return CapabilityHighness.CAPABILITY != null && capability == CapabilityHighness.CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityHighness.CAPABILITY ? CapabilityHighness.CAPABILITY.cast(this.capability) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound) CapabilityHighness.CAPABILITY.getStorage().writeNBT(CapabilityHighness.CAPABILITY, this.capability, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        CapabilityHighness.CAPABILITY.getStorage().readNBT(CapabilityHighness.CAPABILITY, this.capability, null, nbt);
    }

}
