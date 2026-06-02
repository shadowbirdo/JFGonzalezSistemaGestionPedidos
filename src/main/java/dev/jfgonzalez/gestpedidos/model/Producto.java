package dev.jfgonzalez.gestpedidos.model;

import dev.jfgonzalez.gestpedidos.exceptions.Msg;

/**
 * - Atributos: id, nombre, precioBase.
 * - Validación Crítica: Si se intenta asignar un precio negativo, debe lanzar una excepción controlada (ej. IllegalArgumentException).
 */
public class Producto {
    
    private int id;
    private String name;
    private double price;

    public Producto(int id, String name, double price){
        if (price < 0) throw new IllegalArgumentException(Msg.NEGATIVE_PRICE);

        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public double getPrice() {return price;}
    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException(Msg.NEGATIVE_PRICE);
        this.price = price;
    }

    public double calcFinalPrice() {
        return getPrice();
    };

    @Override
    public String toString(){
        return "{\"id\":\"%d\",\"name\":\"%s\",\"basePrice\":\"%.2f\"}".formatted(this.id, this.name, this.price);
    }
}
