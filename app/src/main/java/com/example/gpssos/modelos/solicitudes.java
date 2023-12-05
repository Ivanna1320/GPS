package com.example.gpssos.modelos;

public class solicitudes {
    String name;
    String surname;
    String celular;

    public solicitudes(){}

    public solicitudes(String name, String surname, String celular) {
        this.name = name;
        this.surname = surname;
        this.celular = celular;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}
