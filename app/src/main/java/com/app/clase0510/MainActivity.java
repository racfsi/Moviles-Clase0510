package com.app.clase0510;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.clase0510.Clases.Archivo;

public class MainActivity extends AppCompatActivity {


    public static final String B_TH = Manifest.permission.BLUETOOTH;
    private Context context;

    private Activity activity;

    private TextView txtVersionAndroid;

    private TextView txtNivelBateria;

    IntentFilter batteryFilter;

    private BluetoothAdapter btAdapter;

    private boolean bandera = false;

    private EditText nameFile;

    private Archivo archivo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicio();
        //CONTEXTO Y ACTIVITY
        context = getApplicationContext();
        activity = this;
        //BATERIA
        batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(bateriaCarga,batteryFilter );
    }

    private void inicio(){
        txtVersionAndroid =findViewById(R.id.versionAndroid);
        txtNivelBateria =findViewById(R.id.nivelBateria);
        nameFile = findViewById(R.id.etnombreFile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVersionAndroid();
    }

    public void saveFile(View view){
        String nameF = nameFile.getText().toString();
        if(nameF.length()>0){
            nameF += ".txt";
            String dataBattery = txtNivelBateria.getText().toString();
            archivo.crearGuardarArchivo(nameF,dataBattery);
        }else{
            Toast.makeText(this, "Debe poner el nombre del archivo", Toast.LENGTH_SHORT).show();
        }
    }

    private void setVersionAndroid(){
        String versionSo = Build.VERSION.RELEASE;
        int versionSDK = Build.VERSION.SDK_INT;
        txtVersionAndroid.setText("Sistema operativo:"+versionSo+" SDK: "+versionSDK);
    }

    BroadcastReceiver bateriaCarga = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int nivelActual = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
            int escala = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
            txtNivelBateria.setText("Nivel bateria: "+nivelActual);
        }
    };

    //Verificamos permisos
    private boolean statusPermiso(){
        int response = ContextCompat.checkSelfPermission(this.context, B_TH);
        if(response == PackageManager.PERMISSION_GRANTED) return true;
        else return  false;
    }

    //Solicitamos permisos
     private void solicitarPermisoBT(){
        boolean response = ActivityCompat.shouldShowRequestPermissionRationale(this.activity,B_TH);
        if(!response) ActivityCompat.requestPermissions(activity, new String[]
         {B_TH},100);
    }

    //Habilitamos BT
    public void habilitarBT(View view){
        statusAdapter();
        if (btAdapter == null){
            Toast.makeText(this, "El dispositivo no tiene Bluetooth", Toast.LENGTH_SHORT).show();
        }else{
            if(btAdapter.isEnabled()){
                Toast.makeText(this, "El Bluetooth ESTÁ ACTIVADO", Toast.LENGTH_SHORT).show();
            }else{
                if(!statusPermiso()){
                    solicitarPermisoBT();
                    Intent habilitar = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(habilitar,110);
                }else{
                    Toast.makeText(this, "El Bluetooth ESTÁ ACTIVADO", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void desHabilitarBT(View view){
        statusAdapter();
        if(btAdapter.isEnabled()){
            Toast.makeText(this, "El Bluetooth SE ESTÁ APAGANDO", Toast.LENGTH_SHORT).show();
            btAdapter.disable();
        }else{
            Toast.makeText(this, "El Bluetooth ESTÁ ACTIVO", Toast.LENGTH_SHORT).show();
        }
    }

    private void statusAdapter(){
        if(!bandera){
            btAdapter = BluetoothAdapter.getDefaultAdapter();
            bandera = true;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bateriaCarga);
    }
}