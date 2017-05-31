package cl.superfrigo.beans.proyectos;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.FichaProductoDAO;
import cl.superfrigo.dao.GuiaEntradaDAO;
import cl.superfrigo.dao.RecetaDAO;
import cl.superfrigo.entity.bodega.*;
import cl.superfrigo.entity.proyectos.ProductoReceta;
import cl.superfrigo.entity.proyectos.Receta;
import cl.superfrigo.model.FichasProductoLazyDataModel;
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
public class InformeRecetasBean extends BaseBean implements Serializable {

    /* Corresponde al producto seleccionado en la vista */
    private FichaProducto fichaSeleccionada;
    /* Lista lazy de fichas producto */
    private FichasProductoLazyDataModel productos;
    /* Lista de recetas asociadas al producto */
    private List<Receta> recetasByProducto;
    /* Receta seleccionada en la vista */
    private Long recetaId;
    // Formato date
    private SimpleDateFormat simpleDateFormat;

    @EJB private FichaProductoDAO fichaProductoDAO;
    @EJB private RecetaDAO recetaDAO;


    @PostConstruct
    public void init(){
        fichaSeleccionada = new FichaProducto();
        recetasByProducto = new ArrayList<Receta>();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    public void seleccionarProducto(FichaProducto fichaProducto){
        this.fichaSeleccionada = fichaProducto;

        recetasByProducto = recetaDAO.getByFichaProductoId(fichaProducto.getId());

        showInfo("Aviso", "Se cargaron las recetas asociadas al producto código: \"" + fichaProducto.getCodigoProducto() + "\" de forma correcta.");
    }

    public void limpiar(){
        init();
    }

    public void generarInforme() throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        // Default settings width columns.
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 6500);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 6500);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 6500);
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
        cell.setCellValue("Informe recetas de fabricación");
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
        cell5.setCellValue("                      INFORME RECETAS DE FABRICACION");
        cell5.setCellStyle(boldStyle);

        // Inicio de tabla
        // Header tabla.
        HSSFRow row9 = sheet.createRow(9);
        row9.setHeight((short) 600);

        HSSFCell cell9 = row9.createCell(0);
        cell9.setCellStyle(headersTablaStyle);
        cell9.setCellValue("Código Producto");
        HSSFCell cell10 = row9.createCell(1);
        cell10.setCellStyle(headersTablaStyle);
        cell10.setCellValue("Descripción Producto");
        HSSFCell cell11 = row9.createCell(2);
        cell11.setCellStyle(headersTablaStyle);
        cell11.setCellValue("Código Receta");
        HSSFCell cell12 = row9.createCell(3);
        cell12.setCellStyle(headersTablaStyle);
        cell12.setCellValue("Descripción Receta");
        HSSFCell cell13 = row9.createCell(4);
        cell13.setCellStyle(headersTablaStyle);
        cell13.setCellValue("Cantidad Base");
        HSSFCell cell14 = row9.createCell(5);
        cell14.setCellStyle(headersTablaStyle);
        cell14.setCellValue("Código Material");
        HSSFCell cell15 = row9.createCell(6);
        cell15.setCellStyle(headersTablaStyle);
        cell15.setCellValue("Descripción Material");
        HSSFCell cell16 = row9.createCell(7);
        cell16.setCellStyle(headersTablaStyle);
        cell16.setCellValue("U. medida");
        HSSFCell cell17 = row9.createCell(8);
        cell17.setCellStyle(headersTablaStyle);
        cell17.setCellValue("Cantidad");
        HSSFCell cell18 = row9.createCell(9);
        cell18.setCellStyle(headersTablaStyle);
        cell18.setCellValue("Valor");
        HSSFCell cell19 = row9.createCell(10);
        cell19.setCellStyle(headersTablaStyle);
        cell19.setCellValue("Monto total");


        List<Receta> recetas = new ArrayList<Receta>();
        if(recetaId == 0){
            recetas = recetasByProducto;
        } else {
            Receta recetaSeleccionada = recetaDAO.getById(recetaId);
            recetas.add(recetaSeleccionada);
        }


        // Parte por defecto en la fila 10.
        int i = 10;
        for (Receta receta : recetas) {
            for (ProductoReceta productoReceta : receta.getProductosAsociados()) {
                HSSFRow rowCreated = sheet.createRow(i);
                rowCreated.setHeight((short) 350);
                HSSFCell cellCreated1 = rowCreated.createCell(0);
                cellCreated1.setCellValue(fichaSeleccionada.getCodigoProducto());

                HSSFCell cellCreated2 = rowCreated.createCell(1);
                cellCreated2.setCellValue(fichaSeleccionada.getDescripcion());

                HSSFCell cellCreated3 = rowCreated.createCell(2);
                cellCreated3.setCellValue(receta.getCodigo());
                cellCreated3.setCellStyle(alignRight);

                HSSFCell cellCreated4 = rowCreated.createCell(3);
                cellCreated4.setCellValue(receta.getDescripcion());

                HSSFCell cellCreated5 = rowCreated.createCell(4);
                cellCreated5.setCellValue(receta.getCantidadBase());

                HSSFCell cellCreated6 = rowCreated.createCell(5);
                cellCreated6.setCellValue(productoReceta.getProducto().getCodigoProducto());

                HSSFCell cellCreated7 = rowCreated.createCell(6);
                cellCreated7.setCellValue(productoReceta.getProducto().getDescripcion());

                HSSFCell cellCreated8 = rowCreated.createCell(7);
                cellCreated8.setCellValue(productoReceta.getProducto().getUnidadMedida().getNombre());

                HSSFCell cellCreated9 = rowCreated.createCell(8);
                cellCreated9.setCellValue(productoReceta.getCantidad());

                HSSFCell cellCreated10 = rowCreated.createCell(9);
                cellCreated10.setCellValue(productoReceta.getValor());

                HSSFCell cellCreated11 = rowCreated.createCell(10);
                cellCreated11.setCellValue(productoReceta.getCantidad() * productoReceta.getValor());

                i++;
            }

        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/vnd.ms-excel");
        SimpleDateFormat sdf2 =  new SimpleDateFormat("ddMMyyyy");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"inf_recetas_" + sdf2.format(new Date()) + ".xls\"");

        workbook.write(externalContext.getResponseOutputStream());
        facesContext.responseComplete();
    }

    public FichaProducto getFichaSeleccionada() {
        return fichaSeleccionada;
    }

    public void setFichaSeleccionada(FichaProducto fichaSeleccionada) {
        this.fichaSeleccionada = fichaSeleccionada;
    }

    public FichasProductoLazyDataModel getProductos() {
        if ( productos == null )
            productos = new FichasProductoLazyDataModel(fichaProductoDAO, TipoFichaProducto.INVENTARIABLE);
        return productos;
    }

    public void setProductos(FichasProductoLazyDataModel productos) {
        this.productos = productos;
    }

    public List<Receta> getRecetasByProducto() {
        return recetasByProducto;
    }

    public void setRecetasByProducto(List<Receta> recetasByProducto) {
        this.recetasByProducto = recetasByProducto;
    }

    public Long getRecetaId() {
        return recetaId;
    }

    public void setRecetaId(Long recetaId) {
        this.recetaId = recetaId;
    }
}

