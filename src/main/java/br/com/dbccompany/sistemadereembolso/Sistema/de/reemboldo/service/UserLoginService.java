package br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.dto.UserLoginAuthDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.entity.UserLoginEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.repository.UsuarioRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserLoginService {
    private final ObjectMapper objectMapper;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public String criptografarSenha(String senha) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        String criptografia = bCryptPasswordEncoder.encode(senha);

        return criptografia;
    }

    public String trocarSenha(UserLoginAuthDTO userLoginAuthDTO, UserLoginEntity usuarioLogadoEntity) throws Exception {
        if (userLoginAuthDTO.getLogin().equals(usuarioLogadoEntity.getLogin())) {
            usuarioLogadoEntity.setSenha(criptografarSenha(userLoginAuthDTO.getSenha()));
            usuarioRepository.save(usuarioLogadoEntity);
        } else {
            throw new Exception("Usuário ou senha inválidos");
        }
        return "Senha Alterada com Sucesso !";
    }

    public Optional<UsuarioEntity> findByLoginAndSenha(String email, String senha) {
        return usuarioRepository.findByEmailAndSenha(email, senha);
    }


    public Optional<UsuarioEntity> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }


    public Integer getIdLoggedUser() {
        Integer findUserId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return findUserId;
    }

    public UsuarioEntity getLoggedUser() throws Exception {
        return this.findById(getIdLoggedUser());
    }

    public UsuarioEntity findById(Integer idAutenticacao) throws Exception {
        return usuarioRepository.findById(idAutenticacao)
                .orElseThrow((() -> new Exception("Usuario nao encontrado")));
    }


    public UsuarioEntity findByIdUsuario(Integer idUsuario) throws Exception {
        UsuarioEntity usuario = usuarioRepository.findAll().stream()
                .filter(usuario1 -> usuario1.getId().equals(idUsuario))
                .findFirst()
                .orElseThrow(() -> new Exception("Usuário não encontrado"));
        return usuario;
    }


//    public String desativar(Integer idUsuario, TipoStatus opcao) throws Exception {
//
//        UserLoginEntity userLoginEntity = findByIdUsuario(idUsuario);
//
//        String mudarStatus = opcao.toString();
//
//        if (mudarStatus == "DESATIVAR") {
//            userLoginEntity.setStatus(false);
//            userLoginRepository.save(userLoginEntity);
//            return "login Desativado!";
//
//        } else {
//            userLoginEntity.setStatus(true);
//            userLoginRepository.save(userLoginEntity);
//            return "login Ativado!";
//        }
//
//    }

}
