package cl.superfrigo.model;

import cl.superfrigo.dao.FichaAuxiliarDAO;
import cl.superfrigo.dao.FichaProductoDAO;
import cl.superfrigo.entity.FichaAuxiliar;
import cl.superfrigo.entity.bodega.FichaProducto;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by asgco on 29-Mar-16.
 */
public class FichasLazyDataModel extends LazyDataModel<FichaAuxiliar> {

    private FichaAuxiliarDAO fichaAuxiliarDAO;

    public FichasLazyDataModel(FichaAuxiliarDAO fichaAuxiliarDAO) {
        this.fichaAuxiliarDAO = fichaAuxiliarDAO;
    }

    @Override
    public List<FichaAuxiliar> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        super.setRowCount(fichaAuxiliarDAO.countFiltered(filters).intValue());
        return fichaAuxiliarDAO.findFiltered(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder), filters);
    }

    @Override
    public FichaAuxiliar getRowData(String rowKey) {
        return fichaAuxiliarDAO.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(FichaAuxiliar object) {
        return object.getId();
    }
}
