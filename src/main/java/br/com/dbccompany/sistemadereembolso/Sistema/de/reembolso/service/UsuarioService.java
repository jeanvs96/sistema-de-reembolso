package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioCreateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioUpdateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.RolesEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.AtivarDesativarUsuario;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.TipoRoles;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {
    private static final String EMAIL_HOST = "dbccompany.com.br";
    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final RolesService rolesService;



    public UsuarioDTO saveUsuario(UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
        verificarHostEmail(usuarioCreateDTO.getEmail());
        verificarSeEmailExiste(usuarioCreateDTO.getEmail());

        UsuarioEntity usuarioEntity = createToEntity(usuarioCreateDTO);

        usuarioEntity.setStatus(true);

        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuarioEntity);

        log.info("Usuário "+ usuarioSalvo.getNome()+ " com id: "+usuarioSalvo.getIdUsuario()+" foi criado com sucesso!");

        return entityToDto(usuarioSalvo);
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
        if (usuarioUpdateDTO.getFoto() != null) {
            usuarioEntityRecuperado.setFoto(usuarioEntityRecuperado.getFoto());
        }

        return entityToDto(usuarioRepository.save(usuarioEntityRecuperado));
    }

    public void deleteUsuario(Integer idUsuario) throws RegraDeNegocioException {
        UsuarioEntity usuarioDeletar = findById(idUsuario);
        String nome = usuarioDeletar.getNome();

        usuarioRepository.delete(usuarioDeletar);

        log.info("Usuário "+ nome+ " com id: "+idUsuario+" foi deletado com sucesso!");
    }

    @Transactional
    public UsuarioDTO atribuirRole(Integer idUsuario, TipoRoles role) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntityRecuperado = findById(idUsuario);
        RolesEntity rolesEntity = rolesService.findByRole(role.getTipo());


        Set<RolesEntity> setRoles = usuarioEntityRecuperado.getRolesEntities();
        setRoles.add(rolesEntity);

        usuarioEntityRecuperado.setRolesEntities(setRoles);


//        usuarioEntityRecuperado.setRolesEntities(Set.of(rolesEntity));

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

    public UsuarioDTO getLoggedUser() throws RegraDeNegocioException {
        try {
            Integer idLoggedUser = getIdLoggedUser();
            UsuarioEntity byId = findById(idLoggedUser);
            return entityToDto(byId);
        }catch (RegraDeNegocioException e){
            throw new RegraDeNegocioException("Nao ha usuario logado"); // TODO - tratar melhor essa ClassCastException
        }
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

    private void verificarHostEmail(String email) throws RegraDeNegocioException {
        String[] emailSplit = email.split("@");
        if (!EMAIL_HOST.equals(emailSplit[1])){
            throw new RegraDeNegocioException("Insira uma email DBC válido");
        }
    }

    public Integer getIdLoggedUser() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return (Integer) principal;
    }

    public UsuarioDTO entityToDto(UsuarioEntity usuarioEntity) {
        return objectMapper.convertValue(usuarioEntity, UsuarioDTO.class);
    }

    public UsuarioEntity createToEntity(UsuarioCreateDTO usuarioCreateDTO) {
        UsuarioEntity usuarioEntity = objectMapper.convertValue(usuarioCreateDTO, UsuarioEntity.class);
        usuarioEntity.setStatus(true);
        encodePassword(usuarioEntity);
        return usuarioEntity;
    }
}
