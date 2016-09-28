package com.example.home.ciudades;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Galeria extends AppCompatActivity {

    GridView gvGaleria;
    ArrayList<File>list;

    private AdminSQLiteOpenHelper admin;
    private SQLiteDatabase db;
    private Cursor cursor;
    private String[] cadenaImg;
    private int[] idImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        admin = new AdminSQLiteOpenHelper(this, varsGlobales.bd, null, varsGlobales.ver);
        db = admin.getWritableDatabase();
        cursor = db.rawQuery("SELECT id,img FROM ciudad",null);
        if (cursor.moveToFirst()){

            int i=0;
            cadenaImg = new String[cursor.getCount()];
            idImg = new int[cursor.getCount()];
            do {
                cadenaImg[i] = cursor.getString(cursor.getColumnIndex("img"));
                idImg[i] = cursor.getInt(cursor.getColumnIndex("id"));
               // Toast.makeText(this," :"+ciudad[i],Toast.LENGTH_LONG).show();
                i++;
            }while (cursor.moveToNext());
        }
        //list = imageReader(Environment.getExternalStorageDirectory());
        gvGaleria = (GridView) findViewById(R.id.gv_galeria);
        AdaptadorGaleria adaptadorGaleria = new AdaptadorGaleria(this,cadenaImg);
        gvGaleria.setAdapter(adaptadorGaleria);
        //gvGaleria.setAdapter(new GridAdapter());
        gvGaleria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(Galeria.this, ""+idImg[position], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Galeria.this,ImagenGrande.class);
                intent.putExtra("DatoId",idImg[position]);
                startActivityForResult(intent,0);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
    /*
    class GridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.imagen_miniatura,parent,false);
            ImageView imgMiniatura = (ImageView) convertView.findViewById(R.id.imageView2);
            imgMiniatura.setImageURI(Uri.parse(getItem(position).toString()));
            return convertView;
        }
    }
*/
    /*
    ArrayList<File> imageReader(File root){
        ArrayList<File> a = new ArrayList<>();
        File[] files = root.listFiles();
        for (int i= 0;i < files.length;i++){
            if(files[i].isDirectory()){
                a.addAll(imageReader(files[i]));
            }
            else {
                if (files[i].getName().endsWith(".jpg")){
                    a.add(files[i]);
                }
            }
        }
        return a;

    }*/

}
