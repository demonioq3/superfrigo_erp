package cl.superfrigo.beans.bodega;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.*;
import cl.superfrigo.entity.bodega.*;
import cl.superfrigo.model.FichasProductoLazyDataModel;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class TomaInventarioBean extends BaseBean implements Serializable {

    /**
     * ID del proceso.
     * 1: Corresponde a toma de inventario.
     * 2: Corresponde a Ajustar toma de inventario.
     * 3: Corresponde a Informe para la toma de inventario.
     */
    private Long idProceso;

    /**
     * ID del tipo de toma inventario.
     * 1: Digitación de stock en forma manual.
     * 2: Captura en stock.
     */
    private Long idTipoTomaInventario;

    // Bodega ID
    private Long bodegaId;

    // Fecha seleccionada para la toma de inventario.
    private Date fecha;

    // Archivo excel subido para cargar en la tabla.
    private UploadedFile archivoExcelSubido;

    // Lista de productos lazy para agregar a la tabla.
    private FichasProductoLazyDataModel fichasProducto;

    // Productos asociados a una determinada toma de inventario
    private List<ProductoTomaInventario> productosTomaInventario;

    // Toma de inventario seleccionada para guardar o editar.
    private TomaInventario tomaInventario;

    /* Renders de fragments en la vista */
    private boolean mostrarPrimeraPantalla = true;
    private boolean mostrarPantallaTomaInventario = false;
    private boolean mostrarPantallaTablaTomaInventario = false;
    private boolean bloquearBotonGoPantallaTomaInventario = true;
    private boolean mostrarPantallaSubirExcel = false;
    private boolean mostrarAjustarTomaInventario = false;
    private boolean mostrarTablaAjustar = false;
    private boolean mostrarInforme = false;

    // Pantalla de ajustar
    private Date fechaAjuste;
    private Long bodegaAjusteId;
    private List<ProductoTomaInventario> productosParaAjustar;

    // Pantalla de informe
    private Date fechaInforme;
    private Long bodegaInformeId;
    private List<String> tiposInforme;

    /* EJB's de persistencia */
    @EJB private FichaProductoDAO fichaProductoDAO;
    @EJB private BodegaStockProductoDAO bodegaStockProductoDAO;
    @EJB private TomaInventarioDAO tomaInventarioDAO;
    @EJB private ProductoTomaInventarioDAO productotomaInventarioDAO;
    @EJB private BodegaDAO bodegaDAO;

    public void goPantallaTomaInventario(){
        if(this.idProceso.equals(1L)){
            mostrarPrimeraPantalla = false;
            mostrarPantallaTomaInventario = true;
            mostrarPantallaTablaTomaInventario = false;
            mostrarAjustarTomaInventario = false;
            mostrarTablaAjustar = false;
            mostrarInforme = false;
        } else if(this.idProceso.equals(2L)){
            mostrarPrimeraPantalla = false;
            mostrarPantallaTomaInventario = false;
            mostrarPantallaTablaTomaInventario = false;
            mostrarAjustarTomaInventario = true;
            mostrarTablaAjustar = false;
            mostrarInforme = false;
        } else if(this.idProceso.equals(3L)){
            mostrarPrimeraPantalla = false;
            mostrarPantallaTomaInventario = false;
            mostrarPantallaTablaTomaInventario = false;
            mostrarAjustarTomaInventario = false;
            mostrarTablaAjustar = false;
            mostrarInforme = true;
        }
    }

    /*
        Proceso ajax que revisa si seleccionaron un proceso.
     */
    public void chequearCambioProceso(){
        if(idProceso.equals(1L) || idProceso.equals(2L) || idProceso.equals(3L)){
            bloquearBotonGoPantallaTomaInventario = false;
        } else {
            bloquearBotonGoPantallaTomaInventario = true;
        }
    }

    public void chequearTipoTomaInventario(){
        if(idTipoTomaInventario.equals(1L)){
            mostrarPantallaTablaTomaInventario = true;
            mostrarPantallaSubirExcel = false;
        } else {
            mostrarPantallaTablaTomaInventario = false;
            mostrarPantallaSubirExcel = true;
        }
    }

    public void cargarTablaDesdeExcel(){
        if(bodegaId == null || bodegaId == 0L){
            showError("Error", "Debe ingresar bodega para crear una toma de inventario.");
            return;
        }

        if(fecha == null){
            showError("Error", "Debe ingresar fecha para crear una toma de inventario.");
            return;
        }

        FacesContext context = FacesContext.getCurrentInstance();

        if(this.archivoExcelSubido == null){
            showError("Error", "Debe subir un archivo excel para realizar la carga.");
            mostrarPantallaTablaTomaInventario = false;
            return;
        }

        InputStream file;
        HSSFWorkbook workbook = null;
        try {
            file = archivoExcelSubido.getInputstream();
            workbook = new HSSFWorkbook(file);
        } catch (IOException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error reading file" + e, null));
        }

        HSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        Calendar calendar = new GregorianCalendar();
        List<ProductoTomaInventario> listaProductosEncontrados = new ArrayList<ProductoTomaInventario>();
        List<String> listaProductosError = new ArrayList<String>();

        try {
            for(Row row : sheet) {
                ProductoTomaInventario fila = new ProductoTomaInventario();

                if(row.getRowNum()==0){
                    continue; //just skip the rows if row number is 0
                }

                boolean error = false;
                for(int cn=0; cn<row.getLastCellNum(); cn++) {

                    if(row.getRowNum()==0){
                        continue; //just skip the rows if row number is 0
                    }
                    // If the cell is missing from the file, generate a blank one
                    // (Works by specifying a MissingCellPolicy)
                    Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);

                    if(cn == 0){
                        String codigoProducto = getStringValue(cell);
                        FichaProducto fichaProducto = fichaProductoDAO.findByCodigoProducto(codigoProducto);
                        if(fichaProducto != null){
                            fila.setProductoId(fichaProducto.getId());
                            fila.setProducto(fichaProducto);
                        } else {
                            listaProductosError.add(getStringValue(cell));
                            error = true;
                        }
                    } else if(cn == 1){
                        fila.setCantidadContada(getNumericValue(cell));
                    }
                }

                if(error != true){
                    listaProductosEncontrados.add(fila);
                }

            }
        } catch (IllegalStateException excepcion){
            showError("Error", "El formato del excel ingresado es inválido. Favor intente con un formato válido.");
            return;
        }


        productosTomaInventario = listaProductosEncontrados;

        // Mostrando los con error.
        if(listaProductosError.size() > 0){
            String error = "Los códigos de producto: ";
            for (String s : listaProductosError) {
                error += s + " ,";
            }
            error += " no están registrados en el sistema. Estos productos no fueron agregados a la tabla.";

            showWarn("Aviso", error);
        }

        // Procedemos a hacer el calculo por cada producto ingresado.
        for (ProductoTomaInventario productoTomaInventario : productosTomaInventario) {
            BodegaStockProducto bodegaStockProducto = bodegaStockProductoDAO.findByFichaProductoIdAndBodegaId(productoTomaInventario.getProductoId(), bodegaId);

            productoTomaInventario.setCantidadSistema(bodegaStockProducto.getCantidad());
            if(productoTomaInventario.getCantidadContada() - productoTomaInventario.getCantidadSistema() < 0){
                productoTomaInventario.setResto(productoTomaInventario.getCantidadContada() - productoTomaInventario.getCantidadSistema() * -1);
            } else {
                productoTomaInventario.setResto(productoTomaInventario.getCantidadContada() - productoTomaInventario.getCantidadSistema());
            }
        }

        mostrarPantallaTablaTomaInventario = true;
    }

    private String getStringValue(Cell cell) {
        return cell.getStringCellValue();
    }

    private double getNumericValue(Cell cell) {
        return cell.getNumericCellValue();
    }

    public void agregarProducto(FichaProducto fichaProducto){
        if(bodegaId == null || bodegaId == 0L){
            showError("Error", "Debe seleccionar una bodega para agregar un producto.");
            return;
        }

        if(fecha == null){
            showError("Error", "Debe ingrear una fecha.");
            return;
        }

        if(productosTomaInventario == null){
            productosTomaInventario = new ArrayList<ProductoTomaInventario>();
        }

        ProductoTomaInventario nuevoProducto = new ProductoTomaInventario();
        nuevoProducto.setProductoId(fichaProducto.getId());
        nuevoProducto.setProducto(fichaProducto);

        BodegaStockProducto bodegaStockProducto = bodegaStockProductoDAO.findByFichaProductoIdAndBodegaId(fichaProducto.getId(), bodegaId);
        nuevoProducto.setCantidadSistema(bodegaStockProducto.getCantidad());
        if(nuevoProducto.getCantidadContada() - nuevoProducto.getCantidadSistema() < 0){
            nuevoProducto.setResto((long) nuevoProducto.getCantidadContada() - nuevoProducto.getCantidadSistema() * -1);
        } else {
            nuevoProducto.setResto((long) nuevoProducto.getCantidadContada() - nuevoProducto.getCantidadSistema());
        }

        nuevoProducto.setCantidadContada(0L);

        productosTomaInventario.add(nuevoProducto);

        // Move to bottom
        RequestContext.getCurrentInstance().scrollTo("section_productos");
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
    }

    public void calcularResto(ProductoTomaInventario productoTomaInventario){
        if(productoTomaInventario.getCantidadContada() - productoTomaInventario.getCantidadSistema() < 0){
            productoTomaInventario.setResto((long) productoTomaInventario.getCantidadContada() - productoTomaInventario.getCantidadSistema() * -1);
        } else {
            productoTomaInventario.setResto((long) productoTomaInventario.getCantidadContada() - productoTomaInventario.getCantidadSistema());
        }
    }

    public void borrar(ProductoTomaInventario productoTomaInventario){
        productosTomaInventario.remove(productoTomaInventario);
    }

    public void guardarTomaInventario(){
        if(estaCreada()){
            showError("Error", "La toma de inventario asociada a la fecha y la bodega ya está creada.");
            return;
        }

        // Creando la nueva toma de inventario.
        tomaInventario = new TomaInventario();

        this.tomaInventario.setBodegaId(bodegaId);
        this.tomaInventario.setFecha(fecha);

        TomaInventario tomaInventarioCreada = tomaInventarioDAO.create(tomaInventario);

        for (ProductoTomaInventario productoTomaInventario : productosTomaInventario) {
            productoTomaInventario.setTomaInventarioId(tomaInventarioCreada.getId());
            productotomaInventarioDAO.create(productoTomaInventario);
        }

        showInfo("Aviso", "Se creó correctamente la toma de inventario id: " + this.tomaInventario.getId());
    }

    private boolean estaCreada() {
        TomaInventario tomaInventario = tomaInventarioDAO.findByBodegaIdAndFecha(bodegaId, fecha);

        if(tomaInventario != null){
            return true;
        }

        return false;
    }

    public void limpiar(){
        bodegaId = null;
        fecha = null;
        idTipoTomaInventario = null;
        productosTomaInventario = new ArrayList<ProductoTomaInventario>();

        mostrarPantallaSubirExcel = false;
        mostrarPantallaTablaTomaInventario = false;

    }

    public void buscarParaAjustar(){
        if(bodegaAjusteId == null || bodegaAjusteId == 0){
            showError("Error", "Debe ingresar una bodega para realizar una búsqueda de toma de inventario.");
            return;
        }

        if(fechaAjuste == null ){
            showError("Error", "Debe ingresar una fecha para realizar una búsqueda de toma de inventario.");
            return;
        }

        TomaInventario tomaInventario = tomaInventarioDAO.findByBodegaIdAndFecha(bodegaAjusteId, fechaAjuste);
        mostrarTablaAjustar = true;

        if(tomaInventario != null){
            productosParaAjustar = tomaInventario.getProductosAsociados();
        } else {
            showError("Error", "No existe una toma de inventario con los parámetros buscados.");
            return;
        }
    }

    public void generarInforme() throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        // Default settings width columns.
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 12000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);

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
        cell.setCellValue("Informe Toma de inventario");
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
        cell5.setCellValue("                      INFORME TOMA DE INVENTARIO");
        cell5.setCellStyle(boldStyle);

        // Periodo.
        HSSFRow row5 = sheet.createRow(5);
        row5.setHeight((short) 350);
        HSSFCell cell6 = row5.createCell(0);
        cell6.setCellValue("Bodega: ");
        cell6.setCellStyle(alignRight);
        HSSFCell cell7 = row5.createCell(1);
        Bodega bodega = bodegaDAO.findById(bodegaInformeId);
        cell7.setCellValue("" + bodega.getCodigo() + " - " + bodega.getDescripcion() );


        HSSFRow row6 = sheet.createRow(6);
        row6.setHeight((short) 350);
        HSSFCell cell8 = row6.createCell(0);
        cell8.setCellValue("Stock al: ");
        HSSFCell cellFechaHasta = row6.createCell(1);

        String tipos = "";
        if(tiposInforme.contains("1") && tiposInforme.contains("2")){
            cellFechaHasta.setCellValue("Imprimir el stock y considerar todos los productos que han tenido movimiento en la bodega");
        } else if(tiposInforme.contains("1")){
            cellFechaHasta.setCellValue("Imprimir el stock");
        } else if(tiposInforme.contains("2")){
            cellFechaHasta.setCellValue("Considerar todos los productos que han tenido movimiento en la bodega");
        }
        cell8.setCellStyle(alignRight);

        // Inicio de tabla
        // Header tabla.
        HSSFRow row9 = sheet.createRow(9);
        row9.setHeight((short) 600);

        HSSFCell cell9 = row9.createCell(0);
        cell9.setCellStyle(headersTablaStyle);
        cell9.setCellValue("Codigo");
        HSSFCell cell10 = row9.createCell(1);
        cell10.setCellStyle(headersTablaStyle);
        cell10.setCellValue("Descripción");
        HSSFCell cell11 = row9.createCell(2);
        cell11.setCellStyle(headersTablaStyle);
        cell11.setCellValue("U. medida");
        HSSFCell cell12 = row9.createCell(3);
        cell12.setCellStyle(headersTablaStyle);
        cell12.setCellValue("Stock sistema");


        // Verificando filtros de la vista.
        List<BodegaStockProducto> bodegaStockProductos = bodegaStockProductoDAO.findAll();

        // Parte por defecto en la fila 10.
        int i = 10;

        if(tiposInforme.size() == 1){
            if(tiposInforme.get(0).equals("Imprimir el stock")){
                for (BodegaStockProducto bodegaStockProducto : bodegaStockProductos) {
                    if(bodegaStockProducto.getCantidad() == 0){
                        bodegaStockProductos.remove(bodegaStockProducto);
                    }
                }
            }
        }

        for (BodegaStockProducto bodegaStockProducto: bodegaStockProductos) {

            HSSFRow rowCreated = sheet.createRow(i);
            rowCreated.setHeight((short) 350);
            HSSFCell cellCreated1 = rowCreated.createCell(0);
            cellCreated1.setCellValue(bodegaStockProducto.getFichaProducto().getCodigoProducto());

            HSSFCell cellCreated2 = rowCreated.createCell(1);
            cellCreated2.setCellValue(bodegaStockProducto.getFichaProducto().getDescripcion());

            HSSFCell cellCreated3 = rowCreated.createCell(2);
            cellCreated3.setCellValue(bodegaStockProducto.getFichaProducto().getUnidadMedida().getNombre());
            cellCreated3.setCellStyle(alignRight);

            HSSFCell cellCreated4 = rowCreated.createCell(3);
            cellCreated4.setCellValue(bodegaStockProducto.getCantidad());
            cellCreated4.setCellStyle(alignRight);

            i++;
        }



        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/vnd.ms-excel");
        SimpleDateFormat sdf2 =  new SimpleDateFormat("ddMMyyyy");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"inf_tomainventario_" + sdf2.format(new Date()) + ".xls\"");

        workbook.write(externalContext.getResponseOutputStream());
        facesContext.responseComplete();
    }

    public void ajustar(){
        System.out.println("Se comienza con el ajuste.");
    }

    public Date getFechaAjuste() {
        return fechaAjuste;
    }

    public void setFechaAjuste(Date fechaAjuste) {
        this.fechaAjuste = fechaAjuste;
    }

    public Long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(Long idProceso) {
        this.idProceso = idProceso;
    }

    public boolean isMostrarPrimeraPantalla() {
        return mostrarPrimeraPantalla;
    }

    public boolean isMostrarPantallaTomaInventario() {
        return mostrarPantallaTomaInventario;
    }

    public boolean isMostrarPantallaTablaTomaInventario() {
        return mostrarPantallaTablaTomaInventario;
    }

    public boolean isBloquearBotonGoPantallaTomaInventario() {
        return bloquearBotonGoPantallaTomaInventario;
    }

    public boolean isMostrarPantallaSubirExcel() {
        return mostrarPantallaSubirExcel;
    }

    public Long getIdTipoTomaInventario() {
        return idTipoTomaInventario;
    }

    public void setIdTipoTomaInventario(Long idTipoTomaInventario) {
        this.idTipoTomaInventario = idTipoTomaInventario;
    }

    public UploadedFile getArchivoExcelSubido() {
        return archivoExcelSubido;
    }

    public void setArchivoExcelSubido(UploadedFile archivoExcelSubido) {
        this.archivoExcelSubido = archivoExcelSubido;
    }

    public FichasProductoLazyDataModel getFichasProducto() {
        if ( fichasProducto == null )
            fichasProducto = new FichasProductoLazyDataModel(fichaProductoDAO, TipoFichaProducto.INVENTARIABLE);
        return fichasProducto;
    }

    public void setFichasProducto(FichasProductoLazyDataModel fichasProducto) {
        this.fichasProducto = fichasProducto;
    }

    public List<ProductoTomaInventario> getProductosTomaInventario() {
        return productosTomaInventario;
    }

    public void setProductosTomaInventario(List<ProductoTomaInventario> productosTomaInventario) {
        this.productosTomaInventario = productosTomaInventario;
    }

    public Long getBodegaId() {
        return bodegaId;
    }

    public void setBodegaId(Long bodegaId) {
        this.bodegaId = bodegaId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isMostrarAjustarTomaInventario() {
        return mostrarAjustarTomaInventario;
    }

    public Long getBodegaAjusteId() {
        return bodegaAjusteId;
    }

    public void setBodegaAjusteId(Long bodegaAjusteId) {
        this.bodegaAjusteId = bodegaAjusteId;
    }

    public List<ProductoTomaInventario> getProductosParaAjustar() {
        return productosParaAjustar;
    }

    public void setProductosParaAjustar(List<ProductoTomaInventario> productosParaAjustar) {
        this.productosParaAjustar = productosParaAjustar;
    }

    public boolean isMostrarTablaAjustar() {
        return mostrarTablaAjustar;
    }

    public void setMostrarTablaAjustar(boolean mostrarTablaAjustar) {
        this.mostrarTablaAjustar = mostrarTablaAjustar;
    }

    public Date getFechaInforme() {
        return fechaInforme;
    }

    public void setFechaInforme(Date fechaInforme) {
        this.fechaInforme = fechaInforme;
    }

    public Long getBodegaInformeId() {
        return bodegaInformeId;
    }

    public void setBodegaInformeId(Long bodegaInformeId) {
        this.bodegaInformeId = bodegaInformeId;
    }

    public boolean isMostrarInforme() {
        return mostrarInforme;
    }

    public List<String> getTiposInforme() {
        return tiposInforme;
    }

    public void setTiposInforme(List<String> tiposInforme) {
        this.tiposInforme = tiposInforme;
    }
}

