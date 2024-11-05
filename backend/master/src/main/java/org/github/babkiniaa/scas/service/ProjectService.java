package org.github.babkiniaa.scas.service;

import lombok.AllArgsConstructor;
import net.sourceforge.pmd.reporting.RuleViolation;
import org.github.babkiniaa.scas.Mapper.ProjectMapper;
import org.github.babkiniaa.scas.dto.GetProjectDto;
import org.github.babkiniaa.scas.dto.ProjectDto;
import org.github.babkiniaa.scas.dto.ReportSpotBugsDto;
import org.github.babkiniaa.scas.entity.*;
import org.github.babkiniaa.scas.entity.reportsEntity.RuleViolationCustom;
import org.github.babkiniaa.scas.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final ReportOWASPRepository reportOWASPRepository;
    private final ReportCheckStyleRepository reportCheckStyleRepository;
    private final ReportSpotBugsRepository reportSpotBugsRepository;
    private final ProjectMapper projectMapper;

    /**
     * Создает новый проект для пользователя с указанным идентификатором.
     *
     * @param projectDto DTO с данными проекта для создания.
     * @param id         идентификатор пользователя, которому будет принадлежать проект.
     */
    public int create(ProjectDto projectDto, long id) {
        Project project = projectMapper.projectToEntity(projectDto);
        project.setUserId(id);
        return projectRepository.save(project).getId();
    }

    public void connectingReportPMDAndProject(int projectId, int reportId) {
        Project project = projectRepository.findById(projectId).get();
        List<ReportPMD> reportPMDS = new ArrayList<>();
        if (project.getReportPMDS() != null) {
            reportPMDS = project.getReportPMDS();
        }
        if (!reportPMDRepository.findById(reportId).isEmpty()) {
            ReportPMD reportPMD = reportPMDRepository.findById(reportId).get();
            reportPMDS.add(reportPMD);
            project.setReportPMDS(reportPMDS);
            projectRepository.save(project);
        }
    }

    public void connectingReportOWASPAndProject(int projectId, int reportId) {
        Project project = projectRepository.findById(projectId).get();
        List<ReportOWASP> reportOWASPS = new ArrayList<>();
        if (project.getReportOWASPS() != null) {
            reportOWASPS = project.getReportOWASPS();
        }
        if (!reportOWASPRepository.findById(reportId).isEmpty()) {
            ReportOWASP reportOWASP = reportOWASPRepository.findById(reportId).get();
            reportOWASPS.add(reportOWASP);
            project.setReportOWASPS(reportOWASPS);
            projectRepository.save(project);
        }
    }

    public void connectingReportCheckstyleAndProject(int projectId, int reportId) {
        Project project = projectRepository.findById(projectId).get();
        List<ReportCheckStyle> reportCheckStyles = new ArrayList<>();
        if (project.getReportCheckStyles() != null) {
            reportCheckStyles = project.getReportCheckStyles();
        }
        if (!reportCheckStyleRepository.findById(reportId).isEmpty()) {
            ReportCheckStyle reportCheckStyle = reportCheckStyleRepository.findById(reportId).get();
            reportCheckStyles.add(reportCheckStyle);
            project.setReportCheckStyles(reportCheckStyles);
            projectRepository.save(project);
        }
    }

    public void connectingReportSpotBugsAndProject(int projectId, int reportId) {
        Project project = projectRepository.findById(projectId).get();
        List<ReportSpotBugs> reportSpotBugs = new ArrayList<>();
        if (project.getReportSpotBugs() != null) {
            reportSpotBugs = project.getReportSpotBugs();
        }
        if (!reportSpotBugsRepository.findById(reportId).isEmpty()) {
            ReportSpotBugs reportSpotBug = reportSpotBugsRepository.findById(reportId).get();
            reportSpotBugs.add(reportSpotBug);
            project.setReportSpotBugs(reportSpotBugs);
            projectRepository.save(project);
        }
    }

    public List<ProjectDto> findAll() {
        return projectMapper.projectToListDto(projectRepository.findAll());
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
                Sort.by(Sort.Direction.fromString(projectsDto.getSortDirection()), projectsDto.getSortingField())
        );

        return projectsDto.isMyProject()
                ? projectRepository
                .findAllByNameContainingAndUserId(projectsDto.getName(), projectsDto.getUserId(), pageable).getContent()
                : projectRepository
                .findByNameContainingAndUserIdAndVisibility(projectsDto.getName(), projectsDto.getUserId(), true, pageable).getContent();
    }
}
