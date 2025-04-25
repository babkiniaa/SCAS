package org.github.babkiniaa.scas.Mapper.Custom;

import org.github.babkiniaa.scas.dto.reportsDto.AttentionsCustomDto;
import org.jara.core.Attentions;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportCopyCheckMapper {
    AttentionsCustomDto ATTENTIONS_CUSTOM_DTO(Attentions attentions);

    List<AttentionsCustomDto> ATTENTIONS_CUSTOM_DTO_LIST(List<Attentions> attentions);
}
