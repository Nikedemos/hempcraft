package nikedemos.hempcraft.states;

import nikedemos.hempcraft.capability.CapabilityHighness;
import nikedemos.hempcraft.capability.IHighness;
import nikedemos.hempcraft.capability.RObjects;
import net.minecraft.block.BlockFire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class TypeFiery implements IRegenType {
    @Override
    public String getName() {
        return "FIERY";
    }

    @Override
    public void onInitial(EntityPlayer player) {
        player.extinguish();
        player.setArrowCountInEntity(0);
    }

    @Override
    public void onMidRegen(EntityPlayer player) {
        player.extinguish();

        Random rand = player.world.rand;
        player.rotationPitch += (rand.nextInt(10) - 5) * 0.1;
        player.rotationYaw += (rand.nextInt(10) - 5) * 0.1;

        if (player.world.isRemote)
            return;

        if (player.world.getBlockState(player.getPosition()).getBlock() instanceof BlockFire)
            player.world.setBlockToAir(player.getPosition());
        double x = player.posX + player.getRNG().nextGaussian() * 2;
        double y = player.posY + 0.5 + player.getRNG().nextGaussian() * 2;
        double z = player.posZ + player.getRNG().nextGaussian() * 2;
//RegenConfig.REGENERATION.fieryRegen
        player.world.newExplosion(player, x, y, z, 1, true, false);
        for (BlockPos bs : BlockPos.getAllInBox(player.getPosition().north().west(), player.getPosition().south().east()))
            if (player.world.getBlockState(bs).getBlock() instanceof BlockFire)
                player.world.setBlockToAir(bs);
    }

    @Override
    public void onFinish(EntityPlayer player) {
        if (player.world.isRemote)
            return;

        IHighness handler = CapabilityHighness.get(player);
        // player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, RegenConfig.REGENERATION.postRegenerationDuration * 2, RegenConfig.REGENERATION.postRegenerationLevel - 1, false, false)); // 180 seconds of 20 ticks of Regeneration 4
        //   handler.setTrait(TraitHandler.getRandomTrait());
        //    player.sendStatusMessage(new TextComponentTranslation(handler.getTrait().getMessage()), true);
        player.clearActivePotions();
        handler.sync();
    }

    @Override
    public SoundEvent getSound() {
        return RObjects.Sounds.REGEN_1;
    }

    @Override
    public boolean blockMovement() {
        return true;
    }
}
