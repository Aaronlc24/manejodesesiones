package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Producto;
import services.*;
import models.Categoria;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

@WebServlet({"/productos", "/productos.html"})
public class ProductoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Traemos la conexión
        Connection conn = (Connection) req.getAttribute("conn");

        ProductoService service = new ProductoServiceImplement(conn);
        List<Producto> productos = service.listar();

        LoginService auth = new LoginServiceSessionImplement();
        Optional<String> usernameOptional = auth.getUsername(req);

        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Listado de Productos</title>");

            // ---- ESTILOS WAOS Y ELEGANTES ----
            out.println("<style>");

            out.println("body {");
            out.println("  background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);");
            out.println("  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;");
            out.println("  color: #fff;");
            out.println("  padding: 40px;");
            out.println("}");

            out.println("h1 {");
            out.println("  text-align: center;");
            out.println("  font-size: 40px;");
            out.println("  margin-bottom: 30px;");
            out.println("  color: #f5f5f5;");
            out.println("  text-shadow: 0 2px 5px rgba(0,0,0,0.6);");
            out.println("}");

            out.println(".welcome {");
            out.println("  text-align: center;");
            out.println("  font-size: 20px;");
            out.println("  margin-bottom: 20px;");
            out.println("  color: #00eaff;");
            out.println("}");

            out.println("table {");
            out.println("  width: 90%;");
            out.println("  margin: auto;");
            out.println("  border-collapse: collapse;");
            out.println("  background: rgba(255,255,255,0.1);");
            out.println("  border-radius: 12px;");
            out.println("  overflow: hidden;");
            out.println("  box-shadow: 0 4px 20px rgba(0,0,0,0.3);");
            out.println("}");

            out.println("th, td {");
            out.println("  padding: 14px;");
            out.println("  text-align: center;");
            out.println("}");

            out.println("th {");
            out.println("  background: rgba(255,255,255,0.15);");
            out.println("  font-size: 18px;");
            out.println("  letter-spacing: 1px;");
            out.println("}");

            out.println("tr:nth-child(even) {");
            out.println("  background: rgba(255,255,255,0.05);");
            out.println("}");

            out.println("tr:hover {");
            out.println("  background: rgba(0, 255, 255, 0.2);");
            out.println("  transition: 0.3s;");
            out.println("}");

            out.println("a.btn {");
            out.println("  background: #00eaff;");
            out.println("  color: #000;");
            out.println("  padding: 8px 14px;");
            out.println("  border-radius: 8px;");
            out.println("  font-weight: bold;");
            out.println("  text-decoration: none;");
            out.println("  box-shadow: 0 4px 10px rgba(0,0,0,0.4);");
            out.println("  transition: 0.3s;");
            out.println("}");

            out.println("a.btn:hover {");
            out.println("  transform: scale(1.07);");
            out.println("  background: #67f7ff;");
            out.println("}");

            out.println("</style>");

            out.println("</head>");
            out.println("<body>");

            out.println("<h1>Listado de Productos</h1>");

            if (usernameOptional.isPresent()) {
                out.println("<div class='welcome'>Hola " + usernameOptional.get() + ", ¡Bienvenido!</div>");
            }

            out.println("<table>");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>Nombre</th>");
            out.println("<th>Stock</th>");
            out.println("<th>Fecha Producción</th>");
            if (usernameOptional.isPresent()) {
                out.println("<th>Precio</th>");
                out.println("<th>Opciones</th>");
            }
            out.println("</tr>");

            productos.forEach(p -> {
                out.println("<tr>");
                out.println("<td>" + p.getId() + "</td>");
                out.println("<td>" + p.getNombre() + "</td>");
                out.println("<td>" + p.getStock() + "</td>");
                out.println("<td>" + p.getFechaElaboracion() + "</td>");

                if (usernameOptional.isPresent()) {
                    out.println("<td>$" + p.getPrecio() + "</td>");
                    out.println("<td><a class='btn' href='" +
                            req.getContextPath() +
                            "/agregar-carro?id=" + p.getId() +
                            "'>Agregar al Carro</a></td>");
                }


                out.println("</tr>");
            });

            out.println("</table>");

            out.println("</body>");
            out.println("</html>");
        }

        req.setAttribute("productos", productos);
        req.setAttribute("username", usernameOptional);

        getServletContext().getRequestDispatcher("/productos.jsp").forward(req, resp);
    }
}

