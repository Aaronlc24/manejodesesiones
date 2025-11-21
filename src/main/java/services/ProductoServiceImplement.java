package services;

import models.Categoria;
import models.Producto;
import repositorio.CategoriaRepositoryJdbcImplement;
import repositorio.ProductoRepositoryJdbcImplement;
import repositorio.Repository;
import services.Exception;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductoServiceImplement implements ProductoService {

    private Repository<Producto> repositoryJdbc;
    private Repository<Categoria> repositoryCategoriaJdbc;

    public ProductoServiceImplement(Connection connection) {
        this.repositoryJdbc = new ProductoRepositoryJdbcImplement(connection);
        this.repositoryCategoriaJdbc = new CategoriaRepositoryJdbcImplement(connection);
    }

    @Override
    public List<Producto> listar() {
        try {
            return repositoryJdbc.listar();
        } catch (SQLException e) {
            throw new Exception(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Optional<Producto> porId(Long id) {
        try {
            return Optional.ofNullable(repositoryJdbc.porId(id));
        } catch (SQLException e) {
            throw new Exception(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void guardar(Producto producto) {
        try {
            repositoryJdbc.guardar(producto);
        } catch (SQLException e) {
            throw new Exception(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void eliminar(Long id) {
        try {
            repositoryJdbc.eliminar(id);
        } catch (SQLException e) {
            throw new Exception(e.getMessage(), e.getCause());
        }
    }

    // ✅ AQUÍ ES DONDE SE CARGA LA LISTA PARA EL <select>
    @Override
    public List<Categoria> listarCategorias() {
        try {
            return repositoryCategoriaJdbc.listar();
        } catch (SQLException e) {
            throw new Exception(e.getMessage(), e.getCause());
        }
    }
}
