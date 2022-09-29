package com.OE.pruebapractica;

import static java.lang.Short.decode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import android.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //public static final String almacenDatos = "";
    //Intent intent = new Intent(this, MainActivity.class);


    EditText txtEmail, txtPass;
    Button btnEnviar;
    TextView textoMuestra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtEmail = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtPass);
        btnEnviar = findViewById(R.id.buttonLogin);
        textoMuestra = findViewById(R.id.textView);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginMacropay(txtEmail.getText().toString(), txtPass.getText().toString());
            }
        });
    }

    private void loginMacropay(final String email, final String pass) {
        String url = "https://testandroid.macropay.com.mx";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Recivo de json
                    JSONObject jsonObject = new JSONObject(response);
                    //textoMuestra.setText(jsonObject.getString("success"));
                    String token = jsonObject.getString("token");
                    //Log.i("Token:", token);
                    //textoMuestra.setText(token);

                    //Descompuesto y alamacenado de token
                    String[] split = token.split("\\.");
                    byte[] decodedBytes0 = Base64.decode(split[0], Base64.URL_SAFE);
                    byte[] decodedBytes1 = Base64.decode(split[1], Base64.URL_SAFE);
                    String head = new String(decodedBytes0, "UTF-8");
                    String body = new String(decodedBytes1, "UTF-8");
                    Log.i("Respuesta:", body);

                    //redireccionas a otra Activity
                    if (jsonObject.getString("success") == "true") {
                        //guardamos datos
                    SharedPreferences prefs = getSharedPreferences("datos_usuario",   Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("datosUsuario", body);
                    editor.putString("tokenBody", split[1]);
                    editor.commit();
                    //nos vamos
                        Intent go = new Intent(MainActivity.this, Dashboard.class);
                        startActivity(go);
                    }else{
                        textoMuestra.setText("Credenciales Invalidas");
                    }

                    Toast.makeText(MainActivity.this, "Inicio de Sesion " + jsonObject.getString("success"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.getMessage());
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);//"admin@macropay.mx"
                params.put("password", pass);//"Admin1"
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }
}
