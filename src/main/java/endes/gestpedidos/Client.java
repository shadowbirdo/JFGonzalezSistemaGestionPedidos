package endes.gestpedidos;

/**
 * - Atributos: id, nombre, añosAntiguedad (int), esVip (boolean), pais.
 * - Los años de antigüedad y el estado VIP determinarán el porcentaje de descuento de fidelidad que la Tienda aplicará sobre el total del Pedido.
 * - El país indicará el gasto de envío que haya que aplicar en los casos correspondientes.
 */
public class Client {
    private int id;
    private String name;
    private int seniorityYears;
    private boolean esVip;
    private String pais;
    private String mail;
    private String address;

    public Client(String name, String mail, String address){
        this.name = name;
        this.mail = mail;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String toString(){
        return "Name: " + this.name + " | Mail: " + this.mail + " | Address: " + this.address;
    }
}
