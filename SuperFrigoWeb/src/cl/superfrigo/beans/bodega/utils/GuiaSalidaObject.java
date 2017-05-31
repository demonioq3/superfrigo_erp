package cl.superfrigo.beans.bodega.utils;

/**
 * Created by agustinsantiago on 8/7/16.
 */
public class GuiaSalidaObject {

    private String id;
    private String codigo;
    private String descripcion;
    private String umedida;
    private String cantidad;
    private String punitario;
    private String total;

    public GuiaSalidaObject() {
    }

    public GuiaSalidaObject(String id, String codigo, String descripcion, String umedida, String cantidad, String punitario, String total) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.umedida = umedida;
        this.cantidad = cantidad;
        this.punitario = punitario;
        this.total = total;
    }

    public String getUmedida() {
        return umedida;
    }

    public void setUmedida(String umedida) {
        this.umedida = umedida;
    }

    public String getPunitario() {
        return punitario;
    }

    public void setPunitario(String punitario) {
        this.punitario = punitario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
