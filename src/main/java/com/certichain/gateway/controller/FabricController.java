package com.certichain.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.certichain.gateway.service.FabricService;

@RestController
@RequestMapping("/fabric")
public class FabricController {

    private final FabricService service;

    public FabricController(FabricService service) {
        this.service = service;
    }

    @GetMapping("/assets")
    public String listAssets() throws Exception {
        return service.getAllAssets();
    }

    @PostMapping("/assets/init")
    public void initLedger() throws Exception {
        service.initLedger();
    }

    @PostMapping("/asset")
    public void createAsset(@RequestParam String id, @RequestParam String color,
                            @RequestParam String size, @RequestParam String owner,
                            @RequestParam String value) throws Exception {
        service.createAsset(id, color, size, owner, value);
    }

    @GetMapping("/asset/{id}")
    public String getAsset(@PathVariable String id) throws Exception {
        return service.readAsset(id);
    }

    @PutMapping("/asset/{id}/transfer")
    public void transferAsset(@PathVariable String id, @RequestParam String newOwner) throws Exception {
        service.transferAsset(id, newOwner);
    }

}
