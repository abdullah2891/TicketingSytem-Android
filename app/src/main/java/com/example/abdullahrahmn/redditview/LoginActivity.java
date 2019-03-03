package com.example.abdullahrahmn.redditview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    EditText username;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton =  findViewById(R.id.Login);
        final Context context = this;
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestQueue.add(getAccessToken(username.getText().toString(),password.getText().toString()));
                //context.startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });

    }

    private StringRequest getAccessToken(final String username , final String password){
            String url = "https://shielded-waters-41724.herokuapp.com/api/token/";

            final Context context = this;

            StringRequest accessTokenRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject accessTokenObject=  new JSONObject(response);

                            String token = (String) accessTokenObject.get("token");
                            Log.d("token", token);

                            SharedPreferences sharedPref = context.getSharedPreferences("test",context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("token",token);
                            editor.apply();


                            token = sharedPref.getString("token", "test");
                            Log.d("retrieved token", token);

                            context.startActivity(new Intent(context, MainActivity.class));
                            Log.d("token","token");
                        } catch (JSONException e) {
                            Log.d("error" ,e.toString());
                        }

                        Log.d("Response", response);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };

            return accessTokenRequest;
    }

}
