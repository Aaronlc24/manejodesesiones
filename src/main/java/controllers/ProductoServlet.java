package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Producto;
import services.LoginService;
import services.LoginServiceSessionImplement;
import services.ProductoService;
import services.ProductoServiceImplement;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@WebServlet({"/productos.html", "/productos"})
public class ProductoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Servicio que obtiene la lista de productos
        ProductoService service = new ProductoServiceImplement();
        List<Producto> productos = service.listar();

        // Servicio para obtener el usuario autenticado desde la sesión
        LoginService auth = new LoginServiceSessionImplement();
        Optional<String> usernameOptional = auth.getUsername(req);

        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {

            // --- Estructura HTML ---
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Listado de Productos</title>");

            // --- Estilos CSS modernos integrados ---
            out.println("<style>");
            out.println("body {");
            out.println("  font-family: 'Poppins', sans-serif;");
            out.println("  background: linear-gradient(135deg, #6a11cb, #2575fc);");
            out.println("  color: #333;");
            out.println("  margin: 0;");
            out.println("  padding: 40px;");
            out.println("  display: flex;");
            out.println("  flex-direction: column;");
            out.println("  align-items: center;");
            out.println("}");
            out.println("h1 {");
            out.println("  color: #fff;");
            out.println("  margin-bottom: 20px;");
            out.println("  text-shadow: 1px 1px 3px rgba(0,0,0,0.3);");
            out.println("}");
            out.println("table {");
            out.println("  width: 80%;");
            out.println("  border-collapse: collapse;");
            out.println("  background-color: #fff;");
            out.println("  border-radius: 12px;");
            out.println("  overflow: hidden;");
            out.println("  box-shadow: 0 5px 15px rgba(0,0,0,0.2);");
            out.println("}");
            out.println("th, td {");
            out.println("  padding: 12px 15px;");
            out.println("  text-align: center;");
            out.println("}");
            out.println("th {");
            out.println("  background-color: #2575fc;");
            out.println("  color: #fff;");
            out.println("  text-transform: uppercase;");
            out.println("  letter-spacing: 0.05em;");
            out.println("}");
            out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
            out.println("tr:hover { background-color: #dbe9ff; transition: 0.3s; }");
            out.println("a {");
            out.println("  color: #2575fc;");
            out.println("  text-decoration: none;");
            out.println("  font-weight: bold;");
            out.println("}");
            out.println("a:hover { text-decoration: underline; }");
            out.println(".user-info {");
            out.println("  color: #fff;");
            out.println("  margin-bottom: 20px;");
            out.println("  font-size: 16px;");
            out.println("}");
            out.println("</style>");

            out.println("</head>");
            out.println("<body>");

            // --- Encabezado de la página ---
            out.println("<h1>Listado de Productos</h1>");

            // Si el usuario está logueado, se muestra su nombre
            if (usernameOptional.isPresent()) {
                out.println("<div class='user-info'>Hola <strong>" + usernameOptional.get() + "</strong> ¡Bienvenido!</div>");
            }

            // --- Tabla de productos ---
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>Nombre</th>");
            out.println("<th>Tipo</th>");
            if (usernameOptional.isPresent()) {
                out.println("<th>Precio</th>");
                out.println("<th>Opciones</th>");
            }
            out.println("</tr>");

            // Recorre la lista de productos y genera filas dinámicas
            productos.forEach(p -> {
                out.println("<tr>");
                out.println("<td>" + p.getId() + "</td>");
                out.println("<td>" + p.getNombre() + "</td>");
                out.println("<td>" + p.getTipo() + "</td>");
                if (usernameOptional.isPresent()) {
                    out.println("<td>$" + p.getPrecio() + "</td>");
                    out.println("<td><a href='" + req.getContextPath()
                            + "/agregar-carro?id=" + p.getId()
                            + "'>Agregar al carro 🛒</a></td>");
                }
                out.println("</tr>");
            });
            out.println("</table>");

            out.println("</body>");
            out.println("</html>");
        }
    }
}

