package nikedemos.hempcraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nikedemos.hempcraft.init.ModBlocks;
import nikedemos.hempcraft.init.ModItems;
import nikedemos.hempcraft.util.HempCraftVaria;

public class BlockHempFlowerFemale extends BlockHempStalk implements IGrowable{
	
	public BlockHempFlowerFemale()
	{
		super();
	}
	
	@Override
    public void harvest(World worldIn, BlockPos pos, IBlockState state){
    	//System.out.print("HARVESTING FLOWER!");
		//get a random value between 4 and 32, with a mean of 18, in a normal-distribution.
		//to achieve that, we're gonna "throw" 4 x D8 dices (sided 1-8), mathematically speaking.
		//this throw result will determine your harvesting luck.
		//but if you're fertilised, beware.
		//on average, half of the yield will be seeds.
		//wait, the yield? what's the exchange ratio between a seed and a bud?
		//why, great question, stoned me, clearly talking to himself! here it is:
		// 1 yield = 16 seeds = 1 bud = 9 nuggets = 9 spliffs
		// So at best, one plant can yield up to 288 grams. IF IT'S NOT FERTILISED.
		//If it is fertilised though... we need another throw result. This time,
		//we're gonna throw 4d20. The result (mean around 43, min 9, max 75) will give us the percentage of buds. The rest is seeds.
		int my_age = state.getValue(AGE);
		//System.out.println(my_age);
		int yield_result=HempCraftVaria.dice(worldIn.rand, 8, 4, 1);
		int yield = Math.round((((my_age % 8)+1)/8)*yield_result);
		int how_many_seeds;
		int how_many_buds;
		
		if (my_age<=7) //unfertilised, just buds
		{
		how_many_seeds = 0;
		how_many_buds = yield;
		}
		else //fertilised, mainly seeds
		{
		int bud_dice = HempCraftVaria.dice(worldIn.rand, 20, 4, 1);
		how_many_buds = Math.round(yield*bud_dice/100); //mean is 43% buds for fertilised stuff
		how_many_seeds = yield-how_many_buds;
		}
		
		if (how_many_buds!=0)
		{
			for (int b=0; b<how_many_buds; b++)
			{
			Block.spawnAsEntity(worldIn, pos, new ItemStack(ModItems.HEMP_BUD_FRESH));
			}
		}
		
		if (how_many_seeds!=0)
		{
			for (int s=0; s<how_many_seeds; s++)
			{
			Block.spawnAsEntity(worldIn, pos, new ItemStack(ModItems.HEMP_SEED));
			}
		}
		System.out.print("BUDS: "+how_many_buds+"; SEEDS: "+how_many_seeds+"; END; ");
		worldIn.setBlockToAir(pos);
	}
	/*@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient)
	{
		return state.getValue(AGE).intValue() != 6 && state.getValue(AGE).intValue()<15 && world.getBlockState(pos.up()).getBlock() == Blocks.AIR;
	} */

}
