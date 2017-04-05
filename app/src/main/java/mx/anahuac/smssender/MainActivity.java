package mx.anahuac.smssender;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button bt1;
    double latitud = 0;
    double longitud = 0;
    private GPSReceiver receiver;
    private LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receiver = new GPSReceiver();
        manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            int permissionChech = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
            if(permissionChech != PackageManager.PERMISSION_GRANTED){
                Log.i("MEnsaje", "No se tiene permiso para enviar SMS");
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.SEND_SMS },255 );
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },99 );
            }else{
                Log.i("MEnsaje", "SE tiene permiso para enviar SMS!");
            }
            return;
        }

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 1.0F, receiver);
        clickButton();
    }

    public void clickButton (){
        bt1 =  (Button) findViewById(R.id.btn1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager sms = SmsManager.getDefault();
                String phoneNumber = "+5219515574432";
                String messageBody = "Solicito de la asistencia de la longotud: "+ longitud + " y en latitud "+latitud;
//                String messageBody = "Saca los tacos Jaime";
                try{
                    sms.sendTextMessage(phoneNumber, null, messageBody, null, null);
                }catch (Exception e){
                    String mensaje= "No se pudo mandar el mensaje";
                    Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
                }

            }
        });

    }
    public class GPSReceiver implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            if(location != null){
                latitud = location.getLatitude();
                longitud = location.getLongitude();
                String mensaje= "Estamos Listos para iniciar";
                Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
            }else {
                String mensaje= "Hay un problema con tu GPS, lo siento te moriste";
                Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            String mensaje= "GPD habilitado";
            Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            String mensaje= "GPD Deshabilitado";
            Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
        }
    }
}
