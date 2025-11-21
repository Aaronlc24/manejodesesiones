package repositorio;

import models.Categoria;
import models.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositoryJdbcImplement implements Repository<Producto> {

    private Connection conn;

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
                productos.add(getProducto(rs));
            }
        }
        return productos;
    }

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
    public void guardar(Producto p) throws SQLException {
        boolean esUpdate = p.getId() != null && p.getId() > 0;

        String sql;
        if (esUpdate) {
            sql = "UPDATE producto SET nombreProducto = ?, idCategoria = ?, precio = ?, " +
                    "cantidad = ?, descripcion = ?, fecha_elaboracion = ?, fecha_caducidad = ?, condicion = ? " +
                    "WHERE id = ?";
        } else {
            sql = "INSERT INTO producto (nombreProducto, idCategoria, precio, cantidad, " +
                    "descripcion, fecha_elaboracion, fecha_caducidad, condicion) " +
                    "VALUES (?,?,?,?,?,?,?,?)";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNombre());                         // nombreProducto
            stmt.setLong(2, p.getCategoria().getId());                // idCategoria
            stmt.setDouble(3, p.getPrecio());                         // precio
            stmt.setInt(4, p.getStock());                             // cantidad
            stmt.setString(5, p.getDescripcion());                    // descripcion
            stmt.setDate(6, Date.valueOf(p.getFechaElaboracion()));   // fecha_elaboracion
            stmt.setDate(7, Date.valueOf(p.getFechaCaducidad()));     // fecha_caducidad

            int condicion = p.getCondición() == 0 ? 1 : p.getCondición();
            stmt.setInt(8, condicion);                                // condicion

            if (esUpdate) {
                stmt.setLong(9, p.getId());                           // WHERE id=?
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
        p.setStock(rs.getInt("cantidad"));
        p.setPrecio(rs.getDouble("precio"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setFechaElaboracion(rs.getDate("fecha_elaboracion").toLocalDate());
        p.setFechaCaducidad(rs.getDate("fecha_caducidad").toLocalDate());
        p.setCondición(rs.getInt("condicion"));

        Categoria c = new Categoria();
        c.setId(rs.getLong("idCategoria"));
        c.setNombre(rs.getString("nombreCategoria"));
        p.setCategoria(c);

        return p;
    }
}
