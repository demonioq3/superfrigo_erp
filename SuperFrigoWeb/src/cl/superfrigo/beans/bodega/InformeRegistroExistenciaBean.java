package cl.superfrigo.beans.bodega;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.*;
import cl.superfrigo.entity.abastecimiento.CentroDeCosto;
import cl.superfrigo.entity.bodega.*;
import cl.superfrigo.entity.comercial.OrdenDeTrabajo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;

import javax.annotation.PostConstruct;
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
import java.util.List;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class InformeRegistroExistenciaBean extends BaseBean implements Serializable {

    private Date fechaDesde;
    private Date fechaHasta;
    private String tipoInforme = "fisico";
    private Long bodegaId;
    private List<String> fichasProductoId;

    // Formato date
    private SimpleDateFormat simpleDateFormat;

    @EJB private FichaProductoDAO fichaProductoDAO;
    @EJB private GuiaEntradaDAO guiaEntradaDAO;
    @EJB private GuiaSalidaDAO guiaSalidaDAO;
    @EJB private EntradaProductoCantidadDAO entradaProductoCantidadDAO;
    @EJB private SalidaProductoCantidadDAO salidaProductoCantidadDAO;

    @PostConstruct
    public void init(){
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    public void getReportData() throws IOException {
        if(fechaDesde == null && fechaHasta != null || fechaDesde != null && fechaHasta == null){
            showError("Error", "Debe ingresar las dos fechas para generar el informe.");
            return;
        }

        if(fichasProductoId == null || fichasProductoId.size() == 0){
            showError("Error", "Debe seleccionar 1 o más productos para generar el informe.");
            return;
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        // Default settings width columns.
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 9000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 6000);
        sheet.setColumnWidth(4, 8000);
        sheet.setColumnWidth(5, 8000);
        sheet.setColumnWidth(6, 5000);
        sheet.setColumnWidth(7, 5000);
        sheet.setColumnWidth(8, 5000);
        sheet.setColumnWidth(9, 5000);
        sheet.setColumnWidth(10, 5000);
        sheet.setColumnWidth(11, 5000);
        sheet.setColumnWidth(12, 5000);
        sheet.setColumnWidth(13, 7000);
        sheet.setColumnWidth(14, 7000);
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
        cell.setCellValue("Registro de existencia");
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
        HSSFCell cell5 = row4.createCell(4);
        cell5.setCellValue("                      INFORME REGISTRO DE EXISTENCIAS");
        cell5.setCellStyle(boldStyle);



        // Inicio de tabla
        // Header tabla.
        HSSFRow row9 = sheet.createRow(9);
        row9.setHeight((short) 600);

        HSSFCell cell9 = row9.createCell(0);
        cell9.setCellStyle(headersTablaStyle);
        cell9.setCellValue("Código producto");
        HSSFCell cell10 = row9.createCell(1);
        cell10.setCellStyle(headersTablaStyle);
        cell10.setCellValue("Descripción");
        HSSFCell cell11 = row9.createCell(2);
        cell11.setCellStyle(headersTablaStyle);
        cell11.setCellValue("U. medida");
        HSSFCell cell12 = row9.createCell(3);
        cell12.setCellStyle(headersTablaStyle);
        cell12.setCellValue("Bodega");
        HSSFCell cell13 = row9.createCell(4);
        cell13.setCellStyle(headersTablaStyle);
        cell13.setCellValue("Entrada / Salida");
        HSSFCell cell14 = row9.createCell(5);
        cell14.setCellStyle(headersTablaStyle);
        cell14.setCellValue("Concepto");
        HSSFCell cell15 = row9.createCell(6);
        cell15.setCellStyle(headersTablaStyle);
        cell15.setCellValue("Folio interno");
        HSSFCell cell16 = row9.createCell(7);
        cell16.setCellStyle(headersTablaStyle);
        cell16.setCellValue("Fecha");
        HSSFCell cell17 = row9.createCell(8);
        cell17.setCellStyle(headersTablaStyle);
        cell17.setCellValue("Nº Documento");
        HSSFCell cell18 = row9.createCell(9);
        cell18.setCellStyle(headersTablaStyle);
        cell18.setCellValue("Valor monto");
        HSSFCell cell19 = row9.createCell(10);
        cell19.setCellStyle(headersTablaStyle);
        cell19.setCellValue("Entrada");
        HSSFCell cell20 = row9.createCell(11);
        cell20.setCellStyle(headersTablaStyle);
        cell20.setCellValue("Salida");
        HSSFCell cell21 = row9.createCell(12);
        cell21.setCellStyle(headersTablaStyle);
        cell21.setCellValue("Saldo");
        HSSFCell cell22 = row9.createCell(13);
        cell22.setCellStyle(headersTablaStyle);
        cell22.setCellValue("Costo");
        HSSFCell cell23 = row9.createCell(14);
        cell23.setCellStyle(headersTablaStyle);
        cell23.setCellValue("Saldo $");

        List<EntradaProductoCantidad> entradas = new ArrayList<EntradaProductoCantidad>();
        List<SalidaProductoCantidad> salidas = new ArrayList<SalidaProductoCantidad>();

        for (String fichaId : fichasProductoId) {
            FichaProducto fichaProducto = fichaProductoDAO.getById(Long.valueOf(fichaId));

            if(bodegaId == 0 && fechaDesde == null){
                entradas = entradaProductoCantidadDAO.findByProductoFicha(fichaProducto);
                salidas = salidaProductoCantidadDAO.findByProductoFicha(fichaProducto);
            } else if(bodegaId == 0 && fechaDesde != null){
                entradas = entradaProductoCantidadDAO.findByProductoFichaAndFechas(fichaProducto, fechaDesde, fechaHasta);
                salidas = salidaProductoCantidadDAO.findByProductoFichaAndFechas(fichaProducto, fechaDesde, fechaHasta);
            } else if(bodegaId != 0 && fechaDesde == null){
                entradas = entradaProductoCantidadDAO.findByProductoFichaAndBodegaId(fichaProducto, bodegaId);
                salidas = salidaProductoCantidadDAO.findByProductoFichaAndBodegaId(fichaProducto, bodegaId);
            } else if(bodegaId != 0 && fechaDesde != null){
                entradas = entradaProductoCantidadDAO.findByProductoFichaAndBodegaIdAndFechas(fichaProducto, bodegaId, fechaDesde, fechaHasta);
                salidas = salidaProductoCantidadDAO.findByProductoFichaAndBodegaIdAndFechas(fichaProducto, bodegaId, fechaDesde, fechaHasta);
            }
        }

        // Parte por defecto en la fila 10.
        int i = 10;
        long totalBodega = 0L;

        double saldo = 0;
        for (EntradaProductoCantidad entrada : entradas) {
            HSSFRow rowCreated = sheet.createRow(i);
            rowCreated.setHeight((short) 350);
            HSSFCell cellCreated1 = rowCreated.createCell(0);
            cellCreated1.setCellValue(entrada.getFichaProducto().getCodigoProducto());

            HSSFCell cellCreated2 = rowCreated.createCell(1);
            cellCreated2.setCellValue(entrada.getFichaProducto().getDescripcion());

            HSSFCell cellCreated3 = rowCreated.createCell(2);
            cellCreated3.setCellValue(entrada.getFichaProducto().getUnidadMedida().getNombre());

            HSSFCell cellCreated6 = rowCreated.createCell(3);
            GuiaEntrada guiaEntrada = guiaEntradaDAO.findById(entrada.getGuiaEntradaId());
            cellCreated6.setCellValue(guiaEntrada.getBodega().getCodigo() + " - " + guiaEntrada.getBodega().getDescripcion());

            HSSFCell cellCreated7 = rowCreated.createCell(4);
            if(guiaEntrada.getProveedor()!= null){
                cellCreated7.setCellValue(guiaEntrada.getProveedor().getNombreRazonSocial());
            }

            HSSFCell cellCreated8 = rowCreated.createCell(5);
            cellCreated8.setCellValue(guiaEntrada.getConceptoEntrada().getDescripcion());

            HSSFCell cellCreated9 = rowCreated.createCell(6);
            cellCreated9.setCellValue(entrada.getId());

            HSSFCell cellCreated10 = rowCreated.createCell(7);
            cellCreated10.setCellValue(guiaEntrada.getFecha());

            HSSFCell cellCreated11 = rowCreated.createCell(8);
            cellCreated11.setCellValue(guiaEntrada.getId());

            HSSFCell cellCreated12 = rowCreated.createCell(9);
            cellCreated12.setCellValue(entrada.getPrecioUnitario());

            HSSFCell cellCreated13 = rowCreated.createCell(10);
            cellCreated13.setCellValue(entrada.getCantidad());

            HSSFCell cellCreated14 = rowCreated.createCell(11);
            cellCreated14.setCellValue(entrada.getCantidadUtilizada());

            HSSFCell cellCreated15 = rowCreated.createCell(12);
            cellCreated15.setCellValue(entrada.getCantidad() - entrada.getCantidadUtilizada());

            HSSFCell cellCreated16 = rowCreated.createCell(13);
            cellCreated16.setCellValue(entrada.getPrecioTotal());

            HSSFCell cellCreated17 = rowCreated.createCell(14);
            saldo += entrada.getPrecioTotal();
            cellCreated17.setCellValue(saldo);
            i++;
        }

        for (SalidaProductoCantidad salida : salidas) {
            HSSFRow rowCreated = sheet.createRow(i);
            rowCreated.setHeight((short) 350);
            HSSFCell cellCreated1 = rowCreated.createCell(0);
            cellCreated1.setCellValue(salida.getFichaProducto().getCodigoProducto());

            HSSFCell cellCreated2 = rowCreated.createCell(1);
            cellCreated2.setCellValue(salida.getFichaProducto().getDescripcion());

            HSSFCell cellCreated3 = rowCreated.createCell(2);
            cellCreated3.setCellValue(salida.getFichaProducto().getUnidadMedida().getNombre());

            HSSFCell cellCreated6 = rowCreated.createCell(3);
            GuiaSalida guiaSalida = guiaSalidaDAO.findById(salida.getGuiaSalidaId());
            if(guiaSalida != null)
                cellCreated6.setCellValue(guiaSalida.getBodega().getCodigo() + " - " + guiaSalida.getBodega().getDescripcion());

            HSSFCell cellCreated7 = rowCreated.createCell(4);
            if(guiaSalida != null){
                if(guiaSalida.getProveedor()!= null){
                    cellCreated7.setCellValue(guiaSalida.getProveedor().getNombreRazonSocial());
                }
            }


            HSSFCell cellCreated8 = rowCreated.createCell(5);
            if(guiaSalida != null)
                cellCreated8.setCellValue(guiaSalida.getConceptoSalida().getDescripcion());

            HSSFCell cellCreated9 = rowCreated.createCell(6);
            cellCreated9.setCellValue(salida.getId());

            HSSFCell cellCreated10 = rowCreated.createCell(7);
            if(guiaSalida != null)
                cellCreated10.setCellValue(guiaSalida.getFecha());

            HSSFCell cellCreated11 = rowCreated.createCell(8);
            if(guiaSalida != null)
                cellCreated11.setCellValue(guiaSalida.getId());

            HSSFCell cellCreated12 = rowCreated.createCell(9);
            if(salida.getPrecioUnitario() != null)
                cellCreated12.setCellValue(salida.getPrecioUnitario());

            HSSFCell cellCreated13 = rowCreated.createCell(10);
            cellCreated13.setCellValue(0);

            HSSFCell cellCreated14 = rowCreated.createCell(11);
            if(salida.getCantidad() != null)
                cellCreated14.setCellValue(salida.getCantidad());

            HSSFCell cellCreated15 = rowCreated.createCell(12);
            if(salida.getCantidad() != null)
                cellCreated15.setCellValue(salida.getCantidad());

            HSSFCell cellCreated16 = rowCreated.createCell(13);
            if(salida.getPrecioTotal() != null)
                cellCreated16.setCellValue(salida.getPrecioTotal());

            HSSFCell cellCreated17 = rowCreated.createCell(14);
            if(salida.getPrecioTotal() != null)
                saldo += salida.getPrecioTotal();
            cellCreated17.setCellValue(saldo);
            i++;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/vnd.ms-excel");
        SimpleDateFormat sdf2 =  new SimpleDateFormat("ddMMyyyy");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"inf_regexistencia_" + sdf2.format(new Date()) + ".xls\"");

        workbook.write(externalContext.getResponseOutputStream());
        facesContext.responseComplete();
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

    public String getTipoInforme() {
        return tipoInforme;
    }

    public void setTipoInforme(String tipoInforme) {
        this.tipoInforme = tipoInforme;
    }

    public Long getBodegaId() {
        return bodegaId;
    }

    public void setBodegaId(Long bodegaId) {
        this.bodegaId = bodegaId;
    }

    public List<String> getFichasProductoId() {
        return fichasProductoId;
    }

    public void setFichasProductoId(List<String> fichasProductoId) {
        this.fichasProductoId = fichasProductoId;
    }
}



