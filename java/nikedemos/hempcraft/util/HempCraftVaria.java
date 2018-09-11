package nikedemos.hempcraft.util;
//various common functions used throughout the mod

import java.util.Random;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HempCraftVaria {
	public static double get_distance(BlockPos pos1, BlockPos pos2) {
		double deltaX = pos1.getX() - pos2.getX();
		double deltaY = pos1.getY() - pos2.getY();
		double deltaZ = pos1.getZ() - pos2.getZ();
			
		return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
	}
	
	public static void water_splash(World worldIn, BlockPos blockpos, int count)
	{
		System.out.print("SPLASH");
        for (int k = 0; k < count; k++)
        {
            worldIn.spawnParticle(EnumParticleTypes.WATER_SPLASH, (double)blockpos.getX() + Math.random(), (double)blockpos.up().getY() - Math.random(), (double)blockpos.getZ() + Math.random(), (double)Math.random(), (double)-8+Math.random(), (double)Math.random());
        }
	}

	public static int dice(Random rand, int sides, int times, int dice_starts_at)
	//if you wanna know the distribution of the different throws, simulate them at
	//https://anydice.com/, to see what graphs are produced by combinations of sides and times.
	{
		int result=0;
		
		for (int i=0; i<times; i++)
		{
			result+=rand.nextInt(sides)+dice_starts_at;
		}
		
		return result;
	}

}
