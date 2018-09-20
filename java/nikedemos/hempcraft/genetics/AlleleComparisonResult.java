package nikedemos.hempcraft.genetics;

import nikedemos.hempcraft.genetics.Shared;

public class AlleleComparisonResult {
//
private char gene1_val;
private char gene2_val;
private char gene_id;
private int result;

	AlleleComparisonResult(Gene g1, Gene g2, char gid)
	{
		this.gene1_val=g1.get();
		this.gene2_val=g2.get();
		this.gene_id=gid;
		
		//there are four possibilities
		/* 0: Gene 1 is dominant over Gene2
		 * 1: Gene 1 is recessive to Gene2
		 * 2: Gene 1 is codominant with Gene2
		 * 3: Gene 1 clashes with Gene2
		 *
		 * When the two genes clash, a value of -1 is written in the
		 * genotype. Then, a genetic disease table is consulted. Some -1s
		 * are lethal, some affect fertility, growth,  yield, etc, in a negative way.
		 */
		// to get the result, add the two gene values together, and multiply by the square of (gid+1) - then take modulo 4
		this.result = (gene1_val+gene2_val)*((gid+1)^2) % 4;
	}
	
	public int getDominance()
	{
		return this.result;
	}
	
	public int getResultingAllele()
	{
		int ret=-1; //default, for case 3
		
		switch(getDominance())
		{
		case 0: ret = this.gene1_val; break;
		case 1: ret = this.gene2_val; break;
		case 2: ret = this.gene1_val; break;
		}

		return ret;
	}
	
	public int getId()
	{
		return this.gene_id;
	}
}
