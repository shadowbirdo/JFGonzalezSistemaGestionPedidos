package endes.gestpedidos;

public class Main {
    public static void main(String[] args) {
        executeLogic();
    }

    public static void executeLogic() {
        final String hBar =  "----------------------------------------\n";
        String outputMsg = "";

        outputMsg += hBar;
        outputMsg += "      TEST SISTEMA: EL BADULAQUE        \n";
        outputMsg += hBar;

        // 1. Cliente
        Client homer = new Client(
            "Homer J. Simpson", 
            "amanterechoncho@aol.com@aol.com", 
            "742 de Evergreen Terrace"
        );
        
        outputMsg += "Cliente:\n" + homer.getName() + "\n";

        
        // 2. Productos
        PhysicalProd mrKroket = new PhysicalProd("Detergente Mr. Kroket", 20.0f, 10.0f);
        DigitalProd videoTopo = new DigitalProd("Balonazo en la entrepierna - Director's Cut", 10.0f, "Licencia Hans Topo", 500.0f);
        PhysicalProd duff = new PhysicalProd("Duff (Pack divisible)", 12.0f, 2.0f); 
        PhysicalProd donut = new PhysicalProd("Donut Prohibido", 5.0f, 1.0f);

        // 3. Pedido
        Order order = new Order(homer);
        
        order.addProduct(mrKroket);
        order.addProduct(videoTopo);
        order.addProduct(duff);
        order.addProduct(donut);

        outputMsg += "Productos añadidos...\n";

        // 4. Resultados
        outputMsg += hBar;
        outputMsg += order.showSummary() + "\n";
        outputMsg += hBar;

        // Verificación del resultado
        // 30 (Kroket) + 9.5 (Video) + 14 (Duff) + 6 (Donut) = 60
        float total = order.calcTotal();
        outputMsg += "Total calculado: " + total + "\n\n";
        
        if(total == 59.5f) outputMsg += ">> ¡Mosquis! Todo correcto.";
        else outputMsg += ">> Multiplícate por cero (Error).";

        outputMsg += "\n";

        System.out.println(outputMsg);
    }
}