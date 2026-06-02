package dev.jfgonzalez.gestpedidos;

import dev.jfgonzalez.gestpedidos.model.*;

public class Main {
    public static void main(String[] args) {
        // 1. Create a dummy customer and products
        // (Adjust these constructors to match your Customer and Product classes)
        Cliente customer = new Cliente(1, "Ana Gómez");
        
        Producto laptop = new ProductoFisico(1,"Portátil Asus", 800.00f); 
        Producto mouse = new ProductoFisico(2,"Ratón Logi", 25.50f);
        Producto keyboard = new ProductoFisico(3,"Teclado Mecánico", 60.00f);

        System.out.println("=== TEST 1: Creating a pending order and adding items ===");
        Pedido order = new Pedido(5001, customer);
        
        order.addProducto(laptop, 1);
        order.addProducto(mouse, 2);
        order.addProducto(keyboard); // Default amount: 1

        // Display the initial summary
        System.out.println(order.showSummary());
        System.out.println();

        System.out.println("=== TEST 2: Dynamic deletion and modification ===");
        System.out.println("Removing: " + mouse.getNombre());
        order.delProduct(mouse);
        
        System.out.println("Adding another " + keyboard.getNombre());
        order.addProducto(keyboard, 1);

        // Display updated summary to verify indices remained aligned
        System.out.println(order.showSummary());
        System.out.println();

        System.out.println("=== TEST 3: Safeguard Check (Removing non-existent product) ===");
        Producto standaloneProduct = new ProductoFisico(1,"Impresora 3D", 350.00f);
        try {
            order.delProduct(standaloneProduct);
            System.out.println("Success: No crash when attempting to delete a product not in the order.");
        } catch (Exception e) {
            System.err.println("Failure: The application crashed during deletion: " + e.getMessage());
        }
        System.out.println();

        System.out.println("=== TEST 4: Empty Order Validation (Exception check) ===");
        Pedido emptyOrder = new Pedido(5002, customer);
        try {
            emptyOrder.calcularTotal();
            System.err.println("Failure: The empty order calculated a total instead of throwing an exception!");
        } catch (IllegalStateException e) {
            System.out.println("Success: Caught expected exception -> " + e.getMessage());
        }
    }
}
