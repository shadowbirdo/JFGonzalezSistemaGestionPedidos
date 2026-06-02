package dev.jfgonzalez.gestpedidos.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
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
    private String estado; // Pending, Delivered, Canceled
    private List<Producto> productos;
    private Map<Integer, Integer> cantidades;
    private double shippingFeeByWeight, shippingFeeByZone;

    public Pedido(
        int id, Cliente cliente, String status,
        List<Producto> productos, Map<Integer,Integer> cantidades
    ){
        if (productos != null) {
            for (Producto p : productos) {
                if (p != null && (cantidades == null || !cantidades.containsKey(p.getId()))) {
                    throw new IllegalArgumentException();
                }
            }
        }
        this.idPedido = id;
        this.cliente = cliente;
        this.estado = status;
        this.productos = productos != null ? new ArrayList<>(productos) : new ArrayList<>();
        this.cantidades = cantidades != null ? new HashMap<>(cantidades) : new HashMap<>();
        this.shippingFeeByWeight = 0;
        this.shippingFeeByZone = 0;
    }
    
    public Pedido(int id, Cliente cliente, List<Producto> productos, Map<Integer,Integer> cantidades){
        this(id, cliente, "Pending", productos, cantidades);

    }

    public Pedido(int id, Cliente cliente){
        this(id, cliente, "Pending", new ArrayList<Producto>(), new HashMap<Integer,Integer>());
    }

    public int getIdPedido() {return idPedido;}
    public void setIdPedido(int idPedido) {this.idPedido = idPedido;}
    public Cliente getCliente() {return cliente;}
    public void setCliente(Cliente customer) {this.cliente = customer;}
    public List<Producto> getProductos() {return new ArrayList<>(productos);}
    public void setProductos(List<Producto> productos) {this.productos = new ArrayList<>(productos);}
    public Map<Integer,Integer> getCantidades() {return new HashMap<Integer,Integer>(cantidades);}
    public void setCantidades(Map<Integer, Integer> cantidades) {this.cantidades = new HashMap<>(cantidades);}

    public void addProducto(Producto product, int amount){
        this.productos.add(product);
        this.cantidades.put(product.getSysId(),amount);
    }

    public void addProducto(Producto product){
        this.addProducto(product,1);
    }

    public int delProduct(Producto product){
        int delIndex = this.productos.indexOf(product);
        if (delIndex != -1) {
            this.productos.remove(delIndex);
            this.cantidades.remove(product.getSysId());
        }
        return delIndex;
    }

    public double calcularTotal(){
        if (productos.isEmpty()) throw new IllegalStateException(Msg.EMPTY_ORDER);
        float totalPrice = 0;

        this.shippingFeeByWeight = 0;
        this.shippingFeeByZone = 0;

        for(int i=0; i < productos.size(); i++) {
            Producto product = this.productos.get(i);
            int amount = this.cantidades.getOrDefault(product.getSysId(),1);
            if (product instanceof ProductoFisico p) calcShippingFee(p);
            totalPrice += product.calcFinalPrice() * amount;
        }

        double baseEnvio = 0;
        if (this.cliente != null && this.cliente.getPais() != null) {
            baseEnvio = calcularEnvio(this.cliente.getPais());
        }

        return totalPrice + this.shippingFeeByWeight + this.shippingFeeByZone + baseEnvio;
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
        summary.append("%d - %s - %s\n".formatted(this.idPedido, this.cliente, this.estado));

        summary.append(new TableBuilder(
            List.of("Producto", "Cantidad"),
            this.prepareTableBody()
        ).getRenderedTable());

        return summary.toString();
    }

    private List<List<String>> prepareTableBody() {
        int size = productos.size();

        return IntStream.range(0, size).mapToObj(
            i -> {
                Producto p = productos.get(i);
                return List.of(
                    productos.get(i).getNombre(),
                    String.valueOf(cantidades.getOrDefault(p.getSysId(),0))
                );
            }
        ).toList();
    }

    private void calcShippingFee(ProductoFisico product) {
        if (product.getDeliveryFee() > this.shippingFeeByZone) this.shippingFeeByZone = product.getDeliveryFee();
        this.shippingFeeByWeight += product.getPeso() < 10 ? 1 : 5;
    }

    public float calcularEnvio(String pais) {
        if (!this.productos.stream().anyMatch(prod -> prod instanceof ProductoFisico)) return 0;
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
        return this.productos.stream()
            .filter(p -> p instanceof ProductoDigital)
            .mapToDouble(p -> {
                ProductoDigital pd = (ProductoDigital) p;
                double ivaUd = pd.aplicarIva(tipoIva);
                int cantidad = this.cantidades.getOrDefault(pd.getSysId(), 1);
                return ivaUd * cantidad;
            })
            .sum();
        }

}

