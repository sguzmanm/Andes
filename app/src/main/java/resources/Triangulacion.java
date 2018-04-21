package resources;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Triangulacion {

    /**
     *
     SUP_IZQ = (Decimal('4.60324129935718'), Decimal('-74.06490464017361'))
     SUP_DER = (Decimal('4.602864946901448'), Decimal('-74.06431626817653'))
     INF_IZQ = (Decimal('4.602660916841884'), Decimal('-74.06529444934267'))
     HEIGHT = 3223
     WIDTH = 3168
     */
    private static final BigDecimal SUP_IZQ_LAT=new BigDecimal(4.60324129935718);
    private static final BigDecimal SUP_IZQ_LON=new BigDecimal(-74.06490464017361);
    private static final BigDecimal SUP_DER_LAT=new BigDecimal(4.602864946901448);
    private static final BigDecimal SUP_DER_LON=new BigDecimal(-74.06431626817653);
    private static final BigDecimal INF_IZQ_LAT=new BigDecimal(4.602660916841884);
    private static final BigDecimal INF_IZQ_LON=new BigDecimal(-74.06529444934267);
    private static final BigDecimal WIDTH=new BigDecimal(3223);
    private static final BigDecimal HEIGHT=new BigDecimal(3168);


    /**
     * def vectorDiference(a, b):
     difference = [0,0]
     difference[0] = a[0] - b[0]
     difference[1] = a[1] - b[1]
     return difference
     */
    static BigDecimal[] vectorDiference(BigDecimal[]a,BigDecimal[]b)
    {
        BigDecimal[] difference=new BigDecimal[]{a[0].subtract(b[0]),a[1].subtract(b[1])};
        return difference;
    }
    /*

     def vectorSum(a, b):
     _sum = [0,0]
     _sum[0] = a[0] + b[0]
     _sum[1] = a[0] + b[0]
     return _sum
     */

    BigDecimal[] vectorSum(BigDecimal[]a,BigDecimal[]b)
    {
        BigDecimal[] sum=new BigDecimal[]{a[0].add(b[0]),a[1].add(b[1])};
        return sum;
    }
    /*
     αβ = vectorDiference(SUP_DER, SUP_IZQ)
     θσ = vectorDiference(INF_IZQ, SUP_IZQ)

*/
    static BigDecimal[]alphaBeta=vectorDiference(new BigDecimal[]{SUP_DER_LAT,SUP_DER_LON},new BigDecimal[]{SUP_IZQ_LAT,SUP_IZQ_LON});
    static BigDecimal[]thetaSigma=vectorDiference(new BigDecimal[]{INF_IZQ_LAT,INF_IZQ_LON},new BigDecimal[]{SUP_IZQ_LAT,SUP_IZQ_LON});

    /*
     def transform(x, y):
     latLong = [0,0]
     latLong[0] = (αβ[0]*x/WIDTH + θσ[0]*y/HEIGHT) + SUP_IZQ[0]
     latLong[1] = (αβ[1]*x/WIDTH + θσ[1]*y/HEIGHT) + SUP_IZQ[1]

     return tuple(latLong)
     */
    public static BigDecimal[] transform(long x, long y)
    {
        return new BigDecimal[]{
                SUP_IZQ_LAT.add(alphaBeta[0].multiply(new BigDecimal(x)).divide(WIDTH,14, RoundingMode.HALF_UP).add(thetaSigma[0].multiply(new BigDecimal(y)).divide(HEIGHT,14, RoundingMode.HALF_UP))),
                SUP_IZQ_LON.add(alphaBeta[1].multiply(new BigDecimal(x)).divide(WIDTH,14, RoundingMode.HALF_UP).add(thetaSigma[1].multiply(new BigDecimal(y)).divide(HEIGHT,14, RoundingMode.HALF_UP)))
        };
    }
    /*

     def tranformInput():
     correctInput = []
     fid = 940
     for _input in INPUT:
     obj = {}
     latLong = transform(_input[0], _input[1])
     obj['lat'] = str(latLong[0])
     obj['long'] = str(latLong[1])
     obj['x'] = str(_input[0])
     obj['y'] = str(_input[1])
     obj['floor'] = 7
     obj['area'] = -1
     obj['bloque'] = 'ML'
     obj['FID'] = fid
     fid = fid + 1
     correctInput.append(obj)

     with open('nodos corregidos.json', mode='w') as file:
     file.write(json.dumps(correctInput))

     tranformInput()
     */
    public String resultadosAlgoritmo(double h1, double k1,double r1, double h2, double k2, double r2)
    {
        double A=0;
        double B=0;
        double C=0;
        double x1=0;
        double x2=0;
        double y1=0;
        double y2=0;

        double a=h2-h1;
        double b=(h1*h1-h2*h2)+(k1*k1-k2*k2)-(r1*r1-r2*r2);
        double c=k2-k1;
        System.out.println(a+" "+b+" "+c);

        if(a==0)
        {
            y1=-b/(2*c);
            y2=y1;
            System.out.println("Y:"+y2);

            A=1;
            B=-2*k1;
            C=k1*k1-r1*r1+Math.pow(y1-h1,2);
            System.out.println(A+" "+B+" "+C);
            double[] x=cuadratica(A,B,C);
            x1=x[0];
            x2=x[1];
        }
        else if (k1==k2)
        {
            x1=-b/(2*a);
            x2=x1;
            System.out.println("X:"+x2);

            A=1;
            B=-2*k1;
            C=k1*k1-r1*r1+Math.pow(x1-h1,2);
            System.out.println(A+" "+B+" "+C);
            double[] y=cuadratica(A,B,C);
            y1=y[0];
            y2=y[1];
        }
        else
        {

            A=4*(c*c+a*a);
            B= -8*h1*c*c+4*a*b+8*a*c*k1;
            C=4*h1*h1*c*c+b*b+4*k1*b*c+4*k1*k1*c*c-4*r1*r1*c*c;

            System.out.println("ABC"+A+" "+B+" "+C);
            System.out.println("CUAD"+B*B+" "+4*A*C);



            double[] x= cuadratica(A,B,C);
            x1=x[0];
            x2=x[1];
            y1=(-b-2*a*x[0])/(2*c);
            y2=(-b-2*a*x[1])/(2*c);
        }

        return x1+";"+y1+";"+x2+";"+y2;
    }

    public double[] cuadratica (double A, double B, double C)
    {
        double[]x=new double[2];
        x[0]=(-B-Math.sqrt(B*B-4*A*C))/(2*A);
        x[1]=(-B+Math.sqrt(B*B-4*A*C))/(2*A);
        return x;
    }

    public String ubicacion(double h1, double k1, double r1, double h2, double k2, double r2,double h3, double k3, double r3)
    {
        String x=resultadosAlgoritmo(h1,k1,r1,h2,k2,r2);
        String []datos=x.split(";");

        System.out.println(x+"\n");
        double rta1=Math.pow(Double.parseDouble(datos[0])-h3,2)+Math.pow(Double.parseDouble(datos[1])-k3,2);
        double rta2=Math.pow(Double.parseDouble(datos[2])-h3,2)+Math.pow(Double.parseDouble(datos[3])-k3,2);
        if(Math.abs(Math.pow(r3,2)-rta1)>Math.abs(Math.pow(r3,2)-rta2))
            return datos[2]+";"+datos[3];
        else
            return datos[0]+";"+datos[1];


    }

    public double dbmAMetros(int levelInDb, int freqInMHz)
    {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(levelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }

    public double[] transformPixelToLatLng(double lat0,double lon0,double latinf,double loninf,double latDer,double lonDer, double width, double height,double x, double y)
    {
        double []ac=new double[]{(lonDer-lon0)/width,(latDer-lat0)/width};
        double[] bd=new double[]{(loninf-lon0)/height,(latinf-lat0)/height};
        return new double[]{ac[1]*x+bd[1]*y+lat0,ac[0]*x+bd[0]*y+lon0};
    }

    public static void main (String[] args)
    {
        Triangulacion t = new Triangulacion();
        System.out.println(t.ubicacion(1620, 1276, 508,524,1277,715,1453,1927,405));

        System.out.println(t.ubicacion(2527,1935,12*39,
                2558, 2344, t.dbmAMetros(-53,2462)*39,2005,2344,t.dbmAMetros(-58,2437)*39));

        double[] d=t.transformPixelToLatLng(4.603270627176880,-74.06486481428140,4.602725221337820,-74.06529933214190,
                4.602854888940370,-74.06432099640370,3168,3223,2393.972155790736,2383.695434192323);
        System.out.println(d[0]+" "+d[1]);


    }
}
