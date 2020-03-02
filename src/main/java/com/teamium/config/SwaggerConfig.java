package com.teamium.config;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration class used to do the all swagger configuration.
 * 
 * @author Hareram Giri
 *
 */
@Configuration
//@EnableSwagger2
@ComponentScan(basePackages = "com.teamium.controller")
public class SwaggerConfig {

//    @Value("${teamium.swagger.client.id}")
//    private String clientId;
//   
//    @Value("${teamium.swagger.api.secret}")
//    private String clientSecret;
//
//    @Value("${host.full.dns.auth.link}")
//    private String authLink;
//    
//    @Value("${teamium.swagger.title}")
//    private String docTitle;
//    
//    @Value("${teamium.swagger.description}")
//    private String desc;
//    
//    @Value("${teamium.swagger.termsOfservice.url}")
//    private String termOfServiceUrl;
//    
//    @Value("${teamium.swagger.contact.title}")
//    private String contactTitle;
//    
//    @Value("${teamium.swagger.contact.url}")
//    private String contactTitleUrl;
//    
//    @Value("${teamium.swagger.license.title.url}")
//    private String licenseTitleUrl;
//    
//    @Value("${teamium.swagger.license.title}")
//    private String licenseTitle;
//    
//    @Value("${teamium.swagger.version}")
//    private String docVersion;
//
//    @Bean
//    public Docket api() {
//
//        List<ResponseMessage> list = new java.util.ArrayList<>();
//        list.add(new ResponseMessageBuilder().code(500).message("500 message")
//                .responseModel(new ModelRef("Result")).build());
//        list.add(new ResponseMessageBuilder().code(401).message("Unauthorized")
//                .responseModel(new ModelRef("Result")).build());
//        list.add(new ResponseMessageBuilder().code(406).message("Not Acceptable")
//                .responseModel(new ModelRef("Result")).build());
//
//        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build()
//                .securitySchemes(Collections.singletonList(securitySchema()))
//                .securityContexts(Collections.singletonList(securityContext()))
//                .pathMapping("/")
//                .useDefaultResponseMessages(false)
//                .apiInfo(apiInfo());
////                .globalResponseMessage(RequestMethod.GET, list)
////                .globalResponseMessage(RequestMethod.POST, list);
//
//    }
//
//    private OAuth securitySchema() {
//
//        List<AuthorizationScope> authorizationScopeList = newArrayList();
//        authorizationScopeList.add(new AuthorizationScope("read", "read all"));
//        authorizationScopeList.add(new AuthorizationScope("trust", "trust all"));
//        authorizationScopeList.add(new AuthorizationScope("write", "access all"));
//
//        List<GrantType> grantTypes = newArrayList();
//        GrantType creGrant = new ResourceOwnerPasswordCredentialsGrant(authLink+"/oauth/token");
//
//        grantTypes.add(creGrant);
//
//        return new OAuth("oauth2schema", authorizationScopeList, grantTypes);
//
//    }
//
//    private SecurityContext securityContext() {
//        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any())
//                .build();
//    }
//
//    private List<SecurityReference> defaultAuth() {
//
//        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[3];
//        authorizationScopes[0] = new AuthorizationScope("read", "read all");
//        authorizationScopes[1] = new AuthorizationScope("trust", "trust all");
//        authorizationScopes[2] = new AuthorizationScope("write", "write all");
//
//        return Collections.singletonList(new SecurityReference("oauth2schema", authorizationScopes));
//    }
//
//    @Bean
//    public SecurityConfiguration securityInfo() {
//        return new SecurityConfiguration(clientId, clientSecret, "", "", "", ApiKeyVehicle.HEADER, "", " ");
//    }
//
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder().
//        		title(docTitle).description(desc)
//                .termsOfServiceUrl(termOfServiceUrl)
//                .contact(new Contact(contactTitle, contactTitleUrl,""))
//                .license(licenseTitle)
//                .licenseUrl(licenseTitleUrl)
//                .version(docVersion)
//                .build();
//    }
}