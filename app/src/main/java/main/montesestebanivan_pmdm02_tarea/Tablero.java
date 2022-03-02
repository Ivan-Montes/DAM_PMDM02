package main.montesestebanivan_pmdm02_tarea;

import java.util.Arrays;
import java.util.function.*;
import java.util.Random;

public class Tablero {

    private ConfNivel nivel;
    private Function<Integer,Integer> dameCoordenada = (limite)-> new Random().nextInt(limite);

    public Tablero(ConfNivel confNivel){
        this.nivel = confNivel;
    }

    public int[][] crearArrayTablero(){
        int[][] arrayTablero = arrayTablero = new int[nivel.getNumFilas()][nivel.getNumFilas()];
        rellenarConCeros(arrayTablero);
        addHipote(arrayTablero);
        calcularHipotesCercanas(arrayTablero);
        return arrayTablero;
    }

    private void rellenarConCeros(int[][] arrayTablero){
        Arrays.stream(arrayTablero)
                .forEach( (i)-> Arrays.fill(i, 0) );
    }

    private void addHipote(int[][] arrayTablero){
        int contadorHipote = 0;
        int casilla = 0;
        int x = 0;
        int y = 0;

        while ( contadorHipote < nivel.getNumHipote() ){

            x = dameCoordenada.apply(nivel.getNumFilas());
            y = dameCoordenada.apply(nivel.getNumFilas());
            casilla = arrayTablero[x][y];

            if (casilla == 0){
                arrayTablero[x][y] = -1;
                contadorHipote++;
            }
        }
    }

    private void calcularHipotesCercanas(int[][] arrayTablero){
        int x = 0;
        int y = 0;
        for(x = 0; x < nivel.getNumFilas(); x++){
            for( y = 0; y < nivel.getNumFilas(); y++){
                if ( arrayTablero[x][y] == -1 ) {
                    if ( ( x - 1 >= 0 ) && ( y - 1 >= 0 ) && arrayTablero[ x - 1 ][ y - 1] != -1 ){
                        arrayTablero[ x - 1 ][ y - 1 ] += 1;
                    }
                    if ( ( y - 1 >= 0 ) && arrayTablero[ x ][ y - 1 ] != -1 ){
                        arrayTablero[ x ][ y - 1 ] += 1;
                    }
                    if ( ( x + 1 < nivel.getNumFilas()  &&  y - 1 >= 0 ) && arrayTablero[ x + 1 ][ y - 1 ] != -1){
                        arrayTablero[ x + 1 ][ y - 1 ] += 1;
                    }
                    if ( x + 1 < nivel.getNumFilas() && arrayTablero[ x + 1 ][ y ] != -1){
                        arrayTablero[ x + 1 ][ y ] += 1;
                    }
                    if ( (x + 1 < nivel.getNumFilas()) && (y + 1 < nivel.getNumFilas()) && arrayTablero[ x + 1 ][ y + 1 ] != -1){
                        arrayTablero[ x + 1 ][ y + 1 ] += 1;
                    }
                    if ( y + 1 < nivel.getNumFilas() && arrayTablero[ x  ][ y + 1 ] != -1 ){
                        arrayTablero[ x ][ y + 1 ] += 1;
                    }
                    if ( ( x - 1 >= 0 && y + 1 < nivel.getNumFilas() ) && arrayTablero[ x - 1 ][ y + 1 ] != -1 ){
                        arrayTablero[ x - 1 ][ y + 1 ] += 1;
                    }
                    if ( x - 1 >= 0 && arrayTablero[ x - 1 ][ y ] != -1 ){
                        arrayTablero[ x -1 ][ y ] += 1;
                    }
                }
            }
        }
    }



}
