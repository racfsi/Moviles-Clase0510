package com.app.clase0510.Clases;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileWriter;

public class Archivo {
    private static  final int COD = 300;
    private Context context;
    private Activity activity;


    public Archivo(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    public boolean statusPermisoSD(){
        int respuesta = ContextCompat.checkSelfPermission
                (this.context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if((respuesta == PackageManager.PERMISSION_GRANTED)) return true;
        else return false;
    }

    public void solicitarPermisoExt(){
        //Verificamos la SDK para permisos es necesario apartir de 6.0
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.shouldShowRequestPermissionRationale
                    (this.activity,Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            }
            else{
                ActivityCompat.requestPermissions(this.activity,new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE},COD);
                Toast.makeText(context, "El permiso ha sido otorgado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void crearDirectorio(File file){
        if(!file.exists()) file.mkdirs();
    }


    public void crearGuardarArchivo(String nameFile, String infoSave){
        File directorio = null;
        solicitarPermisoExt();
        if(statusPermisoSD()){
            try{
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P){
                    directorio = new File(Environment.getExternalStorageDirectory(),"ArchivoApp");
                    crearDirectorio(directorio);
                    Toast.makeText(context, "Ruta"+directorio, Toast.LENGTH_SHORT).show();
                }else{
                    directorio = new File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM),"ARCHIVIOAPP");
                    crearDirectorio(directorio);
                }
                if(directorio != null){
                    File file = new File(directorio,nameFile);
                    FileWriter writer = new FileWriter(file);
                    writer.append(infoSave);
                    writer.flush();
                    writer.close();
                    Toast.makeText(context, "Archivo guardado "+nameFile, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context, "No se pudo crear el directorio", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(context, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "No hay permisos", Toast.LENGTH_SHORT).show();
        }
    }
}
