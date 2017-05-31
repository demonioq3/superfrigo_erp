package cl.superfrigo.model;

import cl.superfrigo.dao.OrdenDeCompraDAO;
import cl.superfrigo.dao.RequisicionDAO;
import cl.superfrigo.entity.abastecimiento.OrdenDeCompra;
import cl.superfrigo.entity.abastecimiento.Requisicion;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by asgco on 29-Mar-16.
 */
public class OrdenesDeCompraLazyDataModel extends LazyDataModel<OrdenDeCompra> {

    private OrdenDeCompraDAO ordenDeCompraDAO;
    private RequisicionDAO requisicionDAO;
    private Long proveedorId;
    private Long fichaProductoId;

    public OrdenesDeCompraLazyDataModel(OrdenDeCompraDAO ordenDeCompraDAO) {
        this.ordenDeCompraDAO = ordenDeCompraDAO;
    }

    public OrdenesDeCompraLazyDataModel(OrdenDeCompraDAO ordenDeCompraDAO, RequisicionDAO requisicionDAO) {
        this.ordenDeCompraDAO = ordenDeCompraDAO;
        this.requisicionDAO = requisicionDAO;
    }

    public OrdenesDeCompraLazyDataModel(OrdenDeCompraDAO ordenDeCompraDAO, RequisicionDAO requisicionDAO, Long proveedorId) {
        this.ordenDeCompraDAO = ordenDeCompraDAO;
        this.requisicionDAO = requisicionDAO;
        this.proveedorId = proveedorId;
        this.fichaProductoId = null;
    }

    public OrdenesDeCompraLazyDataModel(OrdenDeCompraDAO ordenDeCompraDAO, RequisicionDAO requisicionDAO, Long fichaProductoId, String s) {
        this.ordenDeCompraDAO = ordenDeCompraDAO;
        this.requisicionDAO = requisicionDAO;
        this.fichaProductoId = fichaProductoId;
        this.proveedorId = null;
    }

    @Override
    public List<OrdenDeCompra> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        if ( proveedorId != null )
            filters.put("proveedorId", proveedorId);

        if( fichaProductoId != null)
            filters.put("fichaProductoId", fichaProductoId);

        super.setRowCount(ordenDeCompraDAO.countFiltered(filters).intValue());
        List<OrdenDeCompra> lista = ordenDeCompraDAO.findFiltered(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder), filters);
        for (OrdenDeCompra ordenDeCompra : lista) {
            ordenDeCompra.setRequisicion(requisicionDAO.getById(ordenDeCompra.getRequisicionId()));
        }

        return lista;
    }

    @Override
    public OrdenDeCompra getRowData(String rowKey) {
        return ordenDeCompraDAO.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(OrdenDeCompra object) {
        return object.getId();
    }
}
