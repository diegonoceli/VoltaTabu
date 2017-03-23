package com.example.diego.voltatabu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class Cadastro extends AppCompatActivity {

    private Button BotaoUpload;
    private ImageView imagefoto;

    private Bitmap bitmap;

    private int PICK_IMAGE_REQUEST = 1;

    private String UPLOAD_URL = "http://voltatabu.6te.net/upload.php";

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    private String KEY_MATRICULA = "matricula";
    private FloatingActionButton trocafoto;
    private EditText edtnome, edtmatricula;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

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
        Bitmap bitmap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar);
        Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap1, 100);

        edtnome = (EditText) findViewById(R.id.edtnome);
        edtmatricula = (EditText) findViewById(R.id.edtmatricula);
        trocafoto = (FloatingActionButton) findViewById(R.id.trocafoto);
        imagefoto = (ImageView) findViewById(R.id.FotoCircle);

        imagefoto.setImageBitmap(circularBitmap);

        BotaoUpload = (Button) findViewById(R.id.botaoupload);
        BotaoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Verifica()==true) {
                    uploadImage(v);
                }

            }
        });


        trocafoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == trocafoto) {
                    showFileChooser();


                }
            }
        });
    }


    private boolean Verifica(){
        if(edtnome.getText().toString().trim()=="" && edtmatricula.getText().toString().trim()==""){
            Toast.makeText(Cadastro.this,"há campos em branco",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }

    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecione a foto"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    //Setting the Bitmap to ImageView
                    imagefoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                bitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar);
                Toast.makeText(Cadastro.this, "Por favor selecione uma foto!", Toast.LENGTH_LONG).show();

            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(final View v) {

        //Showing the progress dialog
               final ProgressDialog loading = ProgressDialog.show(this, "Cadastrando...", "Por favor aguarde...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        Toast.makeText(Cadastro.this,s,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(Cadastro.this, "Ocorreu um erro, você selecionou a foto?", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);
                String name = edtnome.getText().toString();
                String matricula = edtmatricula.getText().toString();

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters

                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);
                params.put(KEY_MATRICULA, matricula);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    private void kill_activity() {
        finish();
    }
}
