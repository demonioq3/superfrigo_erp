package cl.superfrigo.model;

import cl.superfrigo.dao.ControlDespachoDAO;
import cl.superfrigo.dao.ControlFacturacionDAO;
import cl.superfrigo.entity.produccion.ControlDespacho;
import cl.superfrigo.entity.produccion.ControlFacturacion;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by asgco on 29-Mar-16.
 */
public class ControlFacturacionLazyDataModel extends LazyDataModel<ControlFacturacion> {

    private ControlFacturacionDAO controlFacturacionDAO;
    private Long ordenDeTrabajoId;
    private Date fecha;

    public ControlFacturacionLazyDataModel(ControlFacturacionDAO controlFacturacionDAO) {
        this.controlFacturacionDAO = controlFacturacionDAO;
    }

    public ControlFacturacionLazyDataModel(ControlFacturacionDAO controlFacturacionDAO, Long ordenDeTrabajoId) {
        this.controlFacturacionDAO = controlFacturacionDAO;
        this.ordenDeTrabajoId = ordenDeTrabajoId;
    }

    public ControlFacturacionLazyDataModel(ControlFacturacionDAO controlFacturacionDAO, Long ordenDeTrabajoId, Date fecha) {
        this.controlFacturacionDAO = controlFacturacionDAO;
        this.ordenDeTrabajoId = ordenDeTrabajoId;
        this.fecha = fecha;
    }

    @Override
    public List<ControlFacturacion> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        if(ordenDeTrabajoId != null)
            filters.put("ordenDeTrabajoId", ordenDeTrabajoId);

        if(fecha != null)
            filters.put("fecha", fecha);

        super.setRowCount(controlFacturacionDAO.countFiltered(filters).intValue());
        return controlFacturacionDAO.findFiltered(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder), filters);
    }

    @Override
    public ControlFacturacion getRowData(String rowKey) {
        return controlFacturacionDAO.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(ControlFacturacion object) {
        return object.getId();
    }
}
