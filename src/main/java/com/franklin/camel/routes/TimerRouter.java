package com.franklin.camel.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimerRouter extends RouteBuilder {
    @Autowired
    private GetCurrentTimeBean getCurrentTimeBean;

    @Autowired
    private SingleLoggingProcessingComponent loggingComponent;

    @Override
    public void configure() throws Exception {
        from("timer:first-timer")
                .log("Primer log ${body}")
                //.transform().constant("Mi mensaje")
                //.bean("getCurrentTimeBean")
                .bean(getCurrentTimeBean)
                .log("Segundo log ${body}")
                .bean(loggingComponent)
                .log("Tercer log ${body}")
                .process(new SingleLoggingProcessor())
                .to("log:first-timer");
    }
}

@Component
class GetCurrentTimeBean{
    public String getCurretTime(){
        return "Time now is: " + LocalDateTime.now();
    }
}

@Component
class SingleLoggingProcessingComponent{
    private Logger logger = LoggerFactory.getLogger(SingleLoggingProcessingComponent.class);
    public void process(String message){
        logger.info("SingleLoggingProcessingComponent {}", message);
    }
}

class SingleLoggingProcessor implements Processor {
    private Logger logger = LoggerFactory.getLogger(SingleLoggingProcessingComponent.class);

    @Override
    public void process(Exchange exchange) throws Exception{
        logger.info("SingleLoggingProcessor {}", exchange.getMessage().getBody());
    }
}