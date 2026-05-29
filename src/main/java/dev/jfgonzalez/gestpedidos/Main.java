package dev.jfgonzalez.gestpedidos;

import dev.jfgonzalez.gestpedidos.model.*;

public class Main {
    public static void main(String[] args) {
        // 1. Create a dummy customer and products
        // (Adjust these constructors to match your Customer and Product classes)
        Customer customer = new Customer(1, "Ana Gómez");
        
        Product laptop = new PhysicalProduct(1,"Portátil Asus", 800.00f); 
        Product mouse = new PhysicalProduct(2,"Ratón Logi", 25.50f);
        Product keyboard = new PhysicalProduct(3,"Teclado Mecánico", 60.00f);

        System.out.println("=== TEST 1: Creating a pending order and adding items ===");
        Order order = new Order(5001, customer);
        
        order.addProduct(laptop, 1);
        order.addProduct(mouse, 2);
        order.addProduct(keyboard); // Default amount: 1

        // Display the initial summary
        System.out.println(order.showSummary());
        System.out.println();

        System.out.println("=== TEST 2: Dynamic deletion and modification ===");
        System.out.println("Removing: " + mouse.getName());
        order.delProduct(mouse);
        
        System.out.println("Adding another " + keyboard.getName());
        order.addProduct(keyboard, 1);

        // Display updated summary to verify indices remained aligned
        System.out.println(order.showSummary());
        System.out.println();

        System.out.println("=== TEST 3: Safeguard Check (Removing non-existent product) ===");
        Product standaloneProduct = new PhysicalProduct(1,"Impresora 3D", 350.00f);
        try {
            order.delProduct(standaloneProduct);
            System.out.println("Success: No crash when attempting to delete a product not in the order.");
        } catch (Exception e) {
            System.err.println("Failure: The application crashed during deletion: " + e.getMessage());
        }
        System.out.println();

        System.out.println("=== TEST 4: Empty Order Validation (Exception check) ===");
        Order emptyOrder = new Order(5002, customer);
        try {
            emptyOrder.calcTotal();
            System.err.println("Failure: The empty order calculated a total instead of throwing an exception!");
        } catch (IllegalStateException e) {
            System.out.println("Success: Caught expected exception -> " + e.getMessage());
        }
    }
}
