package run.order66.application.service.mapper;

import run.order66.application.domain.RuleReport;
import run.order66.application.domain.User;
import run.order66.application.service.dto.RuleLastReportDTO;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper for the entity User and its DTO UserDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RuleLastReportMapper {
	RuleLastReportMapper INSTANCE = Mappers.getMapper( RuleLastReportMapper.class ); 

	RuleLastReportDTO ruleReportToRuleLastReportDTO(RuleReport ruleReport);

    List<RuleLastReportDTO> ruleReportsToRuleLastReportDTOs(List<RuleReport> ruleReports);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "rule", ignore = true)
    RuleReport ruleLastReportDTOToRuleReport(RuleLastReportDTO ruleLastReportDTO);

    List<RuleReport> RuleLastReportDTOsToRuleReports(List<RuleLastReportDTO> ruleLastReportDTO);

    default RuleReport ruleReportFromId(Long id) {
        if (id == null) {
            return null;
        }
        RuleReport rule = new RuleReport();
        rule.setId(id);
        return rule;
    }
}
