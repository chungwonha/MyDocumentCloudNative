package com.chung.product.mydocumentCN.parsingservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("parsing")
public class ParsingServiceController {

    @GetMapping
    public String checkIfRunning(){
        return "running....";
    }
}
