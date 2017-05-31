package cl.superfrigo.jms;

import cl.superfrigo.dao.*;
import cl.superfrigo.entity.bodega.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by agustinsantiago on 5/19/16.
 */

/**
 * Ésta clase es responsable de obtener guias de salida desde la cola.
 * El proceso formal comienza obteniendo la guia de entrada, revisando la cola de guias de entrada y realizando calculos de stock
 * y valorización por cada producto ingresado
 */
@MessageDriven(name = "TransferService", activationConfig = {
        @ActivationConfigProperty(propertyName =
                "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName =
                "destination", propertyValue = "java:/jboss/queue/colaTransferencias"),
        @ActivationConfigProperty(propertyName =
                "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
public class ProcesoTransferenciasBodegas implements MessageListener {

    public static final Logger logger = LoggerFactory.getLogger(ProcesoTransferenciasBodegas.class);

    @EJB private EntradaProductoCantidadDAO entradaProductoCantidadDAO;
    @EJB private GuiaEntradaDAO guiaEntradaDAO;
    @EJB private BodegaStockProductoDAO bodegaStockProductoDAO;
    @EJB private SalidaProductoCantidadDAO salidaProductoCantidadDAO;
    @EJB private GuiaSalidaDAO guiaSalidaDAO;
    @EJB private ManagerRegistroEntrada entradaRegister;

    public boolean seHizoEfectivo;

    /**
     * Éste método consume el mensaje, el cual contiene una guia de salida (la recientemente ingresada)
     */
    @Override
    public void onMessage(Message message) {
        logger.info("-----------------------------------");
        logger.info("Proceso de transferencia de bodega..");
        logger.info("Obteniendo guia de salida de la cola: " + message.toString());

        /* The message is unfolded */
        GuiaSalida guiaSalida;
        try {
            guiaSalida = (GuiaSalida) ((ObjectMessage) message).getObject();
            
            // Se procede a eliminar elementos de la cola ya usados.
            deleteunusedStockFromQueue();

            logger.info("Se comienza proceso de valorización de la guia salida obtenida...");
            for (SalidaProductoCantidad salidaProductoCantidad : guiaSalida.getProductos()) {
                excecuteValorization(salidaProductoCantidad, guiaSalida.getBodegaId());
            }

            GuiaSalida guiaValorizada = guiaSalidaDAO.findById(guiaSalida.getId());
            guiaValorizada.setProductos(salidaProductoCantidadDAO.findByGuiaSalidaId(guiaValorizada.getId()));

            // Se procede a agregar la entrada.
            GuiaEntrada guiaEntrada = new GuiaEntrada();
            guiaEntrada.setConceptoEntradaId(ConceptoEntrada.TRANSFERENCIA_ENTRE_BODEGA);
            guiaEntrada.setFecha(new Date());
            guiaEntrada.setBodegaOrigenId(guiaValorizada.getBodegaOrigenId());
            guiaEntrada.setBodegaId(guiaValorizada.getBodegaId());
            guiaEntrada.setProveedorId(guiaValorizada.getProveedorId());

            GuiaEntrada guiaGuardada = guiaEntradaDAO.create(guiaEntrada);
            for (SalidaProductoCantidad salidaProductoCantidad : guiaValorizada.getProductos()) {
                EntradaProductoCantidad entrada = new EntradaProductoCantidad();
                entrada.setFichaProductoId(salidaProductoCantidad.getFichaProductoId());
                entrada.setGuiaEntradaId(guiaEntrada.getId());
                entrada.setCantidad(salidaProductoCantidad.getCantidad());
                entrada.setCantidadUtilizada(0D);
                entrada.setPrecioUnitario(salidaProductoCantidad.getPrecioUnitario());
                entrada.setPrecioTotal(salidaProductoCantidad.getPrecioTotal());

                entradaProductoCantidadDAO.create(entrada);
            }

            entradaRegister.record(guiaGuardada);

        } catch (JMSException e) {
            logger.error("ERROR: El mensaje no pudo ser desencolado y se perdió por siempre.", e);
            return;
        } catch (NamingException e) {
            e.printStackTrace();
        }

    }

    private void deleteunusedStockFromQueue() throws NamingException, JMSException {
        //Get the initial context
        InitialContext ctx = new InitialContext();

        // lookup the queue object
        Queue queue = (Queue) ctx.lookup("java:/jboss/queue/colaEntradas");

        // lookup the queue connection factory
        QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup("java:/ConnectionFactory");

        // create a queue connection
        QueueConnection queueConn = connFactory.createQueueConnection();

        // create a queue session
        QueueSession queueSession = queueConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

        // create a queue browser
        QueueBrowser queueBrowser = queueSession.createBrowser(queue);

        // start the connection
        queueConn.start();

        Enumeration<?> enum1 = queueBrowser.getEnumeration();

        while(enum1.hasMoreElements()) {
            Message message = (Message) enum1.nextElement();
            GuiaEntrada guia = (GuiaEntrada) ((ObjectMessage) message).getObject();
            // Obtengo la primera guia de entrada
            GuiaEntrada guiaCola = guiaEntradaDAO.findById(guia.getId());

            List<EntradaProductoCantidad> pendientes = new ArrayList<EntradaProductoCantidad>();
            for (EntradaProductoCantidad entradaProductoCantidad : guiaCola.getProductos()) {
                if(!entradaProductoCantidad.getCantidad().equals(entradaProductoCantidad.getCantidadUtilizada())){
                    pendientes.add(entradaProductoCantidad);
                }
            }

            if(pendientes.size() == 0){
                // Significa que no hay pendientes, entonces procedemos a eliminar la guia de entrada de la cola.
                MessageConsumer consumer = queueSession.createConsumer(queue, "JMSMessageID='" +  message.getJMSMessageID()  + "'");
                consumer.receive(1000);
            }
        }
    }

    private void excecuteValorization(SalidaProductoCantidad salidaProductoCantidad, Long bodegaId) throws NamingException, JMSException {
        this.seHizoEfectivo = false;

        //Get the initial context
        InitialContext ctx = new InitialContext();

        // lookup the queue object
        Queue queue = (Queue) ctx.lookup("java:/jboss/queue/colaEntradas");

        // lookup the queue connection factory
        QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup("java:/ConnectionFactory");

        // create a queue connection
        QueueConnection queueConn = connFactory.createQueueConnection();

        // create a queue session
        QueueSession queueSession = queueConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

        // create a queue browser
        QueueBrowser queueBrowser = queueSession.createBrowser(queue);

        // start the connection
        queueConn.start();

        Enumeration<?> enum1 = queueBrowser.getEnumeration();

        while(enum1.hasMoreElements())
        {
            if(seHizoEfectivo){
                break;
            }

            Message message = (Message)enum1.nextElement();
            GuiaEntrada guia = (GuiaEntrada) ((ObjectMessage) message).getObject();
            // Obtengo la primera guia de entrada
            GuiaEntrada guiaCola = guiaEntradaDAO.findById(guia.getId());

            logger.info("Primera guía obtenida de la cola: " + guiaCola.getId());

            for (EntradaProductoCantidad productoEntrada : guiaCola.getProductos() ) {
                if(!productoEntrada.getCantidad().equals(productoEntrada.getCantidadUtilizada())) {
                    if (productoEntrada.getFichaProductoId().equals(salidaProductoCantidad.getFichaProductoId()) && productoEntrada.getCantidad() != productoEntrada.getCantidadUtilizada()) {
                        // Se verifica las  posibles opciones
                        Double cantidadPosibleParaAgregarUtilizada = productoEntrada.getCantidad() - productoEntrada.getCantidadUtilizada();

                        if (salidaProductoCantidad.getCantidad() < cantidadPosibleParaAgregarUtilizada) {
                            productoEntrada.setCantidadUtilizada(productoEntrada.getCantidadUtilizada() + salidaProductoCantidad.getCantidad());
                            entradaProductoCantidadDAO.update(productoEntrada);

                            // Se actualiza el precio unitario y total que salió la guia de salida.
                            salidaProductoCantidad.setPrecioUnitario(productoEntrada.getPrecioUnitario());
                            salidaProductoCantidad.setPrecioTotal(salidaProductoCantidad.getCantidad() * salidaProductoCantidad.getPrecioUnitario());
                            salidaProductoCantidadDAO.update(salidaProductoCantidad);

                            // Se eliminan del stock también
                            BodegaStockProducto bodegaStockProducto = bodegaStockProductoDAO.findByFichaProductoIdAndBodegaId(productoEntrada.getFichaProductoId(), bodegaId);
                            bodegaStockProducto.setCantidad(bodegaStockProducto.getCantidad() - salidaProductoCantidad.getCantidad());

                            bodegaStockProductoDAO.update(bodegaStockProducto);

                            this.seHizoEfectivo = true;
                            break;
                        } else if (salidaProductoCantidad.getCantidad() == cantidadPosibleParaAgregarUtilizada) {
                            productoEntrada.setCantidadUtilizada(productoEntrada.getCantidadUtilizada() + salidaProductoCantidad.getCantidad());
                            entradaProductoCantidadDAO.update(productoEntrada);

                            // Se actualiza el precio unitario y total que salió la guia de salida.
                            salidaProductoCantidad.setPrecioUnitario(productoEntrada.getPrecioUnitario());
                            salidaProductoCantidad.setPrecioTotal(salidaProductoCantidad.getCantidad() * salidaProductoCantidad.getPrecioUnitario());
                            salidaProductoCantidadDAO.update(salidaProductoCantidad);

                            // Se eliminan del stock también
                            BodegaStockProducto bodegaStockProducto = bodegaStockProductoDAO.findByFichaProductoIdAndBodegaId(productoEntrada.getFichaProductoId(), bodegaId);
                            bodegaStockProducto.setCantidad(bodegaStockProducto.getCantidad() - salidaProductoCantidad.getCantidad());

                            this.seHizoEfectivo = true;

                            /*
                            bodegaStockProductoDAO.update(bodegaStockProducto);

                            MessageConsumer consumer = queueSession.createConsumer(queue, "JMSMessageID='" + message.getJMSMessageID() + "'");
                            consumer.receive(1000);*/

                            break;

                        } else if (salidaProductoCantidad.getCantidad() > cantidadPosibleParaAgregarUtilizada) {
                            Double cantidadAStockear = productoEntrada.getCantidad() - productoEntrada.getCantidadUtilizada();
                            Double resto = salidaProductoCantidad.getCantidad() - cantidadAStockear;

                            // Se disminuye el la cantidad solicitada por la salida, ya que se utilizó solo eso.
                            salidaProductoCantidad.setCantidad(cantidadAStockear);
                            salidaProductoCantidad.setPrecioUnitario(productoEntrada.getPrecioUnitario());
                            salidaProductoCantidad.setPrecioTotal(salidaProductoCantidad.getCantidad() * salidaProductoCantidad.getPrecioUnitario());

                            // Se guarda la primera salida.
                            SalidaProductoCantidad salida = salidaProductoCantidadDAO.update(salidaProductoCantidad);

                            // Se modifica cantidadutilizada en el producto guia entrada.
                            productoEntrada.setCantidadUtilizada(cantidadAStockear);
                            entradaProductoCantidadDAO.update(productoEntrada);

                            // Se modifica el stock de la bodega.
                            BodegaStockProducto bodegaStockProducto = bodegaStockProductoDAO.findByFichaProductoIdAndBodegaId(productoEntrada.getFichaProductoId(), bodegaId);
                            bodegaStockProducto.setCantidad(bodegaStockProducto.getCantidad() - cantidadAStockear);

                            bodegaStockProductoDAO.update(bodegaStockProducto);

                            // Se crea un nuevo SalidaProductoCantidad, con lo que falta.
                            SalidaProductoCantidad salidaRestante = new SalidaProductoCantidad();
                            salidaRestante.setCantidad(resto);
                            salidaRestante.setFichaProductoId(salidaProductoCantidad.getFichaProductoId());
                            salidaRestante.setGuiaSalidaId(salidaProductoCantidad.getGuiaSalidaId());

                            SalidaProductoCantidad salidaNueva = salidaProductoCantidadDAO.create(salidaRestante);

                            this.seHizoEfectivo = false;
                            excecuteValorization(salidaNueva, bodegaId);
                        }
                    } else {
                        break;
                    }
                } else {
                    break;
                }

            }

        }

        queueConn.close();
    }
}

