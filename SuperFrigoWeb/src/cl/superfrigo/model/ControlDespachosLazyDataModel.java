package cl.superfrigo.model;

import cl.superfrigo.dao.ControlDespachoDAO;
import cl.superfrigo.dao.RegistroHHDAO;
import cl.superfrigo.entity.produccion.ControlDespacho;
import cl.superfrigo.entity.registros_hh.RegistroHH;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by asgco on 29-Mar-16.
 */
public class ControlDespachosLazyDataModel extends LazyDataModel<ControlDespacho> {

    private ControlDespachoDAO controlDespachoDAO;
    private Long ordenDeTrabajoId;
    private Date fecha;

    public ControlDespachosLazyDataModel(ControlDespachoDAO controlDespachoDAO) {
        this.controlDespachoDAO = controlDespachoDAO;
    }

    public ControlDespachosLazyDataModel(ControlDespachoDAO controlDespachoDAO, Long ordenDeTrabajoId) {
        this.controlDespachoDAO = controlDespachoDAO;
        this.ordenDeTrabajoId = ordenDeTrabajoId;
    }

    public ControlDespachosLazyDataModel(ControlDespachoDAO controlDespachoDAO, Long ordenDeTrabajoId, Date fecha) {
        this.controlDespachoDAO = controlDespachoDAO;
        this.ordenDeTrabajoId = ordenDeTrabajoId;
        this.fecha = fecha;
    }

    @Override
    public List<ControlDespacho> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        if(ordenDeTrabajoId != null)
            filters.put("ordenDeTrabajoId", ordenDeTrabajoId);

        if(fecha != null)
            filters.put("fechaDespacho", fecha);

        super.setRowCount(controlDespachoDAO.countFiltered(filters).intValue());
        return controlDespachoDAO.findFiltered(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder), filters);
    }

    @Override
    public ControlDespacho getRowData(String rowKey) {
        return controlDespachoDAO.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(ControlDespacho object) {
        return object.getId();
    }
}
