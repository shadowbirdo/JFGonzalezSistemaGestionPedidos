package endes.gestpedidos;

/**
 * Debe incluir un atributo peso y calcular el costeEnvio() basado en el peso y la zona de destino.
 * Estos valores deben ser 0€ para envíos en España, 5€ Francia, Italia y Portugal, y 10€ para el resto.
 */
public class PhysicalProd extends Product{
    private float peso;
    private float costeEnvio;

    public PhysicalProd(int id, String name, float price, float peso){
        super(id,name,price);
        this.peso = peso;
        this.costeEnvio = 0;
    }

    public float getPeso() {return peso;}
    public void setPeso(float peso) {this.peso = peso;}

    @Override
    public String toString(){
        return super.toString() + " | Delivery fee: " + this.costeEnvio;
    }

    @Override
    public float calcPrice(){
        return super.getPrice() + this.costeEnvio;
    }

    public void costeEnvio(String zonaDestino) {
        switch (zonaDestino) {
            case "España":
                this.costeEnvio = 0;
                break;
            case "Francia":
            case "Italia":
            case "Portugal":
                this.costeEnvio = 5;
                break;
            default:
                this.costeEnvio = 10;
                break;
        }
    }
}