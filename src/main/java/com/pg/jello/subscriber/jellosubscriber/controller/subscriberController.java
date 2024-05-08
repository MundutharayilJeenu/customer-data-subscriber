package com.pg.jello.subscriber.jellosubscriber.controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class subscriberController {
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/ping2",method = RequestMethod.GET)
    public String pingService(){
        return "pong";
    }
}
