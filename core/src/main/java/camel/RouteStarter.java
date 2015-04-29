package camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.jndi.JndiContext;

import java.util.Scanner;

/**
 * Created by grzmiejski on 4/27/15.
 */
public class RouteStarter {

    private static CamelContext camelContext;

    public static CamelContext buildRoute() {
        try {
            camelContext = new DefaultCamelContext();
            camelContext.addRoutes(new MyRouteBuilder());

            // ACTIVEMQ (JMS)
            //context.addComponent("activemq",
            //	ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"));

            ProducerTemplate template = camelContext.createProducerTemplate();
            camelContext.start();
            System.out.println("Context started \n\n");

            return camelContext;
        } catch (Exception e) {
            System.out.println("EXCEPTION IN MAIN APP " + e.getMessage());
        }

        return null;
    }

    public static ProducerTemplate getP() {
        return camelContext.createProducerTemplate();
    }

    public static void main(String[] args) {
        try {

            // CONTEXT
            JndiContext jndiContext = new JndiContext();
            CamelContext context = new DefaultCamelContext(jndiContext);

            context.addRoutes(new MyRouteBuilder());

            // ACTIVEMQ (JMS)
            //context.addComponent("activemq",
            //	ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"));

            ProducerTemplate template = context.createProducerTemplate();
            context.start();
            System.out.println("Context started \n\n");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String someString = scanner.nextLine();
                template.sendBody("direct:main", someString);
            }

        } catch (Exception e) {
            System.out.println("EXCEPTION IN MAIN APP " + e.getMessage());
        }

    }
}