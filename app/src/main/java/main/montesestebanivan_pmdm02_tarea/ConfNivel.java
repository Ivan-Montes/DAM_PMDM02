package main.montesestebanivan_pmdm02_tarea;

public enum ConfNivel {

    PRINCIPIANTE(1,"Principiante",8,10),
    AMATEUR(2,"Amateur",12,30),
    AVANZADO(3,"Avanzado",16,60);

    private int id;
    private String nombreNivel = "";
    private int numFilas = 0;
    private int numHipote = 0;

    private ConfNivel(int id, String nom, int filas, int hipotes){
        this.id = id;
        this.nombreNivel = nom;
        this.numFilas = filas;
        this.numHipote = hipotes;
    };

    public String getNombreNivel() {
        return nombreNivel;
    }

    public int getNumFilas() {
        return numFilas;
    }

    public int getNumHipote() {
        return numHipote;
    }

    public int getId() { return id;}

    public static ConfNivel dameEnum(int n){
        return (n == 3)?ConfNivel.AVANZADO:n==2?ConfNivel.AMATEUR:ConfNivel.PRINCIPIANTE;
    }
};
