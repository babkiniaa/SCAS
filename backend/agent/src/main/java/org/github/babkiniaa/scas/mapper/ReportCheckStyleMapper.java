package org.github.babkiniaa.scas.mapper;

import com.puppycrawl.tools.checkstyle.api.Violation;
import org.github.babkiniaa.scas.dto.reportsDto.ViolationCustomDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class ReportCheckStyleMapper {

    public static ViolationCustomDto checkStyleToCheckstyleCustom(Violation violation) {
        if ( violation == null ) {
            return null;
        }

        ViolationCustomDto violationCustomDto = new ViolationCustomDto();

        violationCustomDto.setLineNo( violation.getLineNo() );
        violationCustomDto.setColumnNo( violation.getColumnNo() );
        violationCustomDto.setColumnCharIndex( violation.getColumnCharIndex() );
        violationCustomDto.setTokenType( violation.getTokenType() );
        violationCustomDto.setModuleId( violation.getModuleId() );
        violationCustomDto.setKey( violation.getKey() );
        violationCustomDto.setSource( violation.getSourceName());

        return violationCustomDto;
    }

    public static List<ViolationCustomDto> checkStyleToCheckStyleCustomList(List<Violation> reports) {
        if ( reports == null ) {
            return null;
        }

        List<ViolationCustomDto> list = new ArrayList<ViolationCustomDto>( reports.size() );
        for ( Violation violation : reports ) {
            list.add( checkStyleToCheckstyleCustom( violation ) );
        }

        return list;
    }
}
