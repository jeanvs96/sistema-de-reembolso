package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.arquivos.AnexoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.paginacao.PageDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoCreateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioComposeDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.AnexosEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.StatusReembolso;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.AnexosRepository;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReembolsoService {
    private final ReembolsoRepository reembolsoRepository;
    private final UsuarioService usuarioService;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public ReembolsoDTO create(ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException {
        UsuarioEntity usuarioLogadoEntity = usuarioService.getLoggedUser();

        ReembolsoEntity reembolsoEntity = createToEntity(reembolsoCreateDTO);
        reembolsoEntity.setData(LocalDateTime.now());
        reembolsoEntity.setUsuarioEntity(usuarioLogadoEntity);
        reembolsoEntity.setStatus(StatusReembolso.ABERTO.ordinal());

        ReembolsoEntity reembolsoSavedEntity = reembolsoRepository.save(reembolsoEntity);
        // enviar para todos os GESTORES
        List<UsuarioComposeDTO> gestores = usuarioService.listarTodosGestores();
        for (UsuarioComposeDTO gestor: gestores) {
            log.info(gestor.getEmail());
            emailService.sendEmail(reembolsoSavedEntity, gestor.getEmail());
        }

        ReembolsoDTO reembolsoDTO = entityToDTO(reembolsoSavedEntity);
        reembolsoDTO.setUsuario(usuarioService.entityToComposeDto(usuarioLogadoEntity));

        return reembolsoDTO;
    }

    public ReembolsoDTO updateGestorAprovar(Integer idReembolso, Boolean aprovado) throws RegraDeNegocioException {
        ReembolsoEntity reembolsoEntity = findById(idReembolso);

        if (aprovado) {
            reembolsoEntity.setStatus(StatusReembolso.APROVADO_GESTOR.ordinal());

            ReembolsoEntity reembolsoAtualizado = reembolsoRepository.save(reembolsoEntity);

            log.info("Solicitacao de reembolso APROVADO pelo GESTOR.");

            return entityToDTO(reembolsoAtualizado);
        } else {
            reembolsoEntity.setStatus(StatusReembolso.REPROVADO_GESTOR.ordinal());

            ReembolsoEntity reembolsoAtualizado = reembolsoRepository.save(reembolsoEntity);

            log.info("Solicitacao de reembolso REPROVADO pelo GESTOR.");

            return entityToDTO(reembolsoAtualizado);
        }
    }

    public ReembolsoDTO updateFinanceiroPagar(Integer idReembolso, Boolean pagar) throws RegraDeNegocioException {
        ReembolsoEntity reembolsoEntity = findById(idReembolso);

        if (pagar) {
            reembolsoEntity.setStatus(StatusReembolso.FECHADO_PAGO.ordinal());

            ReembolsoEntity reembolsoAtualizado = reembolsoRepository.save(reembolsoEntity);

            log.info("Solicitacao de reembolso FECHADO E PAGO pelo FINANCEIRO.");

            return entityToDTO(reembolsoAtualizado);
        } else {
            reembolsoEntity.setStatus(StatusReembolso.REPROVADO_FINANCEIRO.ordinal());

            ReembolsoEntity reembolsoAtualizado = reembolsoRepository.save(reembolsoEntity);

            log.info("Solicitacao de reembolso REPROVADO pelo FINANCEIRO.");

            return entityToDTO(reembolsoAtualizado);
        }
    }

    public PageDTO<ReembolsoDTO> findAllReembolsosByStatus(List<StatusReembolso> statusReembolso, Integer pagina, Integer quantidadeDeRegistros) {
        Pageable pageable = PageRequest.of(pagina, quantidadeDeRegistros);
        List<ReembolsoDTO> reembolsoDTOList = new ArrayList<>();

        for (StatusReembolso status : statusReembolso) {
            List<ReembolsoEntity> reembolsoEntities = reembolsoRepository.findAllByStatusOrderByDataAsc(status.ordinal());
            List<ReembolsoDTO> reembolsoDTOS = reembolsoEntities.stream()
                    .map(reembolsoEntity -> {
                        ReembolsoDTO reembolsoDTO = entityToDTO(reembolsoEntity);
                        return reembolsoDTO;
                    }).toList();
            reembolsoDTOList.addAll(reembolsoDTOS);
        }

        return getPageFromList(pagina, quantidadeDeRegistros, pageable, reembolsoDTOList);
    }

    //    =================== METODOS DO PROPRIO USUARIO LOGADO ========================

    public ReembolsoDTO updateByLoggedUser(Integer idReembolso, ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = usuarioService.getLoggedUser();
        ReembolsoEntity reembolsoEntityRecuperado = findByIdAndUsuarioEntity(idReembolso, usuarioEntity);

        ReembolsoEntity reembolsoEntity = createToEntity(reembolsoCreateDTO);
        reembolsoEntity.setIdReembolso(idReembolso);
        reembolsoEntity.setStatus(reembolsoEntityRecuperado.getStatus());
        reembolsoEntity.setData(reembolsoEntityRecuperado.getData());
        reembolsoEntity.setUsuarioEntity(usuarioEntity);

        ReembolsoEntity reembolsoAtualizado = reembolsoRepository.save(reembolsoEntity);

        log.info("Solicitacao de reembolso atualizado");

        return entityToDTO(reembolsoAtualizado);
    }

    public PageDTO<ReembolsoDTO> listAllByLoggedUser(List<StatusReembolso> statusReembolso, Integer pagina, Integer quantidadeDeRegistros) throws RegraDeNegocioException {
        Pageable pageable = PageRequest.of(pagina, quantidadeDeRegistros);
        UsuarioEntity loggedUser = usuarioService.getLoggedUser();
        List<ReembolsoDTO> reembolsoDTOList = new ArrayList<>();


        for (StatusReembolso status : statusReembolso) {
            List<ReembolsoEntity> reembolsoEntities = reembolsoRepository.findAllByUsuarioEntityAndStatusOrderByDataAsc(loggedUser, status.ordinal());
            List<ReembolsoDTO> reembolsoDTOS = reembolsoEntities.stream()
                    .map(reembolsoEntity -> {
                        ReembolsoDTO reembolsoDTO = entityToDTO(reembolsoEntity);
                        return reembolsoDTO;
                    }).toList();
            reembolsoDTOList.addAll(reembolsoDTOS);
        }

        return getPageFromList(pagina, quantidadeDeRegistros, pageable, reembolsoDTOList);
    }

    public void deleteByLoggedUser(Integer idReembolso) throws RegraDeNegocioException {
        ReembolsoEntity reembolsoEntity = findByIdAndUsuarioEntity(idReembolso, usuarioService.getLoggedUser());

        reembolsoRepository.delete(reembolsoEntity);

        log.info("Reembolso deletado com sucesso.");
    }


    //  ===================== METODOS AUXILIARES ====================
    private PageDTO<ReembolsoDTO> getPageFromList(Integer pagina, Integer quantidadeDeRegistros, Pageable pageable, List<ReembolsoDTO> reembolsoDTOList) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), reembolsoDTOList.size());
        Page<ReembolsoDTO> page = new PageImpl<>(reembolsoDTOList.subList(start, end), pageable, reembolsoDTOList.size());

        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, quantidadeDeRegistros, page.getContent());
    }

    public ReembolsoEntity findByIdAndUsuarioEntity(Integer idReembolso, UsuarioEntity usuarioEntity) throws RegraDeNegocioException {
        return reembolsoRepository.findByIdReembolsoAndUsuarioEntity(idReembolso, usuarioEntity).orElseThrow(() -> new RegraDeNegocioException("Reembolso não encontrado"));
    }

    private ReembolsoEntity findById(Integer idReembolso) throws RegraDeNegocioException {
        return reembolsoRepository.findById(idReembolso).orElseThrow(() -> new RegraDeNegocioException("Reembolso não encontrado"));
    }

    private ReembolsoEntity createToEntity(ReembolsoCreateDTO reembolsoCreateDTO) {
        return objectMapper.convertValue(reembolsoCreateDTO, ReembolsoEntity.class);
    }

    private ReembolsoDTO entityToDTO(ReembolsoEntity reembolsoEntity) {
        ReembolsoDTO reembolsoDTO = objectMapper.convertValue(reembolsoEntity, ReembolsoDTO.class);
        reembolsoDTO.setUsuario(usuarioService.entityToComposeDto(reembolsoEntity.getUsuarioEntity()));
        reembolsoDTO.setStatusDoReembolso(StatusReembolso.values()[reembolsoEntity.getStatus()].getTipo());
        reembolsoDTO.setAnexoDTO(objectMapper.convertValue(reembolsoEntity.getAnexosEntity(), AnexoDTO.class));
        return reembolsoDTO;
    }
}
