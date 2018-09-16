package nikedemos.hempcraft.capabilities;

public class Highness implements ICapabilityInt {
private int highness = 0;

public void add(int q) {
	this.highness+=q;
	
}

public void subtract(int q) {
	this.highness-=q;	
}

public void set(int q) {
	this.highness=q;	
}

public int get() {
	return this.highness;
}

}
