package dev.jfgonzalez.gestpedidos.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.jakewharton.picnic.CellStyle;
import com.jakewharton.picnic.Row;
import com.jakewharton.picnic.Table;
import com.jakewharton.picnic.TableSection;
import com.jakewharton.picnic.TextRendering;

/**
 * 
 * - Atributos: codigoFactura (Generado automáticamente), fechaEmision, totalNeto, totalIva, totalEnvio, totalFinal.
 * - Debe incluir un desglose detallado donde se vea claramente cuánto se ha pagado por cada concepto (IVA, envío y descuentos aplicados).
 * - invoiceNumber, issueDate, netTotal, totalIva, totalShipping, finalTotal
 */
public class Factura {
    private String codigoFactura;
    private LocalDate fechaEmision;
    private double totalNeto;
    private double totalIva;
    private double totalEnvio;
    private double totalFinal;
    private double descuento;
    private static int idCounter = 1;

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

    public Factura(String codigoFactura, float totalNeto, float totalIva, float totalEnvio, float totalFinal) {
        this(
            codigoFactura,
            LocalDate.now(),
            totalNeto,
            totalIva,
            totalEnvio,
            totalFinal,
            0
        );
    }

    public Factura(float netTotal, float totalIva, float totalShipping, float finalTotal) {
        this(genId(),netTotal,totalIva,totalShipping,finalTotal);
    }

    public Factura() {
        this(null,0,0,0,0);
    }

    public String getCodigoFactura() {
        return codigoFactura;
    }
    public void setCodigoFactura(String codigoFactura) {
        this.codigoFactura = codigoFactura;
    }
    public LocalDate getFechaEmision() {
        return fechaEmision;
    }
    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    public double getTotalNeto() {
        return totalNeto;
    }
    public void setTotalNeto(double netTotal) {
        this.totalNeto = netTotal;
    }
    public double getTotalIva() {
        return totalIva;
    }
    public void setTotalIva(double totalIva) {
        this.totalIva = totalIva;
    }
    public double getTotalEnvio() {
        return totalEnvio;
    }
    public void setTotalEnvio(double totalShipping) {
        this.totalEnvio = totalShipping;
    }
    public double getTotalFinal() {
        return totalFinal;
    }
    public void setTotalFinal(double finalTotal) {
        this.totalFinal = finalTotal;
    }
    public double getDescuento() {
        return descuento;
    }
    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    /**
     * Genera un pedido
     * @param order
     * @return
     */
    public String genInvoice(Pedido order){
        CellStyle cellStyle = new CellStyle.Builder().setBorder(true).build();
        TableSection header = new TableSection.Builder().addRow(
            new Row.Builder()
            .addCell("Concepto")
            .addCell("Unidades")
            .addCell("Precio/Ud")
            .addCell("IVA")
            .addCell("Envío")
            .addCell("Descuento")
            .build()
        ).build();
        
        TableSection.Builder bodyBuilder = new TableSection.Builder();
        List<Producto> prods = order.getProductos();
        Map<Integer,Integer> amounts = order.getCantidades();

        for (int i = 0; i < prods.size(); i++) {
            Producto p = prods.get(i);
            int amount = amounts.get(i);

            bodyBuilder.addRow(
                new Row.Builder()
                .addCell(p.getName())
                .addCell(String.valueOf(amount))
                .addCell("%.2f".formatted(p.getPrice()))
                .addCell("%.0f%%".formatted(p instanceof ProductoDigital dp ? dp.getIvaMult() * 100 - 100 : 0))
                .addCell("%.0f".formatted(p instanceof ProductoFisico pp ? pp.getDeliveryFee() : 0))
                .addCell("-")
                .build()
            );
        }
        TableSection body = bodyBuilder.build();

        Table factura = new Table.Builder()
            .setCellStyle(cellStyle)
            .setHeader(header)
            .setBody(body)
            .build();

       return TextRendering.render(factura);
    }

    /**
     * Método para generación de id
     * @return
     */
    private static String genId() {
        return "FACT-%s-%d".formatted(LocalDate.now(),idCounter++);
    }

}
