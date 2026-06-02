package dev.jfgonzalez.gestpedidos.model;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import dev.jfgonzalez.gestpedidos.exceptions.Msg;
import dev.jfgonzalez.gestpedidos.util.TableBuilder;



import java.util.ArrayList;
import java.util.HashMap;

/**
 * - Atributos: idPedido, Cliente, Lista de Productos, cantidades.
 * - Método calcularTotal(): Debe sumar el precio de todos los productos (con IVA aplicado) y sumar los gastos de envío.
 * - No se puede procesar un pedido si la lista de productos está vacía (lanzar excepción).
 * - Debe permitir añadir o eliminar productos dinámicamente antes del cálculo final.
 */
public class Pedido {
    public final static String PRODUCT_LIST_EMPTY_EXCEPTION_MESSAGE = "Could not process order. Order is empty.";

    /**
     * Gestionar pedidos, donde cada pedido está asociado a un cliente y puede contener
     * uno o varios productos (usa agregación o composición).
     * Calcular el importe total del pedido, teniendo en cuenta, por ejemplo:
     * ○​ IVA o descuentos para productos digitales.
     * ○​ Coste de envío para productos físicos.
     */
    private int idPedido;
    private Cliente cliente;
    private String status; // Pending, Delivered, Canceled
    private List<Producto> productos;
    private Map<Integer, Integer> cantidades;
    private double shippingFeeByWeight, shippingFeeByZone;

    public Pedido(
        int id, Cliente cliente, String status,
        ArrayList<Producto> productList, HashMap<Integer,Integer> amountList
    ){
        this.idPedido = id;
        this.cliente = cliente;
        this.status = status;
        this.productos = productList;
        this.cantidades = amountList;
        this.shippingFeeByWeight = 0;
        this.shippingFeeByZone = 0;
    }
    
    public Pedido(int id, Cliente cliente, List<Producto> productos, Map<Integer,Integer> cantidades){
        this.idPedido = id;
        this.cliente = cliente;
        this.productos = productos;
        this.cantidades = cantidades;
    }

    public Pedido(int id, Cliente cliente){
        this(id, cliente, "Pending", new ArrayList<Producto>(), new HashMap<Integer,Integer>());
    }

    public int getIdPedido() {
        return idPedido;
    }
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente customer) {
        this.cliente = customer;
    }
    

    public List<Producto> getProductos() {
        return productos;
    }

    public Map<Integer,Integer> getCantidades() {
        return cantidades;
    }

    public void addProducto(Producto product, int amount){
        this.productos.add(product);
        this.cantidades.put(product.getId(),amount);
    }

    public void addProducto(Producto product){
        this.addProducto(product,1);
    }

    public int delProduct(Producto product){
        int delIndex = this.productos.indexOf(product);
        if (delIndex != -1) {
            this.productos.remove(delIndex);
            this.cantidades.remove(delIndex);
        }
        return delIndex;
    }

    public double calcularTotal(){
        if (productos.isEmpty()) throw new IllegalStateException(Msg.EMPTY_ORDER);
        float totalPrice = 0;
        for(int i=0; i < productos.size(); i++) {
            Producto product = this.productos.get(i);
            int amount = this.cantidades.get(i);
            if (product instanceof ProductoFisico p) calcShippingFee(p);
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
        summary.append("%d - %s - %s\n".formatted(this.idPedido, this.cliente, this.status));

        summary.append(new TableBuilder(
            List.of("Producto", "Cantidad"),
            this.prepareTableBody()
        ).getRenderedTable());

        return summary.toString();
    }

    private List<List<String>> prepareTableBody() {
        int size = Math.min(productos.size(),cantidades.size());

        return IntStream.range(0, size).mapToObj(
            i -> List.of(
                productos.get(i).getName(),
                String.valueOf(cantidades.get(i))
            )
        ).toList();
    }

    private void calcShippingFee(ProductoFisico product) {
        if (product.getDeliveryFee() > this.shippingFeeByZone) this.shippingFeeByZone = product.getDeliveryFee();
        this.shippingFeeByWeight += product.getPeso() < 10 ? 1 : 5;
    }

    public float calcularEnvio(String pais) {
        switch (pais.toLowerCase()) {
            case "españa":
                return 0;
            case "francia":
            case "italia":
            case "portugal":
                return 5;
            default:
                return 10;
        }
    }

    public double calcularIva(String tipoIva){
        switch (tipoIva.toUpperCase()) {
            case "GENERAL":
                return ProductoDigital.IVA_GENERAL;
            case "SUPER":
                return ProductoDigital.IVA_SUPER;
            case "REDUCIDO":
                return ProductoDigital.IVA_SUPER;
            default:
                return ProductoDigital.IVA_GENERAL;
        }
    }

}

