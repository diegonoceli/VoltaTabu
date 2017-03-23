package com.example.diego.voltatabu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

public class ListarAct extends AppCompatActivity {

    private static final String TAG = ListarAct.class.getSimpleName();

    // Movies json url
    private static final String url = "http://voltatabu.6te.net/listarvolta.php";
    private ProgressDialog pDialog;
    private List<Alunos> alunoslist = new ArrayList<Alunos>();
    private ListView listView;
    private CustomListAdapter adapter;
    private TextView total, data;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String UPLOADURL = "http://voltatabu.6te.net/delete.php";
    private String KEYDELETE = "matricula";
    private int valormatricula=0;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        Fresco.initialize(this);
        // total = (TextView) findViewById(R.id.total);
        valormatricula = getIntent().getExtras().getInt("matricula");
        data = (TextView) findViewById(R.id.data);
        DateFormat df = new SimpleDateFormat("MMM d, yyyy");
        String now = df.format(new Date());
        data.setText(now);


//botao voltar
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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setImageResource(R.drawable.add);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), Cadastro_Volta.class);
                i.putExtra("matricula", valormatricula);
                startActivity(i);
                overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);

            }
        });



        listView = (ListView) findViewById(R.id.listview);

        adapter = new CustomListAdapter(this, alunoslist);

        listView.setAdapter(adapter);
        alunoslist.clear();

        AtualizaListview();

        listView.setEnabled(false);

        FloatingActionButton fabdelete= (FloatingActionButton) findViewById(R.id.FabDelete);
        fabdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alunoslist.clear();
                Delete();
            }
        });



        FloatingActionButton fabUpdate = (FloatingActionButton) findViewById(R.id.FabAtualizar);
        fabUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alunoslist.clear();
                AtualizaListview();
            }
        });

    }


    public void AtualizaListview() {


        JsonArrayRequest AtualizaListReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Alunos alunos = new Alunos();
                                alunos.setHorario(obj.getString("horarios"));
                                alunos.setNome(obj.getString("nome"));
                                alunos.setUri(obj.getString("foto"));

                                // adding movie to movies array
                                alunoslist.add(alunos);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        //total.setText(String.valueOf(response.length()));
                        adapter.notifyDataSetChanged();

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());


            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(AtualizaListReq);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }




    //menu buscar @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menusecundario, menu);
        return true;
    }




   public void Delete(){

        final ProgressDialog loading = ProgressDialog.show(this, "Verificando...", "Por favor aguarde...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOADURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        if (s.trim().equals("Sucesso")) {
                            loading.setMessage("Atualizando...");
                            loading.show();
                            AtualizaListview();
                            loading.dismiss();

                        } else {
                            Toast.makeText(ListarAct.this, s, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(ListarAct.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

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
                params.put(KEYDELETE, String.valueOf(valormatricula));


                //returning parameters
                return params;
            }

        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void kill_activity() {
        finish();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ListarAct Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

}
