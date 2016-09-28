package com.example.home.ciudades;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private EditText t1;
    private EditText t2;
    private AdminSQLiteOpenHelper admin;
    private SQLiteDatabase db;
    private Cursor fila;
    private ContentValues registro;

    private String nombre = "";
    private int ancho, alto;
    private Intent intent;
    private ImageView iv;
    private Bitmap bitmap, bitmap2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        admin = new AdminSQLiteOpenHelper(this, varsGlobales.bd, null, varsGlobales.ver);

        nombre = Environment.getExternalStorageDirectory() + "/img.jpg";

        t1 = (EditText) findViewById(R.id.editText);
        t2 = (EditText) findViewById(R.id.editText2);
        iv = (ImageView) findViewById(R.id.imageView);

        Display display = this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ancho = size.x;
        //alto = size.y;

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float scale = getResources().getDisplayMetrics().density;
        alto = (int) (150 * scale);
    }

    public void agregar(View view) {
        if(!t2.getText().toString().equals("")) {
            String imgCodificada = "";
            iv.buildDrawingCache(true);
            bitmap = iv.getDrawingCache(true);
            if (bitmap != null) {
                if(iv.getWidth() < iv.getHeight()) {
                    bitmap2 = Bitmap.createScaledBitmap(bitmap, iv.getWidth(), iv.getHeight(), true);
                }
                else {
                    bitmap2 = Bitmap.createScaledBitmap(bitmap, iv.getWidth(), iv.getHeight(), true);
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] imagen = baos.toByteArray();
                imgCodificada = Base64.encodeToString(imagen, Base64.DEFAULT);
            }
            db = admin.getWritableDatabase();
            registro = new ContentValues();
            registro.put("ciudad", t2.getText().toString());
            registro.put("img", imgCodificada);
            long a = db.insert("ciudad", null, registro);
            db.close();

            t1.setText("");
            t2.setText("");
            iv.setImageBitmap(null);
            iv.destroyDrawingCache();
            iv.setImageDrawable(null);

            Log.d("Error: ", "Algo paso");
            if (a == -1) {
                Toast.makeText(this, "Error de inserción: " + a, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Registro almacenado: " + a, Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this, "Ingresar la ciudad", Toast.LENGTH_LONG).show();
        }
    }

    public void imprimir(View view) {
        db = admin.getWritableDatabase();
        fila = db.rawQuery("SELECT * FROM ciudad", null);
        String datos = "";
        while(fila.moveToNext()) {
            datos += fila.getInt(0) + " - " + fila.getString(1) + "\n";
        }
        db.close();

        intent = new Intent(this, ImprimirActivity.class);
        intent.putExtra("datos2", datos);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);

        //Toast.makeText(this, datos, Toast.LENGTH_LONG).show();
    }

    public void eliminar(View view) {
        db = admin.getWritableDatabase();
        int val = db.delete("ciudad", "id=" + "'"+t1.getText().toString()+"'", null);
        if(val == 0) {
            Toast.makeText(this, "El registro no existe", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Registro eliminado", Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    public void editar(View view) {
        db = admin.getWritableDatabase();
        registro = new ContentValues();
        registro.put("ciudad",t2.getText().toString());
        int val = db.update("ciudad", registro, "id=" + "'"+t1.getText().toString()+"'", null);
        if(val == 0) {
            Toast.makeText(this, "El registro no existe", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Registro actualizado", Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    public void galeria(View view){
        Intent nuevoForm = new Intent(this, Galeria.class);
        this.startActivity(nuevoForm);

    }
    public void buscar(View view) {
        db = admin.getWritableDatabase();
        fila = db.rawQuery("SELECT * FROM ciudad WHERE id = " + t1.getText().toString() + "", null);
        String datos = "";
        if(fila.moveToFirst()) {
            t1.setText(fila.getInt(0)+"");
            t2.setText(fila.getString(1));
            byte[] decodedString;
            Bitmap decodedByte = null;

            decodedString = Base64.decode(fila.getString(2), Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            iv.setLayoutParams(new LinearLayout.LayoutParams(ancho, alto));
            iv.setImageBitmap(decodedByte);
        }
        db.close();

        //Toast.makeText(this, datos, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(data == null) {
                if(resultCode != 0) {
                    Bitmap myBitmap  = BitmapFactory.decodeFile(nombre);
                    Bitmap orientedBitmap = ExifUtil.rotateBitmap(nombre, myBitmap);

                    iv.setLayoutParams(new LinearLayout.LayoutParams(ancho, alto));
                    //iv.setImageBitmap(myBitmap);
                    iv.setImageBitmap(orientedBitmap);
                    //Para guardar la imagen en la galería, utilizamos una conexión a un MediaScanner
                    new MediaScannerConnection.MediaScannerConnectionClient() {
                        private MediaScannerConnection msc = null; {
                            msc = new MediaScannerConnection(getApplicationContext(), this);
                            msc.connect();
                        }

                        public void onMediaScannerConnected() {
                            msc.scanFile(nombre, null);
                        }

                        public void onScanCompleted(String path, Uri uri) {
                            msc.disconnect();
                        }
                    };
                }
            }
        } else if (requestCode == 2) {
            if(data != null) {
                Uri selectedImage = data.getData();
                InputStream is;
                try {
                    is = getContentResolver().openInputStream(selectedImage);
                    BufferedInputStream bis = new BufferedInputStream(is);

                    Bitmap myBitmap = BitmapFactory.decodeStream(bis);
                    Bitmap orientedBitmap = ExifUtil.rotateBitmap(nombre, myBitmap);
                    iv.setLayoutParams(new LinearLayout.LayoutParams(ancho, alto));
                    //iv.setImageBitmap(myBitmap);
                    iv.setImageBitmap(orientedBitmap);
                } catch (FileNotFoundException e) {}
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.deCamara) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri output = Uri.fromFile(new File(nombre));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
            startActivityForResult(intent, 1);
        }
        else if (id == R.id.deGaleria) {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }

        return super.onOptionsItemSelected(item);
    }
}