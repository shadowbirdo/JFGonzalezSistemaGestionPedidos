package dev.jfgonzalez.gestpedidos.exceptions;

public class Msg {
    public final static String NEGATIVE_PRICE = "Attempt to assign negative int to basePrice. basePrice cannot be negative.";
    public final static String INVALID_IVA = "IVA type not valid. Valid types are GENERAL, REDUCIDO and SUPER.";
    public final static String EMPTY_ORDER = "Could not process order. Order is empty.";
}
