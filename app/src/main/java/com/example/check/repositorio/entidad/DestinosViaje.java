package com.example.check.repositorio.entidad;

import androidx.annotation.NonNull;

import java.util.Objects;

public class DestinosViaje {
    public String nombre; //NOSONAR
    public String ubicacion; //NOSONAR
    public String imagen; //NOSONAR
    public String url; //NOSONAR
    public String fecha; //NOSONAR

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DestinosViaje that = (DestinosViaje) o;
        return Objects.equals(nombre, that.nombre) && Objects.equals(ubicacion, that.ubicacion) && Objects.equals(imagen, that.imagen) && Objects.equals(url, that.url) && Objects.equals(fecha, that.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, ubicacion, imagen, url, fecha);
    }

    @NonNull
    @Override
    public String toString() {
        return "TravelLocation{" + "Nombre='" + nombre + '\'' + ", ubicación='" + ubicacion + '\'' + ", imagen='" + imagen + '\'' + ", url='" + url + '\'' + ", fecha='" + fecha + '\'' + '}';
    }
}