package dev.jfgonzalez.gestpedidos.model;

import java.util.List;
import java.util.stream.IntStream;

import dev.jfgonzalez.gestpedidos.exceptions.Msg;
import dev.jfgonzalez.gestpedidos.util.TableBuilder;



import java.util.ArrayList;

/**
 * - Atributos: idPedido, Cliente, Lista de Productos, cantidades.
 * - Método calcularTotal(): Debe sumar el precio de todos los productos (con IVA aplicado) y sumar los gastos de envío.
 * - No se puede procesar un pedido si la lista de productos está vacía (lanzar excepción).
 * - Debe permitir añadir o eliminar productos dinámicamente antes del cálculo final.
 */
public class Order {

    /**
     * Gestionar pedidos, donde cada pedido está asociado a un cliente y puede contener
     * uno o varios productos (usa agregación o composición).
     * Calcular el importe total del pedido, teniendo en cuenta, por ejemplo:
     * ○​ IVA o descuentos para productos digitales.
     * ○​ Coste de envío para productos físicos.
     */
    private int id;
    private Customer customer;
    private String status; // Pending, Delivered, Canceled
    private List<Product> productList;
    private List<Integer> amountList;
    private float shippingFeeByWeight, shippingFeeByZone;

    public Order(
        int id, Customer customer, String status,
        ArrayList<Product> productList, ArrayList<Integer> amountList
    ){
        this.id = id;
        this.customer = customer;
        this.status = status;
        this.productList = productList;
        this.amountList = amountList;
        this.shippingFeeByWeight = 0;
        this.shippingFeeByZone = 0;
    }
    
    public Order(int id, Customer client){
        this(id, client, "Pending", new ArrayList<Product>(), new ArrayList<Integer>());
    }

    public List<Product> getProductList() {
        return productList;
    }

    public List<Integer> getAmountList() {
        return amountList;
    }

    public void addProduct(Product product, int amount){
        this.productList.add(product);
        this.amountList.add(amount);
    }

    public void addProduct(Product product){
        this.addProduct(product,1);
    }

    public int delProduct(Product product){
        int delIndex = this.productList.indexOf(product);
        if (delIndex != -1) {
            this.productList.remove(delIndex);
            this.amountList.remove(delIndex);
        }
        return delIndex;
    }

    public float calcTotal(){
        if (productList.isEmpty()) throw new IllegalStateException(Msg.EMPTY_ORDER);
        float totalPrice = 0;
        for(int i=0; i < productList.size(); i++) {
            Product product = this.productList.get(i);
            int amount = this.amountList.get(i);
            if (product instanceof PhysicalProduct p) calcShippingFee(p);
            totalPrice += product.calcFinalPrice() * amount;
        }
        return totalPrice + this.shippingFeeByWeight + this.shippingFeeByZone;
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
        summary.append("%d - %s - %s\n".formatted(this.id, this.customer, this.status));

        summary.append(new TableBuilder(
            List.of("Producto", "Cantidad"),
            this.prepareTableBody()
        ).getRenderedTable());

        return summary.toString();
    }

    private List<List<String>> prepareTableBody() {
        int size = Math.min(productList.size(),amountList.size());

        return IntStream.range(0, size).mapToObj(
            i -> List.of(
                productList.get(i).getName(),
                String.valueOf(amountList.get(i))
            )
        ).toList();
    }

    private void calcShippingFee(PhysicalProduct product) {
        if (product.getDeliveryFee() > this.shippingFeeByZone) this.shippingFeeByZone = product.getDeliveryFee();
        this.shippingFeeByWeight += product.getWeight() < 10 ? 1 : 5;
    }
}

