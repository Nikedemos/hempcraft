package nikedemos.hempcraft.status;

import net.minecraft.entity.EntityLivingBase;

public class EffectHigh extends PseudoPotionBase {
public static int phase;

	public EffectHigh(int phase, String name) {
		super(false, get_color_from_phase(phase), name);
		setIconIndex(get_x_from_phase(phase), get_y_from_phase(phase));
	}
	
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier){
    	/*
        entity.setVelocity(0D, 0D, 0D);
        entity.velocityChanged = true;
        entity.moveVertical =0;
        entity.moveStrafing =0;
        entity.motionX =0;
        entity.motionZ=0;
        */
    }
	
    public boolean isReady(int duration, int amplifier)
    {
    	return true;
    	
    }
	
	public static int get_color_from_phase(int ph)
	{
		//why not create a nice gradient, eh.
		//but the 11th phase is white tho.
		//start: 224,        224,   224
		//end:    96 (-128), 224,   0 (+224)
		int r=96,g=224,b=0;
		
		return new java.awt.Color(r,g,b).getRGB();
	}
	
	public static int get_x_from_phase(int ph)
	{
		return ph % 6;
	}
	
	public static int get_y_from_phase(int ph)
	{
		return ph>5 ? 1 : 0;
	}
}
