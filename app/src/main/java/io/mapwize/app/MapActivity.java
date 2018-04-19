package io.mapwize.app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.UiSettings;
import com.squareup.picasso.Picasso;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import io.indoorlocation.core.IndoorLocation;
import io.mapwize.mapwizeformapbox.FollowUserMode;
import io.mapwize.mapwizeformapbox.MapOptions;
import io.mapwize.mapwizeformapbox.MapwizePlugin;
import io.mapwize.mapwizeformapbox.Marker;
import io.mapwize.mapwizeformapbox.api.Api;
import io.mapwize.mapwizeformapbox.api.ApiCallback;
import io.mapwize.mapwizeformapbox.api.SearchParams;
import io.mapwize.mapwizeformapbox.model.Direction;
import io.mapwize.mapwizeformapbox.model.DirectionPoint;
import io.mapwize.mapwizeformapbox.model.DirectionPointWrapper;
import io.mapwize.mapwizeformapbox.model.LatLngFloor;
import io.mapwize.mapwizeformapbox.model.MapwizeObject;
import io.mapwize.mapwizeformapbox.model.ParsedUrlObject;
import io.mapwize.mapwizeformapbox.model.Place;
import io.mapwize.mapwizeformapbox.model.PlaceList;
import io.mapwize.mapwizeformapbox.model.Route;
import io.mapwize.mapwizeformapbox.model.Translation;
import io.mapwize.mapwizeformapbox.model.Universe;
import io.mapwize.mapwizeformapbox.model.Venue;
import io.realm.RealmList;
import resources.Arco;
import resources.CustomResult;
import resources.Nodo;
import resources.NodoCola;
import resources.PixelLocation;
import resources.Triangulacion;

import static com.mikepenz.materialize.util.UIUtils.convertDpToPixel;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchResultsListAdapter.Listener{

    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 0;

    private final int TOP_PADDING = 80;
    private final int BOTTOM_PADDING = 54;
    private final int TOP_PADDING_DIRECTION = 80;

    private final int NUM_NODOS = 56;

    private MapboxMap mapboxMap;
    private MapView mapView;
    private MapwizePlugin mapwizePlugin;
    private DrawerLayout drawer;
    private RelativeLayout mapRelativeLayout;
    private LinearLayout bottomInfoLayout;
    private TextView mainText;
    private TextView secondText;
    private TextView thirdText;
    private ImageView infoIcon;
    private TextView creditsTextView;
    private ImageButton directionButton;
    private ImageButton languagesButton;
    private ImageButton universesButton;
    private ImageView leftActionButton;
    private LinearLayout searchBarLayout;
    private EditText searchBarEditText;
    private RelativeLayout searchSuggestionsListLayout;
    private RecyclerView searchSuggestionsList;
    private SearchResultsListAdapter searchResultsListAdapter;
    private View backedView;
    private ImageView qrcodeButton;
    private NavigationView navigationView;
    private LinearLayout directionBottomInfoLayout;
    private ImageView directionBottomInfoAccessibleImage;
    private TextView directionDistance;
    private TextView directionTime;
    private LinearLayout directionHeaderMainLayout;
    private EditText directionHeaderFromTextView;
    private EditText directionHeaderToTextView;
    private ImageButton directionHeaderSwapButton;
    private ImageButton directionHeaderAccessibilityButton;
    private ImageButton directionHeaderBackButton;
    private ProgressBar progressBar;
    private BottomSheetBehavior mBottomSheetBehavior;
    private View bottomSheet;
    private WebView placeDetailsWebView;
    private ImageView infoBottomMoreImageView;

    private SearchMode currentSearchMode = SearchMode.NONE;
    private DirectionPoint fromDirectionPoint;
    private DirectionPoint toDirectionPoint;
    private boolean isAccessible = false;
    private boolean inDirectionMode = false;
    private Map<String, FullDirectionObject> directionByVenue = new HashMap<>();
    private List<Place> promotedPlaces = new ArrayList<>();
    private MapwizeLocationProvider mapwizeLocationProvider;
    private Venue mCurrentVenue = null;
    private MapwizeObject shouldBeSelected;
    //------------------
    //Firebase
    //------------------

    private FirebaseFirestore db;

    //------------
    //Algoritmos propios
    //------------
    private List<Nodo> nodos;
    private List<List<Arco>> adj;


    private HashMap<String, PixelLocation> accessPoints;

    private boolean testingRoutes=true;
    private boolean isExternalTest=false;

    //Ubicaciones para aproximar al presionar
    private com.mapbox.mapboxsdk.annotations.Marker destinationMarker;
    private com.mapbox.mapboxsdk.annotations.Marker originMarker;
    private LatLng originCoord;
    private LatLng destinationCoord;
    //Do timer activity
    private Timer timer;
    private TimerTask timeTAGTAGsk = new TimerTask() {

    private boolean inicio=false;

        @Override
        public void run() {
            if(!inicio)
            {
                inicio=true;
                Looper.prepare();
            }

            HashMap<String,CustomResult> bssid=new HashMap<>();
            WifiManager wifiManager=(WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            List<ScanResult> results=wifiManager.getScanResults();
            ArrayList<CustomResult> list=new ArrayList<>();
            String message="No results. Check wireless on";
            if(results!=null)
            {
                final int size=results.size();
                if(size==0) message="No access points in route";
                else {
                    message="";
                    for(ScanResult sc:results)
                    {
                        if(bssid.get(sc.BSSID.substring(0,14))==null)
                        {
                            list.add(new CustomResult(sc));
                            bssid.put(sc.BSSID.substring(0,14),new CustomResult(sc));
                        }
                    }
                    Collections.sort(list);
                    for(CustomResult sc:list)
                    {
                        message+=sc.getScan().BSSID+" "+sc.getScan().SSID+" "+sc.getScan().level+" "+wifiManager.calculateSignalLevel(sc.getScan().level,5)+" "+sc.getScan().frequency+"\n";
                    }
                }
            }
            Log.d("TAGTAG",message);
            CustomResult[] indices=new CustomResult[3];
            int num=0;
            for(CustomResult sc:list)
            {
                if(accessPoints.containsKey(sc.getPreBssid()))
                {
                    indices[num]=sc;
                    num++;
                    Log.d("TAGTAG",sc.getPreBssid());
                }
                if(num>=3)
                    break;
            }
            //FAKE ALGORITHM FOR TESTING Uncomment to use

            /*Triangulacion t = new Triangulacion();
            System.out.println(t.ubicacion(1620, 1276, 508,524,1277,715,1453,1927,405));

            System.out.println(t.ubicacion(2527,1935,12*39,
                    2558, 2344, t.dbmAMetros(-53,2462)*39,2005,2344,t.dbmAMetros(-58,2437)*39));

            double[] d=t.transformPixelToLatLng(4.603270627176880,-74.06486481428140,4.602725221337820,-74.06529933214190,
                    4.602854888940370,-74.06432099640370,3168,3223,2393.972155790736,2383.695434192323);
            System.out.println(d[0]+" "+d[1]);
            if(!pruebaPiso)
            {
                mapwizePlugin.setFloor(-1.0);
                mapwizePlugin.setFloor(7.0);
                pruebaPiso=true;
            }
            mapwizeLocationProvider.defineLocation(new IndoorLocation("Custom",d[0],d[1],7.0,System.currentTimeMillis()));
            Log.d("TAGTAG",mapwizePlugin.getUserPosition().getLatitude()+" "+mapwizePlugin.getUserPosition().getLongitude()+"");
            mapwizeLocationProvider.setAccessPointsRunning(true);*/


            //REAL ALGORITHM Uncomment to use

            Triangulacion t = new Triangulacion();
            //Posiciones de los routers encontrados en el mapa
            String[]temp;
            double[]d;
            if(num>=3)
            {
                temp=t.ubicacion(accessPoints.get(indices[0].getPreBssid()).getX(),accessPoints.get(indices[0].getPreBssid()).getY(),t.dbmAMetros(indices[0].getScan().level,indices[0].getScan().frequency)*39,
                        accessPoints.get(indices[1].getPreBssid()).getX(),accessPoints.get(indices[1].getPreBssid()).getY(),t.dbmAMetros(indices[1].getScan().level,indices[1].getScan().frequency)*39,
                        accessPoints.get(indices[2].getPreBssid()).getX(),accessPoints.get(indices[2].getPreBssid()).getY(),t.dbmAMetros(indices[2].getScan().level,indices[2].getScan().frequency)*39).split(";");
                //Crea el marcador con las ubicaciones en latitud y longitud
                //Estos son los puntos de la transformación lineal guardada para el ML
                d=t.transformPixelToLatLng(4.603270627176880,-74.06486481428140,4.602725221337820,-74.06529933214190,
                        4.602854888940370,-74.06432099640370,3168,3223,Double.parseDouble(temp[0]),Double.parseDouble(temp[1]));
                if(Double.isNaN(d[0])||Double.isNaN(d[1]))
                {
                    Log.d("TAGTAG","Error 1");
                    temp=t.ubicacion(accessPoints.get(indices[1].getPreBssid()).getX(),accessPoints.get(indices[1].getPreBssid()).getY(),t.dbmAMetros(indices[1].getScan().level,indices[1].getScan().frequency)*39,
                            accessPoints.get(indices[2].getPreBssid()).getX(),accessPoints.get(indices[2].getPreBssid()).getY(),t.dbmAMetros(indices[2].getScan().level,indices[2].getScan().frequency)*39,
                            accessPoints.get(indices[0].getPreBssid()).getX(),accessPoints.get(indices[0].getPreBssid()).getY(),t.dbmAMetros(indices[0].getScan().level,indices[0].getScan().frequency)*39).split(";");
                    //Crea el marcador con las ubicaciones en latitud y longitud
                    d=t.transformPixelToLatLng(4.603270627176880,-74.06486481428140,4.602725221337820,-74.06529933214190,
                            4.602854888940370,-74.06432099640370,3168,3223,Double.parseDouble(temp[0]),Double.parseDouble(temp[1]));
                    if(Double.isNaN(d[0])||Double.isNaN(d[1]))
                    {

                        Log.d("TAGTAG","Error 2");
                        temp=t.ubicacion(accessPoints.get(indices[0].getPreBssid()).getX(),accessPoints.get(indices[0].getPreBssid()).getY(),t.dbmAMetros(indices[0].getScan().level,indices[0].getScan().frequency)*39,
                                accessPoints.get(indices[2].getPreBssid()).getX(),accessPoints.get(indices[2].getPreBssid()).getY(),t.dbmAMetros(indices[2].getScan().level,indices[2].getScan().frequency)*39,
                                accessPoints.get(indices[1].getPreBssid()).getX(),accessPoints.get(indices[1].getPreBssid()).getY(),t.dbmAMetros(indices[1].getScan().level,indices[1].getScan().frequency)*39).split(";");
                        //Crea el marcador con las ubicaciones en latitud y longitud
                        d=t.transformPixelToLatLng(4.603270627176880,-74.06486481428140,4.602725221337820,-74.06529933214190,
                                4.602854888940370,-74.06432099640370,3168,3223,Double.parseDouble(temp[0]),Double.parseDouble(temp[1]));

                        if(Double.isNaN(d[0])||Double.isNaN(d[1]))
                        {
                            Log.d("TAGTAG","Es ilocalizable");
                            mapwizeLocationProvider.setAccessPointsRunning(false);
                        }
                        else
                        {
                            Log.d("TAGTAG",d[0]+" "+d[1]);
                            mapwizeLocationProvider.defineLocation(new IndoorLocation("Custom",d[0],d[1],7.0,System.currentTimeMillis()));
                            Log.d("TAGTAG",mapwizePlugin.getUserPosition().getLatitude()+" "+mapwizePlugin.getUserPosition().getLongitude()+"");
                            mapwizeLocationProvider.setAccessPointsRunning(true);

                        }
                    }
                    else
                    {
                        Log.d("TAGTAG",d[0]+" "+d[1]);
                        mapwizeLocationProvider.defineLocation(new IndoorLocation("Custom",d[0],d[1],mapwizePlugin.getFloor(),System.currentTimeMillis()));
                        Log.d("TAGTAG",mapwizePlugin.getUserPosition().getLatitude()+" "+mapwizePlugin.getUserPosition().getLongitude()+"");
                        mapwizeLocationProvider.setAccessPointsRunning(true);

                    }
                }
                else
                {
                    Log.d("TAGTAG",d[0]+" "+d[1]);
                    mapwizeLocationProvider.defineLocation(new IndoorLocation("Custom",d[0],d[1],mapwizePlugin.getFloor(),System.currentTimeMillis()));
                    Log.d("TAGTAG",mapwizePlugin.getUserPosition().getLatitude()+" "+mapwizePlugin.getUserPosition().getLongitude()+"");
                    mapwizeLocationProvider.setAccessPointsRunning(true);

                }
            }
            else
            {
                Log.d("TAGTAG","Es ilocalizable");
                mapwizeLocationProvider.setAccessPointsRunning(false);
            }


        }
    };

    public void initializeData() throws IOException, ParserConfigurationException, SAXException, ParseException {
        nodos = new ArrayList<>(NUM_NODOS);
        adj = new ArrayList<>(NUM_NODOS);
        for(int i = 0; i<NUM_NODOS; i++) {
            adj.add(new ArrayList<Arco>());
        }
        File file = new File(getCacheDir() + "nodos2.json");
        try {

            InputStream is = getAssets().open("nodos2.json");
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
            JSONParser parser = new JSONParser();

            JSONArray arr = (JSONArray) parser.parse(new FileReader(file));
            for(int i = 0; i<arr.size(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                Nodo nodo = new Nodo();
                nodo.piso = ((Long) obj.get("floor")).intValue();
                nodo.FID = ((Long) obj.get("FID")).intValue();
                nodo.area = ((Long) obj.get("area")).doubleValue();
                nodo.bloque = (String) obj.get("bloque");
                Log.d("WAT",obj.toJSONString());
                String lon=(String) obj.get("lon");
                String lat=(String) obj.get("lat");
                nodo.coordenadas = lon.replace(". ",".") + "," + lat.replace(". ",".") + ",0";
                nodos.add(nodo);
            }
            Log.d("NODOS","TAM "+nodos.size());
        }




        //------------------------------------------------------------------------------
        //------------------------------------ARCOS-------------------------------------
        //------------------------------------------------------------------------------


        File f = new File(getCacheDir()+"red-caminos.kml");
        try {

            InputStream is = getAssets().open("red-caminos.kml");
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
            int arcos=0;

            for(int k = 0; k < nl1.getLength(); k++) {
                String[] coords = nl1.item(k).getTextContent().trim().split(" ");

                String desc = nl2.item(k).getTextContent();
                String temp = desc.substring(desc.indexOf("SHAPE_Leng") + 21);
                double length = Double.parseDouble(temp.substring(2,temp.indexOf("<")).replace(',', '.'));

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
                            arcos++;
                        }
                    }

                }

            }
            Log.d("NODOS","ARCOS "+arcos);

        }

    }

    public void start() {
        if(timer != null) {
            return;
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(timeTAGTAGsk, 0, 2000);
    }

    public void stop() {
        timer.cancel();
        timer = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MY TAG",1+"");
        super.onCreate(savedInstanceState);
        accessPoints=new HashMap<>();
        db=FirebaseFirestore.getInstance();
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

        Mapbox.getInstance(this, "pk.eyJ1Ijoic2d1em1hbm0iLCJhIjoiY2pleXB3aW45MDkxZDJxcDZzY3FnaTh2ZCJ9.B7iUjwcIAXVEmjQx6I3iEA");
        setContentView(R.layout.activity_map);
        findViews();

        mapView.onCreate(savedInstanceState);

        final MapOptions opts = new MapOptions.Builder()
                .build();

        initMapWithOptions(opts);

        MapwizeApplication application = (MapwizeApplication) getApplication();

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            setupSearchResult(charSequence.toString(), currentSearchMode);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void setupSearchEditTexts() {
        searchBarEditText.addTextChangedListener(textWatcher);
        searchBarEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    searchBarEditText.setText("");
                    currentSearchMode = SearchMode.DEFAULT;
                    openSearchTable(currentSearchMode);
                }
                else {
                    currentSearchMode = SearchMode.NONE;
                }
                setupLeftActionButton(currentSearchMode);
            }
        });
        directionHeaderFromTextView.addTextChangedListener(textWatcher);
        directionHeaderFromTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    directionHeaderFromTextView.setText("");
                    currentSearchMode = SearchMode.FROM_DIRECTION;
                    openSearchTable(currentSearchMode);
                }
                else {
                    currentSearchMode = SearchMode.NONE;
                }
            }
        });
        directionHeaderToTextView.addTextChangedListener(textWatcher);
        directionHeaderToTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    directionHeaderToTextView.setText("");
                    currentSearchMode = SearchMode.TO_DIRECTION;
                    openSearchTable(currentSearchMode);
                }
                else {
                    currentSearchMode = SearchMode.NONE;
                }
            }
        });
    }

    private void setupSearchResult(String query, SearchMode mode) {
        Log.d("MY TAG",2+"");
        if (mode == SearchMode.DEFAULT) {
            if (mapwizePlugin.getVenue() == null) {
                searchVenues(query);
            }
            else {
                searchInVenue(query);
            }
        }
        if (mode == SearchMode.FROM_DIRECTION) {
            searchFrom(query);
        }

        if (mode == SearchMode.TO_DIRECTION) {
            searchInVenue(query);
        }
    }

    private void searchInVenue(String query) {
        Log.d("MY TAG",3+"");
        if (query.length() > 0) {
            SearchParams params = new SearchParams.Builder()
                    .setObjectClass(new String[]{"place", "placeList"})
                        .setQuery(query)
                        .setVenueId(mapwizePlugin.getVenue().getId())
                        .setUniverseId(mapwizePlugin.getUniverseForVenue(mapwizePlugin.getVenue()).getId())
                        .build();
            Api.search(params, new ApiCallback<List<MapwizeObject>>() {
                @Override
                public void onSuccess(final List<MapwizeObject> object) {
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (object.size() == 0) {
                                List<Object> list = new ArrayList<>();
                                list.add("no_results");
                                searchResultsListAdapter.swapData(list);
                            }
                            else {
                                searchResultsListAdapter.swapData(object);
                            }
                        }
                    };
                    uiHandler.post(runnable);
                }

                @Override
                public void onFailure(Throwable t) {
                    if (t != null) {
                        t.printStackTrace();
                    }
                }
            });
        }
        else {
            Api.getMainSearchesForVenue(mapwizePlugin.getVenue().getId(), new ApiCallback<List<MapwizeObject>>() {
                @Override
                public void onSuccess(final List<MapwizeObject> object) {
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            searchResultsListAdapter.swapData(object);
                        }
                    };
                    uiHandler.post(runnable);
                }

                @Override
                public void onFailure(Throwable t) {
                    if (t != null) {
                        t.printStackTrace();
                    }
                }
            });
        }
    }

    private void searchFrom(String query) {
        Log.d("MY TAG",4+"");
        if (query.length() > 0) {
            SearchParams params = new SearchParams.Builder()
                    .setObjectClass(new String[]{"place"})
                    .setQuery(query)
                    .setVenueId(mapwizePlugin.getVenue().getId())
                    .setUniverseId(mapwizePlugin.getUniverseForVenue(mapwizePlugin.getVenue()).getId())
                    .build();
            Api.search(params, new ApiCallback<List<MapwizeObject>>() {
                @Override
                public void onSuccess(final List<MapwizeObject> response) {
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            List<Object> objects = new ArrayList<>();
                            if (mapwizePlugin.getUserPosition() != null && mapwizePlugin.getUserPosition().getFloor() != null) {
                                objects.add("current_location");
                            }
                            objects.addAll(response);
                            searchResultsListAdapter.swapData(objects);
                        }
                    };
                    uiHandler.post(runnable);
                }

                @Override
                public void onFailure(Throwable t) {
                    if (t != null) {
                        t.printStackTrace();
                    }
                }
            });
        }
        else {
            Api.getMainFromsForVenue(mapwizePlugin.getVenue().getId(), new ApiCallback<List<Place>>() {
                @Override
                public void onSuccess(final List<Place> object) {
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            List<Object> objects = new ArrayList<>();
                            if (mapwizePlugin.getUserPosition() != null && mapwizePlugin.getUserPosition().getFloor() != null) {
                                objects.add("current_location");
                            }
                            objects.addAll(object);
                            searchResultsListAdapter.swapData(objects);
                        }
                    };
                    uiHandler.post(runnable);
                }

                @Override
                public void onFailure(Throwable t) {
                    if (t != null) {
                    t.printStackTrace();
                }
                }
            });
        }
    }

    private void searchVenues(String query) {
        Log.d("MY TAG",5+"");

        SearchParams params = new SearchParams.Builder()
                .setObjectClass(new String[]{"venue"})
                .setQuery(query)
                .build();

        Api.search(params, new ApiCallback<List<MapwizeObject>>() {
            @Override
            public void onSuccess(final List<MapwizeObject> object) {
                Handler uiHandler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (object.size() == 0) {
                            List<Object> list = new ArrayList<>();
                            list.add("no_results");
                            searchResultsListAdapter.swapData(list);
                        }
                        else {
                            searchResultsListAdapter.swapData(object);
                        }
                    }
                };
                uiHandler.post(runnable);
            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null) {
                  t.printStackTrace();
                }
            }
        });
    }

    public void hideSoftKeyboard(AppCompatActivity activity) {
        if (activity == null) return;
        if (activity.getCurrentFocus() == null) return;

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void findViews() {
        Log.d("MY TAG",6+"");
        directionHeaderMainLayout = findViewById(R.id.direction_header_main_layout);
        directionHeaderFromTextView = findViewById(R.id.direction_header_from_text);
        directionHeaderToTextView = findViewById(R.id.direction_header_to_text);
        directionHeaderSwapButton = findViewById(R.id.direction_header_swap_button);
        directionHeaderAccessibilityButton = findViewById(R.id.direction_header_accessibility_button);
        directionHeaderBackButton = findViewById(R.id.direction_header_back_button);
        directionBottomInfoLayout = findViewById(R.id.direction_info_bottom_layout);
        directionBottomInfoAccessibleImage = findViewById(R.id.direction_info_bottom_accessible_image);
        directionDistance = findViewById(R.id.direction_info_bottom_distance_text);
        directionTime = findViewById(R.id.direction_info_bottom_time_text);
        drawer = findViewById(R.id.drawer_layout);
        leftActionButton = findViewById(R.id.left_action);
        bottomInfoLayout = findViewById(R.id.info_bottom_layout);
        mainText = findViewById(R.id.info_bottom_main_text);
        secondText = findViewById(R.id.info_bottom_second_text);
        thirdText = findViewById(R.id.info_bottom_third_text);
        infoIcon = findViewById(R.id.info_bottom_icon);
        mapView = findViewById(R.id.mapboxMap);
        searchBarLayout = findViewById(R.id.search_bar_layout);
        searchBarEditText = findViewById(R.id.search_bar_edittext);
        backedView = findViewById(R.id.backedView);
        mapRelativeLayout = findViewById(R.id.relative_layout);
        creditsTextView = findViewById(R.id.credits_text_view);
        qrcodeButton = findViewById(R.id.qrcode_action);
        progressBar = findViewById(R.id.progress_bar);
        navigationView = findViewById(R.id.nav_view);
        bottomSheet = findViewById(R.id.bottom_sheet1);
        placeDetailsWebView = findViewById(R.id.place_details_webview);
        infoBottomMoreImageView = findViewById(R.id.info_bottom_more_icon);
    }

    private void initMapWithOptions(final MapOptions opts) {
        Log.d("MY TAG",7+"");
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mMap) {
                try {
                    initializeData();
                }
                catch(Exception e)
                {
                    Log.d("ERROR",e.getMessage(),e);
                }
                mapboxMap = mMap;
                Log.d("AAAAAAA","SETUP");
                Log.d("AAAAAAA","SUP IZQ "+mapwizePlugin.getPlaceForId("5aba5b43975fc800138eeef8").getLongitudeMax()+" "+mapwizePlugin.getPlaceForId("5aba5b7585f18700132073eb").getLatitudeMax());
                Log.d("AAAAAAA","SUP DER "+mapwizePlugin.getPlaceForId("5aba803bbdaf3200136ea7c7").getLongitudeMax()+" "+mapwizePlugin.getPlaceForId("5aba5b7585f18700132073eb").getLatitudeMax());
                Log.d("AAAAAAA","ABAJO IZQ "+mapwizePlugin.getPlaceForId("5aba5b1ca7255f00131e6211").getLongitudeMax()+" "+mapwizePlugin.getPlaceForId("5aba5b7585f18700132073eb").getLatitudeMax());

                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                                                    @Override
                                                    public void onMapClick(@NonNull LatLng point) {
                                                        if (originCoord != null && destinationCoord != null) {
                                                            fromDirectionPoint=null;
                                                            toDirectionPoint=null;
                                                            mapboxMap.removeMarker(destinationMarker);
                                                            mapboxMap.removeMarker(originMarker);
                                                            originCoord = null;
                                                            destinationCoord = null;
                                                        } else if (originCoord == null) {
                                                            originCoord = point;
                                                            fromDirectionPoint=mapwizePlugin.getPlaceForId("5aba5b7585f18700132073eb");
                                                            Log.d("AAAAAAA","INICIO "+originCoord.getLatitude()+" "+originCoord.getLongitude());
                                                        } else if (destinationCoord == null) {
                                                            destinationCoord = point;
                                                            toDirectionPoint=mapwizePlugin.getPlaceForId("5aba5b43975fc800138eeef8");
                                                            Log.d("AAAAAAA","FIN "+destinationCoord.getLatitude()+" "+destinationCoord.getLongitude());
                                                        }
                                                        if (originCoord != null && destinationCoord != null) {
                                                            Log.d("AAAAAAA","CALCULO "+destinationCoord.getLatitude()+" "+destinationCoord.getLongitude());
                                                            originMarker = mapboxMap.addMarker(new MarkerOptions().position(originCoord));
                                                            destinationMarker = mapboxMap.addMarker(new MarkerOptions()
                                                                    .position(destinationCoord)
                                                            );
                                                           List<String>route= getRoute(originCoord.getLongitude()+","+originCoord.getLatitude(),destinationCoord.getLongitude()+","+destinationCoord.getLatitude());
                                                           Log.d("AAAAAAA",originCoord.getLongitude()+","+originCoord.getLatitude()+" "+destinationCoord.getLongitude()+","+destinationCoord.getLatitude()+" "+route.size()+"");
                                                           for(String r:route)
                                                               Log.d("AAAAAAA",r);
                                                           tryToStartDirection(route);


                                                        }

                                                    }
                                                });
                mapwizePlugin = new MapwizePlugin(mapView, mapboxMap, opts);
                mapwizePlugin.setPreferredLanguage(Locale.getDefault().getLanguage());
                mapwizePlugin.setTopPadding((int)convertDpToPixel(TOP_PADDING,MapActivity.this));
                mapwizePlugin.setFloor(-1.0);
                initInterfaceComponents();
                initMapwizePluginListeners();
                mapwizePlugin.setFloor(7.0);
                requestLocationPermission();
                setupSearchEditTexts();

            }
        });
    }

    private void openSearchTable(SearchMode mode) {

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) {
            return;
        }
        searchSuggestionsListLayout = (RelativeLayout) inflater.inflate(R.layout.search_suggestions_list_layout, searchSuggestionsListLayout);

        RelativeLayout.LayoutParams params = null;

        if (mode == SearchMode.DEFAULT) {
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = (int)convertDpToPixel(4,this);
            params.leftMargin = (int)convertDpToPixel(4,this);
            searchBarLayout.addView(searchSuggestionsListLayout, params);
        }

        if (mode == SearchMode.FROM_DIRECTION || mode == SearchMode.TO_DIRECTION) {
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = (int)convertDpToPixel(12,this);
            params.leftMargin = (int)convertDpToPixel(12,this);
            params.addRule(RelativeLayout.BELOW, directionHeaderMainLayout.getId());
            if (searchSuggestionsListLayout.getParent() == null) {
                mapRelativeLayout.addView(searchSuggestionsListLayout, params);
            }
        }

        if (params != null) {
            searchResultsListAdapter = new SearchResultsListAdapter(this);
            searchResultsListAdapter.setListener(this);
            searchResultsListAdapter.setLanguage(mapwizePlugin.getLanguage());
            searchSuggestionsList = findViewById(R.id.search_suggestions_list);
            searchSuggestionsList.setLayoutManager(new LinearLayoutManager(this));
            searchSuggestionsList.setAdapter(searchResultsListAdapter);
            setupSearchResult("", mode);
            backedView.setVisibility(View.VISIBLE);
        }



    }

    private void setupLeftActionButton(SearchMode mode) {
        Log.d("MY TAG",8+"");

        if (mode == SearchMode.NONE) {
            leftActionButton.setImageResource(R.drawable.ic_dehaze_black_24dp);
            leftActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawer.openDrawer(GravityCompat.START);
                }
            });
        }
        else {
            leftActionButton.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            leftActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeSearch(null);
                }
            });
        }
    }

    @Override
    public void onItemSelected(Object item) {
        closeSearch(item);
    }

    private void closeSearch(Object object) {

        if (object != null) {
            if (currentSearchMode == SearchMode.DEFAULT) {
                if (object instanceof Venue) {
                    Venue venue = (Venue) object;
                    mapwizePlugin.centerOnVenue(venue);

                }
                if (object instanceof Place) {
                    Place place = (Place) object;
                    mapwizePlugin.centerOnPlace(place);
                    selectContent(place);
                }
                if (object instanceof PlaceList) {
                    PlaceList placeList = (PlaceList) object;
                    selectContent(placeList);
                }
                mapwizePlugin.setFollowUserMode(FollowUserMode.FOLLOW_USER);
            }
            if (currentSearchMode == SearchMode.FROM_DIRECTION) {
                if (object instanceof String) {
                    IndoorLocation userLocation = mapwizePlugin.getUserPosition();
                    fromDirectionPoint = new LatLngFloor(userLocation.getLatitude(), userLocation.getLongitude(), userLocation.getFloor());
                    directionHeaderFromTextView.setText(R.string.current_position);
                }
                if (object instanceof Place) {
                    Place place = (Place) object;
                    directionHeaderFromTextView.setText(place.getTranslation(mapwizePlugin.getLanguage()).getTitle());
                    fromDirectionPoint = place;
                }
                tryToStartDirection(null);
            }
            if (currentSearchMode == SearchMode.TO_DIRECTION) {
                if (object instanceof Place) {
                    Place place = (Place) object;
                    directionHeaderToTextView.setText(place.getTranslation(mapwizePlugin.getLanguage()).getTitle());
                    toDirectionPoint = place;
                }
                if (object instanceof PlaceList) {
                    PlaceList placeList = (PlaceList) object;
                    directionHeaderToTextView.setText(placeList.getTranslation(mapwizePlugin.getLanguage()).getTitle());
                    toDirectionPoint = placeList;
                }
                tryToStartDirection(null);
            }
        }

        mapRelativeLayout.removeView(searchSuggestionsListLayout);
        searchBarLayout.removeView(searchSuggestionsListLayout);
        searchSuggestionsListLayout = null;
        searchSuggestionsList = null;
        currentSearchMode = SearchMode.NONE;
        searchBarEditText.setText("");
        searchBarEditText.clearFocus();
        directionHeaderFromTextView.clearFocus();
        directionHeaderToTextView.clearFocus();
        if (object == null) {
            directionHeaderFromTextView.setText("");
            directionHeaderToTextView.setText("");
        }
        backedView.setVisibility(View.GONE);
        hideSoftKeyboard(MapActivity.this);


    }

    private void initInterfaceComponents() {
        Log.d("MY TAG",9+"");
        navigationView.setNavigationItemSelectedListener(this);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) {
            return;
        }
        RelativeLayout bottomButtonsLayout = (RelativeLayout) inflater.inflate(R.layout.bottom_buttons_layout, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.LEFT_OF, mapwizePlugin.followUserModeButton.getId());
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        int rightMargin = (int) getResources().getDimension(io.mapwize.mapwizeformapbox.R.dimen.followUserModeButtonSize);
        rightMargin += (int) getResources().getDimension(io.mapwize.mapwizeformapbox.R.dimen.marginToScreen);
        params.setMargins(0, 0, rightMargin, 0);
        mapwizePlugin.controllerLayout.addView(bottomButtonsLayout, params);

        languagesButton = findViewById(R.id.languages_button);
        languagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupLanguagesUI();
            }
        });

        universesButton = findViewById(R.id.universes_button);
        universesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupUniversesUI();
            }
        });

        directionButton = findViewById(R.id.direction_button);
        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupSearchDirectionUI();
                tryToStartDirection(null);
            }
        });

        directionHeaderSwapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapDirection();
            }
        });
        directionHeaderAccessibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAccessible = !isAccessible;
                if (isAccessible) {
                    directionHeaderAccessibilityButton.setBackground(getDrawable(R.drawable.rounded_button));
                    directionHeaderAccessibilityButton.setColorFilter(Color.parseColor("#C51586"));
                } else {
                    directionHeaderAccessibilityButton.setBackgroundColor(Color.parseColor("#C51586"));
                    directionHeaderAccessibilityButton.setColorFilter(Color.parseColor("#ffffff"));
                }
                tryToStartDirection(null);
            }
        });
        directionHeaderBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentSearchMode != SearchMode.NONE) {
                    closeSearch(null);
                }
                else {
                    mapwizePlugin.setDirection(null);
                    directionByVenue = new HashMap<>();
                    setupInVenueUI(mapwizePlugin.getVenue());
                }
            }
        });
        leftActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        bottomInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        backedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSearch(null);
            }
        });

        creditsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCredits();
            }
        });

        qrcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, ScannerActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setPeekHeight((int)convertDpToPixel(BOTTOM_PADDING, this));
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);



        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    unselectContent();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        infoBottomMoreImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
    }

    private void initMapwizePluginListeners() {
        Log.d("MY TAG",9+"");


        mapwizePlugin.setOnMapClickListener(new MapwizePlugin.OnMapClickListener() {
            @Override
            public void onMapClick(LatLngFloor latLngFloor) {
                if (!inDirectionMode && selectedContent != null) {
                    unselectContent();
                }
            }
        });

        mapwizePlugin.setOnPlaceClickListener(new MapwizePlugin.OnPlaceClickListener() {
            @Override
            public boolean onPlaceClick(Place place) {
                if (!inDirectionMode) {
                    selectContent(place);
                }
                return true;
            }
        });

        mapwizePlugin.setOnVenueClickListener(new MapwizePlugin.OnVenueClickListener() {
            @Override
            public boolean onVenueClick(Venue venue) {
                mapwizePlugin.centerOnVenue(venue);
                return false;

            }
        });

        mapwizePlugin.setOnVenueEnterListener(new MapwizePlugin.OnVenueEnterListener() {
            @Override
            public void onVenueEnter(Venue venue) {

                List<String> lang = new ArrayList<>();
                lang.add("es");

                RealmList<Universe> univs = new RealmList<>();
                univs.add(new Universe("5a93787a0fd168001331d52e", "Default universe"));

                RealmList<Translation> trns = new RealmList<>();
                trns.add(new Translation("5ab0172618b488002742baf7", "Andes", "", "", "es"));

                SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
                format.setTimeZone(TimeZone.getTimeZone("GMT-05:00"));
                Log.d("VENUE", venue.toString());
                Venue v = null;
                try {
                    v = new Venue("5ab0172618b488002742baf6", "ML", "andes", "es", lang, "https://mapwizecdn2.azureedge.net/sdk/mapwize.js/images/venue-50.png",
                            new LatLngFloor(4.602767998132922, -74.06478032469751, 7.0), "com.mapbox.services.commons.geojson.Polygon@e5f4d34",
               /*var 9 ->*/ univs, trns, true, false, false, format.parse("Tue Mar 27 22:00:22 GMT-05:00 2018"));
                } catch (java.text.ParseException e) {
                    Log.d("ERRORVENUE", e.getMessage());
                    e.printStackTrace();
                }

                Log.d("VENUE", venue.toString());

                Log.d("VENUECOPIAO", v.toString());

                Log.d("VENUEEQ", String.valueOf(v.equals(venue)));

                if (directionByVenue.get(venue.getId()) != null) {
                    FullDirectionObject o = directionByVenue.get(venue.getId());
                    setupSearchDirectionUI();
                    startDirection(o.direction, false,null,null);
                }
                else {
                    setupInVenueUI(venue);
                    mapwizePlugin.setDirection(null);
                    directionByVenue = new HashMap<>();
                }
            }

            @Override
            public void willEnterInVenue(Venue venue) {
                setupEnteringInVenue(venue);
            }
        });

        mapwizePlugin.setOnVenueExitListener(new MapwizePlugin.OnVenueExitListener() {
            @Override
            public void onVenueExit(Venue venue) {
                setupInVenueUI(null);
            }
        });


    }

    private void setupLanguagesUI() {
        if (mapwizePlugin.getVenue() != null) {
            List<String> languages = mapwizePlugin.getVenue().getSupportedLanguages();
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
            builderSingle.setIcon(R.drawable.ic_language_black_24dp);
            builderSingle.setTitle(R.string.language_selection);
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice);
            int i = 0;
            int selected = 0;
            for (String language : languages) {
                arrayAdapter.add(language.toUpperCase());
                if (language.equals(mapwizePlugin.getLanguage())) {
                    selected = i;
                }
                i++;
            }

            builderSingle.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builderSingle.setSingleChoiceItems(arrayAdapter, selected, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName = arrayAdapter.getItem(which);
                    if (strName != null) {
                        mapwizePlugin.setLanguageForVenue(strName.toLowerCase(), mapwizePlugin.getVenue());
                    }
                    dialog.dismiss();
                }
            });
            builderSingle.show();
        }
    }

    private void setupUniversesUI() {
        if (mapwizePlugin.getVenue() != null) {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(MapActivity.this);
            builderSingle.setIcon(R.drawable.ic_apps_black_24dp);
            builderSingle.setTitle(R.string.universe_selection);
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MapActivity.this, android.R.layout.select_dialog_singlechoice);
            int i = 0;
            int selected = 0;
            for (Universe universe : mapwizePlugin.getVenue().getUniverses()) {
                arrayAdapter.add(universe.getName());
                if (universe.equals(mapwizePlugin.getUniverseForVenue(mapwizePlugin.getVenue()))) {
                    selected = i;
                }
                i++;
            }

            builderSingle.setSingleChoiceItems(arrayAdapter, selected, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    String strName = arrayAdapter.getItem(which);
                    for (Universe u : mapwizePlugin.getVenue().getUniverses()) {
                        if (u.getName().equals(strName)) {
                            mapwizePlugin.setUniverseForVenue(u, mapwizePlugin.getVenue());
                        }
                    }
                    dialogInterface.dismiss();
                }
            });

            builderSingle.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builderSingle.show();
        }
    }

    private void setupEnteringInVenue(Venue venue) {
        String formatString = getResources().getString(R.string.entering_in_venue);
        searchBarEditText.setHint(String.format(formatString, venue.getTranslation(mapwizePlugin.getLanguage()).getTitle()));
        progressBar.setVisibility(View.VISIBLE);
        leftActionButton.setVisibility(View.GONE);
    }

    private void setupInVenueUI(Venue venue) {
        if (venue == null) {
            unselectContent();
        }
        else if (shouldBeSelected != null) {
            if (shouldBeSelected.getClass().equals(Place.class)) {
                Place p = (Place) shouldBeSelected;
                if (p.getVenueId().equals(venue.getId())) {
                    selectContent(shouldBeSelected);
                    shouldBeSelected = null;
                }
            }
        }
        progressBar.setVisibility(View.GONE);
        leftActionButton.setVisibility(View.VISIBLE);
        mCurrentVenue = venue;
        mapwizePlugin.setTopPadding((int)convertDpToPixel(TOP_PADDING,MapActivity.this));
        directionHeaderMainLayout.setVisibility(View.GONE);
        directionBottomInfoLayout.setVisibility(View.GONE);
        searchBarLayout.setVisibility(View.VISIBLE);
        if (selectedContent == null) {
            mapwizePlugin.setBottomPadding(0);
        }
        inDirectionMode = false;
        for (Place p : promotedPlaces) {
            mapwizePlugin.removePromotedPlace(p);
        }
        promotedPlaces.clear();

        if (venue != null) {
            if (venue.getSupportedLanguages().size() > 1) {
                languagesButton.setVisibility(View.VISIBLE);
            }
            if (venue.getUniverses().size() > 1) {
                universesButton.setVisibility(View.VISIBLE);
            }
            directionButton.setVisibility(View.VISIBLE);
            String formatString = getResources().getString(R.string.search_in_venue);
            searchBarEditText.setHint(String.format(formatString, venue.getTranslation(mapwizePlugin.getLanguage()).getTitle()));

        } else {
            directionButton.setVisibility(View.GONE);
            languagesButton.setVisibility(View.GONE);
            universesButton.setVisibility(View.GONE);
            searchBarEditText.setHint(getResources().getString(R.string.search_venue));
        }
    }

    private void swapDirection() {
        Log.d("MY TAG",9+"");

        DirectionPoint tmp = fromDirectionPoint;
        fromDirectionPoint = toDirectionPoint;
        toDirectionPoint = tmp;

        CharSequence stmp = directionHeaderFromTextView.getText();
        directionHeaderFromTextView.setText(directionHeaderToTextView.getText());
        directionHeaderToTextView.setText(stmp);

        if (fromDirectionPoint == null) {
            directionHeaderFromTextView.setText(R.string.choose_from);
        }
        if (toDirectionPoint == null) {
            directionHeaderToTextView.setText(R.string.choose_to);
        }

        tryToStartDirection(null);
    }

    private void setupSearchDirectionUI() {
        Log.d("MY TAG",10+"");

        mapwizePlugin.setTopPadding((int)convertDpToPixel(TOP_PADDING_DIRECTION,MapActivity.this));
        inDirectionMode = true;
        searchBarLayout.setVisibility(View.GONE);
        directionHeaderMainLayout.setVisibility(View.VISIBLE);
        directionButton.setVisibility(View.GONE);
        languagesButton.setVisibility(View.GONE);
        universesButton.setVisibility(View.GONE);
        if (isAccessible) {
            directionHeaderAccessibilityButton.setColorFilter(Color.parseColor("#882E88"));
        } else {
            directionHeaderAccessibilityButton.setColorFilter(Color.parseColor("#ffffff"));
        }
        UiSettings settings = mapboxMap.getUiSettings();
        settings.setCompassMargins(settings.getCompassMarginLeft(), 300, settings.getCompassMarginRight(), settings.getCompassMarginBottom());

        if (mapwizePlugin.getUserPosition() != null && mapwizePlugin.getUserPosition().getFloor() != null) {
            fromDirectionPoint = new LatLngFloor(mapwizePlugin.getUserPosition().getLatitude(), mapwizePlugin.getUserPosition().getLongitude(), mapwizePlugin.getUserPosition().getFloor());
            directionHeaderFromTextView.setText(R.string.current_position);
        } else {
            fromDirectionPoint = null;
            directionHeaderFromTextView.setText("");
        }

        if (selectedContent != null) {
            if (selectedContent instanceof Place) {
                toDirectionPoint = (Place) selectedContent;
            }
            if (selectedContent instanceof PlaceList) {
                toDirectionPoint = (PlaceList) selectedContent;
            }
            directionHeaderToTextView.setText(selectedContent.getTranslation(mapwizePlugin.getLanguage()).getTitle());
        } else {
            toDirectionPoint = null;
            directionHeaderToTextView.setText("");
        }
    }

    private void startDirection( Direction direction, boolean fitBounds,LatLngBounds latLngBounds,List<String> route) {

        if (fitBounds) {
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            for (Route r : direction.getRoutes()) {
                boundsBuilder.include(r.getBounds().getNorthEast())
                        .include(r.getBounds().getSouthWest());
            }
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 10, 400, 10, 300);
            mapboxMap.easeCamera(cu);
        }
        if(route!=null)
        {
            testApproximationRoute(route,latLngBounds);
        }
        else if(!isExternalTest && testingRoutes)
        {
            testInternalRoute(latLngBounds);
            isExternalTest=true;
        }
        else if (isExternalTest & testingRoutes)
        {
            testExternalRoute(latLngBounds);
            testingRoutes=false;
        }

       /*
        unselectContent();
        Log.d("RUTA2",direction.toString());
        direction=new Direction(direction.getFrom(),direction.getTo(),direction.getDistance(),direction.getTraveltime(),direction.getRoutes(),null,null,null);
        mapwizePlugin.setDirection(direction);
        Log.d("RUTA2",direction.toString());

        if (fromPoint instanceof Place) {
            Place place = (Place)fromPoint;
            mapwizePlugin.addPromotedPlace(place);
            promotedPlaces.add(place);
        }
        if (toPoint instanceof Place) {
            Place place = (Place)toPoint;
            mapwizePlugin.addPromotedPlace(place);
            promotedPlaces.add(place);
        }

        if (fromPoint instanceof Place) {
            MapwizeObject o = (MapwizeObject) fromPoint;
            directionHeaderFromTextView.setText(o.getTranslation(mapwizePlugin.getLanguage()).getTitle());
        }
        else {
            directionHeaderFromTextView.setText(getResources().getString(R.string.coordinates));
        }
        if (toPoint instanceof Place || toPoint instanceof PlaceList) {
            MapwizeObject o = (MapwizeObject) toPoint;
            directionHeaderToTextView.setText(o.getTranslation(mapwizePlugin.getLanguage()).getTitle());
        }
        else {
            directionHeaderToTextView.setText(getResources().getString(R.string.coordinates));
        }

        directionBottomInfoLayout.setVisibility(View.VISIBLE);
        directionBottomInfoAccessibleImage.setImageResource(!isAccessible ? R.drawable.ic_directions_walk_black_24dp : R.drawable.ic_accessible_black_24dp);
        long time = Math.round(direction.getTraveltime() / 60);
        String timPlaceHolder = getString(R.string.dir_min);
        directionTime.setText(String.format(timPlaceHolder,time));
        directionDistance.setText(UnitLocale.distanceAsString(direction.getDistance()));
        mapwizePlugin.setBottomPadding(Math.round(convertDpToPixel(BOTTOM_PADDING, this)));
        directionButton.setVisibility(View.GONE);*/
    }



    private void tryToStartDirection(final List<String> route) {
        if (fromDirectionPoint != null && toDirectionPoint != null) {
            Api.getDirection(fromDirectionPoint, toDirectionPoint, isAccessible, new ApiCallback<Direction>() {
                @Override
                public void onSuccess(final Direction object) {
                    Log.d("RUTA3",object.toString());
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            int i=0;
                            if (mCurrentVenue != null) {
                                directionByVenue.put(mCurrentVenue.getId(), new FullDirectionObject(object, null, null));
                                Log.d("RUTA1",mCurrentVenue.getId()+" "+object.toString()+" "+fromDirectionPoint.toString()+" "+toDirectionPoint.toString());
                            }
                            Log.d("AAAAAAA","Intento iniciar ruta "+route);
                            if(route!=null)
                                startDirection( object, true,object.getBounds(),route);
                            else
                                startDirection( object, true,object.getBounds(),null);
                        }
                    };
                    uiHandler.post(runnable);

                }

                @Override
                public void onFailure(Throwable t) {
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MapActivity.this, getResources().getString(R.string.direction_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    };
                    uiHandler.post(runnable);
                }
            });
        }
    }

    private List<Marker> contentSelectionMarkers = new ArrayList<>();
    private MapwizeObject selectedContent;
    private void selectContent(MapwizeObject content) {
        Log.d("MY TAG","Entra "+selectedContent+" "+content);


        if (selectedContent != null) {
            if (selectedContent instanceof Place) {
                Log.d("MY TAG","VA "+selectedContent+" "+content);
                mapwizePlugin.removePromotedPlace(((Place) selectedContent));

            }
            selectedContent = null;
            for (Marker m : contentSelectionMarkers) {
                mapwizePlugin.removeMarker(m);
            }
            contentSelectionMarkers.clear();
            mapwizePlugin.removePromotedPlaces(promotedPlaces);
            promotedPlaces.clear();
            //Código para probar donde se colocan marcadores
            //contentSelectionMarkers.add(mapwizePlugin.addMarker(new LatLngFloor(-74.064775,4.60255309,7.0)));

            //Marker m =mapwizePlugin.addMarker(new LatLngFloor(4.602553088830576,-74.06477523042022,7.0));
        }
        Translation tr = content.getTranslation(mapwizePlugin.getLanguage());
        if (tr.getTitle().equals("")) {
            mainText.setVisibility(View.GONE);
        } else {
            mainText.setVisibility(View.VISIBLE);
            mainText.setText(tr.getTitle());
        }
        if (tr.getSubtitle().equals("")) {
            secondText.setVisibility(View.GONE);
        } else {
            secondText.setVisibility(View.VISIBLE);
            secondText.setText(tr.getSubtitle());
        }
        if (content instanceof Place) {
            Place place = (Place) content;
            contentSelectionMarkers.add(mapwizePlugin.addMarker(place));
            mapwizePlugin.addPromotedPlace(place);
            if (place.getFloor() == null) {
                thirdText.setVisibility(View.GONE);
            } else {
                thirdText.setVisibility(View.GONE);
                NumberFormat nf = new DecimalFormat("###.###");
                String floorPlaceHolder = getResources().getString(R.string.floor_placeholder);
                thirdText.setText(String.format(floorPlaceHolder, nf.format(place.getFloor())));
            }

        } else {
            thirdText.setVisibility(View.GONE);
            if (content instanceof PlaceList) {
                final PlaceList placeList = (PlaceList) content;
                contentSelectionMarkers.addAll(mapwizePlugin.addMarkers(placeList));
                for (String placeId : placeList.getPlaceIds()) {
                    Api.getPlace(placeId, new ApiCallback<Place>() {
                        @Override
                        public void onSuccess(Place object) {
                            mapwizePlugin.addPromotedPlace(object);
                            promotedPlaces.add(object);

                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                }

            }
        }
        if (content.getIcon() != null && content.getIcon().length()>0) {
            Picasso.with(getApplicationContext()).load(content.getIcon()).into(infoIcon);
        }
        bottomInfoLayout.setVisibility(View.VISIBLE);
        selectedContent = content;
        mapwizePlugin.setBottomPadding(Math.round(convertDpToPixel(BOTTOM_PADDING, this)));

        if (tr.getDetails() != null && tr.getDetails().length() > 0) {
            ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
            layoutParams.height = mapView.getHeight() - ((int)convertDpToPixel(23, this));
            bottomSheet.setLayoutParams(layoutParams);
            placeDetailsWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            placeDetailsWebView.loadData(tr.getDetails(),"text/html; charset=utf-8", "UTF-8");
            infoBottomMoreImageView.setVisibility(View.VISIBLE);
        }
        else {
            ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
            layoutParams.height = Math.round(convertDpToPixel(BOTTOM_PADDING, this));
            bottomSheet.setLayoutParams(layoutParams);
            infoBottomMoreImageView.setVisibility(View.GONE);
        }

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void unselectContent() {
        if (selectedContent != null) {
            if (selectedContent instanceof Place) {
                mapwizePlugin.removePromotedPlace(((Place) selectedContent));
            }
            selectedContent = null;
            for (Marker m : contentSelectionMarkers) {
                mapwizePlugin.removeMarker(m);
            }
            contentSelectionMarkers.clear();
            mapwizePlugin.removePromotedPlaces(promotedPlaces);
            promotedPlaces.clear();
            mapwizePlugin.setBottomPadding(0);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    private void showCredits() {
        Log.d("MY TAG",11+"");

        drawer.closeDrawer(GravityCompat.START);
        WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });
        webView.loadUrl("file:///android_asset/credits.html");
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Credits");
        alert.setView(webView);
        alert.setNegativeButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void handleAccessKey(String result) {
        Api.getAccess(result, new ApiCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean object) {
                mapwizePlugin.refresh(new MapwizePlugin.OnAsyncTaskReady() {
                    @Override
                    public void ready() {

                        Handler uiHandler = new Handler(Looper.getMainLooper());
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {

                                mCurrentVenue = mapwizePlugin.getVenue();
                                if (mCurrentVenue != null && mCurrentVenue.getUniverses().size() > 1) {
                                    universesButton.setVisibility(View.VISIBLE);
                                }

                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapActivity.this);
                                alertDialog.setTitle(R.string.access_key);
                                alertDialog.setMessage(R.string.access_granted);
                                alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alertDialog.show();
                            }
                        };
                        uiHandler.post(runnable);

                    }
                });



            }

            @Override
            public void onFailure(Throwable t) {
                Handler uiHandler = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapActivity.this);
                        alertDialog.setTitle(R.string.access_key);
                        alertDialog.setMessage(R.string.access_invalid);
                        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    }
                };
                uiHandler.post(runnable);
            }
        });

        // TODO recheck if universe button should be shown


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d("MY TAG",12+"");

        int id = item.getItemId();
        if (id == R.id.access_key) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapActivity.this);
            alertDialog.setTitle(R.string.access_key);
            alertDialog.setMessage(R.string.access_key_enter);
            final EditText input = new EditText(MapActivity.this);
            input.setMaxLines(1);
            input.setSingleLine(true);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            alertDialog.setView(input);
            alertDialog.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int which) {
                            handleAccessKey(input.getText().toString());
                        }
                    });

            alertDialog.setNegativeButton(R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });


            final AlertDialog d = alertDialog.create();
            Window window = d.getWindow();
            WindowManager.LayoutParams wlp;
            if (window != null) {
                wlp = window.getAttributes();
            }
            else {
                return false;
            }
            wlp.gravity = Gravity.TOP;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);
            input.setOnEditorActionListener(new TextView.OnEditorActionListener()
            {
                public boolean onEditorAction(TextView v, int keyCode, KeyEvent event)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_ENDCALL:
                            handleAccessKey(input.getText().toString());
                            d.cancel();
                            return true;
                        default:
                            break;
                    }
                    return false;
                }
            });
            window.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            d.show();
        }

        if (id == R.id.contact_us) {
            Intent emailIntent=new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{  "support@mapwize.io"});
            emailIntent.setType("message/rfc822");
            try {
                startActivity(Intent.createChooser(emailIntent,
                        "Send email using..."));
            } catch (android.content.ActivityNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        if (id == R.id.about_mapwize) {
            String url = "https://www.mapwize.io";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        Log.d("MY TAG",13+"");

        super.onStart();
        if (mapView != null) {
            mapView.onStart();
        }
    }

    @Override
    public void onResume() {
        Log.d("MY TAG",14+"");

        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
        if (mapwizePlugin != null) {
            mapwizePlugin.onResume();
        }
    }

    @Override
    public void onPause() {
        Log.d("MY TAG",15+"");

        super.onPause();
        mapView.onPause();
        if (mapwizePlugin != null) {
            mapwizePlugin.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
        } else {
            setupLocationProvider();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupLocationProvider();
                }
            }
        }
    }
    
    private BigDecimal getDistance(String a, String b) {
    	String[] arrA = a.split(",");
    	String[] arrB = b.split(",");
    	BigDecimal x = new BigDecimal(arrA[0]).subtract(new BigDecimal(arrB[0]));
    	BigDecimal y = new BigDecimal(arrA[1]).subtract(new BigDecimal(arrB[1]));
    	return x.multiply(x).add(y.multiply(y));
    }
    
    public List<String> getRoute(String from, String to) {
    	int minA = 0, minB = 0;
    	BigDecimal dMinA = BigDecimal.TEN, dMinB = BigDecimal.TEN;
    	for(int i = 0; i < nodos.size(); i++) {
    		BigDecimal dA = getDistance(nodos.get(i).coordenadas, from);
    		BigDecimal dB = getDistance(nodos.get(i).coordenadas, to);

    		if(dA.compareTo(dMinA) < 0) {
    			minA = i;
    			dMinA = dA;
    		}
    		if(dB.compareTo(dMinB) < 0) {
    			minB = i;
    			dMinB = dB;
    		}
    	}
    	List<String> ans = new ArrayList<>();
    	ans.add(from);
    	ans.addAll(getRoute(minA,minB));
    	ans.add(to);
    	return ans;
    }



    public List<String> getRoute(int from, int to) {
        Log.d("TAG2","Entra");
        double[] dist = new double[NUM_NODOS];
        boolean[] visitado = new boolean[NUM_NODOS];
        Arco[] padre = new Arco[NUM_NODOS];

        for(int i = 0; i<NUM_NODOS; i++) {
            dist[i] = Double.POSITIVE_INFINITY;
            padre[i] = null;
        }
        dist[from] = 0;
        NodoCola nc = new NodoCola();
        nc.nodo = from;
        nc.peso = 0;

        PriorityQueue<NodoCola> q = new PriorityQueue<>();
        q.add(nc);
        while(!q.isEmpty()) {
            NodoCola nc1 = q.poll();
            int nodoActual = nc1.nodo;
            if(visitado[nodoActual]) {
                continue;
            }
            visitado[nodoActual] = true;

            //terminacion
            if(visitado[to]) {
                break;
            }

            for(Arco arco : adj.get(nodoActual)) {
                int w = arco.origen;
                if(w == nodoActual) {
                    w = arco.destino;
                }

                if(dist[nodoActual]+arco.length<dist[w]) {
                    dist[w] = dist[nodoActual]+arco.length;
                    padre[w] = arco;
                    NodoCola nc2 = new NodoCola();
                    nc2.nodo = w;
                    nc2.peso = dist[w];
                    q.add(nc2);
                }
            }
        }
        //reconstruccion de la ruta
        List<String> ruta = new ArrayList<>();
        if(padre[to] != null) {
            ruta.add(nodos.get(to).coordenadas);
            Arco padreActual = padre[to];
            int nodoActual = to;

            while(padreActual!=null) {
                String[] camino = padreActual.camino;
                int dest = -1;
                if(nodoActual == padreActual.origen) {
                    dest = padreActual.destino;
                    for(int i = 1; i<camino.length; i++) {
                        ruta.add(0, camino[i]);
                    }
                }
                else {
                    dest = padreActual.origen;
                    for(int i = camino.length-2; i>=0; i--) {
                        ruta.add(0, camino[i]);
                    }
                }

                padreActual = padre[dest];
                nodoActual = dest;
            }
        }
        return ruta;
    }

    public Route toRoute(List<String> route,LatLngBounds latLngBounds) {
        List<LatLng> list = new ArrayList<>(route.size());
        Log.d("TAG2",route.size()+"");
        for(String s : route) {
            Log.d("TAG2",s);
            String[] sLatLng = s.split(",");
            LatLng latLng = new LatLng(Double.parseDouble(sLatLng[1]),Double.parseDouble(sLatLng[0]));
            list.add(latLng);
        }
        return new Route(7.0,7.0,7.0,false,false,0.0,0.0,latLngBounds,0.0,"","",list);
    }

    public void showRoute(final int from, final int to, final LatLngBounds latLngBounds) {


       /*
       List<LatLng> lat=new ArrayList<>();
                lat.add(new LatLng(4.602819464288424,-74.06492784619331,0.0));
                lat.add(new LatLng(4.602817934672893,-74.06492566238988, 0.0));
                lat.add(new LatLng(4.602840518562904, -74.06490974128248, 0.0));
                lat.add(new LatLng(4.602822806236791, -74.06488157808782, 0.0));
                lat.add(new LatLng(4.602888823577639, -74.06483093089558, 0.0));
                lat.add(new LatLng(4.602891650369796, -74.06483463943006, 0.0));

                List<LatLngBounds>b=new ArrayList<>();
                Log.d("TAG2","A");
                Route r=new Route(0.0,Double.NaN,Double.NaN,true,true,17.0,13.0,latLngBounds,17.0,"","",lat);
                List<Route> list = new ArrayList<>();
                list.add(r);
                LatLng llFrom = r.getPath().get(0);
                LatLng llTo = r.getPath().get(r.getPath().size()-1);
                Log.d("TAG2","B");


                DirectionPointWrapper dpwFrom = new DirectionPointWrapper(llFrom.getLatitude(), llFrom.getLongitude(), 0.0, mCurrentVenue.getId(), "5ab05de59dd371003b82185a", "");
                Log.d("TAG2","B");

                DirectionPointWrapper dpwTo = new DirectionPointWrapper(llTo.getLatitude(), llTo.getLongitude(), 0.0,  mCurrentVenue.getId(), "5ab05dc966264600137b2d70", "");
                Log.d("TAG2","B");

                Direction dir = new Direction(dpwFrom, dpwTo, 17.0, 13.0, list, null, null, null);
                Log.d("TAG2","B");
                int i=0;
        */


        Log.d("TAG2","Pone "+from+" "+to+" "+latLngBounds);
        Handler uiHandler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("TAG2","A");
                Route r = toRoute(getRoute(from, to),latLngBounds);
                List<Route> list = new ArrayList<>();
                list.add(r);
                LatLng llFrom = r.getPath().get(0);
                LatLng llTo = r.getPath().get(r.getPath().size()-1);
                Log.d("TAG2","B");

                DirectionPointWrapper dpwFrom = new DirectionPointWrapper(llFrom.getLatitude(), llFrom.getLongitude(), 0.0, mCurrentVenue.getId(), "5ab05de59dd371003b82185a", "");
                Log.d("TAG2","B");

                DirectionPointWrapper dpwTo = new DirectionPointWrapper(llTo.getLatitude(), llTo.getLongitude(), 0.0,  mCurrentVenue.getId(), "5ab05dc966264600137b2d70", "");
                Log.d("TAG2","B");

                Direction dir = new Direction(dpwFrom, dpwTo, 0.0, 0.0, list, latLngBounds, null, null);
                Log.d("TAG2","B "+latLngBounds+" "+dir.getBounds());

                Log.d("TAG2","Pone");

                if (mCurrentVenue != null) {
                    directionByVenue.put(mCurrentVenue.getId(), new FullDirectionObject(dir, null, null));
                    Log.d("RUTA1",mCurrentVenue.getId()+" "+dir.toString()+" "+fromDirectionPoint.toString()+" "+toDirectionPoint.toString());
                }
                unselectContent();
                mapwizePlugin.removeMarkers();
                mapwizePlugin.addMarker(new LatLngFloor(llFrom.getLatitude(),llFrom.getLongitude(),llFrom.getAltitude()));
                mapwizePlugin.addMarker(new LatLngFloor(llTo.getLatitude(),llTo.getLongitude(),llTo.getAltitude()));
                mapwizePlugin.setDirection(dir);
            }
        };
        uiHandler.post(runnable);

    }

    public void showRoute(final String from, final String to, final LatLngBounds latLngBounds) {


       /*
       List<LatLng> lat=new ArrayList<>();
                lat.add(new LatLng(4.602819464288424,-74.06492784619331,0.0));
                lat.add(new LatLng(4.602817934672893,-74.06492566238988, 0.0));
                lat.add(new LatLng(4.602840518562904, -74.06490974128248, 0.0));
                lat.add(new LatLng(4.602822806236791, -74.06488157808782, 0.0));
                lat.add(new LatLng(4.602888823577639, -74.06483093089558, 0.0));
                lat.add(new LatLng(4.602891650369796, -74.06483463943006, 0.0));

                List<LatLngBounds>b=new ArrayList<>();
                Log.d("TAG2","A");
                Route r=new Route(0.0,Double.NaN,Double.NaN,true,true,17.0,13.0,latLngBounds,17.0,"","",lat);
                List<Route> list = new ArrayList<>();
                list.add(r);
                LatLng llFrom = r.getPath().get(0);
                LatLng llTo = r.getPath().get(r.getPath().size()-1);
                Log.d("TAG2","B");


                DirectionPointWrapper dpwFrom = new DirectionPointWrapper(llFrom.getLatitude(), llFrom.getLongitude(), 0.0, mCurrentVenue.getId(), "5ab05de59dd371003b82185a", "");
                Log.d("TAG2","B");

                DirectionPointWrapper dpwTo = new DirectionPointWrapper(llTo.getLatitude(), llTo.getLongitude(), 0.0,  mCurrentVenue.getId(), "5ab05dc966264600137b2d70", "");
                Log.d("TAG2","B");

                Direction dir = new Direction(dpwFrom, dpwTo, 17.0, 13.0, list, null, null, null);
                Log.d("TAG2","B");
                int i=0;
        */


        Log.d("TAG2","Pone "+from+" "+to+" "+latLngBounds);
        Handler uiHandler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("TAG2","A");
                Route r = toRoute(getRoute(from, to),latLngBounds);
                List<Route> list = new ArrayList<>();
                list.add(r);
                LatLng llFrom = r.getPath().get(0);
                LatLng llTo = r.getPath().get(r.getPath().size()-1);
                Log.d("TAG2","B");

                DirectionPointWrapper dpwFrom = new DirectionPointWrapper(llFrom.getLatitude(), llFrom.getLongitude(), 0.0, mCurrentVenue.getId(), "5ab05de59dd371003b82185a", "");
                Log.d("TAG2","B");

                DirectionPointWrapper dpwTo = new DirectionPointWrapper(llTo.getLatitude(), llTo.getLongitude(), 0.0,  mCurrentVenue.getId(), "5ab05dc966264600137b2d70", "");
                Log.d("TAG2","B");

                Direction dir = new Direction(dpwFrom, dpwTo, 0.0, 0.0, list, latLngBounds, null, null);
                Log.d("TAG2","B "+latLngBounds+" "+dir.getBounds());

                Log.d("TAG2","Pone");

                if (mCurrentVenue != null) {
                    directionByVenue.put(mCurrentVenue.getId(), new FullDirectionObject(dir, null, null));
                    Log.d("RUTA1",mCurrentVenue.getId()+" "+dir.toString()+" "+fromDirectionPoint.toString()+" "+toDirectionPoint.toString());
                }
                unselectContent();
                mapwizePlugin.removeMarkers();
                mapwizePlugin.addMarker(new LatLngFloor(llFrom.getLatitude(),llFrom.getLongitude(),llFrom.getAltitude()));
                mapwizePlugin.addMarker(new LatLngFloor(llTo.getLatitude(),llTo.getLongitude(),llTo.getAltitude()));
                mapwizePlugin.setDirection(dir);
            }
        };
        uiHandler.post(runnable);

    }

    private void testApproximationRoute(final List<String> route, final LatLngBounds latLngBounds) {
        Handler uiHandler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("TAG2","A");
                Route r = toRoute(route,latLngBounds);
                List<Route> list = new ArrayList<>();
                list.add(r);
                LatLng llFrom = r.getPath().get(0);
                LatLng llTo = r.getPath().get(r.getPath().size()-1);
                Log.d("TAG2","B");

                DirectionPointWrapper dpwFrom = new DirectionPointWrapper(llFrom.getLatitude(), llFrom.getLongitude(), 7.0, mCurrentVenue.getId(), "5ab05de59dd371003b82185a", "");
                Log.d("TAG2","B");

                DirectionPointWrapper dpwTo = new DirectionPointWrapper(llTo.getLatitude(), llTo.getLongitude(), 7.0,  mCurrentVenue.getId(), "5ab05dc966264600137b2d70", "");
                Log.d("TAG2","B");

                Direction dir = new Direction(dpwFrom, dpwTo, 0.0, 0.0, list, latLngBounds, null, null);
                Log.d("TAG2","B "+latLngBounds+" "+dir.getBounds());

                Log.d("TAG2","Pone");

                if (mCurrentVenue != null) {
                    directionByVenue.put(mCurrentVenue.getId(), new FullDirectionObject(dir, null, null));
                    Log.d("RUTA1",mCurrentVenue.getId()+" "+dir.toString()+" "+fromDirectionPoint.toString()+" "+toDirectionPoint.toString());
                }
                unselectContent();
                mapwizePlugin.removeMarkers();
                mapwizePlugin.addMarker(new LatLngFloor(llFrom.getLatitude(),llFrom.getLongitude(),llFrom.getAltitude()));
                mapwizePlugin.addMarker(new LatLngFloor(llTo.getLatitude(),llTo.getLongitude(),llTo.getAltitude()));
                mapwizePlugin.setDirection(dir);
            }
        };
        uiHandler.post(runnable);
    }

    public void testInternalRoute(LatLngBounds latLngBounds) {
        Log.d("TAG2","Entra");
        showRoute(84, 85,latLngBounds);

    }

    public void testExternalRoute(LatLngBounds latLngBounds) {
        showRoute(28, 190,latLngBounds);
    }

    private void setupLocationProvider() {
        Log.d("MY TAG",16+"");
        mapwizeLocationProvider = new MapwizeLocationProvider(MapActivity.this);
        mapwizePlugin.setLocationProvider(mapwizeLocationProvider);
        start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MY TAG",17+"");

        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("url");
                Api.getParsedUrlObject(result, new ApiCallback<ParsedUrlObject>() {
                    @Override
                    public void onSuccess(final ParsedUrlObject object) {
                        Handler uiHandler = new Handler(Looper.getMainLooper());
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                handleParsedUrl(object);
                            }
                        };
                        uiHandler.post(runnable);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (t != null) {
                            t.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    private void handleParsedUrl(final ParsedUrlObject parsedUrlObject) {
        Log.d("MY TAG",18+"");

        if (parsedUrlObject.getAccessKey() != null) {
            Api.getAccess(parsedUrlObject.getAccessKey(), new ApiCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean access) {
                    parsedUrlObject.setAccessKey(null);
                    mapwizePlugin.refresh(new MapwizePlugin.OnAsyncTaskReady() {
                        @Override
                        public void ready() {
                            Handler uiHandler = new Handler(Looper.getMainLooper());
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    handleParsedUrl(parsedUrlObject);
                                }
                            };
                            uiHandler.post(runnable);
                        }
                    });
                }

                @Override
                public void onFailure(Throwable t) {
                    if (t != null) {
                        t.printStackTrace();
                    }
                }
            });
            return;
        }

        if (parsedUrlObject.getIndoorLocation() != null) {
            mapwizeLocationProvider.defineLocation(parsedUrlObject.getIndoorLocation());
        }

        if (parsedUrlObject.getLanguage() != null) {
            mapwizePlugin.setPreferredLanguage(parsedUrlObject.getLanguage());
        }

        if (parsedUrlObject.getUniverse() != null) {
            mapwizePlugin.setUniverseForVenue(parsedUrlObject.getUniverse(), null);
        }

        Double zoom = parsedUrlObject.getZoom();
        LatLngBounds bounds = parsedUrlObject.getBounds();
        if (bounds.getSouthWest().equals(bounds.getNorthEast())) {
            CameraUpdate nextPosition = CameraUpdateFactory.newLatLngZoom(bounds.getSouthWest(), zoom==null?20:zoom);
            mapboxMap.easeCamera(nextPosition);
        }
        else {
            CameraUpdate nextPosition = CameraUpdateFactory.newLatLngBounds(bounds, 10);
            CameraPosition nextCameraPosition = nextPosition.getCameraPosition(this.mapboxMap);
            if (nextCameraPosition != null && nextCameraPosition.zoom < 16) {
                nextPosition = CameraUpdateFactory.newLatLngZoom(nextCameraPosition.target, 16);
            }
            if (nextCameraPosition != null && zoom != null) {
                nextPosition = CameraUpdateFactory.newLatLngZoom(nextCameraPosition.target, zoom);
            }
            mapboxMap.easeCamera(nextPosition);
        }
        if (parsedUrlObject.getFloor() != null) {
            mapwizePlugin.setFloor(parsedUrlObject.getFloor());
        }

        if (parsedUrlObject.getPlace() != null) {
            shouldBeSelected = parsedUrlObject.getPlace();
        }

        if (parsedUrlObject.getAccessible() != null) {
            isAccessible = parsedUrlObject.getAccessible();
        }

        if (parsedUrlObject.getDirection() != null) {
            FullDirectionObject o = new FullDirectionObject(parsedUrlObject.getDirection(), parsedUrlObject.getFrom(), parsedUrlObject.getTo());
            directionByVenue.put(parsedUrlObject.getVenue().getId(), o);
        }

    }

    enum SearchMode {
        NONE,
        DEFAULT,
        FROM_DIRECTION,
        TO_DIRECTION
    }

}
