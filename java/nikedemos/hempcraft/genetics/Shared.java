package nikedemos.hempcraft.genetics;

import java.util.Random;

public class Shared {

	public static enum NUC_E {
		A,
		C,
		G,
		T
	};
	
	public static enum DOMINANCE {
		DOMINANT,
		RECESSIVE,
		CODOMINANT,
		CLASHING
	};
	
	
	public static String[] NUC_S = new String[] {
		"A", //0
		"C", //1
		"G", //2
		"T", //3
	};
	
	//this is used to identify a particular gene in a Quadlet.
	public static enum GENE_ID {
		
		//For every Trait (the ultimate value that the HempPlot tile entity deals with,
		//to decide the life cycle of the stalks and flowers above it - which, themselves, don't contain genetic data)
		
		//IN A NUTSHELL:
		
		/*
		There are three factors...
		
			Light (0-15), Moisture (0-7), Fertilizer (0-7).
		
		...that affect
			
			Speed, Survival Rate.
		
		...separately, for two phases
		
			Vegetative, Flowering
			
		HOW SURVIVAL RATE IS CALCULATED:
		
		"We add weighted factors.
		And normalize the sum
		To get a float
		Between 0 and 1."
		
		- B(F/V)SR*: Weight of 0.4.
		- Let's take the MB(F/V)GR*. That's the base. It will be a number between 1 and 7 - or if the gene is corrupted, 0 - in that case, no matter the water
		  And take the current moisture level.
		
			
		
		
		
		
		*/
		//When you put 4 genes that code a trait through NUTID,
		//you will get a value 0 to 7.
		//0 means this gene is corrupt (1/4 chance when two alleles
		//of the same gene interact at random)
		//When a gene is corrupt, how it affects things
		//is decided on a case-by-case basis, depending on the gene.
		//Corrupt gene doesn't ALWAYS mean a bad thing.
		
		//PHBA* - Potential Height Bound A - since the va
		PHBA1, PHBA2, PHBA3, PHBA4,
		
		//PHBB* - Potential Height Bound B
		PHBB1, PHBB2, PHBB3, PHBB4,
				
		
		//BVSR* - Basic Vegetative Survival Rate
		BVSR1, BVSR2, BVSR3, BVSR4,
		
		//BFSR* - Basic Flowering Survival Rate
		BFSR1, BFSR2, BFSR3, BFSR4,
				
		//BVGS* - Basic Vegetative Growth Speed
		BVGS1, BVGS2, BVGS3, BVGS4,
		
		//BFGS* - Basic Flowering Growth Speed
		BFGS1, BFGS2, BFGS3, BFGS4,
		
		//SBSR* Sunlight Basic Survival Requirement
		SBSR1, SBSR2, SBSR3, SBSR4,
		
		//MBSR* Moisture Basic Survival Requirement
		MBSR1, MBSR2, MBSR3, MBSR4,
		
		//FBSR* Fertilizer Basic Survival Requirement
		FBSR1, FBSR2, FBSR3, FBSR4,
		
		
		//SBVGR* Sunlight Basic Vegetative Growth Requirement
		SBVGR1, SBVGR2, SBVGR3, SBVGR4,
		
		//MBVGR* Moisture Basic Vegetative Growth Requirement
		MBVGR1, MBVGR2, MBVGR3, MBVGR4,
		
		//FBVGR* Fertilizer Basic Vegetative Growth Requirement
		FBVGR1, FBVGR2, FBVGR3, FBVGR4,
		
		
		//SSIGS* - Sunlight Surplus Affects Growth Speed 
		SSIGS1, SSIGS2, SSIGS3, SSIGS4,
		
		//SDDGS* - Sunlight Deficit Affects Growth Speed 
		SDDGS1, SDDGS2, SDDGS3, SDDGS4,
		
		
		//MSIGS* - Moisture Surplus Affects Growth Speed 
		MSIGS1, MSIGS2, MSIGS3, MSIGS4,
		
		//MDDGS* - Moisture Deficit Affects Growth Speed 
		MDDGS1, MDDGS2, MDDGS3, MDDGS4,
		
		
		//FSIGS* - Fertilizer Surplus Affects Growth Speed 
		FSIGS1, FSIGS2, FSIGS3, FSIGS4,
		
		//FDDGS* - Fertilizer Deficit Affects Growth Speed 
		FDDGS1, FDDGS2, FDDGS3, FDDGS4,		
		
		//this is 64 genes so far
		
		
		//SSIGS* - Sunlight Surplus Affects Survival Rate 
		SSISR1, SSISR2, SSISR3, SSISR4,
		
		//SDDGS* - Sunlight Deficit Affects Survival Rate 
		SDDSR1, SDDSR2, SDDSR3, SDDSR4,
		
		
		//MSIGS* - Moisture Surplus Affects Survival Rate 
		MSISR1, MSISR2, MSISR3, MSISR4,
		
		//MDDGS* - Moisture Deficit Affects Survival Rate 
		MDDSR1, MDDSR2, MDDSR3, MDDSR4,
		
		
		//FSISR* - Fertilizer Surplus Affects Survival Rate 
		FSISR1, FSISR2, FSISR3, FSISR4,
		
		//FDASR* - Fertilizer Deficit Affects Survival Rate 
		FDASR1, FDASR2, FDASR3, FDASR4,		
	}
	
	//encode
	public static final int CHROMOSOME_PAIRS=16;
	public static final int QUADLETS_PER_CHROMOSOME=16;
	
	//you can provide your own rng
	public static String getRandomNuc(Random rng)
	{
	Random rand = rng;
	return NUC_S[rng.nextInt(4)];
	}
		
	//or, if you don't provide any arguments, we'll use a newly created one.
	//this is not a very good way of dealing with things, tho
	
	public static String getRandomNuc()
	{
		return NUC_S[new Random().nextInt(4)];
	}
	
	public static String getNucSequenceFromChar(int val)
	{
		String ret="XXXX";
		
		if (val<0 || val>255)
		{
			throw new IllegalArgumentException("Value out of range! Only 0-255 allowed.");
		}
		else
		{
		//convert that number into base 4 orders of magnitude
		//and return it in the following form:
		//pow3 * 4^3 + pow2 * 4^2 + pow1 * 4^1 + pow0 *4^0
		
		int pow3=0,pow2=0,pow1=0,pow0=0;
			
		pow3 = val/64; //how many "wholes"?
		val = (val%64); //modulo remainder
		
		pow2 = val/16;
		val = (val%16);
		
		pow1 = val/4;
		//last remainder
		pow0 = val%4;
		
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
	
	public static String getNucAt(String nuc_seq, int at)
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
	
	//and this ones - an char between 0 and 255 - it's just slav... the same with extra steps
	
	public static String getNucAt(int quadlet_value, int at)
	{
		String ret = "";
		
		if (at<0 || at>3)
			throw new IllegalArgumentException("Value out of range! Only 0-3 allowed.");
		else
		if (quadlet_value<0 || quadlet_value>255)
			throw new IllegalArgumentException("Value out of range! Only 0-255 allowed.");
		else
		{
			String nuc_seq = getNucSequenceFromChar((char)quadlet_value);
			ret = String.valueOf(nuc_seq.charAt(at));
		}
		
		return ret;
	}
	
	//these variants take the String sequence
	
	public static String setNucAt(String nuc_seq, int at, String new_nuc, int chance_1_over_x, Random rng)
	{
		String ret="";
		
		if ((rng!=null && rng.nextInt(chance_1_over_x)==0) || (rng==null))
		{		
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
		}
		else
		{
			ret = nuc_seq; //no changes
		}
		
		
		return ret;
	}
	//if the last argument is not provided, the chance is 1 to 1 (always)
	public static String setNucAt(String nuc_seq, int at, String new_nuc)
	{
		return setNucAt(nuc_seq, at, new_nuc, 1, null);
	}
	
	//and these - numbers
	public static String setNucAt(int quadlet_value, int at, String new_nuc, int chance_1_over_x, Random rng)
	{
		String ret="";
		
		if ((rng!=null && rng.nextInt(chance_1_over_x)==0) || (rng==null))
		{
		if (at<0 || at>3)
			throw new IllegalArgumentException("Value out of range! Only 0-3 allowed.");
		else
		if (quadlet_value<0 || quadlet_value>255)
			throw new IllegalArgumentException("Value out of range! Only 0-255 allowed.");
		else
		{
			String nuc_seq = getNucSequenceFromChar((char)quadlet_value);
			StringBuilder nucs = new StringBuilder(nuc_seq);
			nucs.setCharAt(at, new_nuc.charAt(0));
			
			ret = nucs.toString();
		}
		}
		else
		{
			ret = getNucSequenceFromChar(quadlet_value); //no changes, just get the string
		}
		
		return ret;
	}
	//if the last argument is not provided, the chance is 1 to 1 (always)
	public static String setNucAt(int quadlet_value, int at, String new_nuc)
	{
		return setNucAt(quadlet_value, at, new_nuc, 1, null);
	}

	
	public Shared()
	{
		//initialisation
	}
	
}
