package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoCreateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.StatusReembolso;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.ReembolsoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReembolsoService {
    private final ReembolsoRepository reembolsoRepository;
    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper;

    public ReembolsoDTO create(ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException {
        UsuarioEntity usuarioLogadoEntity = usuarioService.getLoggedUser();

        ReembolsoEntity reembolsoEntity = createToEntity(reembolsoCreateDTO);
        reembolsoEntity.setData(LocalDateTime.now());
        reembolsoEntity.setUsuarioEntity(usuarioLogadoEntity);
        reembolsoEntity.setStatus(StatusReembolso.ABERTO.getTipo());

        ReembolsoDTO reembolsoDTO = entityToDTO(reembolsoRepository.save(reembolsoEntity));
        reembolsoDTO.setUsuario(usuarioService.entityToComposeDto(usuarioLogadoEntity));

        return reembolsoDTO;
    }

    public ReembolsoDTO updateAdmin(Integer idReembolso, ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException {
        ReembolsoEntity reembolsoEntity = reembolsoRepository.findById(idReembolso).get();

        reembolsoEntity = createToEntity(reembolsoCreateDTO);
        reembolsoEntity.setData(LocalDateTime.now());

        ReembolsoEntity savedEntity = reembolsoRepository.save(reembolsoEntity);
        log.info("Solicitacao de reembolso atualizado");
        return entityToDTO(savedEntity);
    }

    public ReembolsoDTO updateGestorAprovar(Integer idReembolso, Boolean aprovado) throws RegraDeNegocioException {
        ReembolsoEntity reembolsoEntity = reembolsoRepository.findById(idReembolso).get();

        if(aprovado){
            reembolsoEntity.setData(LocalDateTime.now());
            reembolsoEntity.setStatus(StatusReembolso.APROVADO_GESTOR.getTipo());

            ReembolsoEntity savedEntity = reembolsoRepository.save(reembolsoEntity);
            log.info("Solicitacao de reembolso APROVADO pelo GESTOR.");

            return entityToDTO(savedEntity);
        } else {
            reembolsoEntity.setData(LocalDateTime.now());
            reembolsoEntity.setStatus(StatusReembolso.REPROVADO_GESTOR.getTipo());

            ReembolsoEntity savedEntity = reembolsoRepository.save(reembolsoEntity);
            log.info("Solicitacao de reembolso REPROVADO pelo GESTOR.");

            return entityToDTO(savedEntity);
        }
    }
    public ReembolsoDTO updateFinanceiroPagar(Integer idReembolso, Boolean pagar) throws RegraDeNegocioException {
        ReembolsoEntity reembolsoEntity = reembolsoRepository.findById(idReembolso).get();

        if(pagar){
            reembolsoEntity.setData(LocalDateTime.now());
            reembolsoEntity.setStatus(StatusReembolso.FECHADO_PAGO.getTipo());

            ReembolsoEntity savedEntity = reembolsoRepository.save(reembolsoEntity);
            log.info("Solicitacao de reembolso FECHADO E PAGO pelo FINANCEIRO.");

            return entityToDTO(savedEntity);
        } else {
            reembolsoEntity.setData(LocalDateTime.now());
            reembolsoEntity.setStatus(StatusReembolso.REPROVADO_FINANCEIRO.getTipo());

            ReembolsoEntity savedEntity = reembolsoRepository.save(reembolsoEntity);
            log.info("Solicitacao de reembolso REPROVADO pelo FINANCEIRO.");

            return entityToDTO(savedEntity);
        }
    }
    public List<ReembolsoDTO> listAdmin() {
        return reembolsoRepository.findAll().stream()
                .map(reembolsoEntity->{
                    UsuarioEntity usuarioEntity = reembolsoEntity.getUsuarioEntity();
                    ReembolsoDTO reembolsoDTO = entityToDTO(reembolsoEntity);
                    reembolsoDTO.setUsuario(usuarioService.entityToComposeDto(usuarioEntity));
                    return reembolsoDTO;
                }).toList();
    }

    public void deleteAdmin(Integer idReembolso) {
        ReembolsoEntity reembolsoEntity = reembolsoRepository.findById(idReembolso).get();
        reembolsoRepository.delete(reembolsoEntity);
        log.info("Solicitacao de reembolso deletada com sucesso.");
    }

    //    =================== METODOS DO PROPRIO USUARIO LOGADO ========================

    public ReembolsoDTO updateProprio(Integer idReembolso, ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = usuarioService.getLoggedUser();
        Set<ReembolsoEntity> proprioReembolsoEntitySet = usuarioEntity.getReembolsoEntities();

        if (proprioReembolsoEntitySet.stream().anyMatch(reemb -> reemb.getIdReembolso().equals(idReembolso))) {

            ReembolsoEntity reembolsoEntity = reembolsoRepository.findById(idReembolso).get();

            reembolsoEntity = createToEntity(reembolsoCreateDTO);
            reembolsoEntity.setData(LocalDateTime.now());

            ReembolsoEntity savedEntity = reembolsoRepository.save(reembolsoEntity);
            log.info("Solicitacao de reembolso atualizado");
            return entityToDTO(savedEntity);
        } else {
            throw new RegraDeNegocioException("Reembolso informado nao pertence ao usuario logado");
        }
    }

    public Set<ReembolsoEntity> listProprio() throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = usuarioService.getLoggedUser();
        Set<ReembolsoEntity> proprioReembolsoEntity = usuarioEntity.getReembolsoEntities();
        return proprioReembolsoEntity;
    }

    public void deleteProprio(Integer idReembolso) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = usuarioService.getLoggedUser();
        Set<ReembolsoEntity> proprioReembolsoEntitySet = usuarioEntity.getReembolsoEntities();

        if (proprioReembolsoEntitySet.stream().anyMatch(reemb -> reemb.getIdReembolso().equals(idReembolso))) {

            ReembolsoEntity reembolsoEntity = reembolsoRepository.findById(idReembolso).get();

            reembolsoRepository.delete(reembolsoEntity);
            log.info("Solicitacao de reembolso deletada com sucesso.");

        } else {
            throw new RegraDeNegocioException("Reembolso informado nao pertence ao usuario logado");
        }
    }

    //  ===================== METODOS AUXILIARES ====================

    private ReembolsoEntity createToEntity(ReembolsoCreateDTO reembolsoCreateDTO) {
        return objectMapper.convertValue(reembolsoCreateDTO, ReembolsoEntity.class);
    }

    private ReembolsoDTO entityToDTO(ReembolsoEntity reembolsoEntity) {
        return objectMapper.convertValue(reembolsoEntity, ReembolsoDTO.class);
    }
}
