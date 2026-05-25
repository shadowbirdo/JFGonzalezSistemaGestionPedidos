package endes.gestpedidos;

public class Client {
    private String name;
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
