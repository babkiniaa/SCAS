package org.github.babkiniaa.scas.mapper;

import org.github.babkiniaa.scas.dto.Response.ReportAndIdProjectDto;
import org.github.babkiniaa.scas.dto.Response.ReportDto;
import org.github.babkiniaa.scas.dto.reportsDto.AttentionsCustomDto;
import org.jara.core.Attentions;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttentionsMapper {

    List<AttentionsCustomDto> attentionsAttentionsCustomLists(List<Attentions> attentions);
}
