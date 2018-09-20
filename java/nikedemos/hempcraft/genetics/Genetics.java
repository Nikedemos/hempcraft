package nikedemos.hempcraft.genetics;

import java.util.Random;

public class Genetics {
//this is the main bit of the genetics package. This object contains
//the Genome (raw DNA handling), Genotype (bool values for either dominant or recessive allele) and finally, Phenotype (traits ready to use by HempPlot TileEntity)

	//genome is a 3D array.
	//indexing goes like this:
	//PAIR_NUMBER[16], MOTHER_OR_FATHER[2], QUADLET_INDEX[4]
	private Quadlet[][][] genome = new Quadlet[Shared.CHROMOSOME_PAIRS][2][Shared.QUADLETS_PER_CHROMOSOME];
	private Random rand;

	
	public Genetics()
	{
		this.rand = new Random();
		
		this.makeRandomGenome(this.rand);
	}
	
	public Genetics(Random rng) //no argument in constructor? Well, we're gonna generate a totally random genetics, then.
	{
		this.rand = rng;
	
		this.makeRandomGenome(this.rand);
	}

	
	//if you provide genetics with rand as the first argument, we're gonna generate some things.
	public void makeRandomGenome(Random randy)
	{
		//populate the array with random quadlets
		for (int i=0; i<Shared.CHROMOSOME_PAIRS; i++)
		{
			for (int k=0; k<Shared.QUADLETS_PER_CHROMOSOME; k++)
			{
			// i - current chromosome pair
			// [0/1] - current strand (mothers or fathers)
			// k - current quadlet idx for that pair and strand
			
			//totally random char-like (0-255)
			genome[i][0][k].set(randy.nextInt(255));
			
			//we're gonna make the other Quadlet some variety
			genome[i][1][k].set(genome[i][1][k].getMutated(64,randy));
				
			}
		}	
	}

}
