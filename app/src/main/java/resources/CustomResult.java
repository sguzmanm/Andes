package resources;

import android.net.wifi.ScanResult;

public class CustomResult implements Comparable<CustomResult> {

    private ScanResult scan;

    private String preBssid;

    public ScanResult getScan() {
        return scan;
    }

    public void setScan(ScanResult scan) {
        this.scan = scan;
    }

    public String getPreBssid() {
        return preBssid;
    }

    public void setPreBssid(String preSsid) {
        this.preBssid = preSsid;
    }

    public CustomResult(ScanResult scan) {
        this.scan = scan;
        this.preBssid=scan.BSSID.substring(0,14);
    }

    public int compareTo(CustomResult o)
    {
        if(scan.level<o.scan.level)
            return -1;
        else if(scan.level>o.scan.level)
            return 1;
        else
            return 0;
    }
}
