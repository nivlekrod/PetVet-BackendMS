package com.gftstart.ms.appointmentscheduling.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/appointments")
public class AppoimentSchedulingController {

    @GetMapping("status")
    public String status() {
        return "ok";
    }



}
