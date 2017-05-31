package cl.superfrigo.beans.bodega;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.BodegaDAO;
import cl.superfrigo.dao.BodegaStockProductoDAO;
import cl.superfrigo.dao.FichaProductoDAO;
import cl.superfrigo.dao.GuiaEntradaDAO;
import cl.superfrigo.entity.bodega.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.hibernate.exception.ConstraintViolationException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.PostActivate;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class InformeGuiasEntradaBean extends BaseBean implements Serializable {

    // Parámetros informe guias de entrada.
    private Date fechaDesde;
    private Date fechaHasta;
    private Long conceptoId;
    private Long bodegaId;

    // Formato date
    private SimpleDateFormat simpleDateFormat;

    @EJB private GuiaEntradaDAO guiaEntradaDAO;


    @PostConstruct
    public void init(){
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    public void getReportData() throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        // Default settings width columns.
        sheet.setColumnWidth(0, 3000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 3000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 9000);
        sheet.setColumnWidth(5, 7500);
        sheet.setColumnWidth(6, 7000);
        sheet.setColumnWidth(7, 11000);
        sheet.setColumnWidth(8, 3000);
        sheet.setColumnWidth(9, 5000);
        sheet.setColumnWidth(10, 5000);
        sheet.setColumnWidth(11, 5600);

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
        cell.setCellValue("Informe guias de entrada");
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
        cell5.setCellValue("                      INFORME GUIAS DE ENTRADA");
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
        cell9.setCellValue("Fecha");
        HSSFCell cell10 = row9.createCell(1);
        cell10.setCellStyle(headersTablaStyle);
        cell10.setCellValue("Bodega");
        HSSFCell cell11 = row9.createCell(2);
        cell11.setCellStyle(headersTablaStyle);
        cell11.setCellValue("Folio");
        HSSFCell cell12 = row9.createCell(3);
        cell12.setCellStyle(headersTablaStyle);
        cell12.setCellValue("Nº Documento");
        HSSFCell cell13 = row9.createCell(4);
        cell13.setCellStyle(headersTablaStyle);
        cell13.setCellValue("Proveedor");
        HSSFCell cell14 = row9.createCell(5);
        cell14.setCellStyle(headersTablaStyle);
        cell14.setCellValue("Concepto");
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
        cell20.setCellValue("Total");

        // Verificando filtros de la vista.
        List<GuiaEntrada> guiaEntradas = applyFilters();

        HashMap<Bodega, List<GuiaEntrada>> hashBodegasGuias = new HashMap<Bodega, List<GuiaEntrada>>();
        // Creando Hashmap<Bodega, Lista de guias de entrada> para exportar a excel.
        for (GuiaEntrada guiaEntrada : guiaEntradas) {

            if(!hashBodegasGuias.containsKey(guiaEntrada.getBodega())){
                hashBodegasGuias.put(guiaEntrada.getBodega(), new ArrayList<GuiaEntrada>());
            }

            List<GuiaEntrada> listGuiaEntradaByBodega = hashBodegasGuias.get(guiaEntrada.getBodega());
            listGuiaEntradaByBodega.add(guiaEntrada);
        }

        // Parte por defecto en la fila 10.
        int i = 10;
        long totalBodega = 0L;
        for (Bodega bodega : hashBodegasGuias.keySet()) {
            List<GuiaEntrada> guiasFromBodega = hashBodegasGuias.get(bodega);

            for (GuiaEntrada guiaEntrada : guiasFromBodega) {

                for (EntradaProductoCantidad entradaProductoCantidad : guiaEntrada.getProductos()) {
                    HSSFRow rowCreated = sheet.createRow(i);
                    rowCreated.setHeight((short) 350);
                    HSSFCell cellCreated1 = rowCreated.createCell(0);
                    cellCreated1.setCellValue(simpleDateFormat.format(guiaEntrada.getFecha()));

                    HSSFCell cellCreated2 = rowCreated.createCell(1);
                    cellCreated2.setCellValue(guiaEntrada.getBodega().getDescripcion());

                    HSSFCell cellCreated3 = rowCreated.createCell(2);
                    cellCreated3.setCellValue(guiaEntrada.getId());
                    cellCreated3.setCellStyle(alignRight);

                    HSSFCell cellCreated4 = rowCreated.createCell(3);


                    HSSFCell cellCreated5 = rowCreated.createCell(4);
                    if (guiaEntrada.getProveedor() != null) {
                        cellCreated5.setCellValue(guiaEntrada.getProveedor().getNombreRazonSocial());
                    }

                    HSSFCell cellCreated6 = rowCreated.createCell(5);
                    if (guiaEntrada.getConceptoEntrada() != null)
                        cellCreated6.setCellValue(guiaEntrada.getConceptoEntrada().getDescripcion());

                    HSSFCell cellCreated7 = rowCreated.createCell(6);
                    cellCreated7.setCellValue(entradaProductoCantidad.getFichaProducto().getCodigoProducto());

                    HSSFCell cellCreated8 = rowCreated.createCell(7);
                    cellCreated8.setCellValue(entradaProductoCantidad.getFichaProducto().getDescripcion());

                    HSSFCell cellCreated9 = rowCreated.createCell(8);
                    cellCreated9.setCellValue(entradaProductoCantidad.getFichaProducto().getUnidadMedida().getNombre());

                    HSSFCell cellCreated10 = rowCreated.createCell(9);
                    cellCreated10.setCellValue(entradaProductoCantidad.getCantidad());

                    HSSFCell cellCreated11 = rowCreated.createCell(10);
                    cellCreated11.setCellValue(entradaProductoCantidad.getPrecioUnitario());

                    HSSFCell cellCreated12 = rowCreated.createCell(11);
                    cellCreated12.setCellValue(entradaProductoCantidad.getPrecioTotal());

                    totalBodega += entradaProductoCantidad.getPrecioTotal();
                    i++;
                }
            }

            HSSFRow rowCreated2 = sheet.createRow(i++);
            rowCreated2.setHeight((short) 350);

            HSSFRow rowCreated3 = sheet.createRow(i++);
            rowCreated3.setHeight((short) 350);
            HSSFCell cellCreated13 = rowCreated3.createCell(8);
            cellCreated13.setCellStyle(alignRightBoldTotal);
            HSSFCell cellCreated14 = rowCreated3.createCell(9);
            cellCreated14.setCellValue("Total " + bodega.getDescripcion());
            cellCreated14.setCellStyle(alignRightBoldTotalMid);
            HSSFCell cellCreated15 = rowCreated3.createCell(10);
            cellCreated15.setCellStyle(alignRightBoldTotalMid);
            HSSFCell cellCreated16 = rowCreated3.createCell(11);
            cellCreated16.setCellValue(totalBodega);
            cellCreated16.setCellStyle(alignRightBoldTotalValue);
        }


        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/vnd.ms-excel");
        SimpleDateFormat sdf2 =  new SimpleDateFormat("ddMMyyyy");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"inf_guiaentrada_" + sdf2.format(new Date()) + ".xls\"");

        workbook.write(externalContext.getResponseOutputStream());
        facesContext.responseComplete();
    }

    private List<GuiaEntrada> applyFilters() {
        List<GuiaEntrada> list = new ArrayList<GuiaEntrada>();

        if(fechaDesde != null && fechaHasta != null){
            list = guiaEntradaDAO.findByFechaAndConceptos(fechaDesde, fechaHasta, conceptoId, bodegaId);
        } else if(fechaDesde != null && fechaHasta == null){
            list = guiaEntradaDAO.findByFechaAndConceptos(fechaDesde, new Date(), conceptoId, bodegaId);
        } else if(fechaDesde == null && fechaHasta != null){
            list = guiaEntradaDAO.findByFechaAndConceptos(new Date(0), fechaHasta, conceptoId, bodegaId);
        } else if(fechaDesde == null && fechaHasta == null){
            list = guiaEntradaDAO.findByFechaAndConceptos(new Date(0), new Date(), conceptoId, bodegaId);
        }

        return list;
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

    public Long getConceptoId() {
        return conceptoId;
    }

    public void setConceptoId(Long conceptoId) {
        this.conceptoId = conceptoId;
    }

    public Long getBodegaId() {
        return bodegaId;
    }

    public void setBodegaId(Long bodegaId) {
        this.bodegaId = bodegaId;
    }
}

