package filter;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import services.Exception;
import util.ConexionBDD;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebFilter("/*")
public class ConexionFilter implements Filter {
    /*
     * Una clase filter en java es un objeto que realiza tareas de filtrado
     * en las solicitudes de petición y respuesta a un recurso. Los filtros
     * se pueden ejecutar de manera dinámica para transformar la
     * información que contienen. El filtrado se realiza mediante el
     * método doFilter()
     */

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        /*
         * request: petición del cliente
         * response: respuesta del servidor
         * chain: Es una clase de filtro que representa el flujo de procesamiento,
         * llama al método chain.doFilter(request, response), dentro de un filtro
         * pasa la solicitud al siguiente filtro o al recurso destino(servlet, jsp,
         * pdf u otro)
         */

        // Llamamos a la conexión
        try (Connection connection = ConexionBDD.getConnection()) {

            // Verificamos que la conexión no se realice automáticamente
            if (connection.getAutoCommit()) {
                // cambiamos a una conexión manual
                connection.setAutoCommit(false);
            }

            try {
                request.setAttribute("conn", connection);
                chain.doFilter(request, response);
                connection.commit();
            } catch (SQLException | Exception e) {
                connection.rollback();
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        e.getMessage());
                e.printStackTrace();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
