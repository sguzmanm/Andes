package resources;

public class NodoCola implements Comparable<NodoCola> {
	public int nodo;
	public double peso;
	@Override
	public int compareTo(NodoCola arg0) {
		// TODO Auto-generated method stub
		return ((Double)peso).compareTo(arg0.peso);
	}
	
	
}
