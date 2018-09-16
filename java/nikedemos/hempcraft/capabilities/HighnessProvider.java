package nikedemos.hempcraft.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class HighnessProvider implements ICapabilitySerializable<NBTBase>  {
	@CapabilityInject(ICapabilityInt.class)
	public static final Capability<ICapabilityInt> HIGHNESS = null;

	private ICapabilityInt instance = HIGHNESS.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
	return capability == HIGHNESS;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
	return capability == HIGHNESS ? HIGHNESS.<T> cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT()
	{
	return HIGHNESS.getStorage().writeNBT(HIGHNESS, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt)
	{
		HIGHNESS.getStorage().readNBT(HIGHNESS, this.instance, null, nbt);
	} 
}
