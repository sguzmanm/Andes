package com.andes.Mapwize;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.andes.resources.Arco;
import com.andes.resources.Nodo;
import com.andes.resources.PixelLocation;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import io.mapwize.mapwizeformapbox.MapOptions;
import io.mapwize.mapwizeformapbox.MapwizePlugin;

public class Mapwize {

    public FirebaseFirestore db;
    public HashMap<String, PixelLocation> accessPoints;
    public MapboxMap mapboxMap;
    public MapView mapView;
    public MapwizePlugin mapwizePlugin;

    //Machete
    private final int NUM_NODOS = 940;
    private List<Nodo> nodos;
    private List<List<Arco>> adj;
    private File cacheDir;
    private AssetManager assets;
    private Context context;

    public Mapwize(Context reactContext, File cacheDir, AssetManager assets){
        Log.d("PRUEBA", "hmm sospechoso");
        this.cacheDir = cacheDir;
        this.assets = assets;
        this.context = reactContext;
        initFirebase();
        try{initializeData();} catch (Exception e) {Log.d("ERROR", e.getMessage()); e.printStackTrace();}
        initMapwize();
    }


    private void initFirebase() {
        accessPoints=new HashMap<>();
        db= FirebaseFirestore.getInstance();
        db.collection("accessPoints")
                .whereEqualTo("piso",Math.round(7.0))
                .whereEqualTo("edificio","ML")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot accessPoint : task.getResult()) {
                                //Get phone field and append to list
                                accessPoints.put(((String)accessPoint.getData().get("BSSID")).substring(0,14),new PixelLocation((Long)accessPoint.getData().get("x"),(Long)accessPoint.getData().get("y")));

                            }
                        } else {
                            Log.w("ACCESSPOINTS", "Error getting access points of the building.", task.getException());
                        }
                        for(String k:accessPoints.keySet())
                        {
                            Log.d("ACCESSPOINTS",k+" "+accessPoints.get(k).getX()+accessPoints.get(k).getY());
                        }
                    }
                });
    }

    public void initMapwize(){
        Mapbox.getInstance(context, "pk.eyJ1Ijoic2d1em1hbm0iLCJhIjoiY2pleXB3aW45MDkxZDJxcDZzY3FnaTh2ZCJ9.B7iUjwcIAXVEmjQx6I3iEA");
        final MapOptions opts = new MapOptions.Builder().build();
        mapView = new MapView(context);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mMap) {
                try {initializeData();}
                catch(Exception e) {Log.d("ERROR",e.getMessage(),e);}
                mapboxMap = mMap;
                mapwizePlugin = new MapwizePlugin(mapView, mapboxMap, opts);/*
                mapwizePlugin.setPreferredLanguage(Locale.getDefault().getLanguage());
                mapwizePlugin.setTopPadding(20); //TODO fix convertDpToPixel import
                mapwizePlugin.setFloor(7.0);*/
            }
        });
    }

    public void initializeData() throws IOException, ParserConfigurationException, SAXException {
        nodos = new ArrayList<>(NUM_NODOS);
        adj = new ArrayList<>(NUM_NODOS);
        for(int i = 0; i<NUM_NODOS; i++) {
            adj.add(new ArrayList<Arco>());
        }
        File file = new File(cacheDir + "doc.kml");
        if (!file.exists()) try {

            InputStream is = assets.open("doc.kml");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();


            FileOutputStream fos = new FileOutputStream(file);
            fos.write(buffer);
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (file.exists())
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            for(int j = 0; j<26; j++) {
                br.readLine();
            }

            while((line = br.readLine()) != null) {
//	    	JSONObject obj = new JSONObject();
                Nodo nodo = new Nodo();

                for(int j = 0; j<30; j++) {
                    line = br.readLine();
                }
                line = line.substring(4, line.length()-5);
                nodo.FID = Integer.parseInt(line);
//	    	obj.put("FID", line);

                for(int j = 0; j<8; j++) {
                    line = br.readLine();
                }
                line = line.substring(4, line.length()-5);
                nodo.nombre = line;
//	    	obj.put("nombre", line);

                for(int j = 0; j<8; j++) {
                    line = br.readLine();
                }
                line = line.substring(4, line.length()-5);
                nodo.piso = Integer.parseInt(line);
//	    	obj.put("piso", line);

                for(int j = 0; j<8; j++) {
                    line = br.readLine();
                }
                line = line.substring(4, line.length()-5);
                nodo.bloque = line;
//	    	obj.put("bloque", line);

                for(int j = 0; j<8; j++) {
                    line = br.readLine();
                }
                line = line.substring(4, line.length()-5);
                nodo.area = Double.parseDouble(line.replaceAll(",", "."));
//	    	obj.put("area", line);

                for(int j = 0; j<8; j++) {
                    line = br.readLine();
                }
//	    	line = line.substring(4, line.length()-5);
//	    	obj.put("concentrac", line);

                for(int j = 0; j<8; j++) {
                    line = br.readLine();
                }
//	    	line = line.substring(4, line.length()-5);
//	    	obj.put("cap", line);

                for(int j = 0; j<20; j++) {
                    line = br.readLine();
                }
                line = line.substring(22, line.length()-14);
                nodo.coordenadas = line;
//	    	obj.put("coordenadas", line);

                //siguiente
                for(int j = 0; j<5; j++) {
                    br.readLine();
                }

                nodos.add(nodo);
                //arr.add(obj);
            }
            //	    theObj.put("nodos",arr);
            //	    System.out.println(theObj);
            //	    try (FileWriter file = new FileWriter("./data/nodos.json")) {
            //			file.write(theObj.toJSONString());
            //			System.out.println("Successfully Copied JSON Object to File...");
            //			System.out.println("\nJSON Object: " + theObj);
            //		}
        }




        //------------------------------------------------------------------------------
        //------------------------------------ARCOS-------------------------------------
        //------------------------------------------------------------------------------

        File f = new File(cacheDir+"red-caminos.kml");
        if (!f.exists()) try {

            InputStream is = assets.open("red-caminos.kml");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();


            FileOutputStream fos = new FileOutputStream(f);
            fos.write(buffer);
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (f.exists())
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(f);
            doc.getDocumentElement().normalize();
            NodeList nl1 = doc.getElementsByTagName("coordinates");
            NodeList nl2 = doc.getElementsByTagName("description");

            //Tiene primero el origen, luego el destino, y de tercero el peso.
            //		List<Arco> aristas = new ArrayList<>();

            for(int k = 0; k < nl1.getLength(); k++) {
                String[] coords = nl1.item(k).getTextContent().trim().split(" ");

                String desc = nl2.item(k).getTextContent();
                String temp = desc.substring(desc.indexOf("SHAPE_Leng") + 21);
                double length = Double.parseDouble(temp.substring(1,temp.indexOf("<")).replace(',', '.'));

                temp = desc.substring(desc.indexOf("Pendiente") + 20);
//			double pendiente = Double.parseDouble(temp.substring(0,temp.indexOf("<")).replace(',', '.'));



                String origen = coords[0];
                String destino = coords[coords.length-1];

                List<Integer> nodosOrigen = new ArrayList<>();
                List<Integer> nodosDestino = new ArrayList<>();

                for(Nodo nodo : nodos) {
                    if(nodo.coordenadas.equals(origen)) {
                        nodosOrigen.add(nodo.FID);
                    }
                    if(nodo.coordenadas.equals(destino)) {
                        nodosDestino.add(nodo.FID);
                    }
                }

                if(nodosOrigen.size() != 0 && nodosDestino.size() != 0) {
                    for(Integer nodo : nodosOrigen) {
                        for(Integer nodo2 : nodosDestino) {
                            Arco arco = new Arco();
                            arco.camino = coords;
                            arco.length = length;
                            arco.origen = nodo;
                            arco.destino = nodo2;
                            adj.get(nodo).add(arco);
                            adj.get(nodo2).add(arco);
                        }
                    }

                }
            }
        }

    }
}
