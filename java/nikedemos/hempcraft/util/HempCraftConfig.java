package nikedemos.hempcraft.util;

import net.minecraftforge.common.config.Configuration;

public class HempCraftConfig {
	
	public static int test;

	public static void load(Configuration config) {
		config.load();
		test = config.getInt("GROWTH_DIVISOR", Configuration.CATEGORY_GENERAL, 4, 1, 16,"1 over X, the smaller the value, the faster the growth is");

		if (config.hasChanged()) config.save();
	}
}
