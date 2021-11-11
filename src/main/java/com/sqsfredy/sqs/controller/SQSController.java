package com.sqsfredy.sqs.controller;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("sqs")
public class SQSController {
    @Value("${cloud.aws.end-point.uri}")
    private String url;

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @GetMapping("push/{msg}")
    public void pushMessage(@PathVariable("msg") String msg){
        //Para normal
        //queueMessagingTemplate.send(url, MessageBuilder.withPayload(msg).build());

        //Para fifo
        Map<String, Object> headers = new HashMap<>();
        headers.put("message-group-id", "1");
        //headers.put("message-deduplication-id", "1");
        queueMessagingTemplate.convertAndSend(url,msg,headers);
    }

    @GetMapping("push/body")
    public void pushMessage(@RequestBody Object body) throws JsonProcessingException {
        ObjectMapper mapper=new ObjectMapper();
        String bodyString =mapper.writeValueAsString(body);
        //Para normal
        queueMessagingTemplate.send(url, MessageBuilder.withPayload(bodyString).build());

        //Para fifo
        /*
        Map<String, Object> headers = new HashMap<>();
        headers.put("message-group-id", "1");
        //headers.put("message-deduplication-id", "1");
        queueMessagingTemplate.convertAndSend(url,body,headers);*/
    }

    @SqsListener("cola2")
    public void listenerMessage(Object message) throws Exception {
        System.out.println(message);
        Double i=0.0;
        while(i<2000000000.0){
            i++;
        }
        System.out.println("terminé");
    }
}
