package models;

import java.util.Objects;

public class ItemCarro {
    private int cantidad;
    private Producto producto;

    public ItemCarro(int cantidad, Producto producto) {
        this.cantidad = cantidad;
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;

    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    /*Creamos un método para comprara si ya un producto esta el la ista del carrito
     * de compras y no repertilo*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemCarro itemCarro = (ItemCarro) o;
        return Objects.equals(producto.getId(), itemCarro.producto.getId())
                && Objects.equals(cantidad, itemCarro.cantidad);
    }


    public double getSubtotal() {
        double SubtotalBase = cantidad * producto.getPrecio();
        //IVA del 15%
        double SubtotalCompuesto= SubtotalBase * 1.15;//agregar el 15% del IVA
        return Math.round(SubtotalCompuesto*100.0)/100.0;  //redondear a dos decimales
    }
}
