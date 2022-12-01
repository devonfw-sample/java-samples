package com.devonfw.devon4j.examples.cloud.access_server.invitedGuest;

import com.devonfw.devon4j.generated.client.handler.ApiClient;
import com.devonfw.devon4j.generated.client.service.InvitedGuestApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InvitedGuestConfig {

    @Value("${MyThaiApi.host.uri:}")
    public String hostInfoBasePath;

    @Bean
    public ApiClient apiClient(){
        ApiClient client = new ApiClient();

        if(!hostInfoBasePath.isEmpty()){
            client.updateBaseUri(hostInfoBasePath);
        }

        return client;
    }

    @Bean
    public InvitedGuestApi invitedGuestApi(){
        return new InvitedGuestApi(apiClient());
    }


}