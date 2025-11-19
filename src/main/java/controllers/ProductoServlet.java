package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import models.Producto;
import services.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

@WebServlet({"/productos.html", "/productos"})
public class ProductoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection conn = (Connection) req.getAttribute("conn");
        ProductoService service = new ProductoServiceImplement(conn);
        List<Producto> productos = service.listar();

        LoginService auth = new LoginServiceSessionImplement();
        Optional<String> usernameOptional = auth.getUsername(req);

        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>Listado de Productos</title>");

            // ESTILOS ELEGANTES 🔥🔥🔥
            out.println("<style>");
            out.println("body { font-family: 'Segoe UI', Tahoma, sans-serif; background: #f3f6fa; margin: 0; padding: 20px; }");
            out.println(".container { max-width: 1000px; margin: auto; background: white; padding: 25px; border-radius: 15px; "
                    + "box-shadow: 0 10px 25px rgba(0,0,0,0.08); }");

            out.println("h1 { color: #2c3e50; text-align: center; margin-bottom: 25px; font-size: 32px; }");

            out.println(".welcome { background: #dff0ff; padding: 12px; border-left: 5px solid #3498db; "
                    + "border-radius: 8px; margin-bottom: 20px; font-size: 18px; color: #2c3e50; }");

            out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
            out.println("th { background: #3498db; color: white; padding: 12px; font-weight: 500; }");
            out.println("td { padding: 12px; border-bottom: 1px solid #e0e0e0; color: #34495e; }");

            out.println("tr:hover { background: #ecf7ff; transition: 0.2s; }");

            out.println(".btn { padding: 8px 15px; background: #3498db; color: white; text-decoration: none; "
                    + "border-radius: 8px; font-size: 14px; transition: .3s; }");

            out.println(".btn:hover { background: #2c82c9; box-shadow: 0 4px 10px rgba(0,0,0,0.15); }");

            out.println("</style>");

            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");

            out.println("<h1>Listado de Productos</h1>");

            if (usernameOptional.isPresent()) {
                out.println("<div class='welcome'>Hola <b>" + usernameOptional.get()
                        + "</b>, ¡bienvenido nuevamente!</div>");
            }

            // TABLA
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>Nombre</th>");
            out.println("<th>Stock</th>");
            out.println("<th>Fecha Producción</th>");

            if (usernameOptional.isPresent()) {
                out.println("<th>Precio</th>");
                out.println("<th>Acción</th>");
            }

            out.println("</tr>");

            productos.forEach(p -> {
                out.println("<tr>");
                out.println("<td>" + p.getId() + "</td>");
                out.println("<td>" + p.getNombre() + "</td>");
                out.println("<td>" + p.getStock() + "</td>");
                out.println("<td>" + p.getFechaElaboracion() + "</td>");

                if (usernameOptional.isPresent()) {
                    out.println("<td>$ " + p.getPrecio() + "</td>");
                    out.println("<td><a class='btn' href='" + req.getContextPath()
                            + "/agregar-carro?id=" + p.getId()
                            + "'>Agregar al Carrito</a></td>");
                }

                out.println("</tr>");
            });

            out.println("</table>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
