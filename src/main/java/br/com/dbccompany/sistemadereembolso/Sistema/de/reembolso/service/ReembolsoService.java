package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.arquivos.AnexoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.paginacao.PageDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoCreateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioComposeDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.StatusReembolso;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.ReembolsoRepository;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.UsuarioRepository;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class ReembolsoService {
    private final ReembolsoRepository reembolsoRepository;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public ReembolsoDTO create(ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException {
        UsuarioEntity usuarioLogadoEntity = usuarioService.getLoggedUser();

        ReembolsoEntity reembolsoEntity = createToEntity(reembolsoCreateDTO);
        reembolsoEntity.setDataEntrada(LocalDateTime.now());
        reembolsoEntity.setUsuarioEntity(usuarioLogadoEntity);
        reembolsoEntity.setStatus(StatusReembolso.ABERTO.ordinal());

        ReembolsoEntity reembolsoSavedEntity = reembolsoRepository.save(reembolsoEntity);

        usuarioLogadoEntity.setValorTotal(usuarioLogadoEntity.getValorTotal() + reembolsoSavedEntity.getValor());
        usuarioRepository.save(usuarioLogadoEntity);

        // enviar para todos os GESTORES
        List<UsuarioComposeDTO> gestores = usuarioService.listarTodosGestores();

        for (UsuarioComposeDTO gestor : gestores) {
            log.info(gestor.getEmail());
            emailService.sendEmail(reembolsoSavedEntity, gestor.getEmail());
        }

        ReembolsoDTO reembolsoDTO = entityToDTO(reembolsoSavedEntity);
        reembolsoDTO.setUsuario(usuarioService.entityToComposeDto(usuarioLogadoEntity));

        return reembolsoDTO;
    }

    public ReembolsoDTO updateGestorAprovar(Integer idReembolso, Boolean aprovado) throws RegraDeNegocioException {
        UsuarioEntity usuarioLogadoEntity = usuarioService.getLoggedUser();
        ReembolsoEntity reembolsoAtualizado;
        ReembolsoEntity reembolsoEntity = findById(idReembolso);

        reembolsoEntity.setDataUltimaAlteracao(LocalDateTime.now());

        if (aprovado) {
            if (StatusReembolso.REPROVADO_GESTOR.equals(StatusReembolso.values()[reembolsoEntity.getStatus()]) || StatusReembolso.REPROVADO_FINANCEIRO.equals(StatusReembolso.values()[reembolsoEntity.getStatus()])) {
                usuarioLogadoEntity.setValorTotal(usuarioLogadoEntity.getValorTotal() + reembolsoEntity.getValor());
            }
            reembolsoEntity.setStatus(StatusReembolso.APROVADO_GESTOR.ordinal());

            reembolsoAtualizado = reembolsoRepository.save(reembolsoEntity);

            log.info("Solicitacao de reembolso APROVADO pelo GESTOR.");
        } else {
            reembolsoEntity.setStatus(StatusReembolso.REPROVADO_GESTOR.ordinal());

            reembolsoAtualizado = reembolsoRepository.save(reembolsoEntity);

            usuarioLogadoEntity.setValorTotal(usuarioLogadoEntity.getValorTotal() - reembolsoAtualizado.getValor());

            log.info("Solicitacao de reembolso REPROVADO pelo GESTOR.");
        }

        usuarioRepository.save(usuarioLogadoEntity);

        return entityToDTO(reembolsoAtualizado);
    }

    public ReembolsoDTO updateFinanceiroPagar(Integer idReembolso, Boolean pagar) throws RegraDeNegocioException {
        UsuarioEntity usuarioLogadoEntity = usuarioService.getLoggedUser();
        ReembolsoEntity reembolsoEntity = findById(idReembolso);
        ReembolsoEntity reembolsoAtualizado;

        if (pagar) {
            reembolsoEntity.setStatus(StatusReembolso.FECHADO_PAGO.ordinal());

            reembolsoAtualizado = reembolsoRepository.save(reembolsoEntity);

            log.info("Solicitacao de reembolso FECHADO E PAGO pelo FINANCEIRO.");
        } else {
            reembolsoEntity.setStatus(StatusReembolso.REPROVADO_FINANCEIRO.ordinal());

            reembolsoAtualizado = reembolsoRepository.save(reembolsoEntity);

            log.info("Solicitacao de reembolso REPROVADO pelo FINANCEIRO.");
        }

        usuarioLogadoEntity.setValorTotal(usuarioLogadoEntity.getValorTotal() - reembolsoAtualizado.getValor());
        usuarioRepository.save(usuarioLogadoEntity);
        reembolsoAtualizado.setDataUltimaAlteracao(LocalDateTime.now());
        return entityToDTO(reembolsoAtualizado);
    }
    public PageDTO<ReembolsoDTO> listAllReembolsosByStatus(StatusReembolso statusReembolso, Integer pagina, Integer quantidadeDeRegistros) {
        Pageable pageable = PageRequest.of(pagina, quantidadeDeRegistros);
        Page<ReembolsoEntity> reembolsosEntities;

        if (statusReembolso.equals(StatusReembolso.TODOS)) {
            reembolsosEntities = reembolsoRepository.findAllOrderByStatusAndDate(pageable);
        } else {
            reembolsosEntities = reembolsoRepository.findAllByStatusOrderByDataEntradaAsc(statusReembolso.ordinal(), pageable);
        }

        List<ReembolsoDTO> reembolsoDTOS = reembolsosEntities.stream().map(reembolsoEntity -> {
            ReembolsoDTO reembolsoDTO = entityToDTO(reembolsoEntity);
            return reembolsoDTO;
        }).toList();

        PageDTO<ReembolsoDTO> pageDTO = new PageDTO<>(reembolsosEntities.getTotalElements(), reembolsosEntities.getTotalPages(), pagina, quantidadeDeRegistros, reembolsoDTOS);

        return pageDTO;
    }

    //    =================== METODOS DO PROPRIO USUARIO LOGADO ========================

    public ReembolsoDTO updateByLoggedUser(Integer idReembolso, ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = usuarioService.getLoggedUser();
        ReembolsoEntity reembolsoEntityRecuperado = findByIdAndUsuarioEntity(idReembolso, usuarioEntity);

        ReembolsoEntity reembolsoEntity = createToEntity(reembolsoCreateDTO);
        reembolsoEntity.setIdReembolso(idReembolso);
        reembolsoEntity.setStatus(reembolsoEntityRecuperado.getStatus());
        reembolsoEntity.setDataEntrada(reembolsoEntityRecuperado.getDataEntrada());
        reembolsoEntity.setUsuarioEntity(usuarioEntity);

        ReembolsoEntity reembolsoAtualizado = reembolsoRepository.save(reembolsoEntity);

        log.info("Solicitacao de reembolso atualizado");

        return entityToDTO(reembolsoAtualizado);
    }

    public PageDTO<ReembolsoDTO> listAllByLoggedUserAndStatus(StatusReembolso statusReembolso, Integer pagina, Integer quantidadeDeRegistros) throws RegraDeNegocioException {
        Page<ReembolsoEntity> reembolsosEntities;
        Pageable pageable = PageRequest.of(pagina, quantidadeDeRegistros);
        UsuarioEntity loggedUser = usuarioService.getLoggedUser();

        if (statusReembolso.equals(StatusReembolso.TODOS)) {
            reembolsosEntities = reembolsoRepository.findAllByUsuarioEntityOrderByStatusAscDataEntradaAsc(loggedUser, pageable);
        } else {
            reembolsosEntities = reembolsoRepository.findAllByUsuarioEntityAndStatusOrderByDataEntradaAsc(loggedUser, statusReembolso.ordinal(), pageable);
        }

        List<ReembolsoDTO> reembolsoDTOS = reembolsosEntities.stream().map(reembolsoEntity -> {
            ReembolsoDTO reembolsoDTO = entityToDTO(reembolsoEntity);
            return reembolsoDTO;
        }).toList();
        PageDTO<ReembolsoDTO> pageDTO = new PageDTO<>(reembolsosEntities.getTotalElements(), reembolsosEntities.getTotalPages(), pagina, quantidadeDeRegistros, reembolsoDTOS);
        return pageDTO;}

    public ReembolsoDTO listById(Integer idReembolso) throws RegraDeNegocioException {
        ReembolsoEntity reembolsoEntity = findById(idReembolso);

        return entityToDTO(reembolsoEntity);
    }

    public PageDTO<ReembolsoDTO> deleteByLoggedUser(Integer idReembolso, Integer pagina, Integer quantidadeDeRegistros) throws RegraDeNegocioException {
        ReembolsoEntity reembolsoEntity = findByIdAndUsuarioEntity(idReembolso, usuarioService.getLoggedUser());

        reembolsoRepository.delete(reembolsoEntity);

        log.info("Reembolso deletado com sucesso.");

        return listAllByLoggedUserAndStatus(StatusReembolso.TODOS, pagina, quantidadeDeRegistros);
    }


    //  ===================== METODOS AUXILIARES ====================

//    private PageDTO<ReembolsoDTO> getPageFromList(Integer pagina, Integer quantidadeDeRegistros, Pageable pageable, List<ReembolsoDTO> reembolsoDTOList) {
//        int start = (int) pageable.getOffset();
//        int end = Math.min((start + pageable.getPageSize()), reembolsoDTOList.size());
//        Page<ReembolsoDTO> page = new PageImpl<>(reembolsoDTOList.subList(start, end), pageable, reembolsoDTOList.size());
//
//        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, quantidadeDeRegistros, page.getContent());
//    }

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
