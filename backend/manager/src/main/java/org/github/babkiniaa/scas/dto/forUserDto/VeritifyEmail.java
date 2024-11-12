package org.github.babkiniaa.scas.dto.forUserDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VeritifyEmail {

    @NotEmpty(message = "The field is not filled in")
    @Email
    private String email;
}
