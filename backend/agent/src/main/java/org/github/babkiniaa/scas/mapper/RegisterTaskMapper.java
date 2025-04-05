package org.github.babkiniaa.scas.mapper;

import org.github.babkiniaa.scas.dto.AnalyserDto;
import org.github.babkiniaa.scas.dto.Request.RegisterTaskDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterTaskMapper {
    RegisterTaskDto toRegister(AnalyserDto analyserDto);
}
