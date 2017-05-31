package cl.superfrigo.beans.abastecimiento;

/**
 * Created by asgco on 29-Feb-16.
 */
public class Producto {

    private String codigoProducto;
    private String descripcion;
    private String unidadMedida;
    private int cantidad;

    public Producto(String codigoProducto, String descripcion, String unidadMedida, int cantidad) {
        this.codigoProducto = codigoProducto;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.cantidad = cantidad;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
