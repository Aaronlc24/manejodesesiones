package services;

import models.Producto;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProductoServiceImplement implements ProductoService{
    @Override
    public List<Producto> listar() {
        return Arrays.asList(new Producto(1L, "notebook", "computacion", 1750),
                new Producto(2L, "mesa escritorio", "oficina", 1000),
                new Producto(3L, "teclado mecanico", "computacion", 4000));
    }

    @Override
    public Optional<Producto> porId(Long id) {
        /*Stream en Java es convertir una lista en una secuencia de elementos
        * sobe la cual se pueden aplicar operaciones funcionales como filter, map, collect
        * filter: aqui se filtra los elementos del stream
        * p-> representa cada Producto de la lista
        * p.getId().equals(id) compara el id del producto con id que recibimos como parámetro
        * Si el id coincide, el producto pasa el filtro; sino, se descarta.
        * findAny(): intenta encontrar un elemento cualquiera del Stream que cumpla la codición
        * si lo encuntra devuelbe un Optional<Producto>,
        * sino le encuentra devuelve Optional.empty()
        * */
        return listar().stream().filter(p -> p.getId().equals(id)).findAny();
    }
}
