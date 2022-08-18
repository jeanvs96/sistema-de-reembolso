package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.arquivos.AnexoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.arquivos.FotoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.paginacao.PageDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.roles.RolesDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.*;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.RolesEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.StatusReembolso;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.TipoRoles;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.ReembolsoRepository;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.UsuarioRepository;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {
    @Value("${jwt.expiration}")
    private String expiration;
    private static final String EMAIL_HOST = "dbccompany.com.br";
    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final RolesService rolesService;
    private final UsuarioRolesService usuarioRolesService;
    private final TokenService tokenService;
    private final ReembolsoRepository reembolsoRepository;


    public UsuarioLoginComSucessoDTO save(UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
        verificarHostEmail(usuarioCreateDTO.getEmail());
        verificarSeEmailExiste(usuarioCreateDTO.getEmail());

        UsuarioEntity usuarioEntity = createToEntity(usuarioCreateDTO);

        usuarioEntity.setStatus(true);
        usuarioEntity.setRolesEntities(Set.of(rolesService.findByRole(TipoRoles.COLABORADOR.getTipo())));
        usuarioEntity.setValorTotal(0.0);

        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuarioEntity);

        log.info("Usuário " + usuarioSalvo.getNome() + " com id: " + usuarioSalvo.getIdUsuario() + " foi criado com sucesso!");

        UsuarioLoginComSucessoDTO usuarioLoginComSucessoDTO = new UsuarioLoginComSucessoDTO();
        usuarioLoginComSucessoDTO.setToken(tokenService.getToken(usuarioSalvo, expiration));
        usuarioLoginComSucessoDTO.setRole(usuarioSalvo.getRolesEntities().stream().findFirst().get().getNome());
        usuarioLoginComSucessoDTO.setIdUsuario(usuarioSalvo.getIdUsuario());

        return usuarioLoginComSucessoDTO;
    }

    public UsuarioDTO saveByAdmin(UsuarioCreateDTO usuarioCreateDTO, TipoRoles role) throws RegraDeNegocioException {
        verificarHostEmail(usuarioCreateDTO.getEmail());
        verificarSeEmailExiste(usuarioCreateDTO.getEmail());

        UsuarioEntity usuarioEntity = createToEntity(usuarioCreateDTO);

        usuarioEntity.setStatus(true);
        usuarioEntity.setRolesEntities(Set.of(rolesService.findByRole(role.getTipo())));
        usuarioEntity.setValorTotal(0.0);

        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuarioEntity);

        log.info("Usuário " + usuarioSalvo.getNome() + " com id: " + usuarioSalvo.getIdUsuario() + " foi criado com sucesso!");

        return entityToDto(usuarioSalvo);
    }

    public void deleteUsuario(Integer idUsuario) throws RegraDeNegocioException {
        UsuarioEntity usuarioDeletar = findById(idUsuario);
        String nome = usuarioDeletar.getNome();

        usuarioRepository.delete(usuarioDeletar);

        log.info("Usuário " + nome + " com id: " + idUsuario + " foi deletado com sucesso!");
    }

    public PageDTO<UsuarioRolesDTO> listAll(Integer pagina, Integer quantidadeDeRegistros) {
        Pageable pageable = PageRequest.of(pagina, quantidadeDeRegistros);
        List<UsuarioRolesDTO> usuarioRolesDTOList = new ArrayList<>();
        usuarioRepository.findAll().forEach(usuarioEntity ->
                usuarioRolesDTOList.add(entityToUsuarioRolesDTO(usuarioEntity)));

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), usuarioRolesDTOList.size());
        Page<UsuarioRolesDTO> page = new PageImpl<>(usuarioRolesDTOList.subList(start, end), pageable, usuarioRolesDTOList.size());

        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, quantidadeDeRegistros, page.getContent());
    }

    public List<UsuarioComposeDTO> listGestores() {
        List<UsuarioComposeDTO> listByCargo = usuarioRepository.findAll().stream()
                .filter(user -> user.getRolesEntities()
                        .stream()
                        .anyMatch(role -> role.getNome().equalsIgnoreCase(TipoRoles.GESTOR.getTipo())))
                .map(this::entityToComposeDto)
                .toList();
        return listByCargo;
    }

    public UsuarioDTO listUsuarioLogged() throws RegraDeNegocioException {
        return entityToDto(getLoggedUser());
    }

    public UsuarioDTO atribuirRole(Integer idUsuario, TipoRoles role) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntityRecuperado = findById(idUsuario);
        RolesEntity rolesEntity = rolesService.findByRole(role.getTipo());
        usuarioRolesService.deleteAllByIdUsuario(idUsuario);

        Set<RolesEntity> rolesEntities = new HashSet<>();
        rolesEntities.addAll(usuarioEntityRecuperado.getRolesEntities());
        rolesEntities.add(rolesEntity);
        usuarioEntityRecuperado.setRolesEntities(rolesEntities);

        UsuarioEntity usuarioEntityAtualizado = usuarioRepository.save(usuarioEntityRecuperado);
        return entityToDto(usuarioEntityAtualizado);
    }

    public UsuarioEntity findById(Integer idUsuario) throws RegraDeNegocioException {
        return usuarioRepository.findById(idUsuario).orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado"));
    }

    public Optional<UsuarioEntity> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    private void verificarSeEmailExiste(String email) throws RegraDeNegocioException {
        if (findByEmail(email).isPresent()) {
            throw new RegraDeNegocioException("Email já possui cadastro");
        }
    }

    public UsuarioEntity getLoggedUser() throws RegraDeNegocioException {
        Integer idLoggedUser = getIdLoggedUser();
        UsuarioEntity usuarioEntity = findById(idLoggedUser);
        return usuarioEntity;
    }

    public Integer getIdLoggedUser() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return (Integer) principal;
    }

    public void verificarHostEmail(String email) throws RegraDeNegocioException {
        String[] emailSplit = email.split("@");
        if (!EMAIL_HOST.equals(emailSplit[1])) {
            throw new RegraDeNegocioException("Insira um email DBC válido");
        }
    }

    public void encodePassword(UsuarioEntity usuarioEntity) {
        usuarioEntity.setSenha(passwordEncoder.encode(usuarioEntity.getPassword()));
    }

    public UsuarioDTO entityToDto(UsuarioEntity usuarioEntity) {
        UsuarioDTO usuarioDTO = objectMapper.convertValue(usuarioEntity, UsuarioDTO.class);
        usuarioDTO.setFotoDTO(objectMapper.convertValue(usuarioEntity.getFotosEntity(), FotoDTO.class));
        return usuarioDTO;
    }

    public UsuarioEntity createToEntity(UsuarioCreateDTO usuarioCreateDTO) {
        UsuarioEntity usuarioEntity = objectMapper.convertValue(usuarioCreateDTO, UsuarioEntity.class);
        usuarioEntity.setStatus(true);
        encodePassword(usuarioEntity);
        return usuarioEntity;
    }

    public UsuarioComposeDTO entityToComposeDto(UsuarioEntity usuarioEntity) {
        return objectMapper.convertValue(usuarioEntity, UsuarioComposeDTO.class);
    }

    public UsuarioRolesDTO entityToUsuarioRolesDTO(UsuarioEntity usuarioEntity) {
        UsuarioRolesDTO usuarioRolesDTO = objectMapper.convertValue(usuarioEntity, UsuarioRolesDTO.class);
        usuarioRolesDTO.setRolesDTO(objectMapper.convertValue(usuarioEntity.getRolesEntities().stream().toList().get(0), RolesDTO.class));
        return usuarioRolesDTO;
    }

    public List<ReembolsoDTO> entityToListReembolsoDTO(UsuarioEntity usuarioEntity) {
        UsuarioComposeDTO usuarioComposeDTO = entityToComposeDto(usuarioEntity);
        List<ReembolsoDTO> reembolsoDTOList = usuarioEntity.getReembolsoEntities().stream()
                .map(reembolsoEntity -> {
                    ReembolsoDTO reembolsoDTO = objectMapper.convertValue(reembolsoEntity, ReembolsoDTO.class);
                    reembolsoDTO.setUsuario(usuarioComposeDTO);
                    reembolsoDTO.setStatusDoReembolso(StatusReembolso.values()[reembolsoEntity.getStatus()].getTipo());
                    reembolsoDTO.setAnexoDTO(objectMapper.convertValue(reembolsoEntity.getAnexosEntity(), AnexoDTO.class));
                    return reembolsoDTO;
                }).toList();
        return reembolsoDTOList;
    }
}
