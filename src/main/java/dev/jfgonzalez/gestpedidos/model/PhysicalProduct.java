package dev.jfgonzalez.gestpedidos.model;

/**
 * Debe incluir un atributo peso y calcular el costeEnvio() basado en el peso y la zona de destino.
 * Estos valores deben ser 0€ para envíos en España, 5€ Francia, Italia y Portugal, y 10€ para el resto.
 */
public class PhysicalProduct extends Product{
    private float weight;
    private float deliveryFee;

    public PhysicalProduct(int id, String name, float price, float weight, float deliveryFee){
        super(id,name,price);
        this.weight = weight;
        this.deliveryFee = deliveryFee;
    }

    public PhysicalProduct(int id, String name, float price) {
        this(id,name,price,0,0);
        
    }

    public float getWeight() {return weight;}
    public void setWeight(float peso) {this.weight = peso;}
    public float getDeliveryFee() {return deliveryFee;}
    public void setDeliveryFee(float deliveryFee) {this.deliveryFee = deliveryFee;}

    @Override
    public String toString(){
        String superToString = super.toString();
        return "%s,\"weight\":\"%.2f\",\"deliveryFee\":\"%.2f\"}".formatted(
            superToString.substring(0, superToString.length()-1),
            this.weight, this.deliveryFee
        );
    }

    @Override
    public float calcFinalPrice(){
        return this.getPrice() + this.deliveryFee;
    }

    public void applyDeliveryFee(String deliveryZone) {
        switch (deliveryZone.toLowerCase()) {
            case "españa":
                this.deliveryFee = 0;
                break;
            case "francia":
            case "italia":
            case "portugal":
                this.deliveryFee = 5;
                break;
            default:
                this.deliveryFee = 10;
                break;
        }
    }
}