package dev.jfgonzalez.gestpedidos.model;

/**
 * Debe incluir un atributo peso y calcular el costeEnvio() basado en el peso y la zona de destino.
 * Estos valores deben ser 0€ para envíos en España, 5€ Francia, Italia y Portugal, y 10€ para el resto.
 */
public class ProductoFisico extends Producto{
    private static final float ENVIO_NACIONAL = 0;
    private static final float ENVIO_EUWEST = 5;
    private static final float ENVIO_OTRO = 10;
    
    private double peso;
    private double deliveryFee;

    public ProductoFisico(int id, String name, double price, double weight, double deliveryFee){
        super(id,name,price);
        this.peso = weight;
        this.deliveryFee = deliveryFee;
    }

    public ProductoFisico(int id, String name, double price, double peso) {
        this(id,name,price,peso,0);
    }

    public ProductoFisico(int id, String name, float price) {
        this(id,name,price,0,0);
    }

    public double getPeso() {return peso;}
    public void setPeso(float peso) {this.peso = peso;}
    public double getDeliveryFee() {return deliveryFee;}
    public void setDeliveryFee(double deliveryFee) {this.deliveryFee = deliveryFee;}

    @Override
    public String toString(){
        String superToString = super.toString();
        return "%s,\"weight\":\"%.2f\",\"deliveryFee\":\"%.2f\"}".formatted(
            superToString.substring(0, superToString.length()-1),
            this.peso, this.deliveryFee
        );
    }

    @Override
    public double calcFinalPrice(){
        return this.getPrecioBase() + this.deliveryFee;
    }

    public void applyDeliveryFee(String deliveryZone) {
        switch (deliveryZone.toLowerCase()) {
            case "españa":
                this.deliveryFee = ENVIO_NACIONAL;
                break;
            case "francia":
            case "italia":
            case "portugal":
                this.deliveryFee = ENVIO_EUWEST;
                break;
            default:
                this.deliveryFee = ENVIO_OTRO;
                break;
        }
    }
}