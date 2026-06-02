package dev.jfgonzalez.gestpedidos.model;

import dev.jfgonzalez.gestpedidos.exceptions.Msg;


/**
 * Debe implementar el método aplicarIVA(String tipoIva). Los tipos válidos son "GENERAL" (21%), "REDUCIDO" (10%) y "SUPER" (4%).
 */
public class ProductoDigital extends Producto{
    public static final double IVA_GENERAL = .21;
    public static final double IVA_REDUCIDO = .10;
    public static final double IVA_SUPER = .04;
    
    private String licencia;
    private double pesoEnMB;
    private double ivaMult;

    public ProductoDigital(
        int id, String name, double price, String license, double sizeInMB, double ivaMult
    ){
        super(id, name, price);
        this.licencia = license;
        this.pesoEnMB = sizeInMB;
        this.ivaMult = ivaMult;
    }

    public ProductoDigital(int id, String name, double price) {
        this(id, name, price, null, 0, 0);
    }

    public String getLicencia() {return licencia;}
    public void setLicencia(String license) {this.licencia = license;}
    public double getPesoEnMB() {return pesoEnMB;}
    public void setSizeInMB(float sizeInMB) {this.pesoEnMB = sizeInMB;}
    public double getIvaMult() {return ivaMult;}
    public void setIvaMult(double ivaMult) {this.ivaMult = ivaMult;}


    public double aplicarIva(String tipoIva) {
        double ivaPorc = 0;
        switch (tipoIva.toUpperCase()) {
            case "GENERAL":
                ivaPorc = IVA_GENERAL;
                break;
            case "REDUCIDO":
                ivaPorc = IVA_REDUCIDO;
                break;
            case "SUPER":
                ivaPorc = IVA_SUPER;
                break;
            default:
                throw new IllegalArgumentException(Msg.INVALID_IVA);
        }
        this.ivaMult = 1 + ivaPorc;
        return this.getPrecioBase() * ivaPorc;
    }

    @Override
    public double calcFinalPrice(){return this.getPrecioBase() * this.ivaMult;}

    @Override
    public String toString(){
        return "%s,\"license\":\"%s\",\"sizeInMB\":\"%.2f\",\"iva\":\"%.2f\"}".formatted(
            super.toString().substring(0, super.toString().length()-1),
            this.licencia, this.pesoEnMB, this.ivaMult
        );
    }
}
