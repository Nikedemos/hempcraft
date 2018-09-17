package nikedemos.hempcraft.genetics;

public class Gene {

	public char value;
	
	public Gene(int quadlet_val)
	{
		this.value = (char)quadlet_val;
	}
	
	public char get()
	{
		return this.value;
	}
	
	public void set(char val)
	{
		this.value=val;
	}
}
