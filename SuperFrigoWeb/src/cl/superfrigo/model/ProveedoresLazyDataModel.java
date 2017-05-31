package cl.superfrigo.model;

import cl.superfrigo.dao.FichaAuxiliarDAO;
import cl.superfrigo.dao.OrdenDeCompraDAO;
import cl.superfrigo.dao.RequisicionDAO;
import cl.superfrigo.entity.FichaAuxiliar;
import cl.superfrigo.entity.abastecimiento.OrdenDeCompra;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by asgco on 29-Mar-16.
 */
public class ProveedoresLazyDataModel extends LazyDataModel<FichaAuxiliar> {

    private FichaAuxiliarDAO fichaAuxiliarDAO;
    private Long tipoFichaAuxiliar;

    public ProveedoresLazyDataModel(FichaAuxiliarDAO fichaAuxiliarDAO) {
        this.fichaAuxiliarDAO = fichaAuxiliarDAO;
    }

    public ProveedoresLazyDataModel(FichaAuxiliarDAO fichaAuxiliarDAO, Long tipoFichaAuxiliar) {
        this.fichaAuxiliarDAO = fichaAuxiliarDAO;
        this.tipoFichaAuxiliar = tipoFichaAuxiliar;
    }

    @Override
    public List<FichaAuxiliar> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        /*if ( tipoFichaAuxiliar != null )
            filters.put("tipoFichaAuxiliar", tipoFichaAuxiliar);*/

        super.setRowCount(fichaAuxiliarDAO.countFiltered(filters).intValue());
        List<FichaAuxiliar> lista = fichaAuxiliarDAO.findFiltered(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder), filters);
        return lista;
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
