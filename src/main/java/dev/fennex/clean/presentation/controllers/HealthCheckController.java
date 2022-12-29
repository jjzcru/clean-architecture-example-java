package dev.fennex.clean.presentation.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @RequestMapping(value="/", method=RequestMethod.GET)
    @ResponseBody
    public HealthCheckResponse index() {
        return new HealthCheckResponse();
    }

    static class HealthCheckResponse {
        public Boolean success = true;
    }
}

