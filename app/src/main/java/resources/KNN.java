package resources;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Map;

public class KNN {

    PuntoRef[] nodosRef;

    private FirebaseFirestore db;

    private static final double MIN=-110;



    public KNN(PuntoRef[] nodosRef)
    {
        this.nodosRef=nodosRef;
    }




    public int[] ubicacion(Map<String, Double> senales, int k) {
        Log.d("KNN","Llega");
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
        Double ini;
        Double fin;
        for (String s : a.keySet()) {
            ini=a.get(s);
            if (ini==null)
                ini=MIN;
            fin=b.get(s);
            if(fin==null)
                fin=MIN;
            ans += fun(ini - fin, tipo);
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
                return Math.exp(Math.abs(x));
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

