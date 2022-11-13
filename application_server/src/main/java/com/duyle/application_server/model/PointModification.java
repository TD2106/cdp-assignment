package com.duyle.application_server.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointModification {
    @NotNull
    @Positive
    private Long gameId;
    @NotEmpty
    private String gameName;
    @NotNull
    private Long pointsChanged;
    @NotNull
    @Positive
    private Long gamerId;
}
