package cl.superfrigo.beans.bodega;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.*;
import cl.superfrigo.entity.bodega.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

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
import java.util.HashMap;
import java.util.List;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class InformeStockBean extends BaseBean implements Serializable {

    // Parámetros informe guias de entrada.
    private Long bodegaId;
    private Long grupoId;
    private Long subGrupoId;
    private Long fichaProductoId;

    // Lista de subgrupos asociadas al grupoId;
    private List<SubGrupo> subGrupos;

    // Lista de productos asociados a subgrupo.
    private List<FichaProducto> fichasProducto;

    // Bloqueo de selects;
    private boolean bloquearSubgrupo = true;
    private boolean bloquearProducto = true;

    private String stockAlRadio = "Acumulado";
    private Date fechaDesde;
    private Date fechaHasta;
    private boolean mostrarFechasStock = false;
    private String considerarRadio = "all";

    // Formato date
    private SimpleDateFormat simpleDateFormat;

    @EJB private GuiaEntradaDAO guiaEntradaDAO;
    @EJB private BodegaDAO bodegaDAO;
    @EJB private FichaProductoDAO fichaProductoDAO;
    @EJB private BodegaStockProductoDAO bodegaStockProductoDAO;
    @EJB private GrupoDAO grupoDAO;
    @EJB private SubGrupoDAO subGrupoDAO;


    @PostConstruct
    public void init(){
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    public void changeGrupo(){
        if(grupoId != 0){
            try{
                Grupo grupo = grupoDAO.getById(grupoId);
                subGrupos = subGrupoDAO.findByPadreId(grupo.getId());
            } catch (Exception e){
                bloquearSubgrupo = true;
                return;
            }

            bloquearSubgrupo = false;
        } else {
            bloquearSubgrupo = true;
        }
    }

    public void changeSubgrupo(){
        if(subGrupoId != 0){
            try{
                SubGrupo subGrupo = subGrupoDAO.getById(subGrupoId);
                fichasProducto = fichaProductoDAO.findBySubGrupoId(subGrupoId);
            } catch (Exception e){
                bloquearProducto = true;
                return;
            }

            bloquearProducto = false;
        } else {
            bloquearProducto = true;
        }
    }

    public void getReportData() throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        // Default settings width columns.
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 9000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
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
        cell.setCellValue("Informe de stock");
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
        cell5.setCellValue("                      INFORME DE STOCK");
        cell5.setCellStyle(boldStyle);

        // Periodo.
        HSSFRow row5 = sheet.createRow(5);
        row5.setHeight((short) 350);
        HSSFCell cell7 = row5.createCell(3);
        cell7.setCellValue("Tipo de informe: ");
        cell7.setCellStyle(alignRight);

        HSSFCell cellTipoInforme = row5.createCell(4);
        cellTipoInforme.setCellStyle(alignRight);
        if(bodegaId == 0){
            cellTipoInforme.setCellValue("Consolidado");
        } else {
            cellTipoInforme.setCellValue(bodegaDAO.getById(bodegaId).getDescripcion());
        }

        HSSFRow row6 = sheet.createRow(6);
        row6.setHeight((short) 350);
        HSSFCell cell8 = row6.createCell(3);
        cell8.setCellValue("Stock al: ");
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
        cell9.setCellValue("Código producto");
        HSSFCell cell10 = row9.createCell(1);
        cell10.setCellStyle(headersTablaStyle);
        cell10.setCellValue("Descripción");
        HSSFCell cell11 = row9.createCell(2);
        cell11.setCellStyle(headersTablaStyle);
        cell11.setCellValue("Bodega");
        HSSFCell cell12 = row9.createCell(3);
        cell12.setCellStyle(headersTablaStyle);
        cell12.setCellValue("U. medida");
        HSSFCell cell13 = row9.createCell(4);
        cell13.setCellStyle(headersTablaStyle);
        cell13.setCellValue("Stock actual");
        HSSFCell cell14 = row9.createCell(5);
        cell14.setCellStyle(headersTablaStyle);
        cell14.setCellValue("Stock minimo");
        HSSFCell cell15 = row9.createCell(6);
        cell15.setCellStyle(headersTablaStyle);
        cell15.setCellValue("Stock reposición");

        // Verificando filtros de la vista.
        List<FichaProducto> lista = applyFilters();

        // Parte por defecto en la fila 10.
        int i = 10;
        long totalBodega = 0L;

        for (FichaProducto fichaProducto : lista) {
            // Consolidado
            if (bodegaId == 0 && considerarRadio.equals("all")) {
                List<BodegaStockProducto> bodegasStock = bodegaStockProductoDAO.findByFichaProductoId(fichaProducto.getId());

                for (BodegaStockProducto bodegaStockProducto : bodegasStock) {
                    HSSFRow rowCreated = sheet.createRow(i);
                    rowCreated.setHeight((short) 350);
                    HSSFCell cellCreated1 = rowCreated.createCell(0);
                    cellCreated1.setCellValue(fichaProducto.getCodigoProducto());

                    HSSFCell cellCreated2 = rowCreated.createCell(1);
                    cellCreated2.setCellValue(fichaProducto.getDescripcion());

                    HSSFCell cellCreated3 = rowCreated.createCell(2);
                    cellCreated3.setCellValue(bodegaStockProducto.getBodega().getCodigo() + " - " + bodegaStockProducto.getBodega().getDescripcion());

                    HSSFCell cellCreated6 = rowCreated.createCell(3);
                    cellCreated6.setCellValue(fichaProducto.getUnidadMedida().getNombre());

                    HSSFCell cellCreated7 = rowCreated.createCell(4);
                    cellCreated7.setCellValue(bodegaStockProducto.getCantidad());

                    HSSFCell cellCreated8 = rowCreated.createCell(5);
                    cellCreated8.setCellValue(fichaProducto.getStockMinimo());

                    HSSFCell cellCreated9 = rowCreated.createCell(6);
                    cellCreated9.setCellValue(fichaProducto.getStockReposicion());

                    i++;
                }
            } else if (bodegaId == 0 && considerarRadio.equals("onlyStock")) {
                List<BodegaStockProducto> bodegasStock = bodegaStockProductoDAO.findByFichaProductoId(fichaProducto.getId());

                for (BodegaStockProducto bodegaStockProducto : bodegasStock) {

                    if (bodegaStockProducto.getCantidad() == 0) {
                        continue;
                    }

                    HSSFRow rowCreated = sheet.createRow(i);
                    rowCreated.setHeight((short) 350);
                    HSSFCell cellCreated1 = rowCreated.createCell(0);
                    cellCreated1.setCellValue(fichaProducto.getCodigoProducto());

                    HSSFCell cellCreated2 = rowCreated.createCell(1);
                    cellCreated2.setCellValue(fichaProducto.getDescripcion());

                    HSSFCell cellCreated3 = rowCreated.createCell(2);
                    cellCreated3.setCellValue(bodegaStockProducto.getBodega().getCodigo() + " - " + bodegaStockProducto.getBodega().getDescripcion());

                    HSSFCell cellCreated6 = rowCreated.createCell(3);
                    cellCreated6.setCellValue(fichaProducto.getUnidadMedida().getNombre());

                    HSSFCell cellCreated7 = rowCreated.createCell(4);
                    cellCreated7.setCellValue(bodegaStockProducto.getCantidad());

                    HSSFCell cellCreated8 = rowCreated.createCell(5);
                    cellCreated8.setCellValue(fichaProducto.getStockMinimo());

                    HSSFCell cellCreated9 = rowCreated.createCell(6);
                    cellCreated9.setCellValue(fichaProducto.getStockReposicion());

                    i++;
                }
            } else if (bodegaId != 0 && considerarRadio.equals("all") && bodegaId != 0 && considerarRadio.equals("onlyStock")) {
                BodegaStockProducto bodegaStockProducto = bodegaStockProductoDAO.findByFichaProductoIdAndBodegaId(fichaProducto.getId(), bodegaId);

                HSSFRow rowCreated = sheet.createRow(i);
                rowCreated.setHeight((short) 350);
                HSSFCell cellCreated1 = rowCreated.createCell(0);
                cellCreated1.setCellValue(fichaProducto.getCodigoProducto());

                HSSFCell cellCreated2 = rowCreated.createCell(1);
                cellCreated2.setCellValue(fichaProducto.getDescripcion());

                HSSFCell cellCreated3 = rowCreated.createCell(2);
                cellCreated3.setCellValue(bodegaStockProducto.getBodega().getCodigo() + " - " + bodegaStockProducto.getBodega().getDescripcion());

                HSSFCell cellCreated6 = rowCreated.createCell(3);
                cellCreated6.setCellValue(fichaProducto.getUnidadMedida().getNombre());

                HSSFCell cellCreated7 = rowCreated.createCell(4);
                cellCreated7.setCellValue(bodegaStockProducto.getCantidad());

                HSSFCell cellCreated8 = rowCreated.createCell(5);
                cellCreated8.setCellValue(fichaProducto.getStockMinimo());

                HSSFCell cellCreated9 = rowCreated.createCell(6);
                cellCreated9.setCellValue(fichaProducto.getStockReposicion());

                i++;
            }

        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/vnd.ms-excel");
        SimpleDateFormat sdf2 =  new SimpleDateFormat("ddMMyyyy");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"inf_stock_" + sdf2.format(new Date()) + ".xls\"");

        workbook.write(externalContext.getResponseOutputStream());
        facesContext.responseComplete();
    }

    private List<FichaProducto> applyFilters() {
        List<FichaProducto> list = new ArrayList<FichaProducto>();

        if(grupoId == 0){
            list = fichaProductoDAO.findAll();
        } else if(grupoId != 0 && subGrupoId == 0){
            list = fichaProductoDAO.findByGrupoId(grupoId);
        } else if(subGrupoId != 0 && fichaProductoId == 0){
            list = fichaProductoDAO.findBySubGrupoId(subGrupoId);
        } else if(subGrupoId != 0 && fichaProductoId != 0){
            FichaProducto fichaProducto = fichaProductoDAO.getById(fichaProductoId);
            list.add(fichaProducto);
        }

        return list;
    }

    public void changeStockRadio(){
        if(stockAlRadio.equals("Acumulado")){
            mostrarFechasStock = false;
        } else if(stockAlRadio.equals("Fechas")){
            mostrarFechasStock = true;
        }
    }

    public Long getBodegaId() {
        return bodegaId;
    }

    public void setBodegaId(Long bodegaId) {
        this.bodegaId = bodegaId;
    }

    public Long getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(Long grupoId) {
        this.grupoId = grupoId;
    }

    public Long getSubGrupoId() {
        return subGrupoId;
    }

    public void setSubGrupoId(Long subGrupoId) {
        this.subGrupoId = subGrupoId;
    }

    public Long getFichaProductoId() {
        return fichaProductoId;
    }

    public void setFichaProductoId(Long fichaProductoId) {
        this.fichaProductoId = fichaProductoId;
    }

    public String getStockAlRadio() {
        return stockAlRadio;
    }

    public void setStockAlRadio(String stockAlRadio) {
        this.stockAlRadio = stockAlRadio;
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

    public boolean isMostrarFechasStock() {
        return mostrarFechasStock;
    }

    public void setMostrarFechasStock(boolean mostrarFechasStock) {
        this.mostrarFechasStock = mostrarFechasStock;
    }

    public String getConsiderarRadio() {
        return considerarRadio;
    }

    public void setConsiderarRadio(String considerarRadio) {
        this.considerarRadio = considerarRadio;
    }

    public boolean isBloquearSubgrupo() {
        return bloquearSubgrupo;
    }

    public boolean isBloquearProducto() {
        return bloquearProducto;
    }

    public List<SubGrupo> getSubGrupos() {
        return subGrupos;
    }

    public List<FichaProducto> getFichasProducto() {
        return fichasProducto;
    }
}



