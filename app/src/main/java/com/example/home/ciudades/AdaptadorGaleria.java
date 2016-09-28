package com.example.home.ciudades;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Cristian on 25/9/2016.
 */

public class AdaptadorGaleria extends ArrayAdapter<String> {
    private final AppCompatActivity context;
    private final String [] cadena;

    public AdaptadorGaleria(AppCompatActivity context, String[] cadena) {
        super(context, R.layout.imagen_miniatura,cadena);
        this.context = context;
        this.cadena = cadena;
    }

    public View getView(int posicion ,View v, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View roView= inflater.inflate(R.layout.imagen_miniatura,null,true);
        ImageView imgGaleria = (ImageView) roView.findViewById(R.id.img_galeria);
        //TextView tvGaleria = (TextView) roView.findViewById(R.id.tv_galeria);

        byte[] decodedString;
        Bitmap decodedByte = null;

        decodedString = Base64.decode(cadena[posicion], Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        int alto = decodedByte.getHeight();
        int ancho = decodedByte.getWidth();
        //imgGaleria.setLayoutParams(new LinearLayout.LayoutParams(decodedByte.getScaledWidth(100), decodedByte.getScaledHeight(100)));
        imgGaleria.setImageBitmap(decodedByte);

        //tvGaleria.setText(cadena[posicion]);
        return roView;

    }
}
