package nikedemos.hempcraft.network.packets;

import io.netty.buffer.ByteBuf;
import nikedemos.hempcraft.capability.CapabilityHighness;
import nikedemos.hempcraft.capability.IHighness;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class MessageUpdateHigh implements IMessage {

    private EntityPlayer player;
    private NBTTagCompound data;

    public MessageUpdateHigh() {
    }

    public MessageUpdateHigh(EntityPlayer player, NBTTagCompound data) {
        this.player = player;
        this.data = data;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, player.getGameProfile().getId().toString());
        ByteBufUtils.writeTag(buf, data);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        if (Minecraft.getMinecraft().player != null)
            player = Minecraft.getMinecraft().player.world.getPlayerEntityByUUID(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
        if (player != null)
            data = ByteBufUtils.readTag(buf);
    }

    public static class Handler implements IMessageHandler<MessageUpdateHigh, IMessage> {

        @Override
        public IMessage onMessage(MessageUpdateHigh message, MessageContext ctx) {
            EntityPlayer player = message.player;
            if (player == null || !player.hasCapability(CapabilityHighness.CAPABILITY, null)) return null;
            IHighness handler = player.getCapability(CapabilityHighness.CAPABILITY, null);
            Minecraft.getMinecraft().addScheduledTask(() -> handler.deserializeNBT(message.data));
            return null;
        }
    }
}

