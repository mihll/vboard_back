package com.mkierzkowski.vboard_back.config;

import com.mkierzkowski.vboard_back.dto.response.board.info.BoardInfoResponseDto;
import com.mkierzkowski.vboard_back.dto.response.board.my.MyBoardInfoResponseDto;
import com.mkierzkowski.vboard_back.dto.response.board.my.links.JoinedBoardLinkInfoResponseDto;
import com.mkierzkowski.vboard_back.dto.response.user.InstitutionUserResponseDto;
import com.mkierzkowski.vboard_back.dto.response.user.PersonUserResponseDto;
import com.mkierzkowski.vboard_back.model.board.Board;
import com.mkierzkowski.vboard_back.model.board.BoardMember;
import com.mkierzkowski.vboard_back.model.user.InstitutionUser;
import com.mkierzkowski.vboard_back.model.user.PersonUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

@Configuration
public class VBoardConfiguration {

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster =
                new SimpleApplicationEventMulticaster();

        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return eventMulticaster;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.LOOSE);

        //custom mappings
        modelMapper.typeMap(BoardMember.class, MyBoardInfoResponseDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getId().getBoardId(), MyBoardInfoResponseDto::setBoardId);
            mapper.map(src -> src.getBoard().getBoardMembers().size(), MyBoardInfoResponseDto::setBoardMembers);
        });
        modelMapper.typeMap(Board.class, BoardInfoResponseDto.class).addMappings(mapper ->
                mapper.map(Board::getBoardId, BoardInfoResponseDto::setBoardId));
        modelMapper.typeMap(Board.class, JoinedBoardLinkInfoResponseDto.class).addMappings(mapper ->
                mapper.map(Board::getBoardId, JoinedBoardLinkInfoResponseDto::setBoardId));
        modelMapper.typeMap(PersonUser.class, PersonUserResponseDto.class).addMappings(mapper ->
                mapper.map(src -> "person", PersonUserResponseDto::setUserType));
        modelMapper.typeMap(InstitutionUser.class, InstitutionUserResponseDto.class).addMappings(mapper ->
                mapper.map(src -> "institution", InstitutionUserResponseDto::setUserType));

        return modelMapper;
    }

    @Bean
    public Docket swaggerVBoardApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("VBoard")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mkierzkowski.vboard_back.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    /**
     * Group User contains operations related to user management such as login/logout
     */
    @Bean
    public Docket swaggerUserApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("User")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mkierzkowski.vboard_back.config"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("JWT", authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Virtual notice board - REST APIs")
                .description("Virtual notice board web app API").termsOfServiceUrl("")
                .contact(new Contact("Michał Kierzkowski", "", "242543@student.pwr.edu.pl"))
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .version("0.0.1")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("apiKey", "Authorization", "header");
    }
}
