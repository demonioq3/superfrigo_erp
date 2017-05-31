package cl.superfrigo.beans.comercial;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.EstadoRequisicionDAO;
import cl.superfrigo.dao.OrdenDeTrabajoDAO;
import cl.superfrigo.dao.ProductoOrdenTrabajoDAO;
import cl.superfrigo.entity.abastecimiento.EstadoRequisicion;
import cl.superfrigo.entity.bodega.Bodega;
import cl.superfrigo.entity.bodega.EntradaProductoCantidad;
import cl.superfrigo.entity.bodega.GuiaEntrada;
import cl.superfrigo.entity.comercial.OrdenDeTrabajo;
import cl.superfrigo.entity.comercial.ProductoOrdenTrabajo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class InformeCotizacionesBean extends BaseBean implements Serializable {

    // Parámetros informe consumo.
    private Long bodegaId;
    private Date fechaDesde;
    private Date fechaHasta;
    private Long cotizacionId;
    private Long estadoId;

    private String cotizacionRadio = "Todas";

    private boolean mostrarInputCotizacion = false;

    @EJB
    private EstadoRequisicionDAO estadoRequisicionDAO;

    @EJB
    private OrdenDeTrabajoDAO ordenDeTrabajoDAO;

    @EJB
    private ProductoOrdenTrabajoDAO productoOrdenTrabajoDAO;


    public void changeCotizacionRadio(){
        if(cotizacionRadio.equals("Todas")){
            mostrarInputCotizacion = false;
        } else if(cotizacionRadio.equals("Una")){
            mostrarInputCotizacion = true;
        }
    }

    public List<EstadoRequisicion> getAllEstados(){
        return estadoRequisicionDAO.findAll();
    }

    public void getReportData() throws IOException {
        if(fechaDesde == null && fechaHasta != null || fechaDesde != null && fechaHasta == null){
            showError("Error", "Debe ingresar las fecha desde y hasta para realizar la búsqueda.");
            return;
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        // Default settings width columns.
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 3500);
        sheet.setColumnWidth(2, 9000);
        sheet.setColumnWidth(3, 8000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 7000);
        sheet.setColumnWidth(6, 10000);
        sheet.setColumnWidth(7, 4000);
        sheet.setColumnWidth(8, 4000);
        sheet.setColumnWidth(9, 4000);
        sheet.setColumnWidth(10, 4000);

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
        cell.setCellValue("Informe cotizaciones");
        HSSFRow row2 = sheet.createRow(1);
        row2.setHeight((short) 350);
        HSSFCell cell2 = row2.createCell(0);
        cell2.setCellValue("Superfrigo Ingenieria y Refrigeración Ltda.");
        HSSFRow row3 = sheet.createRow(2);
        row3.setHeight((short) 350);
        HSSFCell cell3 = row3.createCell(0);
        cell3.setCellValue("Fecha:");
        HSSFCell cell4 = row3.createCell(1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        cell4.setCellValue(simpleDateFormat.format(new Date()));

        // Titulo a la izquierda.
        HSSFRow row4 = sheet.createRow(3);
        row4.setHeight((short) 350);
        HSSFCell cell5 = row4.createCell(6);
        cell5.setCellValue("                      INFORME DE COTIZACIONES");
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
        cell9.setCellValue("Nº cotización");
        HSSFCell cell10 = row9.createCell(1);
        cell10.setCellStyle(headersTablaStyle);
        cell10.setCellValue("Fecha");
        HSSFCell cell11 = row9.createCell(2);
        cell11.setCellStyle(headersTablaStyle);
        cell11.setCellValue("Gestor");
        HSSFCell cell12 = row9.createCell(3);
        cell12.setCellStyle(headersTablaStyle);
        cell12.setCellValue("Nombre cliente");
        HSSFCell cell13 = row9.createCell(4);
        cell13.setCellStyle(headersTablaStyle);
        cell13.setCellValue("Estado");
        HSSFCell cell14 = row9.createCell(5);
        cell14.setCellStyle(headersTablaStyle);
        cell14.setCellValue("Código producto");
        HSSFCell cell15 = row9.createCell(6);
        cell15.setCellStyle(headersTablaStyle);
        cell15.setCellValue("Descripción");
        HSSFCell cell16 = row9.createCell(7);
        cell16.setCellStyle(headersTablaStyle);
        cell16.setCellValue("Cantidad");
        HSSFCell cell17 = row9.createCell(8);
        cell17.setCellStyle(headersTablaStyle);
        cell17.setCellValue("U. medida");
        HSSFCell cell18 = row9.createCell(9);
        cell18.setCellStyle(headersTablaStyle);
        cell18.setCellValue("Precio unitario");
        HSSFCell cell19 = row9.createCell(10);
        cell19.setCellStyle(headersTablaStyle);
        cell19.setCellValue("Monto total");

        // Verificando filtros de la vista.
        List<OrdenDeTrabajo> cotizaciones = applyFilters();

        for (OrdenDeTrabajo cotizacion : cotizaciones) {
            cotizacion.setProductos(productoOrdenTrabajoDAO.findByOrdenTrabajoId(cotizacion.getId()));
        }

        // Parte por defecto en la fila 10.
        int i = 10;
        long totalBodega = 0L;
        if(cotizaciones != null) {
            for (OrdenDeTrabajo cotizacion : cotizaciones) {
                for (ProductoOrdenTrabajo producto : cotizacion.getProductos()) {
                    HSSFRow rowCreated = sheet.createRow(i);
                    rowCreated.setHeight((short) 350);
                    HSSFCell cellCreated1 = rowCreated.createCell(0);
                    cellCreated1.setCellValue(cotizacion.getId());

                    HSSFCell cellCreated2 = rowCreated.createCell(1);
                    cellCreated2.setCellValue(simpleDateFormat.format(cotizacion.getFecha()));

                    HSSFCell cellCreated3 = rowCreated.createCell(2);
                    cellCreated3.setCellValue(cotizacion.getGestor());

                    HSSFCell cellCreated4 = rowCreated.createCell(3);
                    if (cotizacion.getFichaAuxiliar() != null) {
                        cellCreated4.setCellValue(cotizacion.getFichaAuxiliar().getNombreRazonSocial());
                    }

                    HSSFCell cellCreated5 = rowCreated.createCell(4);
                    if (cotizacion.getEstado() != null) {
                        cellCreated5.setCellValue(cotizacion.getEstado().getDescripcion());
                    }

                    HSSFCell cellCreated6 = rowCreated.createCell(5);
                    cellCreated6.setCellValue(producto.getFichaProducto().getCodigoProducto());

                    HSSFCell cellCreated7 = rowCreated.createCell(6);
                    cellCreated7.setCellValue(producto.getFichaProducto().getDescripcion());

                    HSSFCell cellCreated8 = rowCreated.createCell(7);
                    cellCreated8.setCellValue(producto.getCantidad());

                    HSSFCell cellCreated9 = rowCreated.createCell(8);
                    cellCreated9.setCellValue(producto.getFichaProducto().getUnidadMedida().getNombre());

                    HSSFCell cellCreated10 = rowCreated.createCell(9);
                    cellCreated10.setCellValue(producto.getPrecioUnitario());

                    HSSFCell cellCreated11 = rowCreated.createCell(10);
                    cellCreated11.setCellValue(producto.getPrecioTotal());
                    i++;
                }
            }
        }


        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/vnd.ms-excel");
        SimpleDateFormat sdf2 =  new SimpleDateFormat("ddMMyyyy");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"inf_cotizaciones_" + sdf2.format(new Date()) + ".xls\"");

        workbook.write(externalContext.getResponseOutputStream());
        facesContext.responseComplete();
    }

    private List<OrdenDeTrabajo> applyFilters() {
        List<OrdenDeTrabajo> response = new ArrayList<OrdenDeTrabajo>();

        if(fechaDesde == null){
            response = ordenDeTrabajoDAO.findByEstadoIdAndCotizacionId(estadoId, cotizacionId);
        } else {
            response = ordenDeTrabajoDAO.findByEstadoIdAndCotizacionIdAndFechas(estadoId, cotizacionId, fechaDesde, fechaHasta);
        }

        return response;
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

    public Long getBodegaId() {
        return bodegaId;
    }

    public void setBodegaId(Long bodegaId) {
        this.bodegaId = bodegaId;
    }

    public String getCotizacionRadio() {
        return cotizacionRadio;
    }

    public void setCotizacionRadio(String cotizacionRadio) {
        this.cotizacionRadio = cotizacionRadio;
    }

    public boolean isMostrarInputCotizacion() {
        return mostrarInputCotizacion;
    }

    public Long getCotizacionId() {
        return cotizacionId;
    }

    public void setCotizacionId(Long cotizacionId) {
        this.cotizacionId = cotizacionId;
    }

    public Long getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Long estadoId) {
        this.estadoId = estadoId;
    }
}



