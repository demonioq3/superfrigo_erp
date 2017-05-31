package cl.superfrigo.model;

import cl.superfrigo.dao.GuiaEntradaDAO;
import cl.superfrigo.dao.RegistroHHDAO;
import cl.superfrigo.entity.bodega.GuiaEntrada;
import cl.superfrigo.entity.registros_hh.RegistroHH;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by asgco on 29-Mar-16.
 */
public class RegistroHHLazyDataModel extends LazyDataModel<RegistroHH> {

    private RegistroHHDAO registroHHDAO;
    private Long areaId;
    private Date fecha;

    public RegistroHHLazyDataModel(RegistroHHDAO registroHHDAO) {
        this.registroHHDAO = registroHHDAO;
    }

    public RegistroHHLazyDataModel(RegistroHHDAO registroHHDAO, Long areaId, Date fecha) {
        this.registroHHDAO = registroHHDAO;
        this.areaId = areaId;
        this.fecha = fecha;
    }

    @Override
    public List<RegistroHH> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        if(areaId != null)
            filters.put("areaHHId", areaId);

        if(fecha != null)
            filters.put("fecha", fecha);

        super.setRowCount(registroHHDAO.countFiltered(filters).intValue());
        return registroHHDAO.findFiltered(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder), filters);
    }

    @Override
    public RegistroHH getRowData(String rowKey) {
        return registroHHDAO.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(RegistroHH object) {
        return object.getId();
    }
}
