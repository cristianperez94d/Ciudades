package com.example.home.ciudades;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageView;

public class ImagenGrande extends AppCompatActivity {
    int dato;
    ImageView imgVistaImagen;
    private AdminSQLiteOpenHelper admin;
    private SQLiteDatabase db;
    private Cursor cursor;
    private String[]cadenaImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen_grande);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            dato = (int) bundle.get("DatoId");
            imgVistaImagen = (ImageView) findViewById(R.id.img_vista_imagen);
            admin = new AdminSQLiteOpenHelper(this, varsGlobales.bd, null, varsGlobales.ver);
            db= admin.getWritableDatabase();
            cursor = db.rawQuery("Select * From ciudad where id='"+dato+"'",null);
            if (cursor.moveToFirst()){

                int i=0;
                cadenaImg = new String[cursor.getCount()];
                do {
                    cadenaImg[i] = cursor.getString(cursor.getColumnIndex("img"));

                    byte[] decodedString;
                    Bitmap decodedByte = null;

                    decodedString = Base64.decode(cadenaImg[i], Base64.DEFAULT);
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    int alto = decodedByte.getHeight();
                    int ancho = decodedByte.getWidth();
                    //imgGaleria.setLayoutParams(new LinearLayout.LayoutParams(decodedByte.getScaledWidth(100), decodedByte.getScaledHeight(100)));
                    imgVistaImagen.setImageBitmap(decodedByte);
                    // Toast.makeText(this," :"+ciudad[i],Toast.LENGTH_LONG).show();
                    i++;
                }while (cursor.moveToNext());


            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), Galeria.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
