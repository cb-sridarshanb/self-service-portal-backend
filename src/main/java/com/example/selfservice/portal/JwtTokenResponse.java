package com.example.selfservice.portal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenResponse {
    @JsonProperty(value="user")
    public PortalModel portalModel;
    public String jwtToken;
}
