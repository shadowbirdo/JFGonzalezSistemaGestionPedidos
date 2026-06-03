package dev.jfgonzalez.gestpedidos;

/**
 * - Atributos: id, nombre, precioBase.
 * - Validación Crítica: Si se intenta asignar un precio negativo, debe lanzar una excepción controlada (ej. IllegalArgumentException).
 */
public class Producto {
    // Constantes
    public final static String NEGATIVE_PRICE_EXCEPTION_MESSAGE = "Attempt to assign negative int to basePrice. basePrice cannot be negative.";

    // Atributos
    private int id;
    private String nombre;
    private double precioBase;

    // Constructores
    /**
     * Construye un Producto con los parámetros indicados.
     * @param id - Identificador único
     * @param nombre - Nombre del producto
     * @param precioBase - Precio base del producto
     */
    public Producto(int id, String nombre, double precioBase){
        if (precioBase < 0) throw new IllegalArgumentException(NEGATIVE_PRICE_EXCEPTION_MESSAGE);

        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
    }

    /**
     * Construye un Producto a partir de otro objeto del mismo tipo.
     * @param producto - Producto a copiar
     */
    public Producto(Producto producto){
        this(
            producto.getId(),
            producto.getNombre(),
            producto.getPrecioBase()
        );
    }

    // Getters & Setters
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public double getPrecioBase() {return precioBase;}
    public void setPrecioBase(double precio) {
        if (precio < 0) throw new IllegalArgumentException(NEGATIVE_PRICE_EXCEPTION_MESSAGE);
        this.precioBase = precio;
    }

    // Métodos
    /**
     * Calcula el precio final del producto.
     * @return Precio final del producto
     */
    public double calcularPrecioFinal() {
        return getPrecioBase();
    };

    @Override
    public String toString(){
        return "{\"id\":\"%d\",\"nombre\":\"%s\",\"precioBase\":\"%.2f\"}".formatted(this.id, this.nombre, this.precioBase);
    }

}
