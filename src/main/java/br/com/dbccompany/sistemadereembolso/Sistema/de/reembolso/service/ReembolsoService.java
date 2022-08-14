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
        UsuarioEntity usuarioLogadoEntity = getUsuarioLogadoEntity();

        ReembolsoEntity reembolsoEntity = new ReembolsoEntity();
        reembolsoEntity = convertCreateTOEntity(reembolsoCreateDTO);
        reembolsoEntity.setData(LocalDateTime.now());
        reembolsoEntity.setUsuarioEntity(usuarioLogadoEntity);
        reembolsoEntity.setStatus(StatusReembolso.ABERTO.getTipo());

        ReembolsoEntity savedEntity = reembolsoRepository.save(reembolsoEntity);

        ReembolsoDTO reembolsoDTO = convertEntityToDTO(savedEntity);
        reembolsoDTO.setId_usuario(usuarioLogadoEntity.getIdUsuario());

        return reembolsoDTO;
    }

    public ReembolsoDTO updateAdmin(Integer idReembolso, ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException {
        ReembolsoEntity reembolsoEntity = reembolsoRepository.findById(idReembolso).get();

        reembolsoEntity = convertCreateTOEntity(reembolsoCreateDTO);
        reembolsoEntity.setData(LocalDateTime.now());

        ReembolsoEntity savedEntity = reembolsoRepository.save(reembolsoEntity);
        log.info("Solicitacao de reembolso atualizado");
        return convertEntityToDTO(savedEntity);
    }

    public List<ReembolsoDTO> listAdmin() {
        return reembolsoRepository.findAll().stream()
                .map(rb->{
                    UsuarioEntity usuarioEntity = rb.getUsuarioEntity();
                    ReembolsoDTO reembolsoDTO = new ReembolsoDTO();
                    reembolsoDTO = convertEntityToDTO(rb);
                    reembolsoDTO.setId_usuario(usuarioEntity.getIdUsuario());
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
        UsuarioEntity usuarioEntity = getUsuarioLogadoEntity();
        Set<ReembolsoEntity> proprioReembolsoEntitySet = usuarioEntity.getReembolsoEntities();

        if (proprioReembolsoEntitySet.stream().anyMatch(reemb -> reemb.getIdReembolso().equals(idReembolso))) {

            ReembolsoEntity reembolsoEntity = reembolsoRepository.findById(idReembolso).get();

            reembolsoEntity = convertCreateTOEntity(reembolsoCreateDTO);
            reembolsoEntity.setData(LocalDateTime.now());

            ReembolsoEntity savedEntity = reembolsoRepository.save(reembolsoEntity);
            log.info("Solicitacao de reembolso atualizado");
            return convertEntityToDTO(savedEntity);
        } else {
            throw new RegraDeNegocioException("Reembolso informado nao pertence ao usuario logado");
        }
    }

    public Set<ReembolsoEntity> listProprio() throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = getUsuarioLogadoEntity();
        Set<ReembolsoEntity> proprioReembolsoEntity = usuarioEntity.getReembolsoEntities();
        return proprioReembolsoEntity;
    }

    public void deleteProprio(Integer idReembolso) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = getUsuarioLogadoEntity();
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

    private ReembolsoEntity convertCreateTOEntity(ReembolsoCreateDTO reembolsoCreateDTO) {
        return objectMapper.convertValue(reembolsoCreateDTO, ReembolsoEntity.class);
    }

    private ReembolsoDTO convertEntityToDTO(ReembolsoEntity reembolsoEntity) {
        return objectMapper.convertValue(reembolsoEntity, ReembolsoDTO.class);
    }

    private UsuarioEntity getUsuarioLogadoEntity() throws RegraDeNegocioException {
        Integer idUsuarioLogado = usuarioService.getIdLoggedUser();
        UsuarioEntity usuarioLogadoEntity = usuarioService.findById(idUsuarioLogado);
        return usuarioLogadoEntity;
    }
}
