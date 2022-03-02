package main.montesestebanivan_pmdm02_tarea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class AdapterPersonaje  extends ArrayAdapter<String> {

    String[] ciudades = Hipoteminas.dameArrayNombreHipotes();
    Integer[] imagenes = Hipoteminas.dameArrayImgGeneralHipotes();

    public AdapterPersonaje(Context ctx, int txtViewResourceId, String[] objects){
        super(ctx, txtViewResourceId, objects);
    }
    @Override
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt){
        return crearFilaPersonalizada(position, cnvtView, prnt);
    }

    @Override
    public View getView(int position, View cnvtView, ViewGroup prnt){
        return crearFilaPersonalizada(position, cnvtView, prnt);
    }

    public View crearFilaPersonalizada(int position, View convertView, ViewGroup parent){
        LayoutInflater inflador = LayoutInflater.from(getContext());
        View filaPersonaje = inflador.inflate(R.layout.fila_elegir_personaje, parent, false);
        TextView nombre = (TextView) filaPersonaje.findViewById(R.id.txtPersonaje);
        nombre.setText(ciudades[position]);
        ImageView imagen = (ImageView) filaPersonaje.findViewById(R.id.imagenPersonaje);
        imagen.setImageResource(imagenes[position]);
        return filaPersonaje;
    }

}
//FIN