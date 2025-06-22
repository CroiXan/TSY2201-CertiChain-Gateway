package com.certichain.gateway.config;

import java.nio.file.Files;
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

    @Value("${fabric.identity.mspPath}")
    private String mspPath;

    @Value("${fabric.user.cert}")
    private String certPath;

    @Value("${fabric.user.key}")
    private String keyPath;

    @Value("${fabric.peer.tlsCert}")
    private String tlsCert;


    @Bean
    public Gateway gateway() throws Exception {
        var cert = Identities.readX509Certificate(Files.newBufferedReader(Paths.get(certPath)));
        var key = Identities.readPrivateKey(Files.newBufferedReader(Paths.get(keyPath)));

        X509Identity identity = new X509Identity(mspId, cert);
        Signer signer = Signers.newPrivateKeySigner(key);

        var tlsCredentials = TlsChannelCredentials.newBuilder()
                .trustManager(Paths.get(tlsCert).toFile())
                .build();

        ManagedChannel channel = Grpc.newChannelBuilder(peerEndpoint, tlsCredentials)
                .overrideAuthority(overrideAuthority)
                .build();

        return Gateway.newInstance()
                .identity(identity)
                .signer(signer)
                .connection(channel)
                .connect();
    }

    @Bean(name = "publicDocContract")
    public Contract publicDocContract(Gateway gateway) {
        return gateway.getNetwork("public-document").getContract("publicDoc");
    }

    @Bean(name = "privateDocContract")
    public Contract privateDocContract(Gateway gateway) {
        return gateway.getNetwork("private-document").getContract("privateDocument");
    }

    @Bean(name = "basiContract")
    public Contract basiContract(Gateway gateway) {
        return gateway.getNetwork("mychannel").getContract("basic");
    }

}
