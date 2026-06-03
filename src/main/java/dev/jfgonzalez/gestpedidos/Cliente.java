package dev.jfgonzalez.gestpedidos;

public class Cliente {
    // Atributos
    private int id;
    private String nombre;
    private String direccion;
    private String email;
    private int annosAntiguedad;
    private boolean esVip;
    private String pais;

    // Constructores
    /**
     * Construye un Cliente con los parámetros indicados.
     * @param id - Identificador único
     * @param nombre - Nombre completo del cliente
     * @param email - Correo electrónico
     * @param direccion - Dirección de envío
     * @param annosAntiguedad - Años de antigüedad del cliente el la tienda
     * @param esVip - Indica si el cliente es o no es un cliente VIP
     * @param pais - País de residencia del cliente
     */
    public Cliente(int id, String nombre, String email, String direccion, int annosAntiguedad, boolean esVip, String pais){
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
        this.annosAntiguedad = annosAntiguedad;
        this.esVip = esVip;
        this.pais = pais;
    }

    /**
     * Construye un Cliente con los parámetros indicados. A "email" y "dirección" se le asignan cadenas vacías.
     * @param id - Identificador único
     * @param nombre - Nombre completo del cliente
     * @param annosAntiguedad - Años de antigüedad del cliente el la tienda
     * @param esVip - Indica si el cliente es o no es un cliente VIP
     * @param pais - País de residencia del cliente
     */
    public Cliente(int id, String nombre, int annosAntiguedad, boolean esVip, String pais) {
        this(id,nombre,"","",annosAntiguedad,esVip,pais);
    }

    /**
     * Construye un Cliente con los parámetros "id" y "nombre". Por defecto el resto de parámetros son 0, false o null.
     * @param id - Identificador único
     * @param nombre - Nombre completo del cliente
     */
    public Cliente(int id, String nombre){
        this(id,nombre,"","",0,false,"");
    }

    /**
     * Construye un Cliente vacío.
     */
    public Cliente(){}

    // Getters & Setters
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getDireccion() {return direccion;}
    public void setDireccion(String direccion) {this.direccion = direccion;}
    public int getAnnosAntiguedad() {return annosAntiguedad;}
    public void setAnnosAntiguedad(int annosAntiguedad) {this.annosAntiguedad = annosAntiguedad;}
    public boolean getEsVip() {return esVip;}
    public void setIsVip(boolean esVip) {this.esVip = esVip;}
    public String getPais() {return pais;}
    public void setPais(String pais) {this.pais = pais;}

    // Métodos
    @Override
    public String toString(){
        return "{\"id\":\"%d\",\"nombre\":\"%s\",\"direccion\":\"%s\",\"email\":\"%s\",\"annosAntiguedad\":\"%d\",\"esVip\":\"%s\",\"pais\":\"%s\"}".formatted(
            this.id, this.nombre, this.direccion, this.email, this.annosAntiguedad, this.esVip, this.pais
        );
    }
}
