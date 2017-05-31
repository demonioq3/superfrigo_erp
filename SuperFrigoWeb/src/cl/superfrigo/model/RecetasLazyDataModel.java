package cl.superfrigo.model;

import cl.superfrigo.dao.FichaAuxiliarDAO;
import cl.superfrigo.dao.RecetaDAO;
import cl.superfrigo.entity.FichaAuxiliar;
import cl.superfrigo.entity.proyectos.Receta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by asgco on 29-Mar-16.
 */
public class RecetasLazyDataModel extends LazyDataModel<Receta> {

    private RecetaDAO recetaDAO;
    private String codigoProducto;

    public RecetasLazyDataModel(RecetaDAO recetaDAO) {
        this.recetaDAO = recetaDAO;
    }

    public RecetasLazyDataModel(RecetaDAO recetaDAO, String codigoProducto) {
        this.recetaDAO = recetaDAO;
        this.codigoProducto = codigoProducto;
    }

    @Override
    public List<Receta> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        if(codigoProducto != null)
            filters.put("productoId", codigoProducto);

        super.setRowCount(recetaDAO.countFiltered(filters).intValue());
        return recetaDAO.findFiltered(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder), filters);
    }

    @Override
    public Receta getRowData(String rowKey) {
        return recetaDAO.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(Receta object) {
        return object.getId();
    }
}
