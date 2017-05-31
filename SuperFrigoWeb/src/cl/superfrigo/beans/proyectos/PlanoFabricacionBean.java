package cl.superfrigo.beans.proyectos;

import cl.superfrigo.beans.BaseBean;
import cl.superfrigo.dao.OrdenDeTrabajoDAO;
import cl.superfrigo.dao.PlanoFabricacionDAO;
import cl.superfrigo.entity.proyectos.PlanoFabricacion;
import cl.superfrigo.model.PlanosFabricacionLazyDataModel;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by asgco on 28-Feb-16.
 */
@ManagedBean
@ViewScoped
public class PlanoFabricacionBean extends BaseBean implements Serializable {

    // Parámetros de búsqueda.
    private PlanoFabricacion planoFabricacion;
    private UploadedFile file;

    // Todos los planos.
    private PlanosFabricacionLazyDataModel planos;

    private boolean estaEditando = false;

    //private String destination="/Users/agustinsantiago/Desktop/testing/";
    private String destination="/home/superfrigo/planos_fabricacion/";


    @EJB PlanoFabricacionDAO planoFabricacionDAO;
    @EJB OrdenDeTrabajoDAO ordenDeTrabajoDAO;

    SimpleDateFormat simpleDateFormat;


    @PostConstruct
    public void init(){
        planoFabricacion = new PlanoFabricacion();
        planoFabricacion.setFecha(new Date());

        simpleDateFormat = new SimpleDateFormat("dd_MM_YYYY");
    }

    public void limpiar(){
        init();
    }

    public void guardar() throws IOException {
        // Validando los nulls.
        if(hayErrores()){
            return;
        }

        if(!estaEditando){
            // Chequeando si el código ya está creado.
            if(estaCreadoElPlano(planoFabricacion.getCodigo())){
                showError("Error", "El código ya está registrado en otro plano. Intenta con otro código.");
                this.planoFabricacion.setOrdenDeTrabajoId(null);
                return;
            }

            // Chequeando si el archivo fue ingresado o no.
            if(file == null){
                showError("Error", "Debe adjuntar un archivo para guardar un plano.");
                this.planoFabricacion.setOrdenDeTrabajoId(null);
                return;
            }

            // Copiando el archivo en el disco
            String ruta = copyFile(randomName(), this.file.getInputstream());
            this.planoFabricacion.setRutaPdf(ruta);

            PlanoFabricacion planoCreado = planoFabricacionDAO.create(this.planoFabricacion);

            showInfo("Aviso", "Se creo correctamente el plano de fabricación código " + planoCreado.getCodigo());
        } else {

        }
    }

    private boolean hayErrores() {
        if(planoFabricacion.getCodigo().equals("")){
            showError("Error", "Para guardar un plano de fabricación debe ingresar el código.");
            return true;
        }

        if(planoFabricacion.getNombre().equals("")){
            showError("Error", "Para guardar un plano de fabricación debe ingresar un nombre.");
            return true;
        }

        if(planoFabricacion.getOrdenDeTrabajoId()!= null){
            if(ordenDeTrabajoDAO.findById(planoFabricacion.getOrdenDeTrabajoId()) == null){
                showError("Error", "Para guardar un plano de fabricación debe ingresar una orden de trabajo válida.");
                return true;
            }
        }

        return false;
    }

    public void seleccionarPlano(PlanoFabricacion plano){
        planoFabricacion = plano;

        // Se puede guardar.
        estaEditando = true;

        showInfo("Aviso", "Se cargó el plano " + this.planoFabricacion.getCodigo() + " de forma correcta.");
    }

    private String randomName() {
        Random r = new Random();
        return "plano_" + simpleDateFormat.format(new Date()) + "_" + Long.toString (r.nextLong (), 36) + ".pdf";
    }

    public boolean estaCreadoElPlano(String codigo){
        PlanoFabricacion planoFabricacion = planoFabricacionDAO.findByCodigo(codigo);

        if(planoFabricacion != null){
            return true;
        }

        return false;
    }

    public void descargarArchivo(){
        File file = new File(planoFabricacion.getRutaPdf());

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        response.setHeader("Content-Disposition", "attachment;filename=descarga.pdf");
        response.setContentLength((int) file.length());
        ServletOutputStream out = null;
        try {
            FileInputStream input = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            out = response.getOutputStream();
            int i = 0;
            while ((i = input.read(buffer)) != -1) {
                out.write(buffer);
                out.flush();
            }
            FacesContext.getCurrentInstance().getResponseComplete();
        } catch (IOException err) {
            err.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
    }

    public PlanoFabricacion getPlanoFabricacion() {
        return planoFabricacion;
    }

    public void setPlanoFabricacion(PlanoFabricacion planoFabricacion) {
        this.planoFabricacion = planoFabricacion;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    /**
     * Éste método se encarga de escribir el archivo subido en el disco duro, retornando su ruta.
     * @param fileName nombre del archivo.
     * @param in InputStream con el archivo.
     * @return ruta exacta de donde se almacenó el archivo.
     */
    public String copyFile(String fileName, InputStream in) {
        try {
            // write the inputStream to a FileOutputStream
            OutputStream out = new FileOutputStream(new File(destination + fileName));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();

            System.out.println("Archivo creado! " + destination + fileName);
            return destination + fileName;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return "";
    }

    public void test(){

    }

    public boolean isEstaEditando() {
        return estaEditando;
    }

    public void setEstaEditando(boolean estaEditando) {
        this.estaEditando = estaEditando;
    }

    public PlanosFabricacionLazyDataModel getPlanos() {
        if ( planos == null )
            planos = new PlanosFabricacionLazyDataModel(planoFabricacionDAO);
        return planos;
    }

    public void setPlanos(PlanosFabricacionLazyDataModel planos) {
        this.planos = planos;
    }
}

