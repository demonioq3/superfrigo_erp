package cl.superfrigo.model;

import cl.superfrigo.dao.FichaProductoDAO;
import cl.superfrigo.dao.ProductoOrdenCompraDAO;
import cl.superfrigo.entity.abastecimiento.ProductoOrdenCompra;
import cl.superfrigo.entity.bodega.FichaProducto;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by asgco on 29-Mar-16.
 */
public class ProductoOrdenCompraLazyDataModel extends LazyDataModel<ProductoOrdenCompra> {

    private ProductoOrdenCompraDAO productoOrdenCompraDAO;
    private String codigoProducto;

    public ProductoOrdenCompraLazyDataModel(ProductoOrdenCompraDAO productoOrdenCompraDAO, String codigoProducto) {
        this.productoOrdenCompraDAO = productoOrdenCompraDAO;
        this.codigoProducto = codigoProducto;
    }

    @Override
    public List<ProductoOrdenCompra> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        if( codigoProducto != null)
            filters.put("codigoProducto", codigoProducto);

        super.setRowCount(productoOrdenCompraDAO.countFiltered(filters).intValue());
        return productoOrdenCompraDAO.findFiltered(first, pageSize, sortField, SortOrder.ASCENDING.equals(sortOrder), filters);
    }

    @Override
    public ProductoOrdenCompra getRowData(String rowKey) {
        return productoOrdenCompraDAO.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(ProductoOrdenCompra object) {
        return object.getId();
    }
}
