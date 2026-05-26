package dev.jfgonzalez.gestpedidos.model;

/**
 * - Atributos: id, nombre, añosAntiguedad (int), esVip (boolean), pais.
 * - Los años de antigüedad y el estado VIP determinarán el porcentaje de descuento de fidelidad que la Tienda aplicará sobre el total del Pedido.
 * - El país indicará el gasto de envío que haya que aplicar en los casos correspondientes.
 */
public class Customer {
    private int id;
    private String name;
    private String address;
    private String email;
    private int yearsLoyalty;
    private boolean isVip;
    private String country;

    public Customer(int id, String name, String email, String address, int yearsLoyalty, boolean isVip, String country){
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.yearsLoyalty = yearsLoyalty;
        this.isVip = isVip;
        this.country = country;
    }

    public Customer(int id, String name){
        this(id,name,"","",0,false,"");
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}
    public int getYearsLoyalty() {return yearsLoyalty;}
    public void setYearsLoyalty(int yearsLoyalty) {this.yearsLoyalty = yearsLoyalty;}
    public boolean getIsVip() {return isVip;}
    public void setIsVip(boolean isVip) {this.isVip = isVip;}
    public String getCountry() {return country;}
    public void setCountry(String country) {this.country = country;}

    public String toString(){
        return "{\"id\":\"%d\",\"name\":\"%s\",\"address\":\"%s\",\"email\":\"%s\",\"yearsLoyalty\":\"%d\",\"isVip\":\"%s\",\"country\":\"%s\"}".formatted(
            this.id, this.name, this.address, this.email, this.yearsLoyalty, this.isVip, this.country
        );
    }
}
