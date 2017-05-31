package cl.superfrigo.model;

import cl.superfrigo.dao.ControlLiberacionDAO;
import cl.superfrigo.dao.ControlProgramacionDAO;
import cl.superfrigo.entity.produccion.ControlLiberacion;
import cl.superfrigo.entity.produccion.ControlProgramacion;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by asgco on 29-Mar-16.
 */
public class ControlProgramacionLazyDataModel extends LazyDataModel<ControlProgramacion> {

    private ControlProgramacionDAO controlProgramacionDAO;
    private Long ordenDeTrabajoId;
    private Date fecha;

    public ControlProgramacionLazyDataModel(ControlProgramacionDAO controlProgramacionDAO) {
        this.controlProgramacionDAO = controlProgramacionDAO;
    }

    public ControlProgramacionLazyDataModel(ControlProgramacionDAO controlProgramacionDAO, Long ordenDeTrabajoId) {
        this.controlProgramacionDAO = controlProgramacionDAO;
        this.ordenDeTrabajoId = ordenDeTrabajoId;
    }

    public ControlProgramacionLazyDataModel(ControlProgramacionDAO controlProgramacionDAO, Long ordenDeTrabajoId, Date fecha) {
        this.controlProgramacionDAO = controlProgramacionDAO;
        this.ordenDeTrabajoId = ordenDeTrabajoId;
        this.fecha = fecha;
    }

    @Override
    public List<ControlProgramacion> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        if(ordenDeTrabajoId != null)
            filters.put("ordenDeTrabajoId", ordenDeTrabajoId);

        if(fecha != null)
            filters.put("fecha", fecha);

        super.setRowCount(controlProgramacionDAO.countFiltered(filters).intValue());
        return controlProgramacionDAO.findFiltered(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder), filters);
    }

    @Override
    public ControlProgramacion getRowData(String rowKey) {
        return controlProgramacionDAO.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(ControlProgramacion object) {
        return object.getId();
    }
}
