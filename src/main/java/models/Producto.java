package models;

import java.time.LocalDate;

public class Producto {
    private Long id;
    private String nombre;
    //Implementamos las variables de la base de datos
    private Categoria categoria;
    private double precio;
    private int stock;
    //Declaramos la variable tipo por descripción
    private String descripcion;
    //Declaramos las variables fecha de elaboración, caducidad y condición
    private LocalDate fechaElaboracion;
    private LocalDate fechaCaducidad;
    private int condición;

    public Producto() {
    }

    //Modificamos el constructor con las variables añadidas
    public Producto(Long id, int condición, LocalDate fechaCaducidad, LocalDate fechaElaboracion,
                    int stock, double precio, String nombre, String tipo) {
        this.id = id;
        this.condición = condición;

        //Instanciamos un objeto de tipo Categoría
        Categoria categoria = new Categoria();
        this.categoria = categoria;
        this.categoria.setNombre(tipo);

        this.fechaCaducidad = fechaCaducidad;
        this.fechaElaboracion = fechaElaboracion;
        this.stock = stock;
        this.precio = precio;

        this.nombre = nombre;
    }

    //Implementamos los métodos setter and getter
    //de las variables añadidas

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCondición() {
        return condición;
    }

    public void setCondición(int condición) {
        this.condición = condición;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public LocalDate getFechaElaboracion() {
        return fechaElaboracion;
    }

    public void setFechaElaboracion(LocalDate fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
