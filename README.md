# NOETIC STUDIOS - Aplicación JavaFX
**Sistema de Ventas Streetwear**

---

## ESTRUCTURA DEL PROYECTO

```
NoeticStudios/
├── pom.xml                          ← Configuración Maven
├── src/main/
│   ├── java/com/noetic/
│   │   ├── MainApp.java             ← Punto de entrada
│   │   ├── model/
│   │   │   ├── Product.java
│   │   │   ├── CartItem.java
│   │   │   ├── Order.java
│   │   │   └── User.java
│   │   ├── data/
│   │   │   └── DataStore.java       ← Gestión de datos en memoria
│   │   └── view/
│   │       ├── LoginView.java       ← Pantalla de inicio de sesión
│   │       ├── MainView.java        ← Contenedor principal + navbar
│   │       ├── ShopView.java        ← Catálogo de productos
│   │       ├── CartView.java        ← Carrito de compras
│   │       ├── WishlistView.java    ← Lista de deseos
│   │       ├── HistorialView.java   ← Historial de pedidos
│   │       └── AdminView.java       ← Panel de administración
│   └── resources/
│       └── styles/
│           └── main.css             ← Estilos globales
└── README.md
```
--
## CREDENCIALES DE PRUEBA

| Rol         | Correo                  | Contraseña |
|-------------|-------------------------|------------|
| Usuario     | usuario@tienda.com      | user123    |
| Administrador | admin@tienda.com      | admin123   |

> El admin tiene acceso al panel de administración con métricas, gestión de productos y pedidos.

---

## FUNCIONALIDADES

### 🛍 Tienda
- Catálogo de 14 productos de moda streetwear
- Filtros por categoría: Todos / Camisetas / Gorras / Pantalones / Jeans / Buzos
- Botón de lista de deseos por producto (♡/♥)
- Agregar al carrito con notificación toast

### 🛒 Carrito
- Lista de productos agregados
- Aumentar/disminuir cantidad
- Eliminar productos
- Resumen de compra con total
- Botón "Finalizar Compra"

### ♡ Lista de Deseos
- Ver y gestionar productos guardados
- Agregar al carrito desde lista de deseos

### 📋 Historial
- Ver todos los pedidos realizados
- Detalle de productos por pedido
- Estado del pedido

### ⚙ Panel Admin (solo administradores)
- **Resumen**: Ingresos totales, pedidos, productos, valor promedio
- **Productos**: Tabla con CRUD (agregar, editar, eliminar)
- **Pedidos**: Tabla de todos los pedidos

---

## TECNOLOGÍAS USADAS

- **JavaFX 25**
- **CSS**
- **Java 25** 
- **Maven** 

---

## NOTAS

- Los datos son en memoria (se reinician al cerrar la app)
- Las imágenes de productos son emojis (se pueden reemplazar con imágenes reales)

---

## Actualizaciones Futuras — Noetic Studios

Alta Prioridad

 Imágenes reales de productos (PNG/JPG desde recursos)
 Página de detalle de producto con selector de talla y cantidad
 Selector de talla en el carrito (S / M / L / XL)

Media Prioridad

 Fuente Bebas Neue (Google Fonts + Font.loadFont())
 Barra de búsqueda de productos en la tienda
 Corregir bug del filtro activo al volver a la tienda

Baja Prioridad

 Animación fade entre vistas (FadeTransition)
 Toast mejorado con ícono de check verde
 Gráfica de ventas en panel Admin (BarChart / LineChart)
 Persistencia de datos con SQLite + JDBC