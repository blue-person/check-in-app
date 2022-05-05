package com.example.check.Entidad;

import java.util.Objects;

public class TravelLocation {
    public String Nombre, ubicación, imagen, url;
    public String fecha;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TravelLocation that = (TravelLocation) o;
        return Objects.equals(Nombre, that.Nombre) && Objects.equals(ubicación, that.ubicación) && Objects.equals(imagen, that.imagen) && Objects.equals(url, that.url) && Objects.equals(fecha, that.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Nombre, ubicación, imagen, url, fecha);
    }

    @Override
    public String toString() {
        return "TravelLocation{" +
                "Nombre='" + Nombre + '\'' +
                ", ubicación='" + ubicación + '\'' +
                ", imagen='" + imagen + '\'' +
                ", url='" + url + '\'' +
                ", fecha='" + fecha + '\'' +
                '}';
    }
}