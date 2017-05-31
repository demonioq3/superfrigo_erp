package cl.superfrigo.model;

import cl.superfrigo.dao.GuiaEntradaDAO;
import cl.superfrigo.dao.SolicitudMaterialDAO;
import cl.superfrigo.entity.bodega.GuiaEntrada;
import cl.superfrigo.entity.solicitud_material.SolicitudMaterial;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by asgco on 29-Mar-16.
 */
public class SolicitudMaterialLazyDataModel extends LazyDataModel<SolicitudMaterial> {

    private SolicitudMaterialDAO solicitudMaterialDAO;

    public SolicitudMaterialLazyDataModel(SolicitudMaterialDAO solicitudMaterialDAO) {
        this.solicitudMaterialDAO = solicitudMaterialDAO;
    }

    @Override
    public List<SolicitudMaterial> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        super.setRowCount(solicitudMaterialDAO.countFiltered(filters).intValue());
        return solicitudMaterialDAO.findFiltered(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder), filters);
    }

    @Override
    public SolicitudMaterial getRowData(String rowKey) {
        return solicitudMaterialDAO.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(SolicitudMaterial object) {
        return object.getId();
    }
}
