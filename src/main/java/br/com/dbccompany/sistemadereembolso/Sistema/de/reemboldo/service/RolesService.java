package br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.entity.RolesEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.repository.RolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RolesService {
    private final RolesRepository rolesRepository;

    public RolesEntity findByRole(String nome) throws RegraDeNegocioException {
        return rolesRepository.findRolesEntitiesByNome(nome).orElseThrow(() -> new RegraDeNegocioException("Role não encontrada"));
    }
}
