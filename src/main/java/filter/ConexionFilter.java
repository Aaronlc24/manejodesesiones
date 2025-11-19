package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

// IMPORTA TU CLASE DE CONEXIÓN SEGÚN TU PAQUETE
// import config.Conexion; // <-- CAMBIA ESTE IMPORT SI TU PAQUETE ES OTRO
import util.ConexionBDD;

@WebFilter("/*")
public class ConexionFilter implements Filter {

    /*
     UNA CLASE FILTER EN JAVA ES UN OBJETO QUE REALIZA TAREAS
     DE FILTRADO EN LAS SOLICITUDES CLIENTE-SERVIDOR.
     LOS FILTROS SE PUEDEN EJECUTAR EN SERVIDORES COMPATIBLES CON JAKARTA EE.
     LOS FILTROS INTERCEPTAN SOLICITUDES Y RESPUESTAS DE MANERA DINÁMICA
     PARA TRANSFORMAR O UTILIZAR LA INFORMACIÓN.
     EL FILTRADO SE REALIZA MEDIANTE EL MÉTODO doFilter.
    */

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        /*
          request: representa la solicitud del cliente
          response: representa la respuesta del servidor
          filterChain: permite pasar la solicitud y respuesta al siguiente filtro/servlet
        */

        // Obtenemos la conexión de la base de datos
        try (Connection conn = ConexionBDD.getConnection()) {
            // Verificamos que la conexión no esté en auto-commit
            if (conn.getAutoCommit()) {
                conn.setAutoCommit(false);
            }

            try {
                // Agregamos la conexión como atributo en la solicitud
                // para que otros componentes (servlets, DAOs) puedan usarla
                request.setAttribute("conn", conn);

                // Pasamos la solicitud y respuesta al siguiente elemento en la cadena
                filterChain.doFilter(request, response);

                // Si todo salió bien, confirmamos la transacción
                conn.commit();
            } catch (Exception e) {
                // Si ocurre un error, deshacemos la transacción
                conn.rollback();

                // Enviamos un código de error HTTP 500 al cliente
                ((HttpServletResponse) response).sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Error interno del servidor: " + e.getMessage()
                );

                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Lanzamos una excepción para que el contenedor sepa que hubo un problema grave
            throw new ServletException("Error al obtener la conexión a la base de datos", e);
        }
    }
}

