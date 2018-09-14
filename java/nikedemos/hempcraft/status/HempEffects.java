package nikedemos.hempcraft.status;

import javax.annotation.Nullable;

import net.minecraft.init.Bootstrap;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class HempEffects
{
/*
*MAIN (primary, stackable effects:
*(these are just names)

(Sober) 0

Buzzed 1
Strunk 2
Lit up 3
Stoned 4
Ripped 5

Blitzed 6
Faded 7
Blazed 8
Krunked 9
Zoned 10

I and I (reached Zion)

PHASES (called by primary effects. Primary effects can also call vanilla effects.):

Munchies
etc, it will come, no worries


	 */
    public static final PseudoPotionBase HIGH;

    @Nullable
    private static Potion getRegisteredHempEffect(String id)
    {
        Potion potion = Potion.REGISTRY.getObject(new ResourceLocation(id));

        if (potion == null)
        {
            throw new IllegalStateException("Invalid HempEffect requested: " + id);
        }
        else
        {
            return potion;
        }
    }

    static
    {
        if (!Bootstrap.isRegistered())
        {
            throw new RuntimeException("Accessed HempEffects before Bootstrap!");
        }
        else
        {
        	HIGH = (PseudoPotionBase) getRegisteredHempEffect("high");

        }
    }
}