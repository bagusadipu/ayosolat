package good.dicoding.ayosolat;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import good.dicoding.ayosolat.MODEL.Item;
import good.dicoding.ayosolat.MODEL.TodayWeather;
import good.dicoding.ayosolat.MODEL.itemsresponse;
import good.dicoding.ayosolat.URL.url;

public class MainActivity extends AppCompatActivity {
    ImageView imgpress,imgtemp;
    Button btncheck;
    EditText edkota;
    TextView subuh,dzuhur,ashar,maghrib,isya,temperatur,hariini;
    List<Item> itemList = new ArrayList<>();
    String kota, title="profil";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Ayo Solat");




//        LocationManager locationManager = (LocationManager)getSystemService(Context, LOCATION_SERVICE);
//        Geocoder gcd = new Geocoder(this, Locale.getDefault());
//        List<Address> addresses = gcd.getFromLocationName(l)

        hariini = findViewById(R.id.hariini);
        subuh = findViewById(R.id.jsubuh);
        dzuhur = findViewById(R.id.jdzuhur);
        ashar = findViewById(R.id.jashar);
        maghrib = findViewById(R.id.jmaghrib);
        isya = findViewById(R.id.jisya);
        btncheck = findViewById(R.id.btncheck);
        edkota = findViewById(R.id.edkota);
        kota = edkota.getText().toString();
        imgpress = findViewById(R.id.imgpressure);
        imgtemp = findViewById(R.id.imgtemperature);


        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        hariini.setText(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime()));
//        temperatur = findViewById(R.id.temperatur);


        btncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidNetworking.get(url.BASE_URL+edkota.getText().toString()+".json?key="+url.API_KEY)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(itemsresponse.class, new ParsedRequestListener<itemsresponse>() {
                    @Override
                    public void onResponse(itemsresponse response) {
                        Log.d("AYOSOLATRESP", response.getStatusDescription());

                        if(response.getStatusDescription().equals("Success.")){
                            Log.d("TIMENYA",response.getMapImage());
                            itemList = response.getItems();

                            TodayWeather todayWeather = new TodayWeather();
                            todayWeather.setPressure(response.getTodayWeather().getPressure());
                            todayWeather.setTemperature(response.getTodayWeather().getTemperature());

                            int pressure = response.getTodayWeather().getPressure();
                            int temperature = Integer.parseInt(response.getTodayWeather().getTemperature());
//                            Log.d("WEATHER",pres+""+tempe);

                            for(int i=0;i<itemList.size();i++){
                                dzuhur.setText(itemList.get(i).getDhuhr());
                                maghrib.setText(itemList.get(i).getMaghrib());
                                subuh.setText(itemList.get(i).getFajr());
                                isya.setText(itemList.get(i).getIsha());
                                ashar.setText(itemList.get(i).getAsr());
                            }
                            if (pressure>1012){
                                imgpress.setImageResource(R.drawable.rain);
                            }
                            if (temperature>0 && temperature<26) {
                                imgtemp.setImageResource(R.drawable.winter);
                            }
                            else if (temperature<=0){
                                imgtemp.setImageResource(R.drawable.winter);
                            }

//                            temperatur.setText(pres+";"+tempe);
                        }
                        else
                            Log.d("ERROR Gan","ERROR NIH");
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d("ERORRNYA", anError.toString());
                    }
            });
        }
        });
//        btncheck.setOnClickListener(view ->
//                AndroidNetworking.get(url.BASE_URL+edkota.getText().toString()+".json?key="+url.API_KEY)
//                .setPriority(Priority.LOW)
//                .build()
//                .getAsObject(itemsresponse.class, new ParsedRequestListener<itemsresponse>() {
//                    @Override
//                    public void onResponse(itemsresponse response) {
//                        Log.d("AYOSOLATRESP", response.getStatusDescription());
//
//                        if(response.getStatusDescription().equals("Success.")){
//                            Log.d("TIMENYA",response.getMapImage());
//                            itemList = response.getItems();
//                            for(int i=0;i<itemList.size();i++){
//                                dzuhur.setText(itemList.get(i).getDhuhr());
//                                maghrib.setText(itemList.get(i).getMaghrib());
//                                subuh.setText(itemList.get(i).getFajr());
//                                isya.setText(itemList.get(i).getIsha());
//                                ashar.setText(itemList.get(i).getAsr());
////                                Log.d("TIMENYANOW"+i,dzuhur.getText().toString());
//                            }
//                            TodayWeather todayWeather = new TodayWeather();
//                            todayWeather.setPressure(response.getTodayWeather().getPressure());
//                            todayWeather.setTemperature(response.getTodayWeather().getTemperature());
//
//                            String pres = (response.getTodayWeather().getPressure().toString());
//                            String tempe = response.getTodayWeather().getTemperature();
//                            Log.d("WEATHER",pres+""+tempe);
//                            temperatur.setText(pres+";"+tempe);
//                        }
//                        else
//                            Log.d("ERROR Gan","ERROR NIH");
//                    }
//                    @Override
//                    public void onError(ANError anError) {
//                        Log.d("ERORRNYA", anError.toString());
//                    }
//                }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    public void setMode(int selectedMode) {
        switch (selectedMode) {
            case R.id.action_about:
                title = "About";
//                openAbout();
                break;
        }
    }
}

