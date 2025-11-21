package repositorio;

import models.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepositoryJdbcImplement implements Repository<Categoria> {

    private Connection conn;

    public CategoriaRepositoryJdbcImplement(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Categoria> listar() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();

        String sql = "SELECT id, nombreCategoria, descripcion, estado FROM categoria";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Categoria c = new Categoria();
                c.setId(rs.getLong("id"));
                c.setNombre(rs.getString("nombreCategoria"));
                c.setDescripcion(rs.getString("descripcion"));
                c.setEstado(rs.getInt("estado"));
                categorias.add(c);
            }
        }
        return categorias;
    }

    @Override
    public Categoria porId(Long id) throws SQLException {
        Categoria c = null;
        String sql = "SELECT id, nombreCategoria, descripcion, estado FROM categoria WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    c = new Categoria();
                    c.setId(rs.getLong("id"));
                    c.setNombre(rs.getString("nombreCategoria"));
                    c.setDescripcion(rs.getString("descripcion"));
                    c.setEstado(rs.getInt("estado"));
                }
            }
        }
        return c;
    }

    @Override
    public void guardar(Categoria categoria) throws SQLException {
        boolean esUpdate = categoria.getId() != null && categoria.getId() > 0;

        String sql;
        if (esUpdate) {
            sql = "UPDATE categoria SET nombreCategoria=?, descripcion=?, estado=? WHERE id=?";
        } else {
            sql = "INSERT INTO categoria(nombreCategoria, descripcion, estado) VALUES (?,?,?)";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.setInt(3, categoria.getEstado());

            if (esUpdate) {
                stmt.setLong(4, categoria.getId());
            }

            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
