package cl.superfrigo.jms;

/**
 * Created by agustinsantiago on 5/19/16.
 */
import cl.superfrigo.entity.bodega.GuiaEntrada;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.*;

@Stateless
public class ManagerRegistroEntrada {

    /**
     * The ldap factory used to submit the packages received from the nodes' sensors.
     */
    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    /**
     * The queue in to which the packages are sent for later processing.
     */
    @Resource(mappedName = "java:/jboss/queue/colaEntradas")
    private Queue colaEntradas;

    private final static Logger logger = LoggerFactory.getLogger(ManagerRegistroEntrada.class);

    public void record(GuiaEntrada guiaEntrada) {

        logger.info("Se recibio la guía de entrada id: " + guiaEntrada.getId());
        try {

            logger.info("ManagerRegistroEntrada - iniciándose: Éste proceso agrega a la cola una nueva guia de entrada.");
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            MessageProducer producer = session.createProducer(colaEntradas);

            connection.start();

            /* A message is created containing the binary package */
            ObjectMessage packageMessage = session.createObjectMessage(guiaEntrada);

            /* The message is sent */
            producer.send(packageMessage);
            if (session.getTransacted()){
                session.commit();
            }

            logger.info("Guía de entrada encolada satisfactoriamente.");

            /* And the resources closed */
            connection.close();
            session.close();
        } catch (JMSException e) {
            logger.error("La guia de entrada no se pudo escribir en la cola.", e);
        }
    }

}