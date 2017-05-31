package cl.superfrigo.beans.integracion;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.*;
import cl.superfrigo.entity.Comuna;
import cl.superfrigo.entity.FichaAuxiliar;
import cl.superfrigo.entity.Region;
import cl.superfrigo.entity.TipoFichaAuxiliar;
import cl.superfrigo.entity.bodega.*;
import cl.superfrigo.entity.comercial.EstadoCotizacion;
import cl.superfrigo.entity.comercial.EstadoOrdenTrabajo;
import cl.superfrigo.entity.comercial.OrdenDeTrabajo;
import cl.superfrigo.entity.comercial.ProductoOrdenTrabajo;
import cl.superfrigo.entity.registros_hh.RegistroHH;
import cl.superfrigo.jms.ManagerRegistroEntrada;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Row;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by asgco on 19-Mar-16.
 */
@ManagedBean(name = "mainBean")
@ViewScoped
public class Main extends BaseBean implements Serializable {

    public boolean habilitado = true;
    @EJB private FichaProductoDAO fichaProductoDAO;
    @EJB private BodegaDAO bodegaDAO;
    @EJB private GuiaEntradaDAO guiaEntradaDAO;
    @EJB private EntradaProductoCantidadDAO entradaProductoCantidadDAO;
    @EJB private BodegaStockProductoDAO bodegaStockProductoDAO;
    @EJB private TipoFichaAuxiliarDAO tipoFichaAuxiliarDAO;
    @EJB private RegionDAO regionDAO;
    @EJB private ComunaDAO comunaDAO;
    @EJB private FichaAuxiliarDAO fichaAuxiliarDAO;
    @EJB private SubGrupoDAO subGrupoDAO;
    @EJB private UnidadMedidaDAO unidadMedidaDAO;
    @EJB private GrupoDAO grupoDAO;

    @EJB
    private ManagerRegistroEntrada register;

    public void extractFromExcelBodega01() {
        if(habilitado == false){
            return;
        }

        InputStream file;
        HSSFWorkbook workbook = null;
        try {
            //File archivo = new File("/Users/agustinsantiago/SuperfrigoFiles/Stock5.xls");
            File archivo = new File("/home/superfrigo/Stock5.xls");
            InputStream stream = new FileInputStream(archivo);
            workbook = new HSSFWorkbook(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        Calendar calendar = new GregorianCalendar();
        GuiaEntrada guiaEntrada = new GuiaEntrada();
        guiaEntrada.setBodegaId(5L);
        List<EntradaProductoCantidad> productos = new ArrayList<EntradaProductoCantidad>();

        for(Row row : sheet) {

            if(productos.size() == 1275){
                guiaEntrada.setFecha(new Date());
                GuiaEntrada guiaCreada = guiaEntradaDAO.create(guiaEntrada);

                for (EntradaProductoCantidad producto : productos) {
                    producto.setGuiaEntradaId(guiaCreada.getId());
                    entradaProductoCantidadDAO.create(producto);

                    BodegaStockProducto bodegaStockProducto = bodegaStockProductoDAO.findByFichaProductoIdAndBodegaId(producto.getFichaProductoId(), guiaCreada.getBodegaId());
                    bodegaStockProducto.setCantidad(producto.getCantidad());

                    bodegaStockProductoDAO.update(bodegaStockProducto);
                }

                GuiaEntrada guia = guiaEntradaDAO.findById(guiaCreada.getId());
                register.record(guia);

                return;
            }

            EntradaProductoCantidad fila = new EntradaProductoCantidad();

            for(int cn=0; cn<row.getLastCellNum(); cn++) {

                // If the cell is missing from the file, generate a blank one
                // (Works by specifying a MissingCellPolicy)
                Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);

                if(cn == 0){
                    Double val = Double.parseDouble("" +cell.getNumericCellValue());
                    BigDecimal bd = new BigDecimal(val.toString());
                    long lonVal = bd.longValue();
                    FichaProducto fichaProducto = fichaProductoDAO.findByCodigoProducto("" + lonVal);
                    try {
                        fila.setFichaProductoId(fichaProducto.getId());
                    } catch (NullPointerException exception){
                        exception.printStackTrace();
                    }

                } else if(cn == 1){

                } else if(cn == 2){

                } else if(cn == 3){
                    String precioUnitario = cell.getStringCellValue();
                    precioUnitario = precioUnitario.replace("$", "");
                    precioUnitario = precioUnitario.replace(" ", "");
                    precioUnitario = precioUnitario.replace(".", "");
                    precioUnitario = precioUnitario.replace(",", ".");
                    if(precioUnitario.equals("-")){
                        fila.setPrecioUnitario(0D);
                    } else {
                        fila.setPrecioUnitario(Double.parseDouble(precioUnitario));
                    }
                } else if(cn == 4){
                    Double val = cell.getNumericCellValue();
                    System.out.println(val);
                    fila.setCantidad(val.doubleValue());
                } else if(cn == 5){
                    String precioTotal = cell.getStringCellValue();
                    precioTotal = precioTotal.replace("$", "");
                    precioTotal = precioTotal.replace(" ", "");
                    precioTotal = precioTotal.replace(".", "");
                    precioTotal = precioTotal.replace(",", ".");
                    if(precioTotal.equals("-")){
                        fila.setPrecioTotal(0D);
                    } else {
                        fila.setPrecioTotal(Double.parseDouble(precioTotal));
                    }

                } else if(cn == 6){

                } else if(cn == 7){

                } else if(cn == 8){

                } else if(cn == 9){

                }
            }

            productos.add(fila);
        }

        System.out.println("tas");

    }

    public void extractFromExcelBodega02() {
        if(habilitado == false){
            return;
        }

        InputStream file;
        HSSFWorkbook workbook = null;
        try {

            File archivo = new File("/home/superfrigo/Stock6.xls");
            //File archivo = new File("/Users/agustinsantiago/Documents/Stock6.xls");
            InputStream stream = new FileInputStream(archivo);
            workbook = new HSSFWorkbook(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        Calendar calendar = new GregorianCalendar();
        GuiaEntrada guiaEntrada = new GuiaEntrada();
        guiaEntrada.setBodegaId(6L);
        List<EntradaProductoCantidad> productos = new ArrayList<EntradaProductoCantidad>();

        for(Row row : sheet) {

            if(productos.size() == 39){
                guiaEntrada.setFecha(new Date());
                GuiaEntrada guiaCreada = guiaEntradaDAO.create(guiaEntrada);

                for (EntradaProductoCantidad producto : productos) {
                    producto.setGuiaEntradaId(guiaCreada.getId());
                    entradaProductoCantidadDAO.create(producto);

                    BodegaStockProducto bodegaStockProducto = bodegaStockProductoDAO.findByFichaProductoIdAndBodegaId(producto.getFichaProductoId(), guiaCreada.getBodegaId());
                    bodegaStockProducto.setCantidad(producto.getCantidad());

                    bodegaStockProductoDAO.update(bodegaStockProducto);
                }

                GuiaEntrada guia = guiaEntradaDAO.findById(guiaCreada.getId());
                register.record(guia);

                return;
            }

            EntradaProductoCantidad fila = new EntradaProductoCantidad();

            for(int cn=0; cn<row.getLastCellNum(); cn++) {

                // If the cell is missing from the file, generate a blank one
                // (Works by specifying a MissingCellPolicy)
                Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);

                if(cn == 0){
                    Double val = Double.parseDouble("" +cell.getNumericCellValue());
                    BigDecimal bd = new BigDecimal(val.toString());
                    long lonVal = bd.longValue();
                    FichaProducto fichaProducto = fichaProductoDAO.findByCodigoProducto("" + lonVal);
                    try {
                        fila.setFichaProductoId(fichaProducto.getId());
                    } catch (NullPointerException exception){
                        exception.printStackTrace();
                    }

                } else if(cn == 1){

                } else if(cn == 2){

                } else if(cn == 3){
                    String precioUnitario = cell.getStringCellValue();
                    precioUnitario = precioUnitario.replace("$", "");
                    precioUnitario = precioUnitario.replace(" ", "");
                    precioUnitario = precioUnitario.replace(".", "");
                    precioUnitario = precioUnitario.replace(",", ".");
                    if(precioUnitario.equals("-")){
                        fila.setPrecioUnitario(0D);
                    } else {
                        fila.setPrecioUnitario(Double.parseDouble(precioUnitario));
                    }
                } else if(cn == 4){
                    Double val = cell.getNumericCellValue();
                    System.out.println(val);
                    fila.setCantidad(val.doubleValue());
                } else if(cn == 5){
                    String precioTotal = cell.getStringCellValue();
                    precioTotal = precioTotal.replace("$", "");
                    precioTotal = precioTotal.replace(" ", "");
                    precioTotal = precioTotal.replace(".", "");
                    precioTotal = precioTotal.replace(",", ".");
                    if(precioTotal.equals("-")){
                        fila.setPrecioTotal(0D);
                    } else {
                        fila.setPrecioTotal(Double.parseDouble(precioTotal));
                    }

                } else if(cn == 6){

                } else if(cn == 7){

                } else if(cn == 8){

                } else if(cn == 9){

                }
            }

            productos.add(fila);
        }


    }

    public void extractFromExcelBodega03() {
        if(habilitado == false){
            return;
        }

        InputStream file;
        HSSFWorkbook workbook = null;
        try {

            File archivo = new File("/home/superfrigo/Stock7.xls");
            //File archivo = new File("/Users/agustinsantiago/Documents/Stock7.xls");
            InputStream stream = new FileInputStream(archivo);
            workbook = new HSSFWorkbook(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        Calendar calendar = new GregorianCalendar();
        GuiaEntrada guiaEntrada = new GuiaEntrada();
        guiaEntrada.setBodegaId(7L);
        List<EntradaProductoCantidad> productos = new ArrayList<EntradaProductoCantidad>();

        for(Row row : sheet) {

            if(productos.size() == 120){
                guiaEntrada.setFecha(new Date());
                GuiaEntrada guiaCreada = guiaEntradaDAO.create(guiaEntrada);

                for (EntradaProductoCantidad producto : productos) {
                    producto.setGuiaEntradaId(guiaCreada.getId());
                    entradaProductoCantidadDAO.create(producto);

                    BodegaStockProducto bodegaStockProducto = bodegaStockProductoDAO.findByFichaProductoIdAndBodegaId(producto.getFichaProductoId(), guiaCreada.getBodegaId());
                    bodegaStockProducto.setCantidad(producto.getCantidad());

                    bodegaStockProductoDAO.update(bodegaStockProducto);
                }

                GuiaEntrada guia = guiaEntradaDAO.findById(guiaCreada.getId());
                register.record(guia);

                return;
            }

            EntradaProductoCantidad fila = new EntradaProductoCantidad();

            for(int cn=0; cn<row.getLastCellNum(); cn++) {

                // If the cell is missing from the file, generate a blank one
                // (Works by specifying a MissingCellPolicy)
                Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);

                if(cn == 0){
                    Double val = Double.parseDouble("" +cell.getNumericCellValue());
                    BigDecimal bd = new BigDecimal(val.toString());
                    long lonVal = bd.longValue();
                    FichaProducto fichaProducto = fichaProductoDAO.findByCodigoProducto("" + lonVal);
                    try {
                        fila.setFichaProductoId(fichaProducto.getId());
                    } catch (NullPointerException exception){
                        exception.printStackTrace();
                    }

                } else if(cn == 1){

                } else if(cn == 2){

                } else if(cn == 3){
                    String precioUnitario = cell.getStringCellValue();
                    precioUnitario = precioUnitario.replace("$", "");
                    precioUnitario = precioUnitario.replace(" ", "");
                    precioUnitario = precioUnitario.replace(".", "");
                    precioUnitario = precioUnitario.replace(",", ".");
                    if(precioUnitario.equals("-")){
                        fila.setPrecioUnitario(0D);
                    } else {
                        fila.setPrecioUnitario(Double.parseDouble(precioUnitario));
                    }
                } else if(cn == 4){
                    Double val = cell.getNumericCellValue();
                    System.out.println(val);
                    fila.setCantidad(val.doubleValue());
                } else if(cn == 5){
                    String precioTotal = cell.getStringCellValue();
                    precioTotal = precioTotal.replace("$", "");
                    precioTotal = precioTotal.replace(" ", "");
                    precioTotal = precioTotal.replace(".", "");
                    precioTotal = precioTotal.replace(",", ".");
                    if(precioTotal.equals("-")){
                        fila.setPrecioTotal(0D);
                    } else {
                        fila.setPrecioTotal(Double.parseDouble(precioTotal));
                    }

                } else if(cn == 6){

                } else if(cn == 7){

                } else if(cn == 8){

                } else if(cn == 9){

                }
            }

            productos.add(fila);
        }


    }

    public void extractFromExcelBodega04() {
        if(habilitado == false){
            return;
        }

        InputStream file;
        HSSFWorkbook workbook = null;
        try {

            File archivo = new File("/home/superfrigo/Stock8.xls");
            //File archivo = new File("/Users/agustinsantiago/Documents/Stock8.xls");
            InputStream stream = new FileInputStream(archivo);
            workbook = new HSSFWorkbook(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        Calendar calendar = new GregorianCalendar();
        GuiaEntrada guiaEntrada = new GuiaEntrada();
        guiaEntrada.setBodegaId(8L);
        List<EntradaProductoCantidad> productos = new ArrayList<EntradaProductoCantidad>();

        for(Row row : sheet) {

            if(productos.size() == 18){
                guiaEntrada.setFecha(new Date());
                GuiaEntrada guiaCreada = guiaEntradaDAO.create(guiaEntrada);

                for (EntradaProductoCantidad producto : productos) {
                    producto.setGuiaEntradaId(guiaCreada.getId());
                    entradaProductoCantidadDAO.create(producto);

                    BodegaStockProducto bodegaStockProducto = bodegaStockProductoDAO.findByFichaProductoIdAndBodegaId(producto.getFichaProductoId(), guiaCreada.getBodegaId());
                    bodegaStockProducto.setCantidad(producto.getCantidad());

                    bodegaStockProductoDAO.update(bodegaStockProducto);
                }

                GuiaEntrada guia = guiaEntradaDAO.findById(guiaCreada.getId());
                register.record(guia);

                return;
            }

            EntradaProductoCantidad fila = new EntradaProductoCantidad();

            for(int cn=0; cn<row.getLastCellNum(); cn++) {

                // If the cell is missing from the file, generate a blank one
                // (Works by specifying a MissingCellPolicy)
                Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);

                if(cn == 0){
                    Double val = Double.parseDouble("" +cell.getNumericCellValue());
                    BigDecimal bd = new BigDecimal(val.toString());
                    long lonVal = bd.longValue();
                    FichaProducto fichaProducto = fichaProductoDAO.findByCodigoProducto("" + lonVal);
                    try {
                        fila.setFichaProductoId(fichaProducto.getId());
                    } catch (NullPointerException exception){
                        exception.printStackTrace();
                    }

                } else if(cn == 1){

                } else if(cn == 2){

                } else if(cn == 3){
                    String precioUnitario = cell.getStringCellValue();
                    precioUnitario = precioUnitario.replace("$", "");
                    precioUnitario = precioUnitario.replace(" ", "");
                    precioUnitario = precioUnitario.replace(".", "");
                    precioUnitario = precioUnitario.replace(",", ".");
                    if(precioUnitario.equals("-")){
                        fila.setPrecioUnitario(0D);
                    } else {
                        fila.setPrecioUnitario(Double.parseDouble(precioUnitario));
                    }
                } else if(cn == 4){
                    Double val = cell.getNumericCellValue();
                    System.out.println(val);
                    fila.setCantidad(val.doubleValue());
                } else if(cn == 5){
                    String precioTotal = cell.getStringCellValue();
                    precioTotal = precioTotal.replace("$", "");
                    precioTotal = precioTotal.replace(" ", "");
                    precioTotal = precioTotal.replace(".", "");
                    precioTotal = precioTotal.replace(",", ".");
                    if(precioTotal.equals("-")){
                        fila.setPrecioTotal(0D);
                    } else {
                        fila.setPrecioTotal(Double.parseDouble(precioTotal));
                    }

                } else if(cn == 6){

                } else if(cn == 7){

                } else if(cn == 8){

                } else if(cn == 9){

                }
            }

            productos.add(fila);
        }


    }

    public void extractFichas() {
        if(habilitado == false){
            return;
        }

        InputStream file;
        HSSFWorkbook workbook = null;
        try {

            //File archivo = new File("/home/superfrigo/Fichas.xls");
            File archivo = new File("/Users/agustinsantiago/SuperfrigoFiles/Fichas.xls");
            InputStream stream = new FileInputStream(archivo);
            workbook = new HSSFWorkbook(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        Calendar calendar = new GregorianCalendar();
        GuiaEntrada guiaEntrada = new GuiaEntrada();
        guiaEntrada.setBodegaId(8L);
        List<EntradaProductoCantidad> productos = new ArrayList<EntradaProductoCantidad>();

        for(Row row : sheet) {

            FichaAuxiliar fichaAuxiliar = new FichaAuxiliar();
            List<TipoFichaAuxiliar> tipos = new ArrayList<TipoFichaAuxiliar>();

            for(int cn=0; cn<row.getLastCellNum(); cn++) {

                // If the cell is missing from the file, generate a blank one
                // (Works by specifying a MissingCellPolicy)
                Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);

                try {
                    if(cn == 0){
                        fichaAuxiliar.setNombreRazonSocial(cell.getStringCellValue());
                    } else if(cn == 1){
                        if (cell.getCellType() == Cell.CELL_TYPE_STRING)
                            fichaAuxiliar.setRut(cell.getStringCellValue());
                        else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
                            fichaAuxiliar.setRut(("" + cell.getNumericCellValue()).replace("E7", "").replace(".", ""));
                        else if (cell.getCellType() == Cell.CELL_TYPE_BLANK || cell.getCellType() == Cell.CELL_TYPE_ERROR || cell == null)
                            fichaAuxiliar.setRut("");

                    } else if(cn == 2){
                        fichaAuxiliar.setCalle(cell.getStringCellValue());
                    } else if(cn == 3){
                        if(cell.getStringCellValue().equals("Si")) {
                            TipoFichaAuxiliar tipoCliente = tipoFichaAuxiliarDAO.getById(TipoFichaAuxiliar.CLIENTE);
                            tipos.add(tipoCliente);
                        }
                    } else if(cn == 4){
                        if(cell.getStringCellValue().equals("Si")) {
                            TipoFichaAuxiliar tipoProveedor = tipoFichaAuxiliarDAO.getById(TipoFichaAuxiliar.PROVEEDOR);
                            tipos.add(tipoProveedor);
                        }
                    } else if(cn == 11){
                        double asd = cell.getNumericCellValue();
                        Region region = regionDAO.findById((long)asd);
                        fichaAuxiliar.setRegionId(region.getId());
                    } else if(cn == 12){
                        double asd = cell.getNumericCellValue();
                        Comuna comuna = comunaDAO.findById((long)asd);
                        fichaAuxiliar.setComunaId(comuna.getId());
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

            }

            fichaAuxiliar.setTipos(tipos);
            FichaAuxiliar fichaCreada = fichaAuxiliarDAO.create(fichaAuxiliar);
            System.out.println("" + fichaCreada.getId());

        }

    }

    public void extractGruposYSubgrupos() {
        if(habilitado == false){
            return;
        }

        InputStream file;
        HSSFWorkbook workbook = null;
        try {

            //File archivo = new File("/home/superfrigo/Fichas.xls");
            File archivo = new File("/Users/agustinsantiago/SuperfrigoFiles/grupos_subgrupos.xls");
            InputStream stream = new FileInputStream(archivo);
            workbook = new HSSFWorkbook(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        Calendar calendar = new GregorianCalendar();

        for(Row row : sheet) {

            SubGrupo subGrupo = new SubGrupo();

            if(row.getRowNum() == 0){
                continue;
            }

            for(int cn=0; cn<row.getLastCellNum(); cn++) {

                // If the cell is missing from the file, generate a blank one
                // (Works by specifying a MissingCellPolicy)
                Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);

                try {
                    if(cn == 0){
                        subGrupo.setCodigo(cell.getStringCellValue());
                    } else if(cn == 1){
                        subGrupo.setNombreSubGrupo(cell.getStringCellValue());
                    } else if(cn == 2){
                        subGrupo.setPadreId((long) cell.getNumericCellValue());
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

            }

            SubGrupo subGrupoCreado = subGrupoDAO.create(subGrupo);
            System.out.println("" + subGrupo.getId());

        }

    }

    public void extractProductos() {
        if(habilitado == false){
            return;
        }

        InputStream file;
        HSSFWorkbook workbook = null;
        try {

            //File archivo = new File("/home/superfrigo/Fichas.xls");
            File archivo = new File("/Users/agustinsantiago/SuperfrigoFiles/productos.xls");
            InputStream stream = new FileInputStream(archivo);
            workbook = new HSSFWorkbook(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        Calendar calendar = new GregorianCalendar();

        for(Row row : sheet) {

            FichaProducto fichaProducto = new FichaProducto();

            if(row.getRowNum() == 0){
                continue;
            }

            for(int cn=0; cn<row.getLastCellNum(); cn++) {

                // If the cell is missing from the file, generate a blank one
                // (Works by specifying a MissingCellPolicy)
                Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);

                try {
                    if(cn == 0){
                        fichaProducto.setCodigoProducto(cell.getStringCellValue());
                    } else if(cn == 1){
                        fichaProducto.setDescripcion(cell.getStringCellValue());
                    } else if(cn == 3){
                        if(cell.getStringCellValue().equals("si")){
                            fichaProducto.setTipoFichaProductoId(1L);
                        } else {
                            fichaProducto.setTipoFichaProductoId(2L);
                        }
                    } else if(cn == 4){
                        if(cell.getStringCellValue().equals("si")){
                            fichaProducto.setManejaImpuesto("SI");
                        } else {
                            fichaProducto.setManejaImpuesto("NO");
                        }
                    } else if(cn == 5){
                        if(cell.getStringCellValue().equals("si")){
                            fichaProducto.setCompra("SI");
                        } else {
                            fichaProducto.setCompra("NO");
                        }
                    } else if(cn == 6){
                        if(cell.getStringCellValue().equals("si")){
                            fichaProducto.setVenta("SI");
                        } else {
                            fichaProducto.setVenta("NO");
                        }
                    } else if(cn == 7){
                        if(cell.getStringCellValue().equals("Puertas")){
                            fichaProducto.setTipoFormularioId(3L);
                        } else if(cell.getStringCellValue().equals("sin tabla")){
                            fichaProducto.setTipoFormularioId(5L);
                        } else if(cell.getStringCellValue().equals("Paneles")){
                            fichaProducto.setTipoFormularioId(1L);
                        }
                    } else if(cn == 8){
                        UnidadMedida unidadMedida = unidadMedidaDAO.findByCodigo(cell.getStringCellValue());
                        fichaProducto.setUnidadMedidaId(unidadMedida.getId());
                    } else if(cn == 9){
                        Grupo grupo = grupoDAO.findByCodigo(cell.getStringCellValue());
                        fichaProducto.setGrupoId(grupo.getId());
                    } else if(cn == 10){
                        SubGrupo subGrupo = subGrupoDAO.findByCodigo(cell.getStringCellValue());
                        fichaProducto.setSubGrupoId(subGrupo.getId());
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

            }

            FichaProducto fichaCreada = fichaProductoDAO.create(fichaProducto);
            System.out.println("" + fichaCreada.getId());

        }

    }

    /*
    public void generateMetlife() throws IOException {
        InputStream file;
        HSSFWorkbook workbook = null;
        try {

            //File archivo = new File("/home/superfrigo/Fichas.xls");
            File archivo = new File("/Users/agustinsantiago/SuperfrigoFiles/metlife.xls");
            InputStream stream = new FileInputStream(archivo);
            workbook = new HSSFWorkbook(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        Calendar calendar = new GregorianCalendar();
        List<Client> clientes = new ArrayList<Client>();

        int i = 1;
        for(Row row : sheet) {

            if(row.getRowNum() == 0){
                continue;
            }

            for(int cn=0; cn<row.getLastCellNum(); cn++) {

                Client client = new Client();

                // If the cell is missing from the file, generate a blank one
                // (Works by specifying a MissingCellPolicy)
                Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);

                try {
                    if(cn == 0){
                        String normal = cell.getStringCellValue();
                        String rut = normal.substring(0,normal.length()-1);
                        client.setRut(Integer.parseInt(rut));

                        String dv = cell.getStringCellValue().substring(cell.getStringCellValue().length() - 1);
                        client.setDv(dv.charAt(0));

                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

                System.out.println( i + ") Cree " + client.getRut() + "-" +  client.getDv());
                clientes.add(client);
                i++;
            }

        }

        hacerExcel(clientes);

    }

    private void hacerExcel(List<Client> clientes) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

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
        cell.setCellValue("RUT CLIENTE");

        HSSFCell cell2 = row.createCell(1);
        cell2.setCellValue("F INF COMERCIAL");

        HSSFCell cell3 = row.createCell(2);
        cell3.setCellValue("INDICADOR DE RIESGO");

        HSSFCell cell4 = row.createCell(3);
        cell4.setCellValue("INF MOROSOS");

        HSSFCell cell5 = row.createCell(4);
        cell5.setCellValue("INF PROTESTO");

        HSSFCell cell6 = row.createCell(5);
        cell6.setCellValue("MONTO MOROSO");

        HSSFCell cell7 = row.createCell(6);
        cell7.setCellValue("MONTO PROTESTO");

        HSSFCell cell8 = row.createCell(7);
        cell8.setCellValue("VIGENCIA");

        // Parte por defecto en la fila 10.
        int i = 1;
        for (Client client : clientes) {
            HSSFRow rowCreated = sheet.createRow(i);
            HSSFCell cellCreated1 = rowCreated.createCell(0);
            String rutConcatenado = "" + client.getRut() + client.getDv();
            System.out.println("Insertando " + rutConcatenado);
            cellCreated1.setCellValue(rutConcatenado);

            PlatinumfinOutput response = null;

            try{
                Thread.sleep(10000);
                System.out.println("Esperando un poquito...");
                URL wsdlPath = new File("/Users/agustinsantiago/SuperfrigoFiles/PlatinumPublicoService.wsdl").toURI().toURL();

                PlatinumFinService_Service service = new PlatinumFinService_Service(wsdlPath);
                PlatinumFinService iService = service.getPlatinumFINImplPort();

                BindingProvider bp = (BindingProvider) iService;
                Map r = bp.getRequestContext();

                r.put(BindingProvider.USERNAME_PROPERTY, "efx.pr.platinum");
                r.put(BindingProvider.PASSWORD_PROPERTY, "osb2011a");

                PlatinumfinInput input = new PlatinumfinInput();
                input.setUsuario("EFX_METLIFEVIDA");
                input.setClave("DICOM2");
                input.setRut(rutConcatenado);
                input.setDescripcion(null);
                response = iService.getInforme(input, null);
            } catch (Exception e){
                System.out.println("Me cai obteniendo el informe equifax.");
            }

            if(response != null){
                HSSFCell cellCreated2 = rowCreated.createCell(1);
                cellCreated2.setCellValue("7/28/16");

                HSSFCell cellCreated3 = rowCreated.createCell(2);
                cellCreated3.setCellValue(response.getIndicadorRiesgoCrediticio().getPredictorPersonaNatural());

                MetlifeClientData data = generateEquifaxData(client, response);

                if(data.isINFMorosos()){
                    HSSFCell cellCreated4 = rowCreated.createCell(3);
                    cellCreated4.setCellValue("SI");
                } else {
                    HSSFCell cellCreated4 = rowCreated.createCell(3);
                    cellCreated4.setCellValue("NO");
                }

                if(data.isINFProtesto()){
                    HSSFCell cellCreated5 = rowCreated.createCell(4);
                    cellCreated5.setCellValue("SI");
                } else {
                    HSSFCell cellCreated5 = rowCreated.createCell(4);
                    cellCreated5.setCellValue("NO");
                }

                HSSFCell cellCreated6 = rowCreated.createCell(5);
                cellCreated6.setCellValue(data.getMTO_MOR());

                HSSFCell cellCreated7 = rowCreated.createCell(6);
                cellCreated7.setCellValue(data.getMTOPRO());

                HSSFCell cellCreated8 = rowCreated.createCell(7);
                cellCreated8.setCellValue("VIGENTE");
            }

            System.out.println("Primer generado");
            i++;
        }



        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        externalContext.setResponseContentType("application/vnd.ms-excel");
        SimpleDateFormat sdf2 =  new SimpleDateFormat("ddMMyyyy");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=archivo_equifax.xls");

        workbook.write(externalContext.getResponseOutputStream());
        facesContext.responseComplete();
    }

    private MetlifeClientData generateEquifaxData(Client client, PlatinumfinOutput equifaxResponse) {
        boolean INFMorosos = false, INFProtesto = false;
        int MTO_MOR = 0;
        int MTOPRO = 0;

        if (equifaxResponse.getResumenMorosidadesYProtestosImpagos().getTipoDeudaUltimoImpago().equals("PROTESTO")){
            INFMorosos = false;
            INFProtesto = true;
            MTO_MOR = 0;
            MTOPRO = Integer.parseInt(equifaxResponse.getResumenMorosidadesYProtestosImpagos().getMontoTotalImpagos()) * 1000;

        } else {
            if (equifaxResponse.getResumenMorosidadesYProtestosImpagos().getTipoDeudaUltimoImpago().equals("MOROSIDAD")){
                INFMorosos = true;
            }

            INFProtesto = false;
            MTO_MOR = Integer.parseInt(equifaxResponse.getResumenMorosidadesYProtestosImpagos().getMontoTotalImpagos()) * 1000;
            MTOPRO = 0;
        }

        MetlifeClientData data = new MetlifeClientData(INFMorosos, INFProtesto, MTO_MOR, MTOPRO);

        return data;
    }*/

    public String getOutLastChar(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length()-1)=='x') {
            str = str.substring(0, str.length()-1);
        }
        return str;
    }

    private String getStringValue(Cell cell) {
        return cell.getStringCellValue();
    }

    private double getNumericValue(Cell cell) {
        return cell.getNumericCellValue();
    }


}
