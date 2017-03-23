package info.kapable.utils.configurationmanager.reporting.service.mapper;

import info.kapable.utils.configurationmanager.reporting.service.dto.ProcessTreeDTO;
import info.kapable.utils.configurationmanager.reporting.domain.Process;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper for the entity User and its DTO UserDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProcessTreeMapper {
	ProcessTreeMapper INSTANCE = Mappers.getMapper( ProcessTreeMapper.class ); 
    
    ProcessTreeDTO ProcessToProcessTreeDTO(Process process);

    List<ProcessTreeDTO> usersToUserDTOs(List<Process> processes);

    @Mapping(target = "parent", ignore = true)
    Process ProcessTreeDTOToProcess(ProcessTreeDTO processTreeDTO);

    List<Process> userDTOsToUsers(List<ProcessTreeDTO> processTreeDTOs);

    default Process processFromId(Long id) {
        if (id == null) {
            return null;
        }
        Process process = new Process();
        process.setId(id);
        return process;
    }
}
