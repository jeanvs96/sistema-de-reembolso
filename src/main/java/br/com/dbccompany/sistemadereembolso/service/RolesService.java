package br.com.dbccompany.sistemadereembolso.service;

import br.com.dbccompany.sistemadereembolso.entity.RolesEntity;
import br.com.dbccompany.sistemadereembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.repository.RolesRepository;
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
