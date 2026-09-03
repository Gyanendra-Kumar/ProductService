package org.example.productservice.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  //@RestController - host Rest HTTP APIs
@RequestMapping("/sample") // endpoint - /sample
public class SampleController {

    @RequestMapping("/hello/{name}/{times}") // endpoint - /sample/hello/5
    public String sayHello(@PathVariable("name") String name, @PathVariable("times") int x){
        String s = "";
        for(int i=0; i<x; i++){
            s += "Hello " + name + "! <br>";
        }
        return s;
    }

    @RequestMapping("/hi")  // endpoint - /sample/hi
    public String sayHi(){
        return "Hi Gyan!";
    }
}
