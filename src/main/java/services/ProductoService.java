package services;

import models.Categoria;
import models.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> listar();
    Optional<Producto> porId(Long id);
    void guardar(Producto producto);
    void eliminar(Long id);
    //implementamos un metodo para listar una categoria y traer una categoria por id
    List<Categoria> listarCategorias();
    Optional<Categoria> porIdCategoria(Long id);
}
