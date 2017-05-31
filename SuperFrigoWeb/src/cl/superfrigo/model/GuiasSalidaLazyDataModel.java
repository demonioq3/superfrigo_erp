package cl.superfrigo.model;

import cl.superfrigo.dao.GuiaEntradaDAO;
import cl.superfrigo.dao.GuiaSalidaDAO;
import cl.superfrigo.entity.bodega.GuiaEntrada;
import cl.superfrigo.entity.bodega.GuiaSalida;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by asgco on 29-Mar-16.
 */
public class GuiasSalidaLazyDataModel extends LazyDataModel<GuiaSalida> {

    private GuiaSalidaDAO guiaSalidaDAO;

    public GuiasSalidaLazyDataModel(GuiaSalidaDAO guiaSalidaDAO) {
        this.guiaSalidaDAO = guiaSalidaDAO;
    }

    @Override
    public List<GuiaSalida> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        super.setRowCount(guiaSalidaDAO.countFiltered(filters).intValue());
        return guiaSalidaDAO.findFiltered(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder), filters);
    }

    @Override
    public GuiaSalida getRowData(String rowKey) {
        return guiaSalidaDAO.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(GuiaSalida object) {
        return object.getId();
    }
}
