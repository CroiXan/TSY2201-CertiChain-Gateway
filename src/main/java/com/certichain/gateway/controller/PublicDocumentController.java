package com.certichain.gateway.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.certichain.gateway.model.PublicDocument;
import com.certichain.gateway.model.PublicDocumentAuditLog;
import com.certichain.gateway.service.FabricService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/publicdocuments")
public class PublicDocumentController {

    private final FabricService service;
    private final ObjectMapper objectMapper;

    public PublicDocumentController(FabricService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public void registerDocument(@RequestBody PublicDocument document) throws Exception {
        service.registerDocument(document.getDocumentId(), document.getInstitution(), document.getUserId());
    }

    @GetMapping("/{id}")
    public PublicDocument getDocumentById(@PathVariable String id) throws Exception {
        String result = service.getDocumentById(id);
        return objectMapper.readValue(result, new TypeReference<PublicDocument>() {});
    }

    @GetMapping("/institution/{institution}")
    public List<PublicDocument> queryByInstitution(@PathVariable String institution) throws Exception {
        String result = service.queryByInstitution(institution);
        return objectMapper.readValue(result, new TypeReference<List<PublicDocument>>() {});
    }

    @GetMapping("/user/{userId}")
    public List<PublicDocument> queryByUser(@PathVariable String userId) throws Exception {
        String result = service.queryByUser(userId);
        return objectMapper.readValue(result, new TypeReference<List<PublicDocument>>() {});
    }

    @GetMapping("/audit")
    public List<PublicDocumentAuditLog> queryAuditLogs(
        @RequestParam String filterType,
        @RequestParam String filterValue,
        @RequestParam String startDate,
        @RequestParam String endDate
    ) throws Exception {
        String result = service.queryAuditLogs(filterType, filterValue, startDate, endDate);
        return objectMapper.readValue(result, new TypeReference<List<PublicDocumentAuditLog>>() {});
    }
    
}
