package entiites;

import lombok.Data;


@Data

public class AccountCredential {

    private Long id;

    private String email;

    private String accessToken;
    private String expiresIn;
    private String scope;
    private String tokenType;
}
