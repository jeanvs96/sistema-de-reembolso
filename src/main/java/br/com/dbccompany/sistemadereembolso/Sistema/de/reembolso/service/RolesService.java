package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.RolesEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.RolesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RolesService {
    private final RolesRepository rolesRepository;

    public RolesEntity findByRole(String nome) throws RegraDeNegocioException {
        return rolesRepository.findRolesEntitiesByNome(nome)
                .orElseThrow(() -> new RegraDeNegocioException("Role não encontrada"));
    }
}
