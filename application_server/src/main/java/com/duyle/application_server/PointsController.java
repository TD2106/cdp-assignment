package com.duyle.application_server;

import com.duyle.application_server.model.PointModification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/points")
public class PointsController {
    @PostMapping
    public PointModification modifyPlayerPoint(@RequestBody @Valid PointModification request) {
        return request;
    }
}
