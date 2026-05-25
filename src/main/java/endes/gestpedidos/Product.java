package endes.gestpedidos;
/**
 * - Atributos: id, nombre, precioBase.
 * - Validación Crítica: Si se intenta asignar un precio negativo, debe lanzar una excepción controlada (ej. IllegalArgumentException).
 */
public abstract class Product {
    private int id;
    private String name;
    private float price;

    protected Product(int id, String name, float price){
        this.id = id;
        this.name = name;
        this.price = price;

        if (price < 0) throw new IllegalArgumentException("El precio no puede ser negativo.");
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public float getPrice() {return price;}
    public void setPrice(float price) {this.price = price;}

    public String toString(){
        return name + " => " + price;
    }

    public float calcPrice(){
        return this.price;
    }
}
