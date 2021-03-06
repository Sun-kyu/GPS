package com.example.project;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.CompassView;
import com.naver.maps.map.widget.LocationButtonView;
import com.naver.maps.map.widget.ScaleBarView;
import com.naver.maps.map.widget.ZoomControlView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.net.MalformedURLException;
import java.util.List;

public class Pharmacy extends AppCompatActivity {
    TextView text;
    private static String ServiceKey = "hdzJlamqcI9RadMq1bNFB06T1dXVd5zzs6%2FdN50zUW2fpJMTfxlgs8yI54BIstOyE3VXPC9Hw1uCb7yD7VblVA%3D%3D"; //???
    String[] dutyName = new String[2000];
    String[] latitude = new String[2000];
    String[] longitude = new String[2000];
    String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);
        text = (TextView) findViewById(R.id.PharmacyData);
        new Thread(new Runnable() { //????????? ?????? ????????? (???????????????,???????????????)
            @Override
            public void run() {
                data = getPharmacydata();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(data);
                    }
                });
            }
        }).start();
    }


    //?????? ????????? ??????
    //dao??? ????????? ? ?????? ?
    private String getPharmacydata() {
        int count = 0;
        StringBuffer buffer = new StringBuffer();
        String pageNo = "1"; //????????? ??????
        String numOfRows = "30"; // ?????? ??????
        String url = "http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyFullDown?pageNo=1&numOfRows=30&ServiceKey=" + ServiceKey;
        try {
            URL PharmacyUrl = new URL(url);
            InputStream is = PharmacyUrl.openStream(); //?????? ????????? ??????

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;
            xpp.next();

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("?????? ?????? ??????????????????????????????\n\n");
                        break;
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();
                        if (tag.equals("item")) ;
                        else if (tag.equals("dutyName")) { //?????? ??????
                            xpp.next();
                            buffer.append(xpp.getText());
                            dutyName[count] = xpp.getText();
                            buffer.append("\n");
                        } else if (tag.equals("wgs84Lat")) { //??????
                            xpp.next();
                            buffer.append(xpp.getText());
                            latitude[count] = xpp.getText();
                            buffer.append("\n");
                        } else if (tag.equals("wgs84Lon")) { //??????
                            xpp.next();
                            buffer.append(xpp.getText());
                            longitude[count] = xpp.getText();
                            buffer.append("\n");
                            count++;
                        }
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xpp.getName(); //?????? ?????? ????????????
                        if (tag.equals("item")) buffer.append("\n");// ????????? ?????????????????? ?????????
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        buffer.append("?????????ff\n");
        return buffer.toString();
    }
}