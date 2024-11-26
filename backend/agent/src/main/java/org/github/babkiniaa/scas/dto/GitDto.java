package org.github.babkiniaa.scas.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class GitDto {

    private String hash;

    private String branch;
}
