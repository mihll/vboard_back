package com.mkierzkowski.vboard_back.dto.request.board;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeBoardOrderRequestDto {

    @NotNull(message = "{constraints.NotNull.message}")
    List<String> boardIds;
}
