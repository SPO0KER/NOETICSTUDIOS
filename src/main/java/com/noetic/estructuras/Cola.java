package com.noetic.estructuras;

import java.util.ArrayList;
import java.util.List;

   
public class Cola<T> {

    private Nodo<T> frente;
    private Nodo<T> final_;
    private int tamaño;

    public Cola() {
        frente = null;
        final_ = null;
        tamaño = 0;
    }

       
    public void encolar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (estaVacia()) {
            frente = nuevo;
            final_ = nuevo;
        } else {
            final_.siguiente = nuevo;
            final_ = nuevo;
        }
        tamaño++;
    }

     
    public T desencolar() {
        if (estaVacia()) throw new RuntimeException("La cola está vacía");
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) final_ = null;
        tamaño--;
        return dato;
    }

     
    public T verFrente() {
        if (estaVacia()) throw new RuntimeException("La cola está vacía");
        return frente.dato;
    }

   
    public List<T> aLista() {
        List<T> resultado = new ArrayList<>();
        Nodo<T> actual = frente;
        while (actual != null) {
            resultado.add(actual.dato);
            actual = actual.siguiente;
        }
        return resultado;
    }

    public int getTamaño() { return tamaño; }
    public boolean estaVacia() { return frente == null; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Cola(frente)[");
        Nodo<T> actual = frente;
        while (actual != null) {
            sb.append(actual.dato);
            if (actual.siguiente != null) sb.append(" -> ");
            actual = actual.siguiente;
        }
        sb.append("]");
        return sb.toString();
    }
}
