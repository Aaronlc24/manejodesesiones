package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Categoria;
import models.Producto;
import services.ProductoService;
import services.ProductoServiceImplement;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/producto/form")
public class ProductoFormServlet extends HttpServlet {

    // ========= GET: mostrar formulario =========
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Ojo: usamos la conexión que el filtro deja en el request con nombre "conn"
        Connection conn = (Connection) req.getAttribute("conn");
        ProductoService service = new ProductoServiceImplement(conn);

        Long id;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            id = 0L;
        }

        Producto producto = new Producto();
        producto.setCategoria(new Categoria());

        // Si viene id, es edición: cargamos el producto
        if (id > 0) {
            service.porId(id).ifPresent(p -> {
                producto.setId(p.getId());
                producto.setNombre(p.getNombre());
                producto.setCategoria(p.getCategoria());
                producto.setPrecio(p.getPrecio());
                producto.setStock(p.getStock());
                producto.setDescripcion(p.getDescripcion());
                producto.setFechaElaboracion(p.getFechaElaboracion());
                producto.setFechaCaducidad(p.getFechaCaducidad());
                producto.setCondición(p.getCondición());
            });
        }

        // Enviamos producto y categorías al JSP
        req.setAttribute("producto", producto);
        req.setAttribute("categorias", service.listarCategorias());

        getServletContext().getRequestDispatcher("/form.jsp").forward(req, resp);
    }

    // ========= POST: guardar / actualizar =========
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Connection conn = (Connection) req.getAttribute("conn");
        ProductoService service = new ProductoServiceImplement(conn);

        // Parámetros del formulario
        String idParam            = req.getParameter("id");
        String nombreParam        = req.getParameter("nombre");
        String categoriaParam     = req.getParameter("categoria");
        String stockParam         = req.getParameter("stock");
        String precioParam        = req.getParameter("precio");
        String descripcion        = req.getParameter("descripcion");
        String fechaElabParam     = req.getParameter("fecha_elaboracion");
        String fechaCaduParam     = req.getParameter("fecha_caducidad");

        Map<String, String> errores = new HashMap<>();

        // ---- Categoría (idCategoria) ----
        Long categoriaId = null;
        try {
            if (categoriaParam != null && !categoriaParam.isBlank()) {
                categoriaId = Long.parseLong(categoriaParam);
            }
        } catch (NumberFormatException e) {
            errores.put("categoria", "La categoría es inválida");
        }
        if (categoriaId == null || categoriaId.equals(0L)) {
            errores.put("categoria", "La categoría no puede estar vacía");
        }

        // ---- Stock (cantidad) ----
        Integer stock = 0;
        try {
            if (stockParam != null && !stockParam.isBlank()) {
                stock = Integer.valueOf(stockParam);
            }
        } catch (NumberFormatException e) {
            errores.put("stock", "El stock debe ser un número válido");
        }
        if (stockParam == null || stockParam.trim().isEmpty() || stock <= 0) {
            errores.put("stock", "El stock no puede estar vacío o ser cero");
        }

        // ---- Precio ----
        Double precio = null;
        if (precioParam != null && !precioParam.isBlank()) {
            String precioLimpio = precioParam.trim().replace(",", ".");
            try {
                precio = Double.valueOf(precioLimpio);
            } catch (NumberFormatException e) {
                errores.put("precio", "El precio debe ser un número válido");
            }
        }
        if (precio == null || precio <= 0) {
            errores.put("precio", "El precio debe ser mayor que cero");
        }

        // ---- Nombre y descripción ----
        if (nombreParam == null || nombreParam.isBlank()) {
            errores.put("nombre", "El nombre del producto no puede estar vacío");
        }
        if (descripcion == null || descripcion.isBlank()) {
            errores.put("descripcion", "La descripción no puede estar vacía");
        }

        // ---- Fechas ----
        LocalDate fechaElaboracion = null;
        LocalDate fechaCaducidad   = null;

        if (fechaElabParam == null || fechaElabParam.isBlank()) {
            errores.put("fecha_elaboracion", "La fecha de elaboración no puede estar vacía");
        }
        if (fechaCaduParam == null || fechaCaduParam.isBlank()) {
            errores.put("fecha_caducidad", "La fecha de caducidad no puede estar vacía");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (!errores.containsKey("fecha_elaboracion") && !errores.containsKey("fecha_caducidad")) {
            try {
                fechaElaboracion = LocalDate.parse(fechaElabParam, formatter);
                fechaCaducidad   = LocalDate.parse(fechaCaduParam, formatter);
            } catch (DateTimeParseException e) {
                errores.put("fecha_elaboracion", "Formato de fecha inválido");
                errores.put("fecha_caducidad", "Formato de fecha inválido");
            }
        }

        // ---- ID (para saber si es nuevo o edición) ----
        Long id = null;
        if (idParam != null && !idParam.isBlank()) {
            try {
                id = Long.parseLong(idParam);
            } catch (NumberFormatException e) {
                // si falla lo dejamos en null = nuevo
            }
        }

        // ---- Armamos el objeto Producto ----
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombreParam);
        producto.setStock(stock);
        producto.setPrecio(precio);
        producto.setDescripcion(descripcion);
        producto.setFechaElaboracion(fechaElaboracion);
        producto.setFechaCaducidad(fechaCaducidad);
        producto.setCondición(1); // activo por defecto

        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);
        producto.setCategoria(categoria);

        // ---- Flujo final ----
        if (errores.isEmpty()) {
            // Guarda en la BD (INSERT o UPDATE según id)
            service.guardar(producto);
            // Vuelve al listado
            resp.sendRedirect(req.getContextPath() + "/productos");
        } else {
            // Volvemos al form con los datos y los errores
            req.setAttribute("errores", errores);
            req.setAttribute("producto", producto);
            req.setAttribute("categorias", service.listarCategorias());
            getServletContext().getRequestDispatcher("/form.jsp").forward(req, resp);
        }
    }
}
