package dev.jfgonzalez.gestpedidos.model;

import java.sql.Date;
import java.util.List;

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
public class Invoice {
    private int id;
    private Date issueDate;
    private float netTotal;
    private float totalIva;
    private float totalShipping;
    private float finalTotal;
    private static int idCounter = 1;


    public Invoice(int id, Date issueDate, float netTotal, float totalIva, float totalShipping, float finalTotal) {
        this.id = id;
        this.issueDate = issueDate;
        this.netTotal = netTotal;
        this.totalIva = totalIva;
        this.totalShipping = totalShipping;
        this.finalTotal = finalTotal;
    }

    public Invoice(Date issueDate, float netTotal, float totalIva, float totalShipping, float finalTotal) {
        this(genId(),issueDate,netTotal,totalIva,totalShipping,finalTotal);
    }

    public Invoice() {
        this(null,0,0,0,0);
    }

    /**
     * Genera un pedido
     * @param order
     * @return
     */
    public String genInvoice(Order order){
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
        List<Product> prods = order.getProductList();
        List<Integer> amounts = order.getAmountList();

        for (int i = 0; i < prods.size(); i++) {
            Product p = prods.get(i);
            int amount = amounts.get(i);

            bodyBuilder.addRow(
                new Row.Builder()
                .addCell(p.getName())
                .addCell(String.valueOf(amount))
                .addCell("%.2f".formatted(p.getPrice()))
                .addCell("%.0f%%".formatted(p instanceof DigitalProduct dp ? dp.getIvaMult() * 100 - 100 : 0))
                .addCell("%.0f".formatted(p instanceof PhysicalProduct pp ? pp.getDeliveryFee() : 0))
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
    private static int genId() {
        return idCounter++;
    }

}
