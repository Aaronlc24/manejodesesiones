package services;

import models.Producto;
import repositorio.ProductoRepositoryJdbcImplement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductoServiceImplement implements ProductoService {
    // Declaramos una variable de tipo ProductoRepositoryJdbcImplement
    private ProductoRepositoryJdbcImplement repositoryJdbc;

    // Implementamos un constructor para traer la conexión
    public ProductoServiceImplement(Connection connection) {
        this.repositoryJdbc = new ProductoRepositoryJdbcImplement(connection);
    }

    @Override
    public List<Producto> listar() {
        try {
            return repositoryJdbc.listar();
        } catch (SQLException throwables) {
            throw new Exception(throwables.getMessage(), throwables.getCause());
        }
    }

    @Override
    public Optional<Producto> porId(Long id) {
        try {
            return Optional.ofNullable(repositoryJdbc.porId(id));
        } catch (SQLException throwables) {
            throw new Exception(throwables.getMessage(), throwables.getCause());
        }
    }
}
