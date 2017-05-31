package cl.superfrigo.model;

import cl.superfrigo.entity.bodega.FichaProducto;

/**
 * Created by agustinsantiago on 6/18/16.
 */
public class StockRecetaModel{
    private FichaProducto fichaProducto;
    private Double cantidadPresupuestada;
    private Double cantidadEnBodega;
    private Double diferencia;

    public FichaProducto getFichaProducto() {
        return fichaProducto;
    }

    public void setFichaProducto(FichaProducto fichaProducto) {
        this.fichaProducto = fichaProducto;
    }

    public Double getCantidadPresupuestada() {
        return cantidadPresupuestada;
    }

    public void setCantidadPresupuestada(Double cantidadPresupuestada) {
        this.cantidadPresupuestada = cantidadPresupuestada;
    }

    public Double getCantidadEnBodega() {
        return cantidadEnBodega;
    }

    public void setCantidadEnBodega(Double cantidadEnBodega) {
        this.cantidadEnBodega = cantidadEnBodega;
    }

    public Double getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(Double diferencia) {
        this.diferencia = diferencia;
    }
}
