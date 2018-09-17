package nikedemos.hempcraft.genetics;

public class Shared {

	public static enum NUC_E {
		A,
		C,
		G,
		T
	};
	
	
	public static String[] NUC_S = new String[] {
		"A", //0
		"C", //1
		"G", //2
		"T", //3
	};
	
	//encode
	
	public static String getNucSequenceFromChar(char char_value)
	{
		String ret="XXXX";
		
		if (char_value<0 || char_value>255)
		{
			throw new IllegalArgumentException("Value out of range! Only 0-255 allowed.");
		}
		else
		{
		//convert that number charo base 4 orders of magnitude
		//and return it in the following form:
		//pow3 * 4^3 + pow2 * 4^2 + pow1 * 4^1 + pow0 *4^0
		
		int pow3=0,pow2=0,pow1=0,pow0=0;
			
		pow3 = char_value/64; //how many "wholes"?
		char_value = (char) (char_value%64); //modulo remainder
		
		pow2 = char_value/16;
		char_value = (char) (char_value%16);
		
		pow1 = char_value/4;
		//last remainder
		pow0 = (char) char_value%4;
		
		ret = NUC_S[pow3]+NUC_S[pow2]+NUC_S[pow1]+NUC_S[pow0];
		}
		
	return ret;
	}
	
	//decode
	
	public static char getCharFromNucSequence(String nuc_seq)
	{
		char ret=0;
		
		if (nuc_seq.length()!=4)
			{
				throw new IllegalArgumentException("Wrong quadlet length! Only 4 allowed.");
			}
		else
			{
			//reverse-convert from base 4 to base 10
			
			for (char n=0; n<4; n++)
				{
				char nuc_at = nuc_seq.charAt(n);
				
				char at=0;
				//skip A, it's 0
				if (nuc_at=="C".charAt(0))
					at = 1;
				else if (nuc_at=="G".charAt(0))
					at = 2;
				else if (nuc_at=="T".charAt(0))
					at = 3;
				
				ret+=at*(4^(4-n));
				}
			
			}
		
		return ret;
		
	}
	
	//this versions accepts String of length 4 as argument 0
	
	public static String getNucAt(String nuc_seq, char at)
	{
		String ret = "";
		
		if (at<0 || at>3)
			throw new IllegalArgumentException("Value out of range! Only 0-3 allowed.");
		else
		if (nuc_seq.length()!=4)
			throw new IllegalArgumentException("Wrong nuc seq length! Must be exactly 4.");
		else
		{
			ret = String.valueOf(nuc_seq.charAt(at));
		}
		
		return ret;
	}
	
	public static String setNucAt(String nuc_seq, char at, String new_nuc)
	{
		String ret="";
		
		if (at<0 || at>3)
			throw new IllegalArgumentException("Value out of range! Only 0-3 allowed.");
		else
		if (nuc_seq.length()!=4)
			throw new IllegalArgumentException("Wrong nuc seq length! Must be exactly 4.");
		else
		{
			StringBuilder nucs = new StringBuilder(nuc_seq);
			nucs.setCharAt(at, new_nuc.charAt(0));
			
			ret = nucs.toString();
		}
		
		return ret;
	}
	
	
	//and this ones - an char between 0 and 255 - it's just slav... the same with extra steps
	
	public static String getNucAt(char quadlet_value, char at)
	{
		String ret = "";
		
		if (at<0 || at>3)
			throw new IllegalArgumentException("Value out of range! Only 0-3 allowed.");
		else
		if (quadlet_value<0 || quadlet_value>255)
			throw new IllegalArgumentException("Value out of range! Only 0-255 allowed.");
		else
		{
			String nuc_seq = getNucSequenceFromChar(quadlet_value);
			ret = String.valueOf(nuc_seq.charAt(at));
		}
		
		return ret;
	}
	
	public static String setNucAt(char quadlet_value, char at, String new_nuc)
	{
		String ret="";
		
		if (at<0 || at>3)
			throw new IllegalArgumentException("Value out of range! Only 0-3 allowed.");
		else
		if (quadlet_value<0 || quadlet_value>255)
			throw new IllegalArgumentException("Value out of range! Only 0-255 allowed.");
		else
		{
			String nuc_seq = getNucSequenceFromChar(quadlet_value);
			StringBuilder nucs = new StringBuilder(nuc_seq);
			nucs.setCharAt(at, new_nuc.charAt(0));
			
			ret = nucs.toString();
		}
		
		return ret;
	}
	
	public Shared()
	{
		//initialisation
	}
	
}
