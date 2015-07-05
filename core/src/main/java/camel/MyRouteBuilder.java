package camel;

import org.apache.camel.builder.RouteBuilder;

/**
 * Created by grzmiejski on 4/27/15.
 */
public class MyRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:main")
//                .process((Exchange exchange) -> exchange.getOut().setBody("Hahahaha"))
                .to("websocket:localhost:8080/topic/greetings?sendToAll=true");
    }
}
