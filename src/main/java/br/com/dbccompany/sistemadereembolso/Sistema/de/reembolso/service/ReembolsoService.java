package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.paginacao.PageDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        reembolsoEntity.setStatus(StatusReembolso.ABERTO.ordinal());

        ReembolsoDTO reembolsoDTO = entityToDTO(reembolsoRepository.save(reembolsoEntity));
        reembolsoDTO.setUsuario(usuarioService.entityToComposeDto(usuarioLogadoEntity));

        return reembolsoDTO;
    }

    public ReembolsoDTO updateGestorAprovar(Integer idReembolso, Boolean aprovado) throws RegraDeNegocioException {
        ReembolsoEntity reembolsoEntity = reembolsoRepository.findById(idReembolso).get();

        if (aprovado) {
            reembolsoEntity.setData(LocalDateTime.now());
            reembolsoEntity.setStatus(StatusReembolso.APROVADO_GESTOR.ordinal());

            ReembolsoEntity savedEntity = reembolsoRepository.save(reembolsoEntity);
            log.info("Solicitacao de reembolso APROVADO pelo GESTOR.");

            return entityToDTO(savedEntity);
        } else {
            reembolsoEntity.setData(LocalDateTime.now());
            reembolsoEntity.setStatus(StatusReembolso.REPROVADO_GESTOR.ordinal());

            ReembolsoEntity savedEntity = reembolsoRepository.save(reembolsoEntity);
            log.info("Solicitacao de reembolso REPROVADO pelo GESTOR.");

            return entityToDTO(savedEntity);
        }
    }

    public ReembolsoDTO updateFinanceiroPagar(Integer idReembolso, Boolean pagar) throws RegraDeNegocioException {
        ReembolsoEntity reembolsoEntity = reembolsoRepository.findById(idReembolso).get();

        if (pagar) {
            reembolsoEntity.setData(LocalDateTime.now());
            reembolsoEntity.setStatus(StatusReembolso.FECHADO_PAGO.ordinal());

            ReembolsoEntity savedEntity = reembolsoRepository.save(reembolsoEntity);
            log.info("Solicitacao de reembolso FECHADO E PAGO pelo FINANCEIRO.");

            return entityToDTO(savedEntity);
        } else {
            reembolsoEntity.setData(LocalDateTime.now());
            reembolsoEntity.setStatus(StatusReembolso.REPROVADO_FINANCEIRO.ordinal());

            ReembolsoEntity savedEntity = reembolsoRepository.save(reembolsoEntity);
            log.info("Solicitacao de reembolso REPROVADO pelo FINANCEIRO.");

            return entityToDTO(savedEntity);
        }
    }

    public PageDTO<ReembolsoDTO> findAllReembolsos(Integer pagina, Integer quantidadeDeRegistros) {
        Pageable pageable = PageRequest.of(pagina, quantidadeDeRegistros);
        Page<ReembolsoEntity> page = reembolsoRepository.findAllByOrderByStatusAscDataAsc(pageable);
        List<ReembolsoDTO> reembolsoDTOS = page.stream()
                .map(reembolsoEntity -> {
                    ReembolsoDTO reembolsoDTO = entityToDTO(reembolsoEntity);
                    return reembolsoDTO;
                }).toList();
        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, quantidadeDeRegistros, reembolsoDTOS);
    }

    //    =================== METODOS DO PROPRIO USUARIO LOGADO ========================

    public ReembolsoDTO updateByLoggedUser(Integer idReembolso, ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException {
        findById(idReembolso);

        ReembolsoEntity reembolsoEntity = createToEntity(reembolsoCreateDTO);

        ReembolsoEntity reembolsoAtualizado = reembolsoRepository.save(reembolsoEntity);

        log.info("Solicitacao de reembolso atualizado");

        return entityToDTO(reembolsoAtualizado);
    }

    public PageDTO<ReembolsoDTO> listByLoggedUser(Integer pagina, Integer quantidadeDeRegistros) throws RegraDeNegocioException {
        Pageable pageable = PageRequest.of(pagina, quantidadeDeRegistros);
        Page<ReembolsoEntity> page = reembolsoRepository.findAllByUsuarioEntityOrderByStatusAscDataAsc(usuarioService.getLoggedUser(), pageable);
        List<ReembolsoDTO> reembolsoDTOS = page.stream()
                .map(reembolsoEntity -> {
                    ReembolsoDTO reembolsoDTO = entityToDTO(reembolsoEntity);
                    return reembolsoDTO;
                }).toList();
        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, quantidadeDeRegistros, reembolsoDTOS);
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

    public ReembolsoEntity findById(Integer idReembolso) throws RegraDeNegocioException {
        return reembolsoRepository.findById(idReembolso).orElseThrow(() -> new RegraDeNegocioException("Reembolso não encontrado"));
    }

    private ReembolsoEntity createToEntity(ReembolsoCreateDTO reembolsoCreateDTO) {
        return objectMapper.convertValue(reembolsoCreateDTO, ReembolsoEntity.class);
    }

    private ReembolsoDTO entityToDTO(ReembolsoEntity reembolsoEntity) {
        ReembolsoDTO reembolsoDTO = objectMapper.convertValue(reembolsoEntity, ReembolsoDTO.class);
        reembolsoDTO.setUsuario(usuarioService.entityToComposeDto(reembolsoEntity.getUsuarioEntity()));
        reembolsoDTO.setStatusDoReembolso(StatusReembolso.values()[reembolsoEntity.getStatus()].getTipo());
        return reembolsoDTO;
    }
}
