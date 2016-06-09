package com.sdavids.config;

import com.sdavids.web.hateos.ActionResourceAssembler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Web {

    @Bean
    public ActionResourceAssembler actionResourceAssembler() {
        return new ActionResourceAssembler();
    }
}
