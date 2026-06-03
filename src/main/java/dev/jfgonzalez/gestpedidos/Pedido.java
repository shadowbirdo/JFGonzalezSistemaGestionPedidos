package dev.jfgonzalez.gestpedidos;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.HashMap;

public class Pedido {
    // Constantes
    public static final String PRODUCT_LIST_EMPTY_EXCEPTION_MESSAGE = "Could not process order. Order is empty.";
    public static final double PRECIO_POR_KILO = .1;

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
        for (Producto p : productos) {
            if (!cantidades.containsKey(p.getId())) {
                throw new IllegalArgumentException("Falta la cantidad del producto " + p.getId());
            }
        }
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
        this(id, cliente, "PENDIENTE", new ArrayList<>(), new HashMap<>());
    }

    // Getters & Setters
    public int getIdPedido() {return idPedido;}
    public void setIdPedido(int idPedido) {this.idPedido = idPedido;}
    public Cliente getCliente() {return cliente;}
    public void setCliente(Cliente customer) {this.cliente = customer;}
    public List<Producto> getProductos() {return new ArrayList<>(productos);}
    public void setProductos(List<Producto> productos) {this.productos = productos != null ? new ArrayList<>(productos) : new ArrayList<>();}
    public Map<Integer,Integer> getCantidades() {return new HashMap<>(cantidades);}
    public void setCantidades(Map<Integer, Integer> cantidades) {this.cantidades = cantidades != null ? new HashMap<>(cantidades) : new HashMap<>();}

    // Métodos
    /**
     * Incluye un producto al pedido con la cantidad indicada.
     * @param producto - El producto a añadir a la lista "productos".
     * @param cantidad - El número de productos a añadir, registrado en el mapa "cantidades"
     */
    public void addProducto(Producto producto, int cantidad){
        switch (producto) {
            case ProductoDigital pd -> this.productos.add(new ProductoDigital(pd));
            case ProductoFisico pf -> this.productos.add(new ProductoFisico(pf));
            case null, default -> this.productos.add(new Producto(producto));
        }
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
    public void delProducto(int id){
        if (productos.stream().noneMatch(p -> p.getId() == id) || !cantidades.containsKey(id)) throw new IllegalArgumentException();
        this.productos.removeIf(p -> p.getId() == id);
        this.cantidades.remove(id);
    }

    /**
     * Calcula el total del pedido teniendo en cuenta IVA y gastos de envío.
     * @return Coste total del pedido.
     * @throws IllegalStateException La lista de productos no puede estar vacía.
     */
    public double calcularTotal() throws IllegalStateException{
        if (productos.isEmpty()) throw new IllegalArgumentException(PRODUCT_LIST_EMPTY_EXCEPTION_MESSAGE);
        
        double total = 0;

        for(Producto p : this.productos) {
            total += p.getPrecioBase() * cantidades.getOrDefault(p.getId(),1);
        }

        return total;
    }

    /**
     * Calcula los gastos de envío de un pedido en función de la zona de destino.
     * @param pais - Zona de destino
     * @return Gastos de envío del pedido
     */
    public double calcularEnvio(String pais) {
        if (this.productos.stream().allMatch(ProductoDigital.class::isInstance)) return 0;
        double envio = this.productos.stream()
            .filter(ProductoFisico.class::isInstance)
            .map(p -> (ProductoFisico)p)
            .mapToDouble(p ->
                p.getPeso() * PRECIO_POR_KILO * cantidades.getOrDefault(p.getId(),1)
            )
            .sum();

        return switch (pais == null ? null : pais.toLowerCase()) {
            case "españa" -> envio + 0;
            case "francia", "italia", "portugal" -> envio + 5;
            case null, default -> envio + 10;
        };

    }

    /**
     * Calcula el IVA total del pedido en función del tipo de IVA proporcionado.
     * @param tipoIva - Tipo de IVA que puede ser "GENERAL", "REDUCIDO" o "SUPER"
     * @return IVA total del pedido
     */
    public double calcularIva(String tipoIva){
        return this.productos.stream()
            .filter(ProductoDigital.class::isInstance)
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
        if (this.productos == null) return "null";
        return "{%s}".formatted(
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

