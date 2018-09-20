package nikedemos.hempcraft.items;

import nikedemos.hempcraft.capability.CapabilityRegeneration;
import nikedemos.hempcraft.capability.IRegeneration;
import nikedemos.hempcraft.capability.RObjects;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class ItemFobWatch extends Item {

    public ItemFobWatch() {
        setCreativeTab(CreativeTabs.MISC);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {

        IRegeneration capability = CapabilityRegeneration.get(player);

        if (capability.isCapable()) {
            // TODO Store
        } else {
            worldIn.playSound(null, player.posX, player.posY, player.posZ, RObjects.Sounds.FOB_WATCH, SoundCategory.PLAYERS, 0.5F, 1.0F);
            capability.setCapable(true);
            capability.setLivesLeft(12);
        }

        return super.onItemRightClick(worldIn, player, handIn);
    }
}
