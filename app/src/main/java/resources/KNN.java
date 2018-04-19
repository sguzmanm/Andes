package resources;

import java.util.Arrays;
import java.util.Map;

public class KNN {

    PuntoRef[] nodosRef;

    void importarInfo(){
        //Falta acà traer las cosas de Firebase
    }


    public int[] ubicacion(Map<String, Double> senales, int k) {
        importarInfo();

        for (int i = 0; i < nodosRef.length; i++)
            nodosRef[i].diferencia = diferencia(senales, nodosRef[i].senales, 3);

        Arrays.sort(nodosRef);

        double x = 0, y = 0, sum = 0;
        for (int i = 0; i < k; i++) {
            x += nodosRef[i].diferencia * (double) nodosRef[i].coordenadas[0];
            y += nodosRef[i].diferencia * (double) nodosRef[i].coordenadas[1];
            sum += nodosRef[i].diferencia;
        }
        return new int[]{(int) (x / sum), (int) (y / sum)};
    }

    private double diferencia(Map<String, Double> a, Map<String, Double> b, int tipo) {
        double ans = 0;
        for (Map.Entry<String, Double> e : a.entrySet()) {
            ans += fun(e.getValue() - b.get(e.getKey()), tipo);
        }
        return inv(ans, tipo);
    }

    private double fun(double x, int tipo) {
        switch (tipo) {
            case 0:
                return Math.abs(x);
            case 1:
                return x * x;
            case 2:
                return Math.exp(x);
            default:
                return x;
        }
    }

    private double inv(double x, int tipo) {
        switch (tipo) {
            case 0:
                return x;
            case 1:
                return Math.sqrt(x);
            case 2:
                return Math.log(x);
            default:
                return x;
        }
    }
}

