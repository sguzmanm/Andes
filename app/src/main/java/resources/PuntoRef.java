package resources;

import java.util.Map;

public class PuntoRef implements Comparable<PuntoRef> {
    public Map<String, Double> senales;
    public long[] coordenadas;
    public double diferencia;
    public int conteo;

    public int compareTo(PuntoRef o) {
        return Double.compare(diferencia, o.diferencia);
    }
}
