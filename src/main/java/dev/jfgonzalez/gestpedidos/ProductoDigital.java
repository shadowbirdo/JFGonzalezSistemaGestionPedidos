package dev.jfgonzalez.gestpedidos;

/**
 * Debe implementar el método aplicarIVA(String tipoIva). Los tipos válidos son "GENERAL" (21%), "REDUCIDO" (10%) y "SUPER" (4%).
 */
public class ProductoDigital extends Producto{
    // Constantes
    public final static String INVALID_IVA_EXCEPTION_MESSAGE = "IVA type not valid. Valid types are GENERAL, REDUCIDO and SUPER.";

    public static final double IVA_GENERAL = .21;
    public static final double IVA_REDUCIDO = .10;
    public static final double IVA_SUPER = .04;
    
    // Atributos
    private String licencia;
    private double pesoEnMB;
    private double iva;

    // Constructores
    /**
     * Construye un ProductoDigital con los parámetros indicados.
     * @param id - Identificador único
     * @param name - Nombre
     * @param precioBase - Precio base del producto
     * @param license - Licencia del producto digital
     * @param pesoEnMB - Peso en megabytes del producto
     */
    public ProductoDigital(
        int id, String name, double precioBase, String license, double pesoEnMB, double iva
    ){
        super(id, name, precioBase);
        this.licencia = license;
        this.pesoEnMB = pesoEnMB;
        this.iva = iva;
    }

    /**
     * Construye un ProductoDigital con los parámetros indicados. Licencia será null, peso en MB será 0 e iva será IVA_GENERAL.
     * @param id - Identificador Único
     * @param nombre - Nombre
     * @param precioBase - Precio base del producto
     */
    public ProductoDigital(int id, String nombre, double precioBase) {
        this(id, nombre, precioBase, null, 0, IVA_GENERAL);
    }

    // Getters & Setters
    public String getLicencia() {return licencia;}
    public void setLicencia(String license) {this.licencia = license;}
    public double getPesoEnMB() {return pesoEnMB;}
    public void setSizeInMB(float sizeInMB) {this.pesoEnMB = sizeInMB;}
    public double getIva() {return iva;}
    public void setIva(double iva) {this.iva = iva;}

    // Métodos
    /**
     * Calcula el coste del IVA para un producto.
     * @param tipoIva - El tipo de IVA a aplicar.
     * @return El IVA que habrá que sumarle al producto para obtener su precio final
     * @throws IllegalArgumentException tipoIva solo puede ser "GENERAL", "REDUCIDO" o "SUPER"
     */
    public void aplicarIva(String tipoIva) {
        switch (tipoIva.toUpperCase()) {
            case "GENERAL":
                this.iva = IVA_GENERAL;
            case "REDUCIDO":
                this.iva = IVA_REDUCIDO;
            case "SUPER":
                this.iva = IVA_SUPER;
            default:
                throw new IllegalArgumentException(INVALID_IVA_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public double calcularPrecioFinal(){
        return this.getPrecioBase() * (1 + this.iva);
    }

    @Override
    public String toString(){
        return "%s,\"licencia\":\"%s\",\"pesoEnMB\":\"%.2f\"}".formatted(
            super.toString().substring(0, super.toString().length()-1),
            this.licencia, this.pesoEnMB
        );
    }
}
