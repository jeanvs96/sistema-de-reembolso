package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.arquivos.FotoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.*;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.RolesEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.AtivarDesativarUsuario;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.TipoRoles;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.UsuarioRepository;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuarioEntity);

        log.info("Usuário "+ usuarioSalvo.getNome()+ " com id: "+usuarioSalvo.getIdUsuario()+" foi criado com sucesso!");

        UsuarioLoginComSucessoDTO usuarioLoginComSucessoDTO = new UsuarioLoginComSucessoDTO();
        usuarioLoginComSucessoDTO.setToken(tokenService.getToken(usuarioSalvo, expiration));
        usuarioLoginComSucessoDTO.setRole(usuarioSalvo.getRolesEntities().stream().findFirst().get().getNome());

        return usuarioLoginComSucessoDTO;
    }

    public UsuarioDTO update(UsuarioUpdateDTO usuarioUpdateDTO) throws RegraDeNegocioException {
        Integer idUsuario = getIdLoggedUser();
        UsuarioEntity usuarioEntityRecuperado = findById(idUsuario);

        if (usuarioUpdateDTO.getEmail() != null) {
            verificarHostEmail(usuarioUpdateDTO.getEmail());
            verificarSeEmailExiste(usuarioUpdateDTO.getEmail());
            usuarioEntityRecuperado.setEmail(usuarioUpdateDTO.getEmail());
        }
        if (usuarioUpdateDTO.getSenha() != null) {
            usuarioEntityRecuperado.setSenha(usuarioUpdateDTO.getSenha());
            encodePassword(usuarioEntityRecuperado);
        }
        if (usuarioUpdateDTO.getNome() != null) {
            usuarioEntityRecuperado.setNome(usuarioUpdateDTO.getNome());
        }

        return entityToDto(usuarioRepository.save(usuarioEntityRecuperado));
    }

    public void deleteUsuario(Integer idUsuario) throws RegraDeNegocioException {
        UsuarioEntity usuarioDeletar = findById(idUsuario);
        String nome = usuarioDeletar.getNome();

        usuarioRepository.delete(usuarioDeletar);

        log.info("Usuário "+ nome+ " com id: "+idUsuario+" foi deletado com sucesso!");
    }

    public UsuarioDTO atribuirRole(Integer idUsuario, TipoRoles role) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntityRecuperado = findById(idUsuario);
        RolesEntity rolesEntity = rolesService.findByRole(role.getTipo());
        usuarioRolesService.deleteAllByIdUsuario(idUsuario);

        Set<RolesEntity> setRoles = usuarioEntityRecuperado.getRolesEntities();
        setRoles.add(rolesEntity);

        usuarioEntityRecuperado.setRolesEntities(setRoles);

//        if (TipoRoles.ADMINISTRADOR.getTipo().equals(rolesEntity.getNome())) {
//            usuarioEntityRecuperado.getRolesEntities().add(
//                    rolesService.findByRole(TipoRoles.GESTOR.getTipo()));
//            usuarioEntityRecuperado.getRolesEntities().add(
//                    rolesService.findByRole(TipoRoles.COLABORADOR.getTipo()));
//            usuarioEntityRecuperado.getRolesEntities().add(
//                    rolesService.findByRole(TipoRoles.FINANCEIRO.getTipo()));
//        }

        UsuarioEntity usuarioEntityAtualizado = usuarioRepository.save(usuarioEntityRecuperado);
        return entityToDto(usuarioEntityAtualizado);
    }

    public UsuarioEntity findById(Integer idUsuario) throws RegraDeNegocioException {
        return usuarioRepository.findById(idUsuario).orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado"));
    }

    public Optional<UsuarioEntity> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public UsuarioDTO findUsuarioLogged() throws RegraDeNegocioException {
        return entityToDto(getLoggedUser());
    }



    public List<UsuarioRelatorioDTO> listarUsuarios (){
        List<UsuarioRelatorioDTO> all = usuarioRepository.findAll().stream()
                .map(user-> {
                    UsuarioRelatorioDTO usuarioRelatorioDTO = objectMapper.convertValue(user, UsuarioRelatorioDTO.class);
                    usuarioRelatorioDTO.setRolesEntities(user.getRolesEntities());
                    usuarioRelatorioDTO.setReembolsoEntities(user.getReembolsoEntities());
                    return usuarioRelatorioDTO;
                })
                .toList();
        return all;
    }

    public String ativarDesativarUsuario(Integer idUsuario, AtivarDesativarUsuario ativarDesativarUsuario) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntityRecuperado = findById(idUsuario);

        if (ativarDesativarUsuario.equals(AtivarDesativarUsuario.ATIVAR)) {
            usuarioEntityRecuperado.setStatus(true);
            usuarioRepository.save(usuarioEntityRecuperado);
            return "Ativado";
        } else {
            usuarioEntityRecuperado.setStatus(false);
            usuarioRepository.save(usuarioEntityRecuperado);
            return "Desativado";
        }
    }

    //  ===================== METODOS AUXILIARES ====================

    public void encodePassword(UsuarioEntity usuarioEntity) {
        usuarioEntity.setSenha(passwordEncoder.encode(usuarioEntity.getPassword()));
    }

    private void verificarSeEmailExiste(String email) throws RegraDeNegocioException {
        if (findByEmail(email).isPresent()) {
            throw new RegraDeNegocioException("Email já possui cadastro");
        }
    }

    public void verificarHostEmail(String email) throws RegraDeNegocioException {
        String[] emailSplit = email.split("@");
        if (!EMAIL_HOST.equals(emailSplit[1])){
            throw new RegraDeNegocioException("Insira uma email DBC válido");
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

    public UsuarioLoginDTO createToLogin(UsuarioCreateDTO usuarioCreateDTO) {
        return objectMapper.convertValue(usuarioCreateDTO, UsuarioLoginDTO.class);
    }

    public UsuarioDTO saveByAdmin(UsuarioCreateDTO usuarioCreateDTO, TipoRoles role) throws RegraDeNegocioException {
        verificarHostEmail(usuarioCreateDTO.getEmail());
        verificarSeEmailExiste(usuarioCreateDTO.getEmail());

        UsuarioEntity usuarioEntity = createToEntity(usuarioCreateDTO);

        usuarioEntity.setStatus(true);
        usuarioEntity.setRolesEntities(Set.of(rolesService.findByRole(role.getTipo())));

        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuarioEntity);

        log.info("Usuário "+ usuarioSalvo.getNome()+ " com id: "+usuarioSalvo.getIdUsuario()+" foi criado com sucesso!");

        return entityToDto(usuarioEntity);
    }
}
