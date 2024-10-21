package org.github.babkiniaa.scas.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ProfileDto {

  private long id;

  private String username;

  private String email;

  private String about;
}
