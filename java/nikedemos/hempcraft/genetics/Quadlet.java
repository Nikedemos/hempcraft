package nikedemos.hempcraft.genetics;

import java.util.List;
import java.util.Random;

import scala.Char;

import nikedemos.hempcraft.genetics.Shared;

public class Quadlet {
private char value;
	
	public Quadlet(char new_value)
	{
	set(new_value);
	}
	
	public void set(char new_value)
	{
		if (new_value>=0 && new_value<256)
		{
			this.value = new_value;
		}
		else throw new IllegalArgumentException("Value out of range! Only 0-255 allowed.");
	}
	
	
	public Gene toGene()
	{
		return new Gene(this.value);
	}
	
	public char get()
	{
		return this.value;
	}
}
