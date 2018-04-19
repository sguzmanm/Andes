package resources;

import java.util.Map;

class PuntoRef implements Comparable<PuntoRef> {
    Map<String, Double> senales;
    int[] coordenadas;
    double diferencia;

    public int compareTo(PuntoRef o) {
        return Double.compare(diferencia, o.diferencia);
    }
}
