package dev.jfgonzalez.gestpedidos.model;

import dev.jfgonzalez.gestpedidos.exceptions.Msg;


/**
 * Debe implementar el método aplicarIVA(String tipoIva). Los tipos válidos son "GENERAL" (21%), "REDUCIDO" (10%) y "SUPER" (4%).
 */
public class DigitalProduct extends Product{
    private static final float IVA_GENERAL = 1.21f;
    private static final float IVA_REDUCIDO = 1.1f;
    private static final float IVA_SUPER = 1.04f;
    
    private String license;
    private float sizeInMB;
    private float ivaMult;

    public DigitalProduct(int id, String name, float price) {
        this(id, name, price, null, 0);
    }

    public DigitalProduct(int id, String name, float price, String license, float sizeInMB){
        super(id, name, price);
        this.license = license;
        this.sizeInMB = sizeInMB;
        this.ivaMult = 1;
    }

    public String getLicense() {return license;}
    public void setLicense(String license) {this.license = license;}
    public float getSizeInMB() {return sizeInMB;}
    public void setSizeInMB(float sizeInMB) {this.sizeInMB = sizeInMB;}
    public float getIvaMult() {
        return ivaMult;
    }

    public void applyIva(String tipoIva) {
        switch (tipoIva.toUpperCase()) {
            case "GENERAL":
                this.ivaMult = IVA_GENERAL;
                break;
            case "REDUCIDO":
                this.ivaMult = IVA_REDUCIDO;
                break;
            case "SUPER":
                this.ivaMult = IVA_SUPER;
                break;
            default:
                throw new IllegalArgumentException(Msg.INVALID_IVA);
        }
    }

    @Override
    public float calcFinalPrice(){return this.getPrice() * this.ivaMult;}

    @Override
    public String toString(){
        return "%s,\"license\":\"%s\",\"sizeInMB\":\"%.2f\",\"ivaMult\":\"%.2f\"}".formatted(
            super.toString().substring(0, super.toString().length()-1),
            this.license, this.sizeInMB, this.ivaMult
        );
    }
}
