package endes.gestpedidos;

import java.util.List;
import java.util.ArrayList;

public class Order {
    
    /**
     * Gestionar pedidos, donde cada pedido está asociado a un cliente y puede contener
     * uno o varios productos (usa agregación o composición).
     */

    /**
     * Calcular el importe total del pedido, teniendo en cuenta, por ejemplo:
     * ○​ IVA o descuentos para productos digitales.
     * ○​ Coste de envío para productos físicos.
     */

    private Client client;
    private List<Product> products;
    
    public Order(Client client){
        this(client, new ArrayList<>());
    }

    public Order(Client client, List<Product> products){
        this.client = client;
        this.products = products;
    }

    public void addProduct(Product productToAdd){
        this.products.add(productToAdd);
    }

    public float calcTotal(){
        if (products.isEmpty()) throw new IllegalStateException("El pedido está vacío");
        float totalPrice = 0;
        for(Product product : products)
            totalPrice += product.calcPrice();
        return totalPrice;
    }

    public String showSummary(){
    
        /**
         * Mostrar un resumen del pedido, incluyendo:
         * ○​ Datos del cliente.
         * ○​ Productos comprados.
         * ○​ Importe total.
         */

        StringBuilder summary = new StringBuilder();
        summary.append("Client data\n");
        summary.append("%s%n%n".formatted(client.toString()));
        summary.append("Products in order\n");
        
        for(Product product : products)
            summary.append("- %s%n".formatted(product.getName()));

        summary.append("Final price: %.2f".formatted(this.calcTotal()));

        return summary.toString();
    }
}
