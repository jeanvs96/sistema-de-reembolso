package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.arquivos.AnexoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.paginacao.PageDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoCreateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioComposeDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.StatusReembolso;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.EntidadeNaoEncontradaException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.ReembolsoRepository;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public ReembolsoDTO save(ReembolsoCreateDTO reembolsoCreateDTO) throws EntidadeNaoEncontradaException {
        UsuarioEntity usuarioLogadoEntity = usuarioService.getLoggedUser();

        ReembolsoEntity reembolsoEntity = createToEntity(reembolsoCreateDTO);
        reembolsoEntity.setDataEntrada(LocalDateTime.now());
        reembolsoEntity.setUsuarioEntity(usuarioLogadoEntity);
        reembolsoEntity.setStatus(StatusReembolso.ABERTO.ordinal());

        ReembolsoEntity reembolsoSavedEntity = reembolsoRepository.save(reembolsoEntity);

        usuarioLogadoEntity.setValorTotal(usuarioLogadoEntity.getValorTotal().add(reembolsoSavedEntity.getValor()));
        usuarioRepository.save(usuarioLogadoEntity);

        List<UsuarioComposeDTO> gestores = usuarioService.listGestores();

        for (UsuarioComposeDTO gestor : gestores) {
            log.info("Email de gestor enviado para: " + gestor.getEmail());
            emailService.sendEmail(reembolsoSavedEntity, gestor.getEmail());
        }

        ReembolsoDTO reembolsoDTO = entityToDTO(reembolsoSavedEntity);
        reembolsoDTO.setUsuario(usuarioService.entityToComposeDto(usuarioLogadoEntity));

        return reembolsoDTO;
    }

    public ReembolsoDTO updateGestorAprovar(Integer idReembolso, Boolean aprovado) throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        ReembolsoEntity reembolsoAtualizado;
        ReembolsoEntity reembolsoEntity = findById(idReembolso);
        UsuarioEntity usuarioLogadoEntity = reembolsoEntity.getUsuarioEntity();

        verificarStatusReembolsoPago(reembolsoEntity);

        reembolsoEntity.setDataUltimaAlteracao(LocalDateTime.now());
        if (aprovado) {
            if (StatusReembolso.REPROVADO_GESTOR.equals(StatusReembolso.values()[reembolsoEntity.getStatus()]) || StatusReembolso.REPROVADO_FINANCEIRO.equals(StatusReembolso.values()[reembolsoEntity.getStatus()])) {
                usuarioLogadoEntity.setValorTotal(usuarioLogadoEntity.getValorTotal().add(reembolsoEntity.getValor()));
            }
            reembolsoEntity.setStatus(StatusReembolso.APROVADO_GESTOR.ordinal());

            reembolsoAtualizado = reembolsoRepository.save(reembolsoEntity);

            log.info("Reembolso APROVADO pelo GESTOR.");
        } else {
            reembolsoEntity.setStatus(StatusReembolso.REPROVADO_GESTOR.ordinal());

            reembolsoAtualizado = reembolsoRepository.save(reembolsoEntity);

            usuarioLogadoEntity.setValorTotal(usuarioLogadoEntity.getValorTotal().subtract(reembolsoAtualizado.getValor()));

            log.info("Reembolso REPROVADO pelo GESTOR.");
        }

        usuarioRepository.save(usuarioLogadoEntity);

        return entityToDTO(reembolsoAtualizado);
    }

    public ReembolsoDTO updateFinanceiroPagar(Integer idReembolso, Boolean pagar) throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        ReembolsoEntity reembolsoAtualizado;
        ReembolsoEntity reembolsoEntityRecuperado = findById(idReembolso);
        UsuarioEntity usuarioEntity = reembolsoEntityRecuperado.getUsuarioEntity();

        verificarStatusReembolsoPago(reembolsoEntityRecuperado);

        reembolsoEntityRecuperado.setDataUltimaAlteracao(LocalDateTime.now());

        if (pagar) {
            reembolsoEntityRecuperado.setStatus(StatusReembolso.FECHADO_PAGO.ordinal());

            reembolsoAtualizado = reembolsoRepository.save(reembolsoEntityRecuperado);

            log.info("Reembolso FECHADO E PAGO pelo FINANCEIRO.");
        } else {
            reembolsoEntityRecuperado.setStatus(StatusReembolso.REPROVADO_FINANCEIRO.ordinal());

            reembolsoAtualizado = reembolsoRepository.save(reembolsoEntityRecuperado);

            log.info("Reembolso REPROVADO pelo FINANCEIRO.");
        }

        usuarioEntity.setValorTotal(usuarioEntity.getValorTotal().subtract(reembolsoAtualizado.getValor()));
        usuarioRepository.save(usuarioEntity);

        return entityToDTO(reembolsoAtualizado);
    }

    public ReembolsoDTO updateByIdReembolsoIdUsuario(Integer idReembolso, Integer idUsuario, ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        UsuarioEntity usuarioEntity = usuarioService.findById(idUsuario);
        ReembolsoEntity reembolsoEntityRecuperado = findByIdAndUsuarioEntity(idReembolso, usuarioEntity);

        verificarStatusReembolsoAberto(reembolsoEntityRecuperado);

        ReembolsoEntity reembolsoEntity = createToEntity(reembolsoCreateDTO);
        reembolsoEntity.setIdReembolso(idReembolso);
        reembolsoEntity.setStatus(reembolsoEntityRecuperado.getStatus());
        reembolsoEntity.setDataEntrada(reembolsoEntityRecuperado.getDataEntrada());
        reembolsoEntity.setUsuarioEntity(usuarioEntity);

        if (!(reembolsoCreateDTO.getValor().equals(reembolsoEntityRecuperado.getValor()))) {
            usuarioEntity.setValorTotal((usuarioEntity.getValorTotal().subtract(reembolsoEntityRecuperado.getValor())).add(reembolsoCreateDTO.getValor()));
            usuarioRepository.save(usuarioEntity);
        }

        ReembolsoEntity reembolsoAtualizado = reembolsoRepository.save(reembolsoEntity);

        log.info("Reembolso atualizado");

        return entityToDTO(reembolsoAtualizado);
    }

    public void deleteByIdReembolsoIdUsuario(Integer idReembolso, Integer idUsuario) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        UsuarioEntity usuarioEntity = usuarioService.findById(idUsuario);
        ReembolsoEntity reembolsoEntity = findByIdAndUsuarioEntity(idReembolso, usuarioEntity);

        verificarStatusReembolsoAberto(reembolsoEntity);

        usuarioEntity.setValorTotal(usuarioEntity.getValorTotal().subtract(reembolsoEntity.getValor()));

        reembolsoRepository.delete(reembolsoEntity);

        usuarioRepository.save(usuarioEntity);

        log.info("Reembolso deletado com sucesso.");
    }

    public PageDTO<ReembolsoDTO> listAllReembolsosByStatus(StatusReembolso statusReembolso, Integer pagina, Integer quantidadeDeRegistros) {
        Pageable pageable = PageRequest.of(pagina, quantidadeDeRegistros);
        Page<ReembolsoEntity> reembolsosEntities;

        if (statusReembolso.equals(StatusReembolso.TODOS)) {
            reembolsosEntities = reembolsoRepository.findAllOrderByStatusAndDate(pageable);
        } else {
            reembolsosEntities = reembolsoRepository.findAllByStatusOrderByDataEntradaAsc(statusReembolso.ordinal(), pageable);
        }

        List<ReembolsoDTO> reembolsoDTOS = reembolsosEntities.stream().map(this::entityToDTO).toList();

        return new PageDTO<>(reembolsosEntities.getTotalElements(), reembolsosEntities.getTotalPages(), pagina, quantidadeDeRegistros, reembolsoDTOS);
    }

    public PageDTO<ReembolsoDTO> listAllByNomeUsuario(String nome, StatusReembolso statusReembolso, Integer pagina, Integer quantidadeDeRegistros) {
        Pageable pageable = PageRequest.of(pagina, quantidadeDeRegistros);
        Page<ReembolsoEntity> reembolsoEntityPage;
        List<ReembolsoDTO> reembolsoDTOList = new ArrayList<>();

        if (statusReembolso.equals(StatusReembolso.TODOS)) {
            reembolsoEntityPage = reembolsoRepository.findAllByLikeNome(nome, pageable);
            reembolsoEntityPage.getContent().forEach(reembolsoEntity ->
                reembolsoDTOList.add(entityToDTO(reembolsoEntity))
            );
        } else {
            reembolsoEntityPage = reembolsoRepository.findAllByLikeNomeAndStatus(nome, statusReembolso.ordinal(), pageable);
            reembolsoEntityPage.forEach(reembolsoEntity ->
                reembolsoDTOList.add(entityToDTO(reembolsoEntity))
            );
        }

        return new PageDTO<>(reembolsoEntityPage.getTotalElements(), reembolsoEntityPage.getTotalPages(), pagina, quantidadeDeRegistros, reembolsoDTOList);
    }

    public PageDTO<ReembolsoDTO> listAllByLoggedUserAndStatus(StatusReembolso statusReembolso, Integer pagina, Integer quantidadeDeRegistros) throws EntidadeNaoEncontradaException {
        Page<ReembolsoEntity> reembolsosEntities;
        Pageable pageable = PageRequest.of(pagina, quantidadeDeRegistros);
        UsuarioEntity loggedUser = usuarioService.getLoggedUser();

        if (statusReembolso.equals(StatusReembolso.TODOS)) {
            reembolsosEntities = reembolsoRepository.findAllByUsuarioEntityOrderByStatusAscDataEntradaAsc(loggedUser, pageable);
        } else {
            reembolsosEntities = reembolsoRepository.findAllByUsuarioEntityAndStatusOrderByDataEntradaAsc(loggedUser, statusReembolso.ordinal(), pageable);
        }

        List<ReembolsoDTO> reembolsoDTOS = reembolsosEntities.stream().map(this::entityToDTO).toList();

        return new PageDTO<>(reembolsosEntities.getTotalElements(), reembolsosEntities.getTotalPages(), pagina, quantidadeDeRegistros, reembolsoDTOS);
    }

    public ReembolsoDTO listById(Integer idReembolso) throws EntidadeNaoEncontradaException {
        ReembolsoEntity reembolsoEntity = findById(idReembolso);

        return entityToDTO(reembolsoEntity);
    }

    public ReembolsoEntity findByIdAndUsuarioEntity(Integer idReembolso, UsuarioEntity usuarioEntity) throws RegraDeNegocioException {
        return reembolsoRepository.findByIdReembolsoAndUsuarioEntity(idReembolso, usuarioEntity)
                .orElseThrow(() -> new RegraDeNegocioException("Reembolso não encontrado"));
    }

    private ReembolsoEntity findById(Integer idReembolso) throws EntidadeNaoEncontradaException {
        return reembolsoRepository.findById(idReembolso)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Reembolso não encontrado"));
    }

    private void verificarStatusReembolsoPago(ReembolsoEntity reembolsoEntity) throws RegraDeNegocioException {
        if (reembolsoEntity.getStatus().equals(StatusReembolso.FECHADO_PAGO.ordinal())) {
            throw new RegraDeNegocioException("Não é possível atualizar reembolso com status PAGO");
        }
    }

    private void verificarStatusReembolsoAberto(ReembolsoEntity reembolsoEntity) throws RegraDeNegocioException {
        if (!reembolsoEntity.getStatus().equals(StatusReembolso.ABERTO.ordinal())) {
            throw new RegraDeNegocioException("Somente reembolsos com status ABERTO podem ser editados/excluídos");
        }
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
