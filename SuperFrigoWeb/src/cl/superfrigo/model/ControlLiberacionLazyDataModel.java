package cl.superfrigo.model;

import cl.superfrigo.dao.ControlDespachoDAO;
import cl.superfrigo.dao.ControlLiberacionDAO;
import cl.superfrigo.entity.produccion.ControlDespacho;
import cl.superfrigo.entity.produccion.ControlLiberacion;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by asgco on 29-Mar-16.
 */
public class ControlLiberacionLazyDataModel extends LazyDataModel<ControlLiberacion> {

    private ControlLiberacionDAO controlLiberacionDAO;
    private Long ordenDeTrabajoId;
    private Date fecha;

    public ControlLiberacionLazyDataModel(ControlLiberacionDAO controlLiberacionDAO) {
        this.controlLiberacionDAO = controlLiberacionDAO;
    }

    public ControlLiberacionLazyDataModel(ControlLiberacionDAO controlLiberacionDAO, Long ordenDeTrabajoId) {
        this.controlLiberacionDAO = controlLiberacionDAO;
        this.ordenDeTrabajoId = ordenDeTrabajoId;
    }

    public ControlLiberacionLazyDataModel(ControlLiberacionDAO controlLiberacionDAO, Long ordenDeTrabajoId, Date fecha) {
        this.controlLiberacionDAO = controlLiberacionDAO;
        this.ordenDeTrabajoId = ordenDeTrabajoId;
        this.fecha = fecha;
    }

    @Override
    public List<ControlLiberacion> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        if(ordenDeTrabajoId != null)
            filters.put("ordenDeTrabajoId", ordenDeTrabajoId);

        if(fecha != null)
            filters.put("fecha", fecha);

        super.setRowCount(controlLiberacionDAO.countFiltered(filters).intValue());
        return controlLiberacionDAO.findFiltered(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder), filters);
    }

    @Override
    public ControlLiberacion getRowData(String rowKey) {
        return controlLiberacionDAO.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(ControlLiberacion object) {
        return object.getId();
    }
}
