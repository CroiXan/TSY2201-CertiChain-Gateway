package com.certichain.gateway.controller;

import java.lang.reflect.Type;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.certichain.gateway.model.PublicDocument;
import com.certichain.gateway.service.FabricService;
import com.google.cloud.audit.AuditLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@RestController
@RequestMapping("/publicdocuments")
public class PublicDocumentController {

    private final FabricService service;
    private final Gson gson = new Gson();

    public PublicDocumentController(FabricService service) {
        this.service = service;
    }

    @PostMapping
    public void registerDocument(@RequestBody PublicDocument document) throws Exception {
        service.registerDocument(document.getDocumentId(), document.getInstitution(), document.getUserId());
    }

    @GetMapping("/{id}")
    public PublicDocument getDocumentById(@PathVariable String id) throws Exception {
        String result = service.getDocumentById(id);
        return gson.fromJson(result, PublicDocument.class);
    }

    @GetMapping("/institution/{institution}")
    public List<PublicDocument> queryByInstitution(@PathVariable String institution) throws Exception {
        String result = service.queryByInstitution(institution);
        Type listType = new TypeToken<List<PublicDocument>>(){}.getType();
        return gson.fromJson(result, listType);
    }

    @GetMapping("/user/{userId}")
    public List<PublicDocument> queryByUser(@PathVariable String userId) throws Exception {
        String result = service.queryByUser(userId);
        Type listType = new TypeToken<List<PublicDocument>>(){}.getType();
        return gson.fromJson(result, listType);
    }

    @GetMapping("/audit")
    public List<AuditLog> queryAuditLogs(
        @RequestParam String filterType,
        @RequestParam String filterValue,
        @RequestParam String startDate,
        @RequestParam String endDate
    ) throws Exception {
        String result = service.queryAuditLogs(filterType, filterValue, startDate, endDate);
        Type listType = new TypeToken<List<AuditLog>>(){}.getType();
        return gson.fromJson(result, listType);
    }
    
}
