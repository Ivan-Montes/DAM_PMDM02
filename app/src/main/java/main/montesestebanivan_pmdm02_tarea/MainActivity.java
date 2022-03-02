package main.montesestebanivan_pmdm02_tarea;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.stream.IntStream;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private int [][] tablero;
    private int hipoteElegida = R.drawable.flamenca;
    private int hipoteElegidaFail = R.drawable.flamencafails;
    private ConfNivel nivel = ConfNivel.PRINCIPIANTE;
    private  int contadorHipote = 0;
    private TableLayout tabletLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insertCoin(nivel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_opciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.instrucciones:
                mostrarInstrucciones();
                return true;
            case R.id.start:
                Toast t = Toast.makeText(getApplicationContext(),R.string.init,Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER,0,0);
                        t.show();
                insertCoin(nivel);
                return true;
            case R.id.conf:
                elegirNivel();
                return true;
            case R.id.player_select:
                choosePlayer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertCoin(ConfNivel nivel){
        this.tablero = new Tablero(nivel).crearArrayTablero();
        this.nivel = nivel;
        TableLayout tableLayoutMain = new TableLayout(getApplicationContext());
        //TableLayout tableLayoutMain = findViewById(R.id.tableLayout);
        this.tabletLayout = tableLayoutMain;
        tableLayoutMain.removeAllViews();
        tableLayoutMain.setStretchAllColumns(true);
        tableLayoutMain.setShrinkAllColumns(true);
        tableLayoutMain.setWeightSum(nivel.getNumFilas());
        tableLayoutMain.setBackgroundColor(Color.rgb(255,215,0));//Color Amarillo Oro
        tableLayoutMain.setPadding(0,0,0,0);
        TableLayout.LayoutParams layoutTableParam =
                new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT);
        layoutTableParam.setMargins(0,0,0,0);
        tableLayoutMain.setLayoutParams(layoutTableParam);

        TableRow filaTabla;
        Button b;
        ImageButton ib;

        TableLayout.LayoutParams layoutFilas = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        layoutFilas.setMargins(2,2,2,2);
        TableRow.LayoutParams layoutBotones  = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT);
        layoutBotones.setMargins(2,2,2,2);

        Display p = getWindowManager().getDefaultDisplay();
        Point dimensiones = new Point();
        p.getSize(dimensiones);
        int anchoPantalla = dimensiones.x;
        int altoPantalla = dimensiones.y;
        layoutBotones.height = altoPantalla/nivel.getNumFilas();
        layoutBotones.width = anchoPantalla/nivel.getNumFilas();

        for(int i=0;i<nivel.getNumFilas();i++) {

            filaTabla = new TableRow(this);
            filaTabla.setLayoutParams(layoutFilas);

            for (int j = 0; j < nivel.getNumFilas(); j++) {

                if (tablero[i][j] == -1) {
                    ib = new ImageButton(this);
                    ib.setId(View.generateViewId());
                    ib.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    ib.setLayoutParams(layoutBotones);
                    ib.setPadding(0, 0, 0, 0);
                    ib.setBackgroundColor(Color.DKGRAY);
                    ib.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ib.setAdjustViewBounds(true);
                    ib.setOnClickListener(this);
                    ib.setOnLongClickListener(this);
                    filaTabla.addView(ib);

                } else {
                    b = new Button(this);
                    b.setId(View.generateViewId());
                    b.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    b.setLayoutParams(layoutBotones);
                    b.setPadding(0, 0, 0, 0);
                    b.setText(String.valueOf(tablero[i][j]));
                    b.setTextColor(Color.DKGRAY);
                    b.setBackgroundColor(Color.DKGRAY);
                    b.setOnClickListener(this);
                    b.setOnLongClickListener(this);
                    filaTabla.addView(b);
                }
            }
            tableLayoutMain.addView(filaTabla);
        }
        ConstraintLayout baseLayout = findViewById(R.id.baseConstraintLayout);
        baseLayout.removeAllViews();
        baseLayout.addView(tableLayoutMain);

    }

    @Override
    public void onClick(View v) {

        if (v.getClass().getName().equalsIgnoreCase("android.widget.Button")) {
            Button b = (Button)v;
            b.setTextColor(Color.WHITE);
            b.setBackgroundColor(Color.rgb(0,255,0));//Verde
            b.setEnabled(false);
            if (b.getText().toString().equals("0")){
                descubrirAlrededor(b);
            }

        } else if (v.getClass().getName().equalsIgnoreCase("android.widget.ImageButton")) {
            ImageButton bi = (ImageButton)v;
            bi.setImageResource(hipoteElegidaFail);
            bi.setRotation(180);
            bi.setEnabled(false);
            Toast t = Toast.makeText(MainActivity.this, R.string.fin_pisaste, Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
            descubrirTodasLasCasillas();
            reset();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        Button b;
        ImageButton ib;

        if ( esBoton(v) ) {
            b = (Button)v;
            b.setTextColor(Color.WHITE);
            b.setBackgroundColor(Color.rgb(220, 20, 60));//Rojo
            b.setEnabled(false);
            Toast.makeText(MainActivity.this, R.string.fin_nada, Toast.LENGTH_LONG).show();
            descubrirTodasLasCasillas();
            reset();

        } else if ( esImageButton(v) ) {
            ib = (ImageButton)v;
            ib.setImageResource(hipoteElegida);
            ib.setEnabled(false);
            contadorHipote++;
            comprobarFinPartida();
        }

        return false;
    }

    /**
     * Comprueba el contador de hipoteminas para ver si se han descubierto todas. Muestra mensaje y anula botones.
     */
    private void comprobarFinPartida(){
        if ( contadorHipote == nivel.getNumHipote() ) {
            Toast t = Toast.makeText(this,R.string.fin_exito,Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
            desactivarTodosLosBotones();
            reset();
        }else{
            Toast t = Toast.makeText(this,"Te quedan " + (nivel.getNumHipote() - contadorHipote) + " Hipoteminas!!",Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
        }
    }

    /**
     * Desactiva todos los botones.
     */
    private void desactivarTodosLosBotones(){
        IntStream
                .range(0,tabletLayout.getChildCount())
                .forEach( (y) -> {
                    TableRow linea = (TableRow) tabletLayout.getChildAt(y);
                    IntStream.range(0,linea.getChildCount())
                            .forEach( (x) -> {
                                View vista = (View)linea.getChildAt(x);
                                vista.setEnabled(false);
                            });
                        });
    }
    /**
     * Descubre todas las casillas del tablero y desactiva botones
     */
    private void descubrirTodasLasCasillas(){
        TableLayout tableLayoutMain = tabletLayout;
        //TableLayout tableLayoutMain = findViewById(R.id.tableLayout);
        for (int i = 0; i < tableLayoutMain.getChildCount(); i++) {
            TableRow fila = (TableRow)tableLayoutMain.getChildAt(i);
            int n = fila.getChildCount();

            for (int j = 0; j < n; j++) {
                if (fila.getChildAt(j).getClass().getName().equalsIgnoreCase("android.widget.Button")) {
                    Button btn = (Button)fila.getChildAt(j);

                    if ( btn.isEnabled()){
                        btn.setTextColor(Color.WHITE);
                        btn.setBackgroundColor(Color.rgb(0,255,0));
                        btn.setEnabled(false);
                    }

                } else if ( esImageButton(fila.getChildAt(j)) ) {
                    ImageButton ib = (ImageButton)fila.getChildAt(j);

                    if ( ib.isEnabled()){
                        ib.setImageResource(hipoteElegida);
                        ib.setEnabled(false);
                    }
                }
            }
        }
    }

    /**
     * Descubre las casillas de alrededor en el tablero
     * @param btn Elemento de clase View, será un botón/casilla del Tablet layout/tablero
     */
    private void descubrirAlrededor(View btn){

        if ( esBoton(btn) ){
            Point ubik = coordenadasElementoView(btn);
            int x = ubik.x;
            int y = ubik.y;
            Button b;

            if ( ( x - 1 >= 0 ) && ( y - 1 >= 0 ) ){
                b = (Button) dameViewDelTablero(x-1,y-1);
                if (b.isEnabled()){
                    descubrirCasilla( x - 1, y - 1 );
                }
            }
            if ( ( y - 1 >= 0 ) ){
                b = (Button) dameViewDelTablero(x,y - 1);
                if (b.isEnabled()) {
                    descubrirCasilla(x, y - 1);
                }
            }
            if ( x + 1 < nivel.getNumFilas()  &&  y - 1 >= 0 ) {
                b = (Button) dameViewDelTablero(x + 1,y - 1);
                if (b.isEnabled()) {
                    descubrirCasilla(x + 1, y - 1);
                }
            }
            if ( x + 1 < nivel.getNumFilas() ){
                b = (Button) dameViewDelTablero(x + 1, y);
                if (b.isEnabled()) {
                    descubrirCasilla(x + 1, y);
                }
            }
            if ( (x + 1 < nivel.getNumFilas()) && (y + 1 < nivel.getNumFilas()) ){
                b = (Button) dameViewDelTablero(x + 1,y + 1);
                if (b.isEnabled()) {
                    descubrirCasilla(x + 1, y + 1);
                }
            }
            if ( y + 1 < nivel.getNumFilas()  ){
                b = (Button) dameViewDelTablero(x,y + 1);
                if (b.isEnabled()) {
                    descubrirCasilla(x, y + 1);
                }
            }
            if ( ( x - 1 >= 0 && y + 1 < nivel.getNumFilas() )  ){
                b = (Button) dameViewDelTablero(x - 1, y + 1);
                if (b.isEnabled()) {
                    descubrirCasilla(x - 1, y + 1);
                }
            }
            if ( x - 1 >= 0 ){
                b = (Button) dameViewDelTablero(x - 1, y);
                if (b.isEnabled()) {
                    descubrirCasilla(x - 1, y);
                }
            }
        }
    }

    /**
     * Busca y devuelve la vista que esté en las coordenadas pasadas
     * @param x coordenada
     * @param y coordenada
     * @return View solicitada
     */
    private View dameViewDelTablero(int x, int y){
        TableRow tr = (TableRow) tabletLayout.getChildAt(y);
        Button b = (Button) tr.getChildAt(x);
        return b;
    }

    /**
     * Descubre la casilla que coincida con las coordenadas y lanza la revisión de las casillas de alrededor
     * @param x coordenada ancho
     * @param y coordenada alto
     */
    private void descubrirCasilla(int x, int y){

        Button b = (Button) dameViewDelTablero(x,y);
        b.setTextColor(Color.WHITE);
        b.setBackgroundColor(Color.rgb(0,255,0));//Verde
        b.setEnabled(false);
        if (b.getText().toString().equals("0")){
            descubrirAlrededor(b);
        }
    }

    /**
     * Devuelve la ubicación del elemento
     * @param btn Elemento de clase View, será un botón/casilla del Tablet layout/tablero
     * @return Clase point con las coordenadas dentro de la tabla teniendo en cuenta su orden de elemento. Coincide con el array de las casillas
     */
    private Point coordenadasElementoView(View btn){
        TableRow filaPadre = (TableRow) btn.getParent();
        int x = filaPadre.indexOfChild(btn);
        TableLayout mesaPadre = (TableLayout) filaPadre.getParent();
        int y = mesaPadre.indexOfChild(filaPadre);
        Point ubicacion = new Point(x,y);
        return ubicacion;
    }

    /**
     * Comprueba si es un Button
     * @param v Elemento de clase View, será un botón/casilla del Tablet layout/tablero
     * @return True si es un Button
     */
    private boolean esBoton(View v){
        boolean resultado = false;
        if (v.getClass().getName().equalsIgnoreCase("android.widget.Button")){
            resultado = true;
        }
        return resultado;
    }

    /**
     * Comprueba si es un ImageButton
     * @param v Elemento de clase View, será un botón/casilla del Tablet layout/tablero
     * @return True si es un ImageButton
     */
    private boolean esImageButton(View v){
        boolean resultado = false;
        if (v.getClass().getName().equalsIgnoreCase("android.widget.ImageButton")){
            resultado = true;
        }
        return resultado;
    }

    /**
     * Reseteamos valores
     */
    private void reset(){
         int [][] tablero = new int[8][8];
         int hipoteElegida = R.drawable.flamenca;
         int hipoteElegidaFail = R.drawable.flamencafails;
         ConfNivel nivel = ConfNivel.PRINCIPIANTE;
         int contadorHipote = 0;
    }

    /**
     * Creamos dialogo con instrucciones y las mostramos si han pulsado en su item menú
     */
    private void mostrarInstrucciones(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage(R.string.txt_instrucciones)
                .setTitle(R.string.intrucciones)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog d = b.create();
        d.show();
    }

    /**
     * Creamos dialogo con los diferentes niveles para que elijan y si han pulsado en su item menú
     */
    private void elegirNivel(){
        CharSequence[] listaItems = new CharSequence[] {ConfNivel.PRINCIPIANTE.getNombreNivel(),ConfNivel.AMATEUR.getNombreNivel(),ConfNivel.AVANZADO.getNombreNivel()};
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(R.string.conf)
                .setSingleChoiceItems(listaItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        establecerNivel(ConfNivel.dameEnum( i + 1));
                    }
                })
                .setPositiveButton(R.string.ok,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
                });
        AlertDialog d = b.create();
        d.show();
    }

    /**
     * Establece el nivel que haya elegido el jugardor. Lo guarda en el atributo de clase.
     * @param n Dato escogido en el menú
     */
    private void establecerNivel(ConfNivel n){
        switch (n){
            case PRINCIPIANTE:
                nivel = ConfNivel.PRINCIPIANTE;
                break;
            case AMATEUR:
                nivel = ConfNivel.AMATEUR;
                break;
            case AVANZADO:
                nivel = ConfNivel.AVANZADO;
                break;
            default:
                nivel = ConfNivel.PRINCIPIANTE;
        }
    }

    /**
     * Muestra el dialogo para elegir un personaje usando una adaptador personalizado
     */
    private void choosePlayer(){
        Dialog dialogElegirPersonaje = new Dialog(this);
        dialogElegirPersonaje.setContentView(R.layout.spinner_elegir_personaje);
        dialogElegirPersonaje.setTitle("Personajes");
        AdapterPersonaje personajeAdapter = new AdapterPersonaje(this,R.id.spListaPersonajes,Hipoteminas.dameArrayNombreHipotes());
        Spinner spinner = dialogElegirPersonaje.findViewById(R.id.spListaPersonajes);
        spinner.setAdapter(personajeAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               hipoteElegida = Hipoteminas.dameHipote(i+1).getIdImagenGeneral();
               hipoteElegidaFail = Hipoteminas.dameHipote(i+1).getIdImagenFail();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        dialogElegirPersonaje.show();

    }
}
//Fin