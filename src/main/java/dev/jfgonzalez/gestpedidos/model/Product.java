package dev.jfgonzalez.gestpedidos.model;

import dev.jfgonzalez.gestpedidos.exceptions.Msg;

/**
 * - Atributos: id, nombre, precioBase.
 * - Validación Crítica: Si se intenta asignar un precio negativo, debe lanzar una excepción controlada (ej. IllegalArgumentException).
 */
public abstract class Product {
    
    private int id;
    private String name;
    private float price;

    protected Product(int id, String name, float price){
        if (price < 0) throw new IllegalArgumentException(Msg.NEGATIVE_PRICE);

        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public float getPrice() {return price;}
    public void setPrice(float price) {
        if (price < 0) throw new IllegalArgumentException(Msg.NEGATIVE_PRICE);

        this.price = price;
    }

    public abstract float calcFinalPrice();

    @Override
    public String toString(){
        return "{\"id\":\"%d\",\"name\":\"%s\",\"basePrice\":\"%.2f\"}".formatted(this.id, this.name, this.price);
    }
}
