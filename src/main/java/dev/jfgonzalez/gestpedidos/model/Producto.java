package dev.jfgonzalez.gestpedidos.model;

import dev.jfgonzalez.gestpedidos.exceptions.Msg;

/**
 * - Atributos: id, nombre, precioBase.
 * - Validación Crítica: Si se intenta asignar un precio negativo, debe lanzar una excepción controlada (ej. IllegalArgumentException).
 */
public class Producto {
    
    private int id;
    private String nombre;
    private double precioBase;

    public Producto(int id, String nombre, double precioBase){
        if (precioBase < 0) throw new IllegalArgumentException(Msg.NEGATIVE_PRICE);

        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public double getPrecioBase() {return precioBase;}
    public void setPrecioBase(double precio) {
        if (precio < 0) throw new IllegalArgumentException(Msg.NEGATIVE_PRICE);
        this.precioBase = precio;
    }

    public int getSysId() {return System.identityHashCode(this);}

    public double calcFinalPrice() {
        return getPrecioBase();
    };

    @Override
    public String toString(){
        return "{\"id\":\"%d\",\"nombre\":\"%s\",\"precioBase\":\"%.2f\"}".formatted(this.id, this.nombre, this.precioBase);
    }

}
