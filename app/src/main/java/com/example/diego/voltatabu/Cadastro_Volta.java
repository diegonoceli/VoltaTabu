package com.example.diego.voltatabu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Cadastro_Volta extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleApiClient client;
    private ProgressDialog pDialog,dDialog;
    private Spinner spinnerCidade,spinnerHorario;
    private String urlcidade="http://voltatabu.6te.net/listarCidade.php";
    private String urlhorario="http://voltatabu.6te.net/listarHorario.php";
    private ArrayList<String> arrayListCidade;
    private  ArrayAdapter AdapterCidade;
    private ArrayList<String> arrayListHorario;
    private  ArrayAdapter AdapterHorario;
    private Button btnCadastrar;
    private String UPLOAD="http://voltatabu.6te.net/atualiza.php";

    private String KEY_MATRICULA="matricula";
    private String KEY_CIDADE="cidade";
    private String KEY_HORARIO="horario";
    private int valormat=0;
    private String escspinnercidade="";
    private String escspinnerhorairo="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro__volta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kill_activity();
            }
        });
        int valormatricula = getIntent().getExtras().getInt("matricula");
        btnCadastrar= (Button) findViewById(R.id.btncadastrovolta);
        valormat=valormatricula;
        AtualizaSpinnerCidade();
        AtualizaSpinnerHorario(1);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemSelectedSpinners();
                EnviaMatricula(v);

            }
        });
        spinnerCidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int escolha=parent.getSelectedItemPosition()+1;
                AtualizaSpinnerHorario(escolha);
                Log.e("ESCOLHA",String.valueOf(escolha));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               spinnerCidade.setSelection(1);
                AtualizaSpinnerHorario(1);

            }
        });



    }


    private void ItemSelectedSpinnerCidade() {
        escspinnercidade=String.valueOf(spinnerCidade.getSelectedItemPosition()+1);
    }
    private void ItemSelectedSpinners() {
        escspinnercidade=String.valueOf(spinnerCidade.getSelectedItemPosition()+1);
        escspinnerhorairo=String.valueOf(spinnerHorario.getSelectedItemPosition()+1);
    }

    private void kill_activity() {
        finish();
    }

    private void EnviaMatricula(final View v) {
        final ProgressDialog loading = ProgressDialog.show(this, "Inserindo...", "Por favor aguarde...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        if (s.trim().equals("Sucesso")) {

                            Toast.makeText(Cadastro_Volta.this, s, Toast.LENGTH_LONG).show();
                            finish();

                        } else {
                            Toast.makeText(Cadastro_Volta.this, s, Toast.LENGTH_LONG).show();
                        }


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        Log.e("aqui", "chegou");
                        //Showing toast
                        Toast.makeText(Cadastro_Volta.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

                        if (volleyError instanceof NetworkError) {
                        } else if (volleyError instanceof ServerError) {
                        } else if (volleyError instanceof AuthFailureError) {
                        } else if (volleyError instanceof ParseError) {
                        } else if (volleyError instanceof NoConnectionError) {
                        } else if (volleyError instanceof TimeoutError) {
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_MATRICULA, String.valueOf(valormat));
                params.put(KEY_CIDADE, escspinnercidade);
                params.put(KEY_HORARIO, escspinnerhorairo);


                //returning parameters
                return params;
            }

        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    private void AtualizaSpinnerCidade() {
        spinnerCidade= (Spinner) findViewById(R.id.spinnerCidade);
        arrayListCidade=new ArrayList<String>();
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Carregando...");
        pDialog.show();




        JsonArrayRequest CidadeReq = new JsonArrayRequest(urlcidade,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                          hidePDialog();

                        // Parsing json

                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject obj = response.getJSONObject(i);
                                    arrayListCidade.add(obj.getString("nome_cidade"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        AdapterCidade.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        AppController.getInstance().addToRequestQueue(CidadeReq);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        AdapterCidade =new ArrayAdapter(this,R.layout.spinner,arrayListCidade);
        spinnerCidade.setAdapter(AdapterCidade);
    }



    private void AtualizaSpinnerHorario(final int cidade) {
        spinnerHorario = (Spinner) findViewById(R.id.spinnerHorario);
        arrayListHorario = new ArrayList<String>();
        dDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        dDialog.setMessage("Carregando...");
        dDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(new StringRequest(Request.Method.POST, urlhorario, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    Log.e("response", response);
                    hidedDialog();
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {


                            JSONObject object = array.getJSONObject(i);
                            arrayListHorario.add(object.getString("horarios"));


                        }
                        AdapterHorario.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap map = new HashMap();
                map.put(KEY_CIDADE, String.valueOf(cidade));
                return map;
            }
        });


        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        AdapterHorario =new ArrayAdapter(this,R.layout.spinner,arrayListHorario);
        spinnerHorario.setAdapter(AdapterHorario);

    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
    private void hidedDialog() {
        if (dDialog != null) {
            dDialog.dismiss();
            dDialog = null;
        }
    }
}
