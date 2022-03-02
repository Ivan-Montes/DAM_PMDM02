package main.montesestebanivan_pmdm02_tarea;

import java.util.stream.Stream;

public enum Hipoteminas {

    FLAMENCA(1,"Flamenca",R.drawable.flamenca,R.drawable.flamencafails),
    MATRIX(2,"Matrix",R.drawable.matrix,R.drawable.matrixfails),
    CAMUFLAJE(3,"Camuflaje",R.drawable.camuflaje,R.drawable.camuflajefails);

    private int id;
    private String nombre;
    private int idImagenGeneral;
    private int idImagenFail;

    private Hipoteminas(int id, String nom, int idImagenGeneral, int idImagenFail){
        this.id = id;
        this.nombre = nom;
        this.idImagenGeneral = idImagenGeneral;
        this.idImagenFail = idImagenFail;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIdImagenGeneral() {
        return idImagenGeneral;
    }

    public int getIdImagenFail() {
        return idImagenFail;
    }

    public static Hipoteminas dameHipote(int id){
        return (id == 3)?Hipoteminas.CAMUFLAJE:(id == 2)? Hipoteminas.MATRIX:Hipoteminas.FLAMENCA;
    }

    public static String[] dameArrayNombreHipotes(){
        return Stream.of(Hipoteminas.values())
                .map( h -> h.getNombre())
                .toArray(String[]::new);
    }
    public static Integer[] dameArrayImgGeneralHipotes(){
        return Stream.of(Hipoteminas.values())
                .map( h -> h.getIdImagenGeneral())
                .toArray(Integer[]::new);
    }
}
