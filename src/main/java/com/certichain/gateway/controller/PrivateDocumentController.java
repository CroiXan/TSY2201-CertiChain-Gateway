package com.certichain.gateway.controller;

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
    public PrivateDocument getPrivateDocument(@PathVariable String id) throws Exception {
        String json = service.getPrivateDocumentById(id);
        return objectMapper.readValue(json, PrivateDocument.class);
    }

    @GetMapping("/institution/{institution}")
    public List<PrivateDocument> queryByInstitution(@PathVariable String institution) throws Exception {
        String json = service.queryPrivateByInstitution(institution);
        return objectMapper.readValue(json, new TypeReference<>() {});
    }

    @GetMapping("/user/{userId}")
    public List<PrivateDocument> queryByUser(@PathVariable String userId) throws Exception {
        String json = service.queryPrivateByUser(userId);
        return objectMapper.readValue(json, new TypeReference<>() {});
    }

    @GetMapping("/audit")
    public List<PrivateDocumentAuditLog> queryPrivateAuditLogs(
            @RequestParam String filterType,
            @RequestParam String filterValue,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) throws Exception {
        String json = service.queryPrivateAuditLogs(filterType, filterValue, startDate, endDate);
        return objectMapper.readValue(json, new TypeReference<>() {});
    }

}
