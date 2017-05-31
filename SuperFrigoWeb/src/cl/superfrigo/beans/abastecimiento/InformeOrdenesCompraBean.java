package cl.superfrigo.beans.abastecimiento;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.CondicionPagoDAO;
import cl.superfrigo.dao.EstadoOrdenCompraDAO;
import cl.superfrigo.dao.EtapaOrdenCompraDAO;
import cl.superfrigo.dao.OrdenDeCompraDAO;
import cl.superfrigo.entity.abastecimiento.*;
import cl.superfrigo.entity.bodega.Bodega;
import cl.superfrigo.entity.bodega.EntradaProductoCantidad;
import cl.superfrigo.entity.bodega.GuiaEntrada;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.hibernate.exception.ConstraintViolationException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by asgco on 29-Feb-16.
 */
@ManagedBean(name = "informeOrdenCompraBean")
@ViewScoped
public class InformeOrdenesCompraBean extends BaseBean implements Serializable {

    private String listarRadio = "list1"; // Default
    private Long estadoId;
    private Long etapaId;
    private Long ordenDeCompraId;
    private String ocRadio = "o1"; // Default
    private Long centroDeCostoId;
    private Date fechaDesde;
    private Date fechaHasta;
    private List<Integer> conDetalle;

    private boolean mostrarList2 = false;
    private boolean mostrarOc2 = false;

    // Formato date
    private SimpleDateFormat simpleDateFormat;

    @EJB private EstadoOrdenCompraDAO estadoOrdenCompraDAO;
    @EJB private EtapaOrdenCompraDAO etapaOrdenCompraDAO;
    @EJB private OrdenDeCompraDAO ordenDeCompraDAO;


    @PostConstruct
    public void init(){
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        fechaHasta = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        fechaDesde = cal.getTime();
    }

    public void changeListar(){
        if(listarRadio.equals("list1")){
            mostrarList2 = false;
        } else if(listarRadio.equals("list2")){
            mostrarList2 = true;
        }
    }

    public void changeOc(){
        if(ocRadio.equals("o1")){
            mostrarOc2 = false;
        } else if(ocRadio.equals("o2")){
            mostrarOc2 = true;
        }
    }

    public void getReportData() throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        // Default settings width columns.
        sheet.setColumnWidth(0, 3000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 7500);
        sheet.setColumnWidth(6, 7000);
        sheet.setColumnWidth(7, 11000);
        sheet.setColumnWidth(8, 3000);
        sheet.setColumnWidth(9, 4500);
        sheet.setColumnWidth(10, 4500);
        sheet.setColumnWidth(11, 4500);

        // Setiando estilos
        // Bold.
        HSSFCellStyle boldStyle = workbook.createCellStyle();
        HSSFFont boldFont = workbook.createFont();
        boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        boldStyle.setFont(boldFont);//set it to bold

        // Text align right.
        HSSFCellStyle alignRight = workbook.createCellStyle();
        alignRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        // Text align right with bold.
        HSSFCellStyle alignRightBold = workbook.createCellStyle();
        alignRightBold.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        alignRightBold.setFont(boldFont);

        // Text align right with bold and borders to field "TOTAL".
        HSSFCellStyle alignRightBoldTotal = workbook.createCellStyle();
        alignRightBoldTotal.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        alignRightBoldTotal.setFont(boldFont);
        alignRightBoldTotal.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        alignRightBoldTotal.setBorderTop(HSSFCellStyle.BORDER_THICK);
        alignRightBoldTotal.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        alignRightBoldTotal.setLeftBorderColor(HSSFColor.BLACK.index);
        alignRightBoldTotal.setTopBorderColor(HSSFColor.BLACK.index);
        alignRightBoldTotal.setBottomBorderColor(HSSFColor.BLACK.index);
        alignRightBoldTotal.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // Text align right with bold and borders to value of "TOTAL" mid.
        HSSFCellStyle alignRightBoldTotalMid= workbook.createCellStyle();
        alignRightBoldTotalMid.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        alignRightBoldTotalMid.setFont(boldFont);
        alignRightBoldTotalMid.setBorderTop(HSSFCellStyle.BORDER_THICK);
        alignRightBoldTotalMid.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        alignRightBoldTotalMid.setTopBorderColor(HSSFColor.BLACK.index);
        alignRightBoldTotalMid.setBottomBorderColor(HSSFColor.BLACK.index);
        alignRightBoldTotalMid.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // Text align right with bold and borders to value of "TOTAL".
        HSSFCellStyle alignRightBoldTotalValue = workbook.createCellStyle();
        alignRightBoldTotalValue.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        alignRightBoldTotalValue.setFont(boldFont);
        alignRightBoldTotalValue.setBorderRight(HSSFCellStyle.BORDER_THICK);
        alignRightBoldTotalValue.setBorderTop(HSSFCellStyle.BORDER_THICK);
        alignRightBoldTotalValue.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        alignRightBoldTotalValue.setRightBorderColor(HSSFColor.BLACK.index);
        alignRightBoldTotalValue.setTopBorderColor(HSSFColor.BLACK.index);
        alignRightBoldTotalValue.setBottomBorderColor(HSSFColor.BLACK.index);
        alignRightBoldTotalValue.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // Headers table columns.
        HSSFCellStyle headersTablaStyle = workbook.createCellStyle();
        headersTablaStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headersTablaStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        headersTablaStyle.setFont(boldFont);
        // FIN Setiando estilos

        // Header 1
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 350);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("Informe órdenes de compra");
        HSSFRow row2 = sheet.createRow(1);
        row2.setHeight((short) 350);
        HSSFCell cell2 = row2.createCell(0);
        cell2.setCellValue("Superfrigo Ingenieria y Refrigeración Ltda.");
        HSSFRow row3 = sheet.createRow(2);
        row3.setHeight((short) 350);
        HSSFCell cell3 = row3.createCell(0);
        cell3.setCellValue("Fecha:");
        HSSFCell cell4 = row3.createCell(1);
        cell4.setCellValue(simpleDateFormat.format(new Date()));

        // Titulo a la izquierda.
        HSSFRow row4 = sheet.createRow(3);
        row4.setHeight((short) 350);
        HSSFCell cell5 = row4.createCell(6);
        cell5.setCellValue("                      INFORME ORDENES DE COMPRA");
        cell5.setCellStyle(boldStyle);

        // Periodo.
        HSSFRow row5 = sheet.createRow(5);
        row5.setHeight((short) 350);
        HSSFCell cell6 = row5.createCell(4);
        cell6.setCellValue("Periodo ");
        cell6.setCellStyle(alignRight);
        HSSFCell cell7 = row5.createCell(5);
        cell7.setCellValue("Desde: ");
        cell7.setCellStyle(alignRight);
        if(fechaDesde != null){
            HSSFCell cellFechaDesde = row5.createCell(6);
            cellFechaDesde.setCellStyle(alignRight);
            cellFechaDesde.setCellValue(simpleDateFormat.format(fechaDesde));
        }

        HSSFRow row6 = sheet.createRow(6);
        row6.setHeight((short) 350);
        HSSFCell cell8 = row6.createCell(5);
        cell8.setCellValue("Hasta: ");
        if(fechaHasta != null){
            HSSFCell cellFechaHasta = row6.createCell(6);
            cellFechaHasta.setCellStyle(alignRight);
            cellFechaHasta.setCellValue(simpleDateFormat.format(fechaHasta));
        }
        cell8.setCellStyle(alignRight);

        // Inicio de tabla
        // Header tabla.
        HSSFRow row9 = sheet.createRow(9);
        row9.setHeight((short) 600);

        HSSFCell cell9 = row9.createCell(0);
        cell9.setCellStyle(headersTablaStyle);
        cell9.setCellValue("Nº Orden");
        HSSFCell cell10 = row9.createCell(1);
        cell10.setCellStyle(headersTablaStyle);
        cell10.setCellValue("Estado");
        HSSFCell cell11 = row9.createCell(2);
        cell11.setCellStyle(headersTablaStyle);
        cell11.setCellValue("Etapa");
        HSSFCell cell12 = row9.createCell(3);
        cell12.setCellStyle(headersTablaStyle);
        cell12.setCellValue("Fecha emisión");
        HSSFCell cell13 = row9.createCell(4);
        cell13.setCellStyle(headersTablaStyle);
        cell13.setCellValue("Fecha entrega");
        HSSFCell cell14 = row9.createCell(5);
        cell14.setCellStyle(headersTablaStyle);
        cell14.setCellValue("Proveedor");
        HSSFCell cell15 = row9.createCell(6);
        cell15.setCellStyle(headersTablaStyle);
        cell15.setCellValue("Código producto");
        HSSFCell cell16 = row9.createCell(7);
        cell16.setCellStyle(headersTablaStyle);
        cell16.setCellValue("Descripción");
        HSSFCell cell17 = row9.createCell(8);
        cell17.setCellStyle(headersTablaStyle);
        cell17.setCellValue("U. medida");
        HSSFCell cell18 = row9.createCell(9);
        cell18.setCellStyle(headersTablaStyle);
        cell18.setCellValue("Cantidad");
        HSSFCell cell19 = row9.createCell(10);
        cell19.setCellStyle(headersTablaStyle);
        cell19.setCellValue("Precio unitario");
        HSSFCell cell20 = row9.createCell(11);
        cell20.setCellStyle(headersTablaStyle);
        cell20.setCellValue("Total movimiento");

        // Verificando filtros de la vista.
        List<OrdenDeCompra> ordenes = applyFilters();

        int i = 10;
        for (OrdenDeCompra orden : ordenes) {
            for (ProductoOrdenCompra productoOrdenCompra : orden.getProductos()) {
                HSSFRow rowCreated = sheet.createRow(i);
                rowCreated.setHeight((short) 350);
                HSSFCell cellCreated1 = rowCreated.createCell(0);
                cellCreated1.setCellValue(orden.getId());

                HSSFCell cellCreated2 = rowCreated.createCell(1);
                cellCreated2.setCellValue(orden.getEstadoOrdenCompra().getDescripcion());

                HSSFCell cellCreated3 = rowCreated.createCell(2);
                cellCreated3.setCellValue(orden.getEtapaOrdenCompra().getDescripcion());

                HSSFCell cellCreated4 = rowCreated.createCell(3);
                if(orden.getFecha() != null){
                    cellCreated4.setCellValue(simpleDateFormat.format(orden.getFecha()));
                }


                HSSFCell cellCreated5 = rowCreated.createCell(4);
                if(orden.getEntregaFinal() != null){
                    cellCreated5.setCellValue(simpleDateFormat.format(orden.getEntregaFinal()));
                }

                HSSFCell cellCreated6 = rowCreated.createCell(5);
                if(orden.getProveedor() != null){
                    cellCreated6.setCellValue(orden.getProveedor().getNombreRazonSocial());
                }

                HSSFCell cellCreated7 = rowCreated.createCell(6);
                cellCreated7.setCellValue(productoOrdenCompra.getProductoRequisicion().getFichaProducto().getCodigoProducto());

                HSSFCell cellCreated8 = rowCreated.createCell(7);
                cellCreated8.setCellValue(productoOrdenCompra.getProductoRequisicion().getFichaProducto().getDescripcion());

                HSSFCell cellCreated9 = rowCreated.createCell(8);
                cellCreated9.setCellValue(productoOrdenCompra.getProductoRequisicion().getFichaProducto().getUnidadMedida().getNombre());

                HSSFCell cellCreated10 = rowCreated.createCell(9);
                cellCreated10.setCellValue(productoOrdenCompra.getProductoRequisicion().getCantidad());

                HSSFCell cellCreated11 = rowCreated.createCell(10);
                cellCreated11.setCellValue(productoOrdenCompra.getPrecioUnitario());

                HSSFCell cellCreated12 = rowCreated.createCell(11);
                cellCreated12.setCellValue(productoOrdenCompra.getPrecioUnitario() * productoOrdenCompra.getProductoRequisicion().getCantidad());

                i++;
            }
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/vnd.ms-excel");
        SimpleDateFormat sdf2 =  new SimpleDateFormat("ddMMyyyy");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"inf_ordencompra_" + sdf2.format(new Date()) + ".xls\"");

        workbook.write(externalContext.getResponseOutputStream());
        facesContext.responseComplete();
    }

    private List<OrdenDeCompra> applyFilters() {
        List<OrdenDeCompra> list = new ArrayList<OrdenDeCompra>();

        if(listarRadio.equals("list1") && ocRadio.equals("o1")) {
            list = ordenDeCompraDAO.queryList1o1(fechaDesde, fechaHasta, centroDeCostoId);
        } else if(listarRadio.equals("list1") && ocRadio.equals("o2")){
            list = ordenDeCompraDAO.queryList1o2(fechaDesde, fechaHasta, centroDeCostoId, ordenDeCompraId);
        } else if(listarRadio.equals("list2") && ocRadio.equals("o1")){
            list = ordenDeCompraDAO.queryList2o1(fechaDesde, fechaHasta, centroDeCostoId, estadoId, etapaId);
        } else if(listarRadio.equals("list2") && ocRadio.equals("o2")){
            list = ordenDeCompraDAO.queryList2o2(fechaDesde, fechaHasta, centroDeCostoId, estadoId, etapaId, ordenDeCompraId);
        }

        return list;
    }

    public List<EstadoOrdenCompra> getAllEstados(){
        return estadoOrdenCompraDAO.findAll();
    }

    public List<EtapaOrdenCompra> getAllEtapas(){
        return etapaOrdenCompraDAO.findAll();
    }

    public boolean isMostrarList2() {
        return mostrarList2;
    }

    public String getListarRadio() {
        return listarRadio;
    }

    public void setListarRadio(String listarRadio) {
        this.listarRadio = listarRadio;
    }

    public Long getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Long estadoId) {
        this.estadoId = estadoId;
    }

    public Long getEtapaId() {
        return etapaId;
    }

    public void setEtapaId(Long etapaId) {
        this.etapaId = etapaId;
    }

    public Long getOrdenDeCompraId() {
        return ordenDeCompraId;
    }

    public void setOrdenDeCompraId(Long ordenDeCompraId) {
        this.ordenDeCompraId = ordenDeCompraId;
    }

    public String getOcRadio() {
        return ocRadio;
    }

    public void setOcRadio(String ocRadio) {
        this.ocRadio = ocRadio;
    }

    public boolean isMostrarOc2() {
        return mostrarOc2;
    }

    public Long getCentroDeCostoId() {
        return centroDeCostoId;
    }

    public void setCentroDeCostoId(Long centroDeCostoId) {
        this.centroDeCostoId = centroDeCostoId;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public List<Integer> getConDetalle() {
        return conDetalle;
    }

    public void setConDetalle(List<Integer> conDetalle) {
        this.conDetalle = conDetalle;
    }
}
