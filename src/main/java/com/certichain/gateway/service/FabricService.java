package com.certichain.gateway.service;

import java.nio.charset.StandardCharsets;

import org.hyperledger.fabric.client.Contract;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class FabricService {

    private final Contract basicContract;
    private final Contract publicContract;
    private final Contract privateContract;

    public FabricService( 
        @Qualifier("basiContract") Contract basicContract,
        @Qualifier("publicDocContract") Contract publicContract,
        @Qualifier("privateDocContract") Contract privateContract) {
        this.basicContract = basicContract;
        this.publicContract = publicContract;
        this.privateContract = privateContract;
    }

    public String getAllAssets() throws Exception {
        return new String(basicContract.evaluateTransaction("GetAllAssets"), StandardCharsets.UTF_8);
    }

    public void initLedger() throws Exception {
        basicContract.submitTransaction("InitLedger");
    }

    public void createAsset(String id, String color, String size, String owner, String value) throws Exception {
        basicContract.submitTransaction("CreateAsset", id, color, size, owner, value);
    }

    public String readAsset(String id) throws Exception {
        return new String(basicContract.evaluateTransaction("ReadAsset", id), StandardCharsets.UTF_8);
    }

    public void transferAsset(String id, String newOwner) throws Exception {
        basicContract.submitTransaction("TransferAsset", id, newOwner);
    }
    
    public void registerDocument(String documentId, String institution, String userId) throws Exception {
        publicContract.submitTransaction("RegisterDocument", documentId, institution, userId);
    }

    public String getDocumentById(String documentId) throws Exception {
        byte[] result = publicContract.evaluateTransaction("GetDocumentByID", documentId);
        return new String(result, StandardCharsets.UTF_8);
    }

    public String queryByInstitution(String institution) throws Exception {
        byte[] result = publicContract.evaluateTransaction("QueryByInstitution", institution);
        return new String(result, StandardCharsets.UTF_8);
    }

    public String queryByUser(String userId) throws Exception {
        byte[] result = publicContract.evaluateTransaction("QueryByUser", userId);
        return new String(result, StandardCharsets.UTF_8);
    }

    public String queryAuditLogs(String filterType, String filterValue, String startDate, String endDate) throws Exception {
        byte[] result = publicContract.evaluateTransaction(
                "QueryAuditLogs", filterType, filterValue, startDate, endDate);
        return new String(result, StandardCharsets.UTF_8);
    }
}
