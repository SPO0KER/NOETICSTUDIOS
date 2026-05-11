package com.noetic.data;

import com.noetic.estructuras.Cola;
import com.noetic.estructuras.ListaEnlazada;
import com.noetic.estructuras.Pila;
import com.noetic.model.*;

import java.util.List;


public class DataStore {

    private static DataStore instance;


    private List<User> usuarios;

  
    private ListaEnlazada<Product> catalogo;

   
    private Pila<CartItem> carrito;

   
    private ListaEnlazada<Product> listaDeseos;

    
    private Cola<Order> historialPedidos;

    private User usuarioActual;

    private DataStore() {
        usuarios         = GestorArchivos.leerUsuarios();
        catalogo         = new ListaEnlazada<>();
        carrito          = new Pila<>();
        listaDeseos      = new ListaEnlazada<>();
        historialPedidos = new Cola<>();
        cargarProductos();
    }

    public static DataStore getInstance() {
        if (instance == null) instance = new DataStore();
        return instance;
    }


    public User login(String email, String password) {
        for (User u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email.trim())
                    && u.getPassword().equals(password)) {
                usuarioActual = u;
                GestorArchivos.registrarAcceso(email, true);
                return u;
            }
        }
        GestorArchivos.registrarAcceso(email, false);
        return null;
    }

    public void logout() {
        usuarioActual = null;
        carrito = new Pila<>();
    }

    public User getCurrentUser()    { return usuarioActual; }
    public User getUsuarioActual()  { return usuarioActual; }

     
    private void cargarProductos() {
        catalogo.agregar(new Product(1,  "Camiseta NCTI Rosario",       "Edición limitada con diseño de manos en oración y rosario. Arte espiritual único.", 120000, 12, "Camisetas", ""));
        catalogo.agregar(new Product(2,  "Noetic Studios Leopard",       "Diseño exclusivo con leopardo y logo Noetic Studios. Elegancia salvaje.",           120000, 15, "Camisetas", ""));
        catalogo.agregar(new Product(3,  "NTC CMPY Palm Trees",          "Diseño inspirado en palmeras con caligrafía árabe. Estilo único y vibrante.",       120000, 18, "Camisetas", ""));
        catalogo.agregar(new Product(4,  "Faith Black Edition",          "Camiseta negra con diseño espiritual y caligrafía árabe. Edición oscura.",          125000, 10, "Camisetas", ""));
        catalogo.agregar(new Product(5,  "Through Your Eyes",            "Diseño místico con ojo y pirámide. Visión más allá de lo visible.",                 120000, 14, "Camisetas", ""));
        catalogo.agregar(new Product(6,  "Red Seal Garden",              "Diseño minimalista con sello rojo y elementos florales. Frescura natural.",         120000, 16, "Camisetas", ""));
        catalogo.agregar(new Product(7,  "Gorra New Era Olas",           "Gorra negra, estilo único de olas de mar.",                                          80000, 20, "Gorras",    ""));
        catalogo.agregar(new Product(8,  "Gorra New Era Flores",         "Gorra azul de flores.",                                                              95000, 12, "Gorras",    ""));
        catalogo.agregar(new Product(9,  "Jort Noetic",                  "Jort jean gris con diseño 94.",                                                     160000, 18, "Pantalones",""));
        catalogo.agregar(new Product(10, "Jack & Jones Blue Denim",      "Jean clásico de mezclilla azul con corte moderno y cómodo.",                        180000, 15, "Jeans",     ""));
        catalogo.agregar(new Product(11, "Mid Rise Wide Jean Ash Black", "Jean negro de tiro medio con corte ancho, perfecto para look urbano.",              190000, 12, "Jeans",     ""));
        catalogo.agregar(new Product(12, "FC Barcelona Half-Zip",        "Buzo deportivo con cierre medio y gráficos del FC Barcelona.",                      150000, 10, "Buzos",     ""));
        catalogo.agregar(new Product(13, "Buzo Noetic Classic",          "Buzo clásico con logo bordado Noetic Studios.",                                     140000,  8, "Buzos",     ""));
        catalogo.agregar(new Product(14, "Oversize Streetwear Black",    "Buzo oversize negro para look streetwear.",                                         145000,  9, "Buzos",     ""));
    }

    public List<Product> getProducts() { return catalogo.aLista(); }
    public List<Product> getProductos() { return catalogo.aLista(); }

    public List<Product> getProductsByCategory(String categoria) {
        return getProductosPorCategoria(categoria);
    }

    public List<Product> getProductosPorCategoria(String categoria) {
        if (categoria.equals("Todos")) return catalogo.aLista();
        ListaEnlazada<Product> filtrado = new ListaEnlazada<>();
        for (Product p : catalogo.aLista()) {
            if (p.getCategory().equals(categoria)) filtrado.agregar(p);
        }
        return filtrado.aLista();
    }

    public void addProduct(Product p)    { catalogo.agregar(p); }
    public void agregarProducto(Product p) { catalogo.agregar(p); }

    public void removeProduct(int id) {
        catalogo.aLista().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .ifPresent(p -> catalogo.eliminar(p));
    }

    public int getNextProductId() {
        return catalogo.aLista().stream().mapToInt(Product::getId).max().orElse(0) + 1;
    }

    public int getTotalProducts()  { return catalogo.getTamaño(); }
    public int getTotalProductos() { return catalogo.getTamaño(); }

   

    public List<CartItem> getCart()    { return carrito.aLista(); }
    public List<CartItem> getCarrito() { return carrito.aLista(); }

    public int getCartCount() {
        return carrito.aLista().stream().mapToInt(CartItem::getQuantity).sum();
    }

    public double getCartTotal() {
        return carrito.aLista().stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    public void addToCart(Product producto) {
        for (CartItem item : carrito.aLista()) {
            if (item.getProduct().getId() == producto.getId()) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        carrito.apilar(new CartItem(producto, 1));
    }

    public void removeFromCart(CartItem item) { carrito.eliminar(item); }

    public void updateQuantity(CartItem item, int cantidad) {
        if (cantidad <= 0) removeFromCart(item);
        else item.setQuantity(cantidad);
    }

    public Order checkout() {
        if (carrito.estaVacia()) return null;
        Order pedido = new Order(carrito.aLista(), getCartTotal());
        historialPedidos.encolar(pedido);
        carrito = new Pila<>();
        return pedido;
    }

   

    public List<Product> getWishlist()          { return listaDeseos.aLista(); }
    public int getWishlistCount()               { return listaDeseos.getTamaño(); }
    public boolean isInWishlist(Product p)      { return listaDeseos.contiene(p); }

    public void toggleWishlist(Product p) {
        if (listaDeseos.contiene(p)) {
            listaDeseos.eliminar(p);
            p.setWishlist(false);
        } else {
            listaDeseos.agregar(p);
            p.setWishlist(true);
        }
    }

   

    public List<Order> getOrders()        { return historialPedidos.aLista(); }
    public int getTotalOrders()           { return historialPedidos.getTamaño(); }

    public double getTotalRevenue() {
        return historialPedidos.aLista().stream().mapToDouble(Order::getTotal).sum();
    }

    public double getAverageOrderValue() {
        if (historialPedidos.estaVacia()) return 0;
        return getTotalRevenue() / getTotalOrders();
    }
}
