package repositorio;

import models.Categoria;
import models.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositoryJdbcImplement implements Repository<Producto> {

    // Obtener la conexión a la BBDD
    private Connection conn;

    // Obtengo mi conexión mediante el constructor
    public ProductoRepositoryJdbcImplement(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Producto> listar() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombreCategoria " +
                "FROM producto AS p " +
                "INNER JOIN categoria AS c ON p.idCategoria = c.id " +
                "ORDER BY p.id ASC";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = getProducto(rs);
                productos.add(p);
            }
        }
        return productos;
    }

    // Implementamos un método para buscar un registro por ID
    @Override
    public Producto porId(Long id) throws SQLException {
        Producto producto = null;
        String sql = "SELECT p.*, c.nombreCategoria " +
                "FROM producto AS p " +
                "INNER JOIN categoria AS c ON p.idCategoria = c.id " +
                "WHERE p.id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    producto = getProducto(rs);
                }
            }
        }
        return producto;
    }

    @Override
    public void guardar(Producto producto) throws SQLException {
        boolean esUpdate = producto.getId() != null && producto.getId() > 0;

        String sql;
        if (esUpdate) {
            sql = "UPDATE producto SET nombreProducto = ?, idCategoria = ?, precio = ?, " +
                    "cantidad = ?, descripcion = ?, fecha_elaboracion = ?, " +
                    "fecha_caducidad = ?, condicion = ? WHERE id = ?";
        } else {
            sql = "INSERT INTO producto (nombreProducto, idCategoria, precio, cantidad, " +
                    "descripcion, fecha_elaboracion, fecha_caducidad, condicion) " +
                    "VALUES (?,?,?,?,?,?,?,?)";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // OJO: mapeamos stock -> columna cantidad
            stmt.setString(1, producto.getNombre());                       // nombreProducto
            stmt.setLong(2, producto.getCategoria().getId());              // idCategoria
            stmt.setDouble(3, producto.getPrecio());                       // precio
            stmt.setInt(4, producto.getStock());                           // cantidad
            stmt.setString(5, producto.getDescripcion());                  // descripcion
            stmt.setDate(6, Date.valueOf(producto.getFechaElaboracion())); // fecha_elaboracion
            stmt.setDate(7, Date.valueOf(producto.getFechaCaducidad()));   // fecha_caducidad
            // si tu campo condición en el objeto está en 0, lo dejamos en 1 por defecto
            int condicion = producto.getCondición() == 0 ? 1 : producto.getCondición();
            stmt.setInt(8, condicion);                                     // condicion

            if (esUpdate) {
                stmt.setLong(9, producto.getId());                         // id (WHERE)
            }

            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = "DELETE FROM producto WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private static Producto getProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getLong("id"));
        p.setNombre(rs.getString("nombreProducto"));
        // cantidad en la BD -> stock en el modelo
        p.setStock(rs.getInt("cantidad"));
        p.setPrecio(rs.getDouble("precio"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setFechaElaboracion(rs.getDate("fecha_elaboracion").toLocalDate());
        p.setFechaCaducidad(rs.getDate("fecha_caducidad").toLocalDate());
        p.setCondición(rs.getInt("condicion"));

        // Creamos un objeto de tipo categoria
        Categoria c = new Categoria();
        c.setId(rs.getLong("idCategoria"));
        c.setNombre(rs.getString("nombreCategoria"));
        p.setCategoria(c);

        return p;
    }
}
