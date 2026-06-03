package dev.jfgonzalez.gestpedidos;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * - Atributos: idPedido, Cliente, Lista de Productos, cantidades.
 * - Método calcularTotal(): Debe sumar el precio de todos los productos (con IVA aplicado) y sumar los gastos de envío.
 * - No se puede procesar un pedido si la lista de productos está vacía (lanzar excepción).
 * - Debe permitir añadir o eliminar productos dinámicamente antes del cálculo final.
 * Calcular el importe total del pedido, teniendo en cuenta, por ejemplo:
 * ○​ IVA o descuentos para productos digitales.
 * ○​ Coste de envío para productos físicos.
 */
public class Pedido {
    // Constantes
    public final static String PRODUCT_LIST_EMPTY_EXCEPTION_MESSAGE = "Could not process order. Order is empty.";

    // Atributos
    private int idPedido;
    private Cliente cliente;
    private String estado; // Pendiente, Entregado, Cancelado
    private List<Producto> productos;
    private Map<Integer, Integer> cantidades;

    // Constructores
    /**
     * Construye un Pedido con los parámetros indicados.
     * @param idPedido - Identificador único
     * @param cliente - Cliente al que se le asigna el Pedido
     * @param estado - Estado del pedido. Puede ser "PENDIENTE", "ENTREGADO" o "CANCELADO"
     * @param productos - Lista de productos que componen el pedido
     * @param cantidades - Mapa de cantidades que relaciona cada producto con las unidades del mismo en el pedido
     */
    public Pedido (
        int idPedido, Cliente cliente, String estado,
        List<Producto> productos, Map<Integer,Integer> cantidades
    ){
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.estado = estado;
        this.productos = productos != null ? new ArrayList<>(productos) : new ArrayList<>();
        this.cantidades = cantidades != null ? new HashMap<>(cantidades) : new HashMap<>();
    }
    
    /**
     * Construye un Pedido con los parámetros indicados. Se asigna "PENDIENTE" a "estado" por defecto.
     * @param idPedido - Identificador único
     * @param cliente - Cliente al que se le asigna el Pedido
     * @param productos - Lista de productos que componen el pedido
     * @param cantidades - Mapa de cantidades que relaciona cada producto con las unidades del mismo en el pedido
     */
    public Pedido(int id, Cliente cliente, List<Producto> productos, Map<Integer,Integer> cantidades){
        this(id, cliente, "PENDIENTE", productos, cantidades);

    }

    /**
     * Construye un Pedido con los parámetros indicados. "estado" es "PENDIENTE" y tanto productos como cantidades son objetos vacíos del tipo correspondiente.
     * @param idPedido - Identificador único
     * @param cliente - Cliente al que se le asigna el Pedido
     */
    public Pedido(int id, Cliente cliente){
        this(id, cliente, "PENDIENTE", new ArrayList<Producto>(), new HashMap<Integer,Integer>());
    }

    // Getters & Setters
    public int getIdPedido() {return idPedido;}
    public void setIdPedido(int idPedido) {this.idPedido = idPedido;}
    public Cliente getCliente() {return cliente;}
    public void setCliente(Cliente customer) {this.cliente = customer;}
    public List<Producto> getProductos() {return new ArrayList<>(productos);}
    public void setProductos(List<Producto> productos) {this.productos = new ArrayList<>(productos);}
    public Map<Integer,Integer> getCantidades() {return new HashMap<Integer,Integer>(cantidades);}
    public void setCantidades(Map<Integer, Integer> cantidades) {this.cantidades = new HashMap<>(cantidades);}

    // Métodos
    /**
     * Incluye un producto al pedido con la cantidad indicada.
     * @param producto - El producto a añadir a la lista "productos".
     * @param cantidad - El número de productos a añadir, registrado en el mapa "cantidades"
     */
    public void addProducto(Producto producto, int cantidad){
        this.productos.add(new Producto(producto));
        this.cantidades.put(producto.getId(),cantidad);
    }

    /**
     * Incluye una unidad de un producto al pedido.
     * @param producto - El producto a añadir a la lista "productos".
     */
    public void addProducto(Producto producto){
        this.addProducto(producto,1);
    }

    /**
     * Elimina un producto de la lista productos según su id.
     * @param id - Identificador único
     */
    public void delProduct(int id){
        if (!productos.stream().anyMatch(p -> p.getId() == id) || !cantidades.containsKey(id)) throw new IllegalArgumentException();
        this.productos.removeIf(p -> p.getId() == id);
        this.cantidades.remove(id);
    }

    /**
     * Calcula el total del pedido teniendo en cuenta IVA y gastos de envío.
     * @return Coste total del pedido.
     * @throws IllegalStateException La lista de productos no puede estar vacía.
     */
    public double calcularTotal() throws IllegalStateException{
        if (productos.isEmpty()) throw new IllegalStateException(PRODUCT_LIST_EMPTY_EXCEPTION_MESSAGE);
        
        double costeEnvio = 0;
        if (productos.stream().anyMatch(p -> p instanceof ProductoFisico)) costeEnvio = ProductoFisico.costeEnvio(this.cliente.getPais());

        double precioConIva = 0;
        for(Producto p : this.productos) {
            precioConIva += p.calcularPrecioFinal() * cantidades.get(p.getId());
        }

        return precioConIva + costeEnvio;
    }

    /**
     * Calcula los gastos de envío de un pedido en función de la zona de destino.
     * @param pais - Zona de destino
     * @return Gastos de envío del pedido
     */
    public double calcularEnvio(String pais) {
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

    /**
     * Calcula el IVA total del pedido en función del tipo de IVA proporcionado.
     * @param tipoIva - Tipo de IVA que puede ser "GENERAL", "REDUCIDO" o "SUPER"
     * @return IVA total del pedido
     */
    public double calcularIva(String tipoIva){
        return this.productos.stream()
            .filter(p -> p instanceof ProductoDigital)
            .mapToDouble(p -> {
                ProductoDigital pd = (ProductoDigital) p;
                pd.aplicarIva(tipoIva);
                double ivaUd = pd.getPrecioBase() * pd.getIva();
                int cantidad = this.cantidades.getOrDefault(pd.getId(), 1);
                return ivaUd * cantidad;
            })
            .sum();
    }
    
    @Override
    public String toString() {
        return "{\"idPedido\":\"%d\",\"cliente\":%s,\"estado\":\"%s\",\"productos\":\"%s\",\"cantidades\":\"%s\"}".formatted(
            this.idPedido, 
            this.cliente != null ? this.cliente.toString() : "null", 
            this.estado, 
            this.productosToString(), 
            this.cantidadesToString()
        );
    }

    /**
     * Método auxiliar que permite pasar a cadena la lista de productos en formato JSON.
     * @return Lista de productos en formato JSON
     */
    public String productosToString() {
        return this.productos == null ? "" : "{%s}".formatted(
            this.productos.stream()
                .map(p -> p != null ? p.toString() : "null")
                .collect(Collectors.joining(","))
        );
    }

    /**
     * Método auxiliar que permite pasar a cadena el mapa de cantidades en formato JSON.
     * @return Mapa de cantidades en formato JSON
     */
    public String cantidadesToString() {
        return this.cantidades == null ? "" : "{%s}".formatted(
            this.cantidades.entrySet().stream()
                .map(entry -> "\"%d\":%d".formatted(entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(","))
        );
    }
}

