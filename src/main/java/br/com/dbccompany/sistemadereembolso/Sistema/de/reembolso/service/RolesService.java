package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.roles.RolesDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.RolesEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.TipoRoles;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.RolesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RolesService {
    private final RolesRepository rolesRepository;
    private final ObjectMapper objectMapper;

    public RolesEntity findByRole(String nome) throws RegraDeNegocioException {
        return rolesRepository.findRolesEntitiesByNome(nome).orElseThrow(() -> new RegraDeNegocioException("Role não encontrada"));
    }

    public RolesDTO insertRole (TipoRoles tipoRoles){
        RolesEntity rolesEntity = new RolesEntity();
        rolesEntity.setNome(tipoRoles.getTipo());
        return convertToDTO(rolesRepository.save(rolesEntity));
    }

    public void removeRole (TipoRoles tipoRoles) {
        RolesEntity roles = rolesRepository.findAll().stream()
                .filter(role -> role.getNome().equalsIgnoreCase(tipoRoles.getTipo()))
                .findFirst().get();
        rolesRepository.delete(roles);
        log.info("Role: "+tipoRoles.getTipo()+" foi removida.");
    }

    public List<RolesDTO> findAll () {
        return rolesRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }
    public RolesDTO updateRoleById (Integer idRole, TipoRoles tipoRoles){
        RolesEntity rolesEntity =  rolesRepository.findById(idRole).get();
        rolesEntity.setNome(tipoRoles.getTipo());

        return convertToDTO(rolesRepository.save(rolesEntity));
    }

//   =============== METODOS AUXILIARES ====================
    private RolesEntity convertToEntity(RolesDTO rolesDTO) {
        return objectMapper.convertValue(rolesDTO, RolesEntity.class);
    }
    private RolesDTO convertToDTO(RolesEntity rolesEntity) {
        return objectMapper.convertValue(rolesEntity, RolesDTO.class);
    }
}