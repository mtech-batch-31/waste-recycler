package com.mtech.recycler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class RefreshTokenRequest {
    @JsonProperty("refreshToken")
    private String refreshToken;
}
