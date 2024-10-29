package org.github.babkiniaa.scas.service;

import lombok.AllArgsConstructor;
import net.sourceforge.pmd.reporting.RuleViolation;
import org.github.babkiniaa.scas.Mapper.ProjectMapper;
import org.github.babkiniaa.scas.dto.GetProjectDto;
import org.github.babkiniaa.scas.dto.ProjectDto;
import org.github.babkiniaa.scas.entity.Project;
import org.github.babkiniaa.scas.entity.ReportPMD;
import org.github.babkiniaa.scas.entity.reportsEntity.RuleViolationCustom;
import org.github.babkiniaa.scas.repository.ProjectRepository;
import org.github.babkiniaa.scas.repository.ReportPMDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления проектами. Предоставляет методы для создания проектов,
 * получения всех проектов, а также выборки проектов по фильтрам.
 */
@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ReportPMDRepository reportPMDRepository;
    private final ProjectMapper projectMapper;

    /**
     * Создает новый проект для пользователя с указанным идентификатором.
     *
     * @param projectDto DTO с данными проекта для создания.
     * @param id идентификатор пользователя, которому будет принадлежать проект.
     */
    public int create(ProjectDto projectDto, long id) {
        Project project = projectMapper.projectToEntity(projectDto);
        project.setUserId(id);
        return projectRepository.save(project).getId();
    }

    public void connectingReportAndProject(int projectId, int reportId){
        Optional<Project> project = projectRepository.findById(projectId);
        if(!project.isEmpty()){
            List<ReportPMD> ruleViolations =  project.get().getReportPMDS();
            if(!reportPMDRepository.findById(reportId).isEmpty()){
                ReportPMD reportPMD = reportPMDRepository.findById(reportId).get();
                ruleViolations.add(reportPMD);
            }
        }
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    /**
     * Возвращает список проектов, отфильтрованных и отсортированных по параметрам из {@link GetProjectDto}.
     * <p>
     * Если флаг {@code isMyProject} равен true, возвращаются только проекты пользователя с заданным идентификатором.
     * Если флаг {@code isMyProject} равен false, возвращаются только публичные проекты данного пользователя.
     *
     * @param projectsDto DTO с параметрами для фильтрации и сортировки проектов.
     * @return список проектов, соответствующих фильтру.
     */
    public List<Project> getAllProject(GetProjectDto projectsDto) {
        Pageable pageable = PageRequest.of(
                projectsDto.getPage(),
                projectsDto.getCount(),
                Sort.by(projectsDto.getSortingField())
        );

        return projectsDto.isMyProject()
                ? projectRepository
                .findAllByNameContainingAndUserId(projectsDto.getName(), projectsDto.getUserId(), pageable).getContent()
                : projectRepository
                .findByNameContainingAndUserIdAndVisibility(projectsDto.getName(), projectsDto.getUserId(), true, pageable).getContent();
    }
}
