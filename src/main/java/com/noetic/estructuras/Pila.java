package com.noetic.estructuras;

import java.util.ArrayList;
import java.util.List;

public class Pila<T> {

    private Nodo<T> tope;
    private int tamaño;

    public Pila() {
        tope = null;
        tamaño = 0;
    }
   
    public void apilar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = tope;
        tope = nuevo;
        tamaño++;
    }

    public T desapilar() {
        if (estaVacia()) throw new RuntimeException("La pila está vacía");
        T dato = tope.dato;
        tope = tope.siguiente;
        tamaño--;
        return dato;
    }

    public T verTope() {
        if (estaVacia()) throw new RuntimeException("La pila está vacía");
        return tope.dato;
    }

    public boolean eliminar(T dato) {
        if (estaVacia()) return false;
        Pila<T> temporal = new Pila<>();
        boolean encontrado = false;
   
        while (!estaVacia()) {
            T actual = desapilar();
            if (actual.equals(dato) && !encontrado) {
                encontrado = true;   
            } else {
                temporal.apilar(actual);
            }
        }
         
        while (!temporal.estaVacia()) {
            apilar(temporal.desapilar());
        }
        return encontrado;
    }
   
    public List<T> aLista() {
        List<T> resultado = new ArrayList<>();
        Nodo<T> actual = tope;
        while (actual != null) {
            resultado.add(actual.dato);
            actual = actual.siguiente;
        }
        return resultado;
    }

    public int getTamaño() { return tamaño; }
    public boolean estaVacia() { return tope == null; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Pila(tope)[");
        Nodo<T> actual = tope;
        while (actual != null) {
            sb.append(actual.dato);
            if (actual.siguiente != null) sb.append(" | ");
            actual = actual.siguiente;
        }
        sb.append("]");
        return sb.toString();
    }
}