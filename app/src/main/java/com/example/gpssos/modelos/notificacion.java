package com.example.gpssos.modelos;

public class notificacion {
    String name;
    String mensaje;
    int image;

    public notificacion(String name, String mensaje, int image) {
        this.name = name;
        this.mensaje = mensaje;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
