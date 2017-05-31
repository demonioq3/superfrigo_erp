package cl.superfrigo.jms;

/**
 * Created by agustinsantiago on 5/19/16.
 */
import cl.superfrigo.entity.bodega.GuiaSalida;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.*;

@Stateless
public class ManagerRegistroTransferencias {

    /**
     * The ldap factory used to submit the packages received from the nodes' sensors.
     */
    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    /**
     * The queue in to which the packages are sent for later processing.
     */
    @Resource(mappedName = "java:/jboss/queue/colaTransferencias")
    private Queue colaTransferencias;

    private final static Logger logger = LoggerFactory.getLogger(ManagerRegistroTransferencias.class);

    public void record(GuiaSalida guiaSalida) {

        logger.info("Se recibio la guía de salida id: " + guiaSalida.getId());

        try {
            logger.info("ManagerRegistroTransferencias - iniciándose:  Èste proceso ingresa a la cola la guia de salida creada.");
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            MessageProducer producer = session.createProducer(colaTransferencias);

            connection.start();

            /* A message is created containing the out guide */
            ObjectMessage packageMessage = session.createObjectMessage(guiaSalida);

            /* The message is sent */
            producer.send(packageMessage);
            if (session.getTransacted()){
                session.commit();
            }


            logger.info("Guía de salida encolada satisfactoriamente en cola transferencias.");

            /* And the resources closed */
            connection.close();
            session.close();
        } catch (JMSException e) {
            logger.error("La guia de salida no se pudo escribir en la cola.", e);
        }
    }

}