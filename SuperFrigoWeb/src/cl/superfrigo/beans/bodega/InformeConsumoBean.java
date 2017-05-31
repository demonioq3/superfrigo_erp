package cl.superfrigo.beans.bodega;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.CentroDeCostoDAO;
import cl.superfrigo.dao.GuiaSalidaDAO;
import cl.superfrigo.dao.OrdenDeTrabajoDAO;
import cl.superfrigo.entity.abastecimiento.CentroDeCosto;
import cl.superfrigo.entity.bodega.*;
import cl.superfrigo.entity.comercial.OrdenDeTrabajo;
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
public class InformeConsumoBean extends BaseBean implements Serializable {

    // Parámetros informe consumo.
    private Long bodegaId;
    private Date fechaDesde;
    private Date fechaHasta;
    private Long centroDeCostoId;
    private Long ordenDeTrabajoId;

    private String conceptoRadio = "Consumo por centro de costos";

    private boolean mostrarCC = false;

    @EJB private GuiaSalidaDAO guiaSalidaDAO;
    @EJB private OrdenDeTrabajoDAO ordenDeTrabajoDAO;
    @EJB private CentroDeCostoDAO centroDeCostoDAO;


    public void changeConceptoRadio(){
        if(conceptoRadio.equals("Consumo por centro de costos")){
            mostrarCC = false;
        } else if(conceptoRadio.equals("Consumo por orden de trabajo")){
            mostrarCC = true;
        }
    }

    public void getReportData() throws IOException {
        HSSFWorkbook workbook = null;

        if(fechaDesde != null && fechaHasta == null || fechaDesde == null && fechaHasta != null){
            showError("Error", "Debe ingresar las 2 fechas para realizar la búsqueda.");
            return;
        }

        if(conceptoRadio.equals("Consumo por centro de costos")){
            workbook = generarInformeCC();
        } else if(conceptoRadio.equals("Consumo por orden de trabajo")){
            workbook = generarInformeOT();
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/vnd.ms-excel");
        SimpleDateFormat sdf2 =  new SimpleDateFormat("ddMMyyyy");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"inf_consumo_" + sdf2.format(new Date()) + ".xls\"");

        workbook.write(externalContext.getResponseOutputStream());
        facesContext.responseComplete();
    }

    private HSSFWorkbook generarInformeOT() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        // Default settings width columns.
        sheet.setColumnWidth(0, 3000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 7000);
        sheet.setColumnWidth(3, 6000);
        sheet.setColumnWidth(4, 9000);
        sheet.setColumnWidth(5, 5000);
        sheet.setColumnWidth(6, 6500);
        sheet.setColumnWidth(7, 5000);
        sheet.setColumnWidth(8, 5000);
        sheet.setColumnWidth(9, 5000);
        sheet.setColumnWidth(10, 5000);
        sheet.setColumnWidth(11, 5000);
        sheet.setColumnWidth(12, 5000);
        sheet.setColumnWidth(13, 5000);

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
        cell.setCellValue("Informe de consumo por orden de trabajo");
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
        cell5.setCellValue("                      INFORME DE CONSUMO POR ORDEN DE TRABAJO");
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
        cell9.setCellValue("Nº OT");
        HSSFCell cell10 = row9.createCell(1);
        cell10.setCellStyle(headersTablaStyle);
        cell10.setCellValue("Grupo");
        HSSFCell cell11 = row9.createCell(2);
        cell11.setCellStyle(headersTablaStyle);
        cell11.setCellValue("Subgrupo");
        HSSFCell cell12 = row9.createCell(3);
        cell12.setCellStyle(headersTablaStyle);
        cell12.setCellValue("Código producto");
        HSSFCell cell13 = row9.createCell(4);
        cell13.setCellStyle(headersTablaStyle);
        cell13.setCellValue("Descripción");
        HSSFCell cell14 = row9.createCell(5);
        cell14.setCellStyle(headersTablaStyle);
        cell14.setCellValue("U. medida");
        HSSFCell cell15 = row9.createCell(6);
        cell15.setCellStyle(headersTablaStyle);
        cell15.setCellValue("Bodega");
        HSSFCell cell16 = row9.createCell(7);
        cell16.setCellStyle(headersTablaStyle);
        cell16.setCellValue("Folio interno");
        HSSFCell cell17 = row9.createCell(8);
        cell17.setCellStyle(headersTablaStyle);
        cell17.setCellValue("Fecha");
        HSSFCell cell18 = row9.createCell(9);
        cell18.setCellStyle(headersTablaStyle);
        cell18.setCellValue("Cantidad solicitada");
        HSSFCell cell19 = row9.createCell(10);
        cell19.setCellStyle(headersTablaStyle);
        cell19.setCellValue("Cantidad devuelta");
        HSSFCell cell20 = row9.createCell(11);
        cell20.setCellStyle(headersTablaStyle);
        cell20.setCellValue("Cantidad neta");
        HSSFCell cell21 = row9.createCell(11);
        cell21.setCellStyle(headersTablaStyle);
        cell21.setCellValue("Valor unitario");
        HSSFCell cell22 = row9.createCell(11);
        cell22.setCellStyle(headersTablaStyle);
        cell22.setCellValue("Costo total");

        // Verificando filtros de la vista.
        List<GuiaSalida> guiasSalida = applyFiltersOT();

        // Parte por defecto en la fila 10.
        int i = 10;
        long totalBodega = 0L;
        OrdenDeTrabajo ordenDeTrabajo = ordenDeTrabajoDAO.findById(ordenDeTrabajoId);
        for (GuiaSalida guiaSalida : guiasSalida) {
            for (SalidaProductoCantidad salida : guiaSalida.getProductos()) {
                HSSFRow rowCreated = sheet.createRow(i);
                rowCreated.setHeight((short) 350);
                HSSFCell cellCreated1 = rowCreated.createCell(0);
                cellCreated1.setCellValue(ordenDeTrabajoId);

                HSSFCell cellCreated2 = rowCreated.createCell(1);
                if(salida.getFichaProducto().getGrupo() != null)
                    cellCreated2.setCellValue(salida.getFichaProducto().getGrupo().getNombreGrupo());

                HSSFCell cellCreated3 = rowCreated.createCell(2);
                if(salida.getFichaProducto().getSubGrupo() != null)
                    cellCreated3.setCellValue(salida.getFichaProducto().getSubGrupo().getNombreSubGrupo());

                HSSFCell cellCreated4 = rowCreated.createCell(3);
                if(salida.getFichaProducto() != null)
                    cellCreated4.setCellValue(salida.getFichaProducto().getCodigoProducto());

                HSSFCell cellCreated5 = rowCreated.createCell(4);
                if(salida.getFichaProducto() != null)
                    cellCreated5.setCellValue(salida.getFichaProducto().getDescripcion());

                HSSFCell cellCreated6 = rowCreated.createCell(5);
                if(salida.getFichaProducto().getUnidadMedida() != null)
                    cellCreated6.setCellValue(salida.getFichaProducto().getUnidadMedida().getNombre());

                HSSFCell cellCreated7 = rowCreated.createCell(6);
                if(guiaSalida.getBodega() != null)
                    cellCreated7.setCellValue(guiaSalida.getBodega().getCodigo() + " - " + guiaSalida.getBodega().getDescripcion());

                HSSFCell cellCreated8 = rowCreated.createCell(7);
                cellCreated8.setCellValue(salida.getId());

                HSSFCell cellCreated9 = rowCreated.createCell(8);
                cellCreated9.setCellValue(guiaSalida.getFecha());

                HSSFCell cellCreated10 = rowCreated.createCell(9);
                cellCreated10.setCellValue(salida.getCantidad());

                HSSFCell cellCreated11 = rowCreated.createCell(10);
                cellCreated11.setCellValue(salida.getCantidad());

                HSSFCell cellCreated12 = rowCreated.createCell(11);
                cellCreated12.setCellValue(salida.getCantidad());

                HSSFCell cellCreated13 = rowCreated.createCell(12);
                cellCreated12.setCellValue(salida.getPrecioUnitario());

                HSSFCell cellCreated14 = rowCreated.createCell(13);
                cellCreated14.setCellValue(salida.getPrecioTotal());
            }
        }

        return workbook;

    }

    private List<GuiaSalida> applyFiltersOT() {
        List<GuiaSalida> salidaList = new ArrayList<GuiaSalida>();

        if(fechaDesde != null){
            if(bodegaId == 0){
                salidaList = guiaSalidaDAO.findByOrdenDeTrabajoId(ordenDeTrabajoId, fechaDesde, fechaHasta);
            } else {
                salidaList = guiaSalidaDAO.findByOrdenDeTrabajoIdBodegaId(ordenDeTrabajoId, bodegaId, fechaDesde, fechaHasta);
            }
        }
        else {
            if(bodegaId == 0){
                salidaList = guiaSalidaDAO.findByOrdenDeTrabajoId(ordenDeTrabajoId);
            } else {
                salidaList = guiaSalidaDAO.findByOrdenDeTrabajoIdBodegaId(ordenDeTrabajoId, bodegaId);
            }
        }

        return salidaList;
    }

    private List<GuiaSalida> applyFiltersCC() {
        List<GuiaSalida> salidaList = new ArrayList<GuiaSalida>();

        if(fechaDesde != null){
            if(bodegaId == 0){
                salidaList = guiaSalidaDAO.findByCC(centroDeCostoId, fechaDesde, fechaHasta);
            } else {
                salidaList = guiaSalidaDAO.findByCCBodegaId(centroDeCostoId, bodegaId, fechaDesde, fechaHasta);
            }
        }
        else {
            if(bodegaId == 0){
                salidaList = guiaSalidaDAO.findByCC(centroDeCostoId);
            } else {
                salidaList = guiaSalidaDAO.findByCCBodegaId(centroDeCostoId, bodegaId);
            }
        }

        return salidaList;
    }

    private HSSFWorkbook generarInformeCC() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        // Default settings width columns.
        sheet.setColumnWidth(0, 3000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 7000);
        sheet.setColumnWidth(3, 6000);
        sheet.setColumnWidth(4, 9000);
        sheet.setColumnWidth(5, 5000);
        sheet.setColumnWidth(6, 6500);
        sheet.setColumnWidth(7, 5000);
        sheet.setColumnWidth(8, 5000);
        sheet.setColumnWidth(9, 5000);
        sheet.setColumnWidth(10, 5000);
        sheet.setColumnWidth(11, 5000);
        sheet.setColumnWidth(12, 5000);
        sheet.setColumnWidth(13, 5000);

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
        cell.setCellValue("Informe de consumo por centro de costo");
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
        cell5.setCellValue("                      INFORME DE CONSUMO POR CENTRO DE COSTOS");
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
        cell9.setCellValue("Centro de costos");
        HSSFCell cell10 = row9.createCell(1);
        cell10.setCellStyle(headersTablaStyle);
        cell10.setCellValue("Grupo");
        HSSFCell cell11 = row9.createCell(2);
        cell11.setCellStyle(headersTablaStyle);
        cell11.setCellValue("Subgrupo");
        HSSFCell cell12 = row9.createCell(3);
        cell12.setCellStyle(headersTablaStyle);
        cell12.setCellValue("Código producto");
        HSSFCell cell13 = row9.createCell(4);
        cell13.setCellStyle(headersTablaStyle);
        cell13.setCellValue("Descripción");
        HSSFCell cell14 = row9.createCell(5);
        cell14.setCellStyle(headersTablaStyle);
        cell14.setCellValue("U. medida");
        HSSFCell cell15 = row9.createCell(6);
        cell15.setCellStyle(headersTablaStyle);
        cell15.setCellValue("Bodega");
        HSSFCell cell16 = row9.createCell(7);
        cell16.setCellStyle(headersTablaStyle);
        cell16.setCellValue("Folio interno");
        HSSFCell cell17 = row9.createCell(8);
        cell17.setCellStyle(headersTablaStyle);
        cell17.setCellValue("Fecha");
        HSSFCell cell18 = row9.createCell(9);
        cell18.setCellStyle(headersTablaStyle);
        cell18.setCellValue("Cantidad solicitada");
        HSSFCell cell19 = row9.createCell(10);
        cell19.setCellStyle(headersTablaStyle);
        cell19.setCellValue("Cantidad devuelta");
        HSSFCell cell20 = row9.createCell(11);
        cell20.setCellStyle(headersTablaStyle);
        cell20.setCellValue("Cantidad neta");
        HSSFCell cell21 = row9.createCell(11);
        cell21.setCellStyle(headersTablaStyle);
        cell21.setCellValue("Valor unitario");
        HSSFCell cell22 = row9.createCell(11);
        cell22.setCellStyle(headersTablaStyle);
        cell22.setCellValue("Costo total");

        // Verificando filtros de la vista.
        List<GuiaSalida> guiasSalida = applyFiltersCC();

        // Parte por defecto en la fila 10.
        int i = 10;
        long totalBodega = 0L;
        OrdenDeTrabajo ordenDeTrabajo = ordenDeTrabajoDAO.findById(ordenDeTrabajoId);
        for (GuiaSalida guiaSalida : guiasSalida) {
            for (SalidaProductoCantidad salida : guiaSalida.getProductos()) {
                HSSFRow rowCreated = sheet.createRow(i);
                rowCreated.setHeight((short) 350);
                HSSFCell cellCreated1 = rowCreated.createCell(0);
                CentroDeCosto centroDeCosto = centroDeCostoDAO.getById(centroDeCostoId);
                cellCreated1.setCellValue(centroDeCosto.getCodigo() + " - " + centroDeCosto.getNombre());

                HSSFCell cellCreated2 = rowCreated.createCell(1);
                if(salida.getFichaProducto().getGrupo() != null)
                    cellCreated2.setCellValue(salida.getFichaProducto().getGrupo().getNombreGrupo());

                HSSFCell cellCreated3 = rowCreated.createCell(2);
                if(salida.getFichaProducto().getSubGrupo() != null)
                    cellCreated3.setCellValue(salida.getFichaProducto().getSubGrupo().getNombreSubGrupo());

                HSSFCell cellCreated4 = rowCreated.createCell(3);
                if(salida.getFichaProducto() != null)
                    cellCreated4.setCellValue(salida.getFichaProducto().getCodigoProducto());

                HSSFCell cellCreated5 = rowCreated.createCell(4);
                if(salida.getFichaProducto() != null)
                    cellCreated5.setCellValue(salida.getFichaProducto().getDescripcion());

                HSSFCell cellCreated6 = rowCreated.createCell(5);
                if(salida.getFichaProducto().getUnidadMedida() != null)
                    cellCreated6.setCellValue(salida.getFichaProducto().getUnidadMedida().getNombre());

                HSSFCell cellCreated7 = rowCreated.createCell(6);
                if(guiaSalida.getBodega() != null)
                    cellCreated7.setCellValue(guiaSalida.getBodega().getCodigo() + " - " + guiaSalida.getBodega().getDescripcion());

                HSSFCell cellCreated8 = rowCreated.createCell(7);
                cellCreated8.setCellValue(salida.getId());

                HSSFCell cellCreated9 = rowCreated.createCell(8);
                cellCreated9.setCellValue(guiaSalida.getFecha());

                HSSFCell cellCreated10 = rowCreated.createCell(9);
                cellCreated10.setCellValue(salida.getCantidad());

                HSSFCell cellCreated11 = rowCreated.createCell(10);
                cellCreated11.setCellValue(salida.getCantidad());

                HSSFCell cellCreated12 = rowCreated.createCell(11);
                cellCreated12.setCellValue(salida.getCantidad());

                HSSFCell cellCreated13 = rowCreated.createCell(12);
                cellCreated12.setCellValue(salida.getPrecioUnitario());

                HSSFCell cellCreated14 = rowCreated.createCell(13);
                cellCreated14.setCellValue(salida.getPrecioTotal());
            }
        }

        return workbook;
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

    public String getConceptoRadio() {
        return conceptoRadio;
    }

    public void setConceptoRadio(String conceptoRadio) {
        this.conceptoRadio = conceptoRadio;
    }

    public boolean isMostrarCC() {
        return mostrarCC;
    }

    public Long getCentroDeCostoId() {
        return centroDeCostoId;
    }

    public void setCentroDeCostoId(Long centroDeCostoId) {
        this.centroDeCostoId = centroDeCostoId;
    }

    public Long getOrdenDeTrabajoId() {
        return ordenDeTrabajoId;
    }

    public void setOrdenDeTrabajoId(Long ordenDeTrabajoId) {
        this.ordenDeTrabajoId = ordenDeTrabajoId;
    }
}



