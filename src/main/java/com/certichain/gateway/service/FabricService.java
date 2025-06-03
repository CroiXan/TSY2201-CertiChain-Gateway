package com.certichain.gateway.service;

import java.nio.charset.StandardCharsets;

import org.hyperledger.fabric.client.Contract;
import org.springframework.stereotype.Service;

@Service
public class FabricService {

    private final Contract contract;

    public FabricService(Contract contract) {
        this.contract = contract;
    }

    public String getAllAssets() throws Exception {
        return new String(contract.evaluateTransaction("GetAllAssets"), StandardCharsets.UTF_8);
    }

    public void initLedger() throws Exception {
        contract.submitTransaction("InitLedger");
    }

    public void createAsset(String id, String color, String size, String owner, String value) throws Exception {
        contract.submitTransaction("CreateAsset", id, color, size, owner, value);
    }

    public String readAsset(String id) throws Exception {
        return new String(contract.evaluateTransaction("ReadAsset", id), StandardCharsets.UTF_8);
    }

    public void transferAsset(String id, String newOwner) throws Exception {
        contract.submitTransaction("TransferAsset", id, newOwner);
    }
    
}
