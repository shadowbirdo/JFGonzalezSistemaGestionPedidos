package dev.jfgonzalez.gestpedidos.model;

import java.sql.Date;

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

    public String genInvoice(Order order){
        /*
        AsciiTable invoiceTable = new AsciiTable();
        invoiceTable.addRule();
        invoiceTable.addRow( null,null,"Título");
        invoiceTable.addRule();
        invoiceTable.addRow("null","","");
        invoiceTable.addRule();
        return invoiceTable.render();
        */
       return null;//fix
    }

    /**
     * todo: Implement method
     * @return
     */
    private static int genId() {
        return 1;
    }

}
