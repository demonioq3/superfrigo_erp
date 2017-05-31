package cl.superfrigo.model;

import cl.superfrigo.dao.FichaAuxiliarDAO;
import cl.superfrigo.dao.PlanoFabricacionDAO;
import cl.superfrigo.entity.FichaAuxiliar;
import cl.superfrigo.entity.proyectos.PlanoFabricacion;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by asgco on 29-Mar-16.
 */
public class PlanosFabricacionLazyDataModel extends LazyDataModel<PlanoFabricacion> {

    private PlanoFabricacionDAO planoFabricacionDAO;

    public PlanosFabricacionLazyDataModel(PlanoFabricacionDAO planoFabricacionDAO) {
        this.planoFabricacionDAO = planoFabricacionDAO;
    }

    @Override
    public List<PlanoFabricacion> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        super.setRowCount(planoFabricacionDAO.countFiltered(filters).intValue());
        return planoFabricacionDAO.findFiltered(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder), filters);
    }

    @Override
    public PlanoFabricacion getRowData(String rowKey) {
        return planoFabricacionDAO.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(PlanoFabricacion object) {
        return object.getId();
    }
}
