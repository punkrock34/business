package com.business.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainBusinessController {

    @RequestMapping(value = "/")
    public String mainInterface() {
        return "index";
    }

    @RequestMapping(value = "/get")
    public String getInterface() {
        return "get";
    }

    @RequestMapping(value = "/create")
    public String createInterface() {
        return "create";
    }

    @RequestMapping(value = "/update")
    public String updateInterface() {
        return "update";
    }

    @RequestMapping(value = "/delete")
    public String deleteInterface() {
        return "delete";
    }

}
