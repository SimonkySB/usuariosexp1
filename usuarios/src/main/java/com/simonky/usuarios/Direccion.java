package com.simonky.usuarios;



public class Direccion {
    private int direccionId;
    private String calle;
    private String pais;
    private String ciudad;
    private String zipcode;

    public Direccion(int direccionId, String calle, String pais, String ciudad, String zipcode) {
        this.direccionId = direccionId;
        this.calle = calle;
        this.pais = pais;
        this.ciudad = ciudad;
        this.zipcode = zipcode;
    }

    
    public int getDireccionId() {
        return direccionId;
    }

    public String getCalle() {
        return calle;
    }

    public String getPais() {
        return pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getZipcode() {
        return zipcode;
    }    
}
