package com.obss.jsi.controller;

import com.obss.jsi.service.ESKnowService;
import com.obss.jsi.service.KnowService;
import com.obss.jsi.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasRole('ROLE_ADMINS')")
public class SyncController {
    // This class is to use while development.
    // I often need to reset the mysql database and then I have to sync elastic search db with mysql.
    // so I made an endpoint to make this easier for me.
    // after the development this class can be deleted. But I opted to not delete it.
    // elasticsearch db has its own limits, if at some point it reaches capacity
    // and needs to reinstantiated with larger index size this class can come in handy.
    @Autowired
    private KnowService knowService;

    @Autowired
    private ESKnowService esknowService;

    @Autowired
    private Mapper mapper;

    @RequestMapping(value = "/api/sync")
    public void syncUsers() {
        knowService.findAll().forEach(know -> esknowService.save(mapper.KnowToESKnow(know)));
    }

}
