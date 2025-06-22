package com.certichain.gateway.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.certichain.gateway.model.PrivateDocument;
import com.certichain.gateway.model.PrivateDocumentAuditLog;
import com.certichain.gateway.service.FabricService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/privatedocuments")
public class PrivateDocumentController {

    private final FabricService service;
    private final ObjectMapper objectMapper;

    public PrivateDocumentController(FabricService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public void savePrivateDocument(@RequestBody PrivateDocument doc) throws Exception {
        service.savePrivateDocument(doc);
    }

    @PutMapping("/{id}/state")
    public void updateState(@PathVariable String id, @RequestParam String newState) throws Exception {
        service.updateDocumentState(id, newState);
    }

    @GetMapping("/{id}")
    public PrivateDocument getPrivateDocument(@PathVariable String id){
        String json;
        try {
            json = service.getPrivateDocumentById(id);
            return objectMapper.readValue(json, PrivateDocument.class);
        } catch (Exception e) {
            return new PrivateDocument();
        }
    }

    @GetMapping("/institution/{institution}")
    public List<PrivateDocument> queryByInstitution(@PathVariable String institution){
        String json;
        try {
            json = service.queryPrivateByInstitution(institution);
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
        
    }

    @GetMapping("/user/{userId}")
    public List<PrivateDocument> queryByUser(@PathVariable String userId){
        String json;
        try {
            json = service.queryPrivateByUser(userId);
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @GetMapping("/audit")
    public List<PrivateDocumentAuditLog> queryPrivateAuditLogs(
            @RequestParam String filterType,
            @RequestParam String filterValue,
            @RequestParam String startDate,
            @RequestParam String endDate
    ){
        String json;
        try {
            json = service.queryPrivateAuditLogs(filterType, filterValue, startDate, endDate);
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
        
    }

}
