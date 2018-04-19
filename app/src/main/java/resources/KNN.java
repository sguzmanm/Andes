package resources;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KNN {

    PuntoRef[] nodosRef;

    private FirebaseFirestore db;

    boolean llegoFirebase=false;


    public KNN()
    {

    }

    void importarInfo(){

        nodosRef=new PuntoRef[63];
        db=FirebaseFirestore.getInstance();
        db.collection("lecturas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            for (DocumentSnapshot refPoint : task.getResult()) {
                                PuntoRef puntoRef=new PuntoRef();
                                puntoRef.coordenadas=new long[]{(long)refPoint.getData().get("x"),(long)refPoint.getData().get("y")};
                                Map<String,Double> map=new HashMap<>();
                                Map<String,Object> val=(Map<String,Object>)refPoint.getData().get("lectura");
                                for(String s:val.keySet())
                                {
                                    map.put((String)val.get("BSSID"),(Double)val.get("dbm"));
                                }
                                puntoRef.senales=map;
                                boolean encontrado=false;
                                for(int j=0;j<i;j++)
                                {
                                    if(nodosRef[j].coordenadas[0]==puntoRef.coordenadas[0] && nodosRef[j].coordenadas[1]==puntoRef.coordenadas[1])
                                    {
                                        for(String s:nodosRef[j].senales.keySet())
                                        {
                                            if(puntoRef.senales.get(s)!=null)
                                                nodosRef[j].senales.put(s,(nodosRef[j].senales.get(s)+puntoRef.senales.get(s))/2);
                                        }
                                        encontrado=true;
                                        break;
                                    }

                                }
                                if(!encontrado)
                                {
                                    nodosRef[i]=puntoRef;
                                    i++;

                                }
                            }

                        } else {
                            Log.d("KNN", "Error getting access points of the building.", task.getException());
                        }
                        llegoFirebase=true;
                    }
                });

        //Falta acà traer las cosas de Firebase
    }


    public int[] ubicacion(Map<String, Double> senales, int k) {
        importarInfo();
        while(!llegoFirebase)
        {
            try {
                Thread.sleep(10000);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
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

