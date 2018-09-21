package nikedemos.hempcraft.network;

import nikedemos.hempcraft.Main;
import nikedemos.hempcraft.network.packets.MessageUpdateHigh;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class NetworkHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Main.MODID);

    public static void init() {
        INSTANCE.registerMessage(MessageUpdateHigh.Handler.class, MessageUpdateHigh.class, 1, Side.CLIENT);
    }

}
