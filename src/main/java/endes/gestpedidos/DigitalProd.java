package endes.gestpedidos;

/**
 * Debe implementar el método aplicarIVA(String tipoIva). Los tipos válidos son "GENERAL" (21%), "REDUCIDO" (10%) y "SUPER" (4%).
 */
public class DigitalProd extends Product{
    private String license;
    private float sizeInMb;
    private boolean tieneIva;

    public DigitalProd(int id, String name, float price) {
        this(id, name, price, null, 0);
    }

    public DigitalProd(int id, String name, float price, String license, float sizeInMb){
        super(id, name, price);
        this.license = license;
        this.sizeInMb = sizeInMb;
        this.tieneIva = false;
    }

    public String getLicense() {
        return license;
    }

    public float getSizeInMb() {
        return sizeInMb;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setSizeInMb(float sizeInMb) {
        this.sizeInMb = sizeInMb;
    }

    @Override
    public String toString(){
        return super.toString() + " License: " + this.license + " | Size (MB): " + this.sizeInMb;
    }
    
    @Override
    public float calcPrice(){
        return super.getPrice() * 0.95f;
    }

    public void aplicarIva(String tipoIva) {
        if (this.tieneIva) return;

        switch (tipoIva) {
            case "GENERAL":
                this.setPrice(this.getPrice() * 1.21f);
                break;
            case "REDUCIDO":
                this.setPrice(this.getPrice() * 1.10f);
                break;
            case "SUPER":
                this.setPrice(this.getPrice() * 1.04f);
                break;
            default:
                throw new IllegalArgumentException("Tipo de IVA no válido.");
        }

        this.tieneIva = true;

    }
    
}
