package com.mkierzkowski.vboard_back.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "userType")
@JsonSubTypes({@JsonSubTypes.Type(value = PersonUserUpdateRequestDto.class, name = "person"),
        @JsonSubTypes.Type(value = InstitutionUserUpdateRequestDto.class, name = "institution")})
public abstract class AbstractUserUpdateRequestDto {
    private String userType;
}
