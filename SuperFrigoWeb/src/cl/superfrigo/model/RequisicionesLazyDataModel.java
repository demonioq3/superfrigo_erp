package cl.superfrigo.model;

import cl.superfrigo.dao.GuiaEntradaDAO;
import cl.superfrigo.dao.RequisicionDAO;
import cl.superfrigo.entity.abastecimiento.Requisicion;
import cl.superfrigo.entity.bodega.GuiaEntrada;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by asgco on 29-Mar-16.
 */
public class RequisicionesLazyDataModel extends LazyDataModel<Requisicion> {

    private RequisicionDAO requisicionDAO;

    public RequisicionesLazyDataModel(RequisicionDAO requisicionDAO) {
        this.requisicionDAO = requisicionDAO;
    }

    @Override
    public List<Requisicion> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        super.setRowCount(requisicionDAO.countFiltered(filters).intValue());
        return requisicionDAO.findFiltered(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder), filters);
    }

    @Override
    public Requisicion getRowData(String rowKey) {
        return requisicionDAO.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(Requisicion object) {
        return object.getId();
    }
}
