package dev.jfgonzalez.gestpedidos;

import dev.jfgonzalez.gestpedidos.model.*;

public class Main {
    public static void main(String[] args) {
        Invoice invoice = new Invoice();
        System.out.println(invoice.genInvoice(null));
    }
}
