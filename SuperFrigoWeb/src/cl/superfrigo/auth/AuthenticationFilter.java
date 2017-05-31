package cl.superfrigo.auth;

import cl.superfrigo.entity.Usuario;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Blueprints on 9/14/2015.
 */
public class AuthenticationFilter implements Filter {

    static private final Logger LOGGER = Logger.getLogger(AuthenticationFilter.class);

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (  req.getRequestURI().contains("login.xhtml") || req.getRequestURI().contains("error.xhtml") || hasPermission(req)) {
            chain.doFilter(request, response);
        } else
        if(!hasPermission(req)) {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
        }
        else {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
        }

    }

    private boolean hasPermission(HttpServletRequest req) {

        Object value = req.getSession().getAttribute("user");
        LOGGER.trace("Filtrando autenticacion de usuario: " + "user" + " = " + value);
        if(value == null)
            return false;


        AuthenticationBean auth = (AuthenticationBean) req.getSession().getAttribute("authBean");

        Usuario u = auth.getLoggedUser();

        if(u == null)
            return false;

        return checkPermission(u,req.getRequestURI());

    }

    private boolean checkPermission(Usuario u, String requestURI) {
        boolean verdad = true;

        LOGGER.trace("Verificando permisos de usuario " + u.getNombres() + " para url " + requestURI);

        if(verdad = true){
            if(        requestURI.endsWith("/abastecimiento/BusquedaPorProducto.xhtml")
                    || requestURI.endsWith("/abastecimiento/BusquedaPorProveedor.xhtml")
                    || requestURI.endsWith("/abastecimiento/FichaAuxiliares.xhtml")
                    || requestURI.endsWith("/abastecimiento/OrdenDeCompra.xhtml")
                    || requestURI.endsWith("/abastecimiento/MantenedorCondicionPago.xhtml")
                    || requestURI.endsWith("/abastecimiento/SolicitudRequisicion.xhtml")
                    || requestURI.endsWith("/abastecimiento/ValidarRequisiciones.xhtml")
                    || requestURI.endsWith("/abastecimiento/PreciosProductos.xhtml")
                    || requestURI.endsWith("/abastecimiento/MantenedorCentrosDeCostos.xhtml")
                    || requestURI.endsWith("/abastecimiento/FichaAuxiliar.xhtml")
                    || requestURI.endsWith("/abastecimiento/RecepcionCompletaOT.xhtml")
                    || requestURI.endsWith("/abastecimiento/InformeOrdenesDeCompra.xhtml")
                    || requestURI.endsWith("/abastecimiento/InformeRequisiciones.xhtml")
                    || requestURI.endsWith("/abastecimiento/FichaProducto.xhtml")
                    || requestURI.endsWith("/bodega/ConsultaStock.xhtml")
                    || requestURI.endsWith("/bodega/FichaProducto.xhtml")
                    || requestURI.endsWith("/bodega/GuiaEntrada.xhtml")
                    || requestURI.endsWith("/bodega/GuiaSalida.xhtml")
                    || requestURI.endsWith("/bodega/MantenedorBodegas.xhtml")
                    || requestURI.endsWith("/bodega/MantenedorGrupos.xhtml")
                    || requestURI.endsWith("/bodega/MantenedorSubGrupos.xhtml")
                    || requestURI.endsWith("/bodega/MantenedorUnidadMedida.xhtml")
                    || requestURI.endsWith("/bodega/MantenedorCentrosDeCostos.xhtml")
                    || requestURI.endsWith("/bodega/FichaAuxiliar.xhtml")
                    || requestURI.endsWith("/bodega/InformeStock.xhtml")
                    || requestURI.endsWith("/bodega/InformeGuiaEntrada.xhtml")
                    || requestURI.endsWith("/bodega/InformeGuiaSalida.xhtml")
                    || requestURI.endsWith("/bodega/InformeConsumo.xhtml")
                    || requestURI.endsWith("/bodega/InformeRegistroExistencia.xhtml")
                    || requestURI.endsWith("/bodega/TomaInventario.xhtml")
                    || requestURI.endsWith("/comercial/Cotizaciones.xhtml")
                    || requestURI.endsWith("/comercial/CotizacionesEnEstudio.xhtml")
                    || requestURI.endsWith("/comercial/CotizacionesParaAprobacionCliente.xhtml")
                    || requestURI.endsWith("/comercial/CotizacionesParaAprobacionComercial.xhtml")
                    || requestURI.endsWith("/comercial/CotizacionesParaAprobacionFinanciera.xhtml")
                    || requestURI.endsWith("/comercial/FichaAuxiliar.xhtml")
                    || requestURI.endsWith("/comercial/SolicitudCotizacion.xhtml")
                    || requestURI.endsWith("/comercial/MantenedorCentrosDeCostos.xhtml")
                    || requestURI.endsWith("/comercial/InformeCotizaciones.xhtml")
                    || requestURI.endsWith("/usuarios/MantenedorUsuarios.xhtml")
                    || requestURI.endsWith("/usuarios/MantenedorPerfiles.xhtml")
                    || requestURI.endsWith("/proyectos/DefinicionRecetas.xhtml")
                    || requestURI.endsWith("/proyectos/PlanosFabricacion.xhtml")
                    || requestURI.endsWith("/proyectos/InformeRecetas.xhtml")
                    || requestURI.endsWith("/registrohh/MantenedorHH.xhtml")
                    || requestURI.endsWith("/registrohh/FichaTrabajador.xhtml")
                    || requestURI.endsWith("/registrohh/MantenedorAreasHH.xhtml")
                    || requestURI.endsWith("/registrohh/RegistroHH.xhtml")
                    || requestURI.endsWith("/produccion/OrdenTrabajo.xhtml")
                    || requestURI.endsWith("/produccion/ControlDespacho.xhtml")
                    || requestURI.endsWith("/produccion/ControlFacturacion.xhtml")
                    || requestURI.endsWith("/produccion/ControlLiberacion.xhtml")
                    || requestURI.endsWith("/produccion/ControlProgramacion.xhtml")
                    || requestURI.endsWith("/produccion/ConsultaStockRecetas.xhtml")
                    || requestURI.endsWith("/produccion/ProgramacionPlanta.xhtml")
                    || requestURI.endsWith("/solicitud_material/SolicitudMaterial.xhtml")
                    || requestURI.endsWith("/home.xhtml")
                    )
                return true;
        } else{
            return false;
        }

        return  false;

    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

}