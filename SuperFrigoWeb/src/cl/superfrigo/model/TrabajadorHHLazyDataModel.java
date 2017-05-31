package cl.superfrigo.model;

import cl.superfrigo.dao.GuiaEntradaDAO;
import cl.superfrigo.dao.TrabajadorHHDAO;
import cl.superfrigo.entity.bodega.GuiaEntrada;
import cl.superfrigo.entity.registros_hh.TrabajadorHH;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by asgco on 29-Mar-16.
 */
public class TrabajadorHHLazyDataModel extends LazyDataModel<TrabajadorHH> {

    private TrabajadorHHDAO trabajadorHHDAO;

    public TrabajadorHHLazyDataModel(TrabajadorHHDAO trabajadorHHDAO) {
        this.trabajadorHHDAO = trabajadorHHDAO;
    }

    @Override
    public List<TrabajadorHH> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        super.setRowCount(trabajadorHHDAO.countFiltered(filters).intValue());
        return trabajadorHHDAO.findFiltered(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder), filters);
    }

    @Override
    public TrabajadorHH getRowData(String rowKey) {
        return trabajadorHHDAO.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(TrabajadorHH object) {
        return object.getId();
    }
}
