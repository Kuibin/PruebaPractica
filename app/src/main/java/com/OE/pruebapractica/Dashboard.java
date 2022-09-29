package com.OE.pruebapractica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity {

    String datosUsuario = "";
    String tokenUsuario = "";

    TextView nombreU, viewToken;
    ImageView barra;
    Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        nombreU = findViewById(R.id.textViewNombre);
        barra = findViewById(R.id.imageView);
        boton = findViewById(R.id.button);
        viewToken = findViewById(R.id.textViewToken);

        SharedPreferences prefs = getSharedPreferences("datos_usuario",   Context.MODE_PRIVATE);
        datosUsuario = prefs.getString("datosUsuario", "");
        tokenUsuario = prefs.getString("tokenBody", "");

        viewToken.setText(tokenUsuario);

        Log.i("Datos: ", datosUsuario);

        JSONObject json = null;
        try {
            json = new JSONObject(datosUsuario);
            //JSONObject messageJson = json.getJSONObject ("titular");
            Log.i("titular: ", json.getString("titular"));
            nombreU.setText("Bienvenido "+json.getString("titular"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codigoBarras();
            }
        });

    }
    private void codigoBarras(){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode("tokenUsuario", BarcodeFormat.CODE_128, barra.getWidth(), barra.getHeight());
            Bitmap bitmap = Bitmap.createBitmap(barra.getWidth(),barra.getHeight(), Bitmap.Config.RGB_565);
            for (int i = 0; i < barra.getWidth(); i++){
                for (int j = 0; j < barra.getHeight(); j++){
                    bitmap.setPixel(i,j,bitMatrix.get(i,j)? Color.BLACK:Color.WHITE);
                }
            }
            barra.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


}