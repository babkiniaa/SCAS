package org.github.babkiniaa.scas.dto.Response;

import lombok.Getter;
import lombok.Setter;
import org.github.babkiniaa.scas.entity.StatusTask;

@Getter
@Setter
public class TaskInQueueDto {

    private long id;

    private String statusTask;
}
