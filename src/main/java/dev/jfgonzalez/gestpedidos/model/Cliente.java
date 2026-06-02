package dev.jfgonzalez.gestpedidos.model;

/**
 * - Atributos: id, nombre, añosAntiguedad (int), esVip (boolean), pais.
 * - Los años de antigüedad y el estado VIP determinarán el porcentaje de descuento de fidelidad que la Tienda aplicará sobre el total del Pedido.
 * - El país indicará el gasto de envío que haya que aplicar en los casos correspondientes.
 */
public class Cliente {
    private int id;
    private String nombre;
    private String direccion;
    private String email;
    private int annosAntiguedad;
    private boolean esVip;
    private String pais;

    public Cliente(int id, String nombre, String email, String direccion, int annosAntiguedad, boolean esVip, String pais){
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
        this.annosAntiguedad = annosAntiguedad;
        this.esVip = esVip;
        this.pais = pais;
    }

    public Cliente(int id, String nombre, int annosAntiguedad, boolean esVip, String pais) {
        this(id,nombre,"","",annosAntiguedad,esVip,pais);
    }

    public Cliente(int id, String nombre){
        this(id,nombre,"","",0,false,"");
    }

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

    public String toString(){
        return "{\"id\":\"%d\",\"nombre\":\"%s\",\"direccion\":\"%s\",\"email\":\"%s\",\"annosAntiguedad\":\"%d\",\"esVip\":\"%s\",\"pais\":\"%s\"}".formatted(
            this.id, this.nombre, this.direccion, this.email, this.annosAntiguedad, this.esVip, this.pais
        );
    }
}
