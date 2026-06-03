package dev.jfgonzalez.gestpedidos;

import java.time.LocalDate;

public class Factura {
    // Constantes
    private static int idCounter = 1;

    // Atributos
    private String codigoFactura;
    private LocalDate fechaEmision;
    private double totalNeto;
    private double totalIva;
    private double totalEnvio;
    private double totalFinal;
    private double descuento;

    // Constructores
    /**
     * Construye una Factura usando los parámetros indicados.
     * @param codigoFactura - Identificador único
     * @param fechaEmision - Fecha en la que genera la factura
     * @param totalNeto - Coste total antes de aplicar IVA, envío y descuento
     * @param totalIva - Importe adicional a los productos digitales
     * @param totalEnvio - Importe adicional en caso de tener productos físicos
     * @param totalFinal - Coste total del pedido
     * @param descuento - Descuento aplicable por tipo de membresía (VIP) y antigüedad
     */
    public Factura(
        String codigoFactura, LocalDate fechaEmision, double totalNeto,
        double totalIva, double totalEnvio, double totalFinal, double descuento
    ) {
        this.codigoFactura = codigoFactura;
        this.fechaEmision = fechaEmision;
        this.totalNeto = totalNeto;
        this.totalIva = totalIva;
        this.totalEnvio = totalEnvio;
        this.totalFinal = totalFinal;
        this.descuento = descuento;
    }

    /**
     * Construye una Factura usando los parámetros indicados. "totalFinal" es calculado sumando "totalNeto", "totalEnvio", "totalIva" y restando "descuento".
     * @param codigoFactura - Identificador único
     * @param fechaEmision - Fecha en la que genera la factura
     * @param totalNeto - Coste total antes de aplicar IVA, envío y descuento
     * @param totalIva - Importe adicional a los productos digitales
     * @param totalEnvio - Importe adicional en caso de tener productos físicos
     * @param descuento - Descuento aplicable por tipo de membresía (VIP) y antigüedad
     */
    public Factura(
        String codigoFactura, LocalDate fechaEmision, double totalNeto,
        double totalIva, double totalEnvio, double descuento
    ) {
        this(
            codigoFactura,
            fechaEmision,
            totalNeto,
            totalIva,
            totalEnvio,
            totalNeto + totalEnvio + totalIva - descuento,
            descuento
        );
    }

    /**
     * Construye una Factura usando los parámetros indicados. "fechaEmision" es LocalDate.now() y "descuento" es 0.
     * @param codigoFactura - Identificador único
     * @param totalNeto - Coste total antes de aplicar IVA, envío y descuento
     * @param totalIva - Importe adicional a los productos digitales
     * @param totalEnvio - Importe adicional en caso de tener productos físicos
     * @param totalFinal - Coste total del pedido
     */
    public Factura(String codigoFactura, double totalNeto, double totalIva, double totalEnvio, double totalFinal) {
        this(
            codigoFactura,
            LocalDate.now(),
            totalNeto,
            totalIva,
            totalEnvio,
            0
        );
    }

    /**
     * Construye una Factura usando los parámetros indicados. "codigoFactura" es genId(), "fechaEmision" es LocalDate.now() y "descuento" es 0.
     * @param netTotal
     * @param totalIva
     * @param totalShipping
     * @param finalTotal
     */
    public Factura(float netTotal, float totalIva, float totalShipping, float finalTotal) {
        this(genId(),LocalDate.now(),netTotal,totalIva,totalShipping,finalTotal,0);
    }

    /**
     * Construye una Factura vacía con "codigoFactura" generado automáticamente.
     */
    public Factura() {
        this(genId(),0,0,0,0);
    }

    // Getters & Setters
    public String getCodigoFactura() {return codigoFactura;}
    public void setCodigoFactura(String codigoFactura) {this.codigoFactura = codigoFactura;}
    public LocalDate getFechaEmision() {return fechaEmision;}
    public void setFechaEmision(LocalDate fechaEmision) {this.fechaEmision = fechaEmision;}
    public double getTotalNeto() {return totalNeto;}
    public void setTotalNeto(double totalNeto) {this.totalNeto = totalNeto;}
    public double getTotalIva() {return totalIva;}
    public void setTotalIva(double totalIva) {this.totalIva = totalIva;}
    public double getTotalEnvio() {return totalEnvio;}
    public void setTotalEnvio(double totalEnvio) {this.totalEnvio = totalEnvio;}
    public double getTotalFinal() {return totalFinal;}
    public void setTotalFinal(double totalFinal) {this.totalFinal = totalFinal;}
    public double getDescuento() {return descuento;}
    public void setDescuento(double descuento) {this.descuento = descuento;}

    // Métodos
    /**
     * Método para generación de "códigoFactura".
     * @return Identificador único con formato "FACT-{LocalDate.now()}-N", siendo N un contador interno de la clase
     */
    private static String genId() {
        return "FACT-%s-%d".formatted(LocalDate.now(),idCounter++);
    }

    /**
     * Genera una Factura a partir de un Pedido. Incluir un desglose detallado donde se ve claramente cuánto se ha pagado por cada concepto (IVA, envío y descuentos aplicados).
     * @param pedido - El pedido del que se obtendrán los datos que usará la factura
     * @return Vista de la factura en forma de String 
     */
    public String generarFactura(Pedido pedido){
        StringBuilder factura = new StringBuilder();
        factura.append("---%s---%n".formatted(this.codigoFactura));
        factura.append("==========%n");
        for (Producto p : pedido.getProductos()) {
            int cantidad = pedido.getCantidades().get(p.getId());
            String nombre = p.getNombre();
            double precioFinal = p.calcularPrecioFinal();
            factura.append("%d - %s - %.2f€%n".formatted(cantidad,nombre,precioFinal));
        }
        factura.append("==========%n");
        factura.append("Total neto: %.2f€%n".formatted(totalNeto));
        factura.append("IVA: %.2f€%n".formatted(totalIva));
        factura.append("Envío: %.2f€%n".formatted(totalEnvio));
        factura.append("Descuento: %.2f€%n".formatted(descuento));
        factura.append("----------");
        factura.append("Total final: %.2f€%n".formatted(totalFinal));

        return factura.toString();
    }

    @Override
    public String toString() {
        return "Factura: " + codigoFactura + "%n" +
               "Fecha de emision: " + fechaEmision + "%n" +
               "Total neto: " + totalNeto + "%n" +
               "Total IVA: " + totalIva + "%n" +
               "Total envio: " + totalEnvio + "%n" +
               "Descuento: " + descuento + "%n" +
               "Total final: " + totalFinal;
    }

    /**
     * Método que pasa los datos del objeto a formato JSON.
     * @return Cadena en formato JSON
     */
    public String toJson() {
        return "{\"codigoFactura\":\"%s\",\"fechaEmision\":\"%s\",\"totalNeto\":\"%.2f\",\"totalIva\":\"%.2f\",\"totalEnvio\":\"%.2f\",\"totalFinal\":\"%.2f\",\"descuento\":\"%.2f\"}".formatted(
            this.codigoFactura, 
            this.fechaEmision, 
            this.totalNeto, 
            this.totalIva, 
            this.totalEnvio, 
            this.totalFinal, 
            this.descuento
        );
    }

    /**
     * Método auxiliar que calcula todos los totales de la factura.
     * @param pedido - Pedido con el que se está trabajando
     */
    public void calcularFactura(Pedido pedido) {
        for (Producto p : pedido.getProductos()) {
            totalNeto += p.getPrecioBase() * pedido.getCantidades().get(p.getId());
        }
        this.totalIva = 0;
        this.totalEnvio = 0;
        this.totalFinal = 0;
        this.descuento = 0;
    }
}
