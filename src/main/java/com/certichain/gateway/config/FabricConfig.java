package com.certichain.gateway.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.identity.Identities;
import org.hyperledger.fabric.client.identity.Signer;
import org.hyperledger.fabric.client.identity.Signers;
import org.hyperledger.fabric.client.identity.X509Identity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.TlsChannelCredentials;

@Configuration
public class FabricConfig {

    @Value("${fabric.peer.endpoint}")
    private String peerEndpoint;

    @Value("${fabric.peer.host}")
    private String overrideAuthority;

    @Value("${fabric.msp.id}")
    private String mspId;

    @Value("${fabric.channel.name}")
    private String channelName;

    @Value("${fabric.chaincode.name}")
    private String chaincodeName;

    @Value("${fabric.identity.mspPath}")
    private String mspPath;

    @Bean
    public Contract fabricContract() throws Exception {
        Path cryptoPath = Paths.get(mspPath);

        Path certPath = Files.list(cryptoPath.resolve("users/User1@org1.example.com/msp/signcerts")).findFirst().get();
        Path keyPath = Files.list(cryptoPath.resolve("users/User1@org1.example.com/msp/keystore")).findFirst().get();
        Path tlsCertPath = cryptoPath.resolve("peers/peer0.org1.example.com/tls/ca.crt");

        var certificate = Identities.readX509Certificate(Files.newBufferedReader(certPath));
        var privateKey = Identities.readPrivateKey(Files.newBufferedReader(keyPath));

        X509Identity identity = new X509Identity(mspId, certificate);
        Signer signer = Signers.newPrivateKeySigner(privateKey);

        var tlsCredentials = TlsChannelCredentials.newBuilder()
                .trustManager(tlsCertPath.toFile())
                .build();

        ManagedChannel channel = Grpc.newChannelBuilder(peerEndpoint, tlsCredentials)
                .overrideAuthority(overrideAuthority)
                .build();

        Gateway gateway = Gateway.newInstance()
                .identity(identity)
                .signer(signer)
                .connection(channel)
                .connect();

        return gateway.getNetwork(channelName).getContract(chaincodeName);
    }
    
}
