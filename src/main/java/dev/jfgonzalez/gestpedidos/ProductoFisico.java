package dev.jfgonzalez.gestpedidos;

public class ProductoFisico extends Producto{
    // Constantes
    private static final float ENVIO_NACIONAL = 0;
    private static final float ENVIO_VECINO = 5;
    private static final float ENVIO_OTRO = 10;
    
    // Atributos
    private double peso;

    // Constructores
    /**
     * Construye un ProductoFisico con los parámetros indicados.
     * @param id - Identificador único
     * @param nombre - Nombre
     * @param precioBase - Precio base
     * @param peso - Peso en Kg
     */
    public ProductoFisico(int id, String nombre, double precioBase, double peso){
        super(id,nombre,precioBase);
        this.peso = peso;
    }

    /**
     * Construye un ProductoFísico con peso 0.
     * @param id - Identificador único
     * @param nombre - Nombre
     * @param precioBase - Precio base
     */
    public ProductoFisico(int id, String name, float price) {
        this(id,name,price,0);
    }

    /**
     * Construye un ProductoFisico a partir de otro objeto del mismo tipo.
     * @param pf - Producto a copiar
     */
    public ProductoFisico(ProductoFisico pf) {
        this(
            pf.getId(),
            pf.getNombre(),
            pf.getPrecioBase(),
            pf.getPeso()
        );
    }

    // Getters & Setters
    public double getPeso() {return peso;}
    public void setPeso(float peso) {this.peso = peso;}

    // Métodos
    /**
     * Calcula el coste de envío en función de la zona de destino. 0€ para envíos en España, 5€ Francia, Italia y Portugal, y 10€ para el resto.
     * @param zonaDestino - País al que se envía el producto
     * @return Coste de envío
     */
    public double costeEnvio(String zonaDestino) {
        switch (zonaDestino.toLowerCase()) {
            case "españa":
                return ENVIO_NACIONAL;
            case "francia","italia","portugal":
                return ENVIO_VECINO;
            default:
                return ENVIO_OTRO;
        }
    }

    @Override
    public String toString(){
        String superToString = super.toString();
        return "%s,\"weight\":\"%.2f\"}".formatted(
            superToString.substring(0, superToString.length()-1),
            this.peso
        );
    }
}