package cl.superfrigo.model;

import cl.superfrigo.dao.FichaProductoDAO;
import cl.superfrigo.dao.GuiaEntradaDAO;
import cl.superfrigo.entity.bodega.FichaProducto;
import cl.superfrigo.entity.bodega.GuiaEntrada;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by asgco on 29-Mar-16.
 */
public class GuiasEntradaLazyDataModel extends LazyDataModel<GuiaEntrada> {

    private GuiaEntradaDAO guiaEntradaDAO;

    public GuiasEntradaLazyDataModel(GuiaEntradaDAO guiaEntradaDAO) {
        this.guiaEntradaDAO = guiaEntradaDAO;
    }

    @Override
    public List<GuiaEntrada> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        super.setRowCount(guiaEntradaDAO.countFiltered(filters).intValue());
        return guiaEntradaDAO.findFiltered(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder), filters);
    }

    @Override
    public GuiaEntrada getRowData(String rowKey) {
        return guiaEntradaDAO.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(GuiaEntrada object) {
        return object.getId();
    }
}
