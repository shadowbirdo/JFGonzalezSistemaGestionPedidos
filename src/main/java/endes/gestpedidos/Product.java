package endes.gestpedidos;

public abstract class Product {
    private String name;
    private float price;

    protected Product(String name, float price){
        this.name = name;
        this.price = price;

        if (price < 0) throw new IllegalArgumentException("El precio no puede ser negativo.");
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String toString(){
        return name + " => " + price;
    }

    public float calcPrice(){
        return this.price;
    }
}
