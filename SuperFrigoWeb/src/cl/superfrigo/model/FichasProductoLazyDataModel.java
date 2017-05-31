package cl.superfrigo.model;

import cl.superfrigo.dao.FichaProductoDAO;
import cl.superfrigo.entity.bodega.FichaProducto;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by asgco on 29-Mar-16.
 */
public class FichasProductoLazyDataModel extends LazyDataModel<FichaProducto> {

    private FichaProductoDAO fichaProductoDAO;
    private Long tipoProductoId;

    public FichasProductoLazyDataModel(FichaProductoDAO fichaProductoDAO) {
        this.fichaProductoDAO = fichaProductoDAO;
    }

    public FichasProductoLazyDataModel(FichaProductoDAO fichaProductoDAO, Long tipoProductoId) {
        this.fichaProductoDAO = fichaProductoDAO;
        this.tipoProductoId = tipoProductoId;
    }

    @Override
    public List<FichaProducto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        if ( tipoProductoId != null )
            filters.put("tipoFichaProductoId", tipoProductoId);


        super.setRowCount(fichaProductoDAO.countFiltered(filters).intValue());
        return fichaProductoDAO.findFiltered(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder), filters);
    }

    @Override
    public FichaProducto getRowData(String rowKey) {
        return fichaProductoDAO.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(FichaProducto object) {
        return object.getId();
    }
}
