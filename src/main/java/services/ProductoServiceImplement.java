package services;

import models.Categoria;
import models.Producto;
import repositorio.CategoriaRepositoryJdbcImplement;
import repositorio.ProductoRepositoryJdbcImplement;
import repositorio.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductoServiceImplement implements ProductoService {

    //Declaramos una variable de tipo ProductoRepositoryJdbcImplemen
    //private ProductoRepositoryJdbcImplemt repositoryJdbc;
    //Implementamos un constructor para traer la conexion

    // Usamos el tipo generico de la interfaz repository pero pasamos el tipo de objeto que vamos a implementar
    private Repository<Producto> repositoryJdbc;
    private Repository<Categoria> repositoryCategoriaJdbc;
    public ProductoServiceImplement(Connection connection) {
        this.repositoryJdbc = new ProductoRepositoryJdbcImplement(connection);
        this.repositoryCategoriaJdbc = new CategoriaRepositoryJdbcImplement(connection);
    }

    @Override
    public List<Producto> listar() {
        try{
            return repositoryJdbc.listar();
        }catch(SQLException throwables){
            throw new Exception(throwables.getMessage(), throwables.getCause());
        }
    }

    @Override
    public Optional<Producto> porId(Long id) {
        try{
            return Optional.ofNullable(repositoryJdbc.porId(id));
        }catch (SQLException throwables){
            throw new Exception(throwables.getMessage(), throwables.getCause());
        }
    }

    @Override
    public void guardar(Producto producto) {
        try{
            repositoryJdbc.guardar(producto);
        }catch (SQLException throwables){
            throw new Exception(throwables.getMessage(), throwables.getCause());
        }
    }

    @Override
    public void eliminar(Long id) {
        try{
            repositoryJdbc.eliminar(id);
        }catch (SQLException throwables){
            throw new Exception(throwables.getMessage(), throwables.getCause());
        }
    }

    @Override
    public List<Categoria> listarCategorias() {
        return List.of();
    }

    public List<Categoria> listarCategoria() {
        try{
            return repositoryCategoriaJdbc.listar();
        }catch (SQLException throwables){
            throw new Exception(throwables.getMessage(), throwables.getCause());
        }
    }

    @Override
    public Optional<Categoria> porIdCategoria(Long id) {
        try{
            return Optional.ofNullable(repositoryCategoriaJdbc.porId(id));
        }catch (SQLException throwables){
            throw new Exception(throwables.getMessage(), throwables.getCause());
        }
    }
}

