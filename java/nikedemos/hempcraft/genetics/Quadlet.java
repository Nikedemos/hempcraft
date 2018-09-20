package nikedemos.hempcraft.genetics;

import java.util.List;
import java.util.Random;

import scala.Char;

import nikedemos.hempcraft.genetics.Shared;

public class Quadlet {
private char value;
	
	public Quadlet(int new_value)
	{
	set(new_value);
	}
	
	public Quadlet()
	{
	set(0);
	}
	
	public void set(int new_value)
	{
		if (new_value>=0 && new_value<256)
		{
			this.value = (char) new_value;
		}
		else throw new IllegalArgumentException("Value out of range! Only 0-255 allowed.");
	}
	
	
	public Gene toGene()
	{
		return new Gene(this.value);
	}
	
	public int get()
	{
		return (int) this.value;
	}
	
	public int getMutated(int nuc_mutation_chance_1_over_x, Random rand)
	{
		int ret= (int) this.value;
		String seq="";
		
		seq = Shared.setNucAt(ret, 0, Shared.getRandomNuc(rand), nuc_mutation_chance_1_over_x, rand);
		//now we take from seq
		seq = Shared.setNucAt(seq, 1, Shared.getRandomNuc(rand), nuc_mutation_chance_1_over_x, rand);
		seq = Shared.setNucAt(seq, 2, Shared.getRandomNuc(rand), nuc_mutation_chance_1_over_x, rand);
		seq = Shared.setNucAt(seq, 3, Shared.getRandomNuc(rand), nuc_mutation_chance_1_over_x, rand);
		
		//and finally, back to a number, for storage
		
		return (int) Shared.getCharFromNucSequence(seq);
	}
	
	//if you don't provide a rand, one will be created for you
	public int getMutated(int nuc_mutation_chance_1_over_x)
	{
		return this.getMutated(nuc_mutation_chance_1_over_x, new Random());
	}

}
