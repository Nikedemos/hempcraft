package nikedemos.hempcraft.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class HighnessStorage implements IStorage<ICapabilityInt>{

	@Override
	public NBTBase writeNBT(Capability<ICapabilityInt> capability, ICapabilityInt instance, EnumFacing side) {
		return new NBTTagInt(instance.get()); 
	}

	@Override
	public void readNBT(Capability<ICapabilityInt> capability, ICapabilityInt instance, EnumFacing side, NBTBase nbt) {
		instance.set(((NBTPrimitive) nbt).getInt()); 
	}

}
