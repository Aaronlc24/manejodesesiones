package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Producto;
import services.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

@WebServlet("/productos")
public class ProductoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Traemos la conexión
        Connection conn = (Connection) req.getAttribute("conn");

        // Service que va a BBDD
        ProductoService service = new ProductoServiceImplement(conn);
        List<Producto> productos = service.listar();

        // Usuario logueado (si existe)
        LoginService auth = new LoginServiceSessionImplement();
        Optional<String> usernameOptional = auth.getUsername(req);

        // Mandamos datos al JSP
        req.setAttribute("productos", productos);
        req.setAttribute("username", usernameOptional);

        // El JSP productos.jsp ya tiene los estilos waos
        getServletContext().getRequestDispatcher("/productos.jsp").forward(req, resp);
    }
}
