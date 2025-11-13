package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import services.LoginService;
import services.LoginServiceSessionImplement;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet({"/login", "/login.html"})
public class LoginServlet extends HttpServlet {
    final static String USERNAME = "admin";
    final static String PASSWORD = "12345";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoginService auth = new LoginServiceSessionImplement();
        Optional<String> usernameOptional = auth.getUsername(req);

        if (usernameOptional.isPresent()) {
            resp.setContentType("text/html;charset=UTF-8");
            HttpSession session = req.getSession();

            // contador de inicios de sesión
            Integer contador = (Integer) session.getAttribute("contador");
            if (contador == null) {
                contador = 1;
            } else {
                contador++;
            }
            session.setAttribute("contador", contador);

            try (PrintWriter out = resp.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html lang='es'>");
                out.println("<head>");
                out.println("<meta charset='UTF-8'>");
                out.println("<title>Bienvenido " + usernameOptional.get() + "</title>");
                out.println("<style>");
                out.println("body {");
                out.println("  font-family: 'Segoe UI', Roboto, Helvetica, sans-serif;");
                out.println("  background: linear-gradient(135deg, #8EC5FC, #E0C3FC);");
                out.println("  margin: 0;");
                out.println("  height: 100vh;");
                out.println("  display: flex;");
                out.println("  justify-content: center;");
                out.println("  align-items: center;");
                out.println("  color: #333;");
                out.println("}");
                out.println(".container {");
                out.println("  background: rgba(255, 255, 255, 0.85);");
                out.println("  border-radius: 20px;");
                out.println("  box-shadow: 0 8px 24px rgba(0,0,0,0.2);");
                out.println("  padding: 40px 60px;");
                out.println("  text-align: center;");
                out.println("  backdrop-filter: blur(10px);");
                out.println("  animation: fadeIn 1s ease-in-out;");
                out.println("}");
                out.println("h1 {");
                out.println("  font-size: 2.2em;");
                out.println("  margin-bottom: 20px;");
                out.println("  color: #2c3e50;");
                out.println("  text-shadow: 1px 1px 3px rgba(0,0,0,0.1);");
                out.println("}");
                out.println("p { font-size: 1.1em; margin: 10px 0; }");
                out.println("a {");
                out.println("  display: inline-block;");
                out.println("  margin-top: 15px;");
                out.println("  padding: 10px 25px;");
                out.println("  border-radius: 30px;");
                out.println("  text-decoration: none;");
                out.println("  background: linear-gradient(135deg, #667eea, #764ba2);");
                out.println("  color: white;");
                out.println("  font-weight: bold;");
                out.println("  transition: all 0.3s ease;");
                out.println("}");
                out.println("a:hover {");
                out.println("  transform: scale(1.05);");
                out.println("  box-shadow: 0 6px 20px rgba(118,75,162,0.4);");
                out.println("}");
                out.println("@keyframes fadeIn { from {opacity: 0; transform: translateY(20px);} to {opacity: 1; transform: translateY(0);} }");
                out.println("</style>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class='container'>");
                out.println("<h1>¡Hola, " + usernameOptional.get() + "!</h1>");
                out.println("<p>Has iniciado sesión con éxito.</p>");
                out.println("<p><strong>Te has logueado " + contador + " veces.</strong></p>");
                out.println("<a href='" + req.getContextPath() + "/index.html'>Volver</a>");
                out.println("<a href='" + req.getContextPath() + "/logout'>Cerrar sesión</a>");
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");
            }
        } else {
            getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            HttpSession session = req.getSession();
            session.setAttribute("username", username);
            resp.sendRedirect(req.getContextPath() + "/login.html");
        } else {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Lo sentimos, no está autorizado para ingresar a esta página!");
        }
    }
}

