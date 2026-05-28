package dev.jfgonzalez.gestpedidos.model;

import java.util.List;
import com.jakewharton.picnic.*;



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

    public Order(int id, Customer customer, String status, List<Product> productList, List<Integer> amountList){
        this.id = id;
        this.customer = customer;
        this.status = status;
        this.productList = productList;
        this.amountList = amountList;
    }
    
    public Order(int id, Customer client){
        this(id, client, "Pending", new ArrayList<Product>(), new ArrayList<Integer>());
    }

    public void addProduct(Product product, int amount){
        this.productList.add(product);
        this.amountList.add(amount);
    }

    public void addProduct(Product product){
        this.addProduct(product,1);;
    }

    public void delProduct(Product product){
        int delIndex = this.productList.indexOf(product);
        this.productList.remove(delIndex);
        this.amountList.remove(delIndex);
    }


    public float calcTotal(){
        if (productList.isEmpty()) throw new IllegalStateException("El pedido está vacío");
        float totalPrice = 0;
        for(int i=0; i < productList.size(); i++) {
            Product product = this.productList.get(i);
            int amount = this.amountList.get(i);
            totalPrice += product.calcFinalPrice() * amount;
        }
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
        summary.append("%d - %s - %s".formatted(this.id, this.customer, this.status));

        summary.append(buildProductTable());

        summary.append("Final price: %.2f".formatted(this.calcTotal()));

        return summary.toString();
    }

    private String buildProductTable() {
        CellStyle cellStyle = new CellStyle.Builder()
            .setBorder(true)
            .build();
        
        TableSection header = new TableSection.Builder()
            .addRow(new Row.Builder()
                .addCell(new Cell.Builder("Product").build())
                .addCell(new Cell.Builder("Amount").build())
                .build()
            ).build();

        TableSection.Builder bodyBuilder = new TableSection.Builder();
        for (int i = 0; i < productList.size(); i++) {
            Product product = this.productList.get(i);
            int amount = this.amountList.get(i);
            
            bodyBuilder.addRow(new Row.Builder()
                .addCell(new Cell.Builder(product.getName()).build())
                .addCell(new Cell.Builder(String.valueOf(amount)).build())
                .build()
            );
        }
        TableSection body = bodyBuilder.build();

        Table table = new Table.Builder()
            .setCellStyle(cellStyle)
            .setHeader(header)
            .setBody(body)
            .build();

        return TextRendering.render(table);
    }
}

