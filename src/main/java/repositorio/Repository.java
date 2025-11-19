package repositorio;

import java.sql.SQLException;
import java.util.List;

public interface Repository<T> {
    //Implementamos un método para listar
    List<T> listar() throws SQLException;
    T porId(Long id) throws SQLException;
    void guardar(T t) throws SQLException;
    void eliminar(Long id) throws SQLException;
}
