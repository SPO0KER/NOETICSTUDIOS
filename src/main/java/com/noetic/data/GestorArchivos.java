package com.noetic.data;

import com.noetic.model.User;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

   
public class GestorArchivos {

    private static final String ARCHIVO_USUARIOS = "/data/usuarios.txt";

    
    public static List<User> leerUsuarios() {
        List<User> usuarios = new ArrayList<>();

        try {
             
            InputStream is = GestorArchivos.class.getResourceAsStream(ARCHIVO_USUARIOS);

            if (is == null) {
                System.err.println("[GestorArchivos] No se encontró el archivo: " + ARCHIVO_USUARIOS);
                return usuariosPorDefecto();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String linea;

            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();

                
                if (linea.isEmpty() || linea.startsWith("#")) continue;

                String[] partes = linea.split(",");
                if (partes.length != 4) {
                    System.err.println("[GestorArchivos] Línea con formato inválido: " + linea);
                    continue;
                }

                String email    = partes[0].trim();
                String password = partes[1].trim();
                String nombre   = partes[2].trim();
                boolean esAdmin = partes[3].trim().equalsIgnoreCase("admin");

                usuarios.add(new User(email, password, nombre, esAdmin));
            }

            reader.close();
            System.out.println("[GestorArchivos] " + usuarios.size() + " usuario(s) cargados desde archivo.");

        } catch (IOException e) {
            System.err.println("[GestorArchivos] Error al leer usuarios: " + e.getMessage());
            return usuariosPorDefecto();
        }

        return usuarios;
    }

   
    private static List<User> usuariosPorDefecto() {
        System.out.println("[GestorArchivos] Usando usuarios por defecto.");
        List<User> lista = new ArrayList<>();
        lista.add(new User("usuario@tienda.com", "user123", "Usuario Demo", false));
        lista.add(new User("admin@tienda.com",   "admin123", "Administrador", true));
        return lista;
    }

    
    public static void registrarAcceso(String email, boolean exitoso) {
        try {
              
            File logFile = new File("log_accesos.txt");
            FileWriter fw = new FileWriter(logFile, true);    
            BufferedWriter bw = new BufferedWriter(fw);

            String estado = exitoso ? "EXITOSO" : "FALLIDO";
            String timestamp = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            bw.write(timestamp + " | " + estado + " | " + email);
            bw.newLine();
            bw.close();

        } catch (IOException e) {
            System.err.println("[GestorArchivos] No se pudo escribir el log: " + e.getMessage());
        }
    }
}
