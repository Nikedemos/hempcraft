package nikedemos.hempcraft.nutid;

import java.util.ArrayList;
import java.util.Random;

public class BinomialRand {

private int n=0; //number of distinct values
private ArrayList weights = new ArrayList();
private ArrayList values = new ArrayList();
private int offset=0;
private Random rand;
private double p=0.5;
private double q=0.5;

public BinomialRand(int n, int offset, Random rng)
	{
	init(n,offset,rng);
	}

public BinomialRand(int n, Random rng)
{
	this(n, 0, rng);
}

public BinomialRand(int n)
{
	this(n, 0, new Random());
}

public BinomialRand(int n, int offset)
{
	this(n, offset, new Random());
}


public void init(int n, int offset, Random rng)
{
	this.n=n;
	this.offset = offset;
	this.populateValues(n);
	this.populateWeights(this.n);
	this.rand = rng;
}

public void setP(double p)
{
	this.p=p;
}

public double getP()
{
	return this.p;
}

public void setQ(double q)
{
	this.q=q;
}

public double getQ()
{
	return this.q;
}


public int getIntNorm()
{
	int rettie=0;
	double randy = this.rand.nextDouble();
	int i;
	
	for (i=0; i<this.n; i++)
	{
		double cur_weight = Double.valueOf((double)this.weights.get(i));
		if (randy<=cur_weight)
			{
			//found it
			break;
			}
		else //keep going
			{
			randy-=cur_weight;
			}
	}
	
	return Integer.valueOf((int)this.values.get(i))+this.offset;
}

public void populateWeights(int n)
{
	if (!this.weights.isEmpty())
		this.weights.clear();
	
	for (int k=0; k<n; k++)
	{
	this.weights.add(getProbability(n, k));
	}
}

public void populateValues(int n)
{
	if (!this.values.isEmpty())
		this.values.clear();
	
	for (int k=0; k<n; k++)
	{
	this.values.add(k);
	}
}

public double getProbability(int n, int k)
{
	n--; //because if we feed it 8, we're gonna get 9 possible values
	
	return (fact(n)/(fact(k)*fact(n-k))*Math.pow(p, k)*Math.pow(q, (n-k)));
}

public static int fact(int number) {

if (number < 0)
	{
	throw new IllegalArgumentException("Factorial: sorry, no negative numbers.");
	}
else
	{
	int rettie = 1;
    
	for(int i=2; i<=number; i++)
    	{
        rettie *= i;               
    	}
    
    return rettie;
	}
}

}
