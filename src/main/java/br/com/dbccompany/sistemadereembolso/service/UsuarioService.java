package br.com.dbccompany.sistemadereembolso.service;

import br.com.dbccompany.sistemadereembolso.dto.arquivos.FotoDTO;
import br.com.dbccompany.sistemadereembolso.dto.paginacao.PageDTO;
import br.com.dbccompany.sistemadereembolso.dto.roles.RolesDTO;
import br.com.dbccompany.sistemadereembolso.dto.usuario.*;
import br.com.dbccompany.sistemadereembolso.entity.RolesEntity;
import br.com.dbccompany.sistemadereembolso.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.enums.TipoRoles;
import br.com.dbccompany.sistemadereembolso.exceptions.EntidadeNaoEncontradaException;
import br.com.dbccompany.sistemadereembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.repository.UsuarioRepository;
import br.com.dbccompany.sistemadereembolso.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
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


    public UsuarioLoginComSucessoDTO save(UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
        verificarHostEmail(usuarioCreateDTO.getEmail());
        verificarSeEmailExiste(usuarioCreateDTO.getEmail());

        UsuarioEntity usuarioEntity = createToEntity(usuarioCreateDTO);

        usuarioEntity.setStatus(true);
        usuarioEntity.setRolesEntities(Set.of(rolesService.findByRole(TipoRoles.COLABORADOR.getTipo())));
        usuarioEntity.setValorTotal(new BigDecimal(0));

        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuarioEntity);

        log.info("Usu??rio " + usuarioSalvo.getNome() + " com id: " + usuarioSalvo.getIdUsuario() + " foi criado com sucesso!");

        UsuarioLoginComSucessoDTO usuarioLoginComSucessoDTO = new UsuarioLoginComSucessoDTO();
        usuarioLoginComSucessoDTO.setToken(tokenService.getToken(usuarioSalvo, expiration));
        RolesEntity rolesEntity = usuarioSalvo.getRolesEntities().stream()
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Role n??o encontrada"));
        usuarioLoginComSucessoDTO.setRole(rolesEntity.getNome());
        usuarioLoginComSucessoDTO.setIdUsuario(usuarioSalvo.getIdUsuario());

        return usuarioLoginComSucessoDTO;
    }

    public UsuarioDTO saveByAdmin(UsuarioCreateDTO usuarioCreateDTO, TipoRoles role) throws RegraDeNegocioException {
        verificarHostEmail(usuarioCreateDTO.getEmail());
        verificarSeEmailExiste(usuarioCreateDTO.getEmail());

        UsuarioEntity usuarioEntity = createToEntity(usuarioCreateDTO);

        usuarioEntity.setStatus(true);
        usuarioEntity.setRolesEntities(Set.of(rolesService.findByRole(role.getTipo())));
        usuarioEntity.setValorTotal(new BigDecimal(0));

        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuarioEntity);

        log.info("Usu??rio " + usuarioSalvo.getNome() + " com id: " + usuarioSalvo.getIdUsuario() + " foi criado com sucesso!");

        return entityToDto(usuarioSalvo);
    }

    public void deleteUsuario(Integer idUsuario) throws EntidadeNaoEncontradaException {
        UsuarioEntity usuarioDeletar = findById(idUsuario);
        String nome = usuarioDeletar.getNome();

        usuarioRepository.delete(usuarioDeletar);

        log.info("Usu??rio " + nome + " com id: " + idUsuario + " foi deletado com sucesso!");
    }

    public PageDTO<UsuarioRolesDTO> listAll(Integer pagina, Integer quantidadeDeRegistros) {
        Pageable pageable = PageRequest.of(pagina, quantidadeDeRegistros);
        List<UsuarioRolesDTO> usuarioRolesDTOList = new ArrayList<>();

        Page<UsuarioEntity> usuarioEntityPage = usuarioRepository.findAll(pageable);
        usuarioEntityPage.getContent().forEach(usuarioEntity ->
                usuarioRolesDTOList.add(entityToUsuarioRolesDTO(usuarioEntity)));

        return new PageDTO<>(usuarioEntityPage.getTotalElements(), usuarioEntityPage.getTotalPages(), pagina, quantidadeDeRegistros, usuarioRolesDTOList);
    }

    public PageDTO<UsuarioRolesDTO> listAllByNome(String nome, Integer pagina, Integer quantidadeDeRegistros) {
        Pageable pageable = PageRequest.of(pagina, quantidadeDeRegistros);
        List<UsuarioRolesDTO> usuarioRolesDTOList = new ArrayList<>();

        Page<UsuarioEntity> usuarioEntityPage = usuarioRepository.findAllByNomeContainsIgnoreCase(nome, pageable);
        usuarioEntityPage.getContent().forEach(usuarioEntity ->
                usuarioRolesDTOList.add(entityToUsuarioRolesDTO(usuarioEntity)));

        return new PageDTO<>(usuarioEntityPage.getTotalElements(), usuarioEntityPage.getTotalPages(), pagina, quantidadeDeRegistros, usuarioRolesDTOList);
    }

    public List<UsuarioComposeDTO> listGestores() {
        return usuarioRepository.findAll().stream()
                .filter(user -> user.getRolesEntities()
                        .stream()
                        .anyMatch(role -> role.getNome().equalsIgnoreCase(TipoRoles.GESTOR.getTipo())))
                .map(this::entityToComposeDto)
                .toList();
    }

    public UsuarioDTO listUsuarioLogged() throws EntidadeNaoEncontradaException {
        return entityToDto(getLoggedUser());
    }

    public UsuarioDTO atribuirRole(Integer idUsuario, TipoRoles role) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        UsuarioEntity usuarioEntityRecuperado = findById(idUsuario);
        RolesEntity rolesEntity = rolesService.findByRole(role.getTipo());
        usuarioRolesService.deleteAllByIdUsuario(idUsuario);

        Set<RolesEntity> rolesEntities = new HashSet<>(usuarioEntityRecuperado.getRolesEntities());
        rolesEntities.add(rolesEntity);
        usuarioEntityRecuperado.setRolesEntities(rolesEntities);

        UsuarioEntity usuarioEntityAtualizado = usuarioRepository.save(usuarioEntityRecuperado);
        return entityToDto(usuarioEntityAtualizado);
    }

    public UsuarioEntity findById(Integer idUsuario) throws EntidadeNaoEncontradaException {
        return usuarioRepository.findById(idUsuario).orElseThrow(() -> new EntidadeNaoEncontradaException("Usu??rio n??o encontrado"));
    }

    public Optional<UsuarioEntity> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    private void verificarSeEmailExiste(String email) throws RegraDeNegocioException {
        if (findByEmail(email).isPresent()) {
            throw new RegraDeNegocioException("Email j?? possui cadastro");
        }
    }

    public UsuarioEntity getLoggedUser() throws EntidadeNaoEncontradaException {
        Integer idLoggedUser = getIdLoggedUser();
        return findById(idLoggedUser);
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
            throw new RegraDeNegocioException("Insira um email DBC v??lido");
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
}
