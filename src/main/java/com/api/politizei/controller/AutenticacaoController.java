package com.api.politizei.controller;

import com.api.politizei.exception.BusinessRuleException;
import com.api.politizei.jwt.AccountCredentials;
import com.api.politizei.jwt.TokenAuthenticationService;
import com.api.politizei.model.Usuario;
import com.api.politizei.repository.UsuarioRepository;
import com.api.politizei.shared.EnviaEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.regex.Pattern;

@RestController
public class AutenticacaoController {

    @Autowired
    private Environment env;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private EnviaEmail enviaEmail;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean validaComplexidade(String senha) {
        Pattern ptUpperCase = Pattern.compile(".*[A-Z].*");
        Pattern ptLowerCase = Pattern.compile(".*[a-z].*");
        Pattern ptNumbers = Pattern.compile(".*\\d.*");

        boolean hasUpperCase = ptUpperCase.matcher(senha).matches();
        boolean hasLowerCase = ptLowerCase.matcher(senha).matches();
        boolean hasNumbers = ptNumbers.matcher(senha).matches();
        boolean hasLength = senha.length() > 7;

        return !(!hasLowerCase || !hasUpperCase || !hasNumbers || !hasLength);
    }

    @Transactional
    @PostMapping("/login")
    public ResponseEntity<HashMap<String, String>> loginSenha(@RequestBody AccountCredentials value) throws BusinessRuleException{
        Optional<Usuario> optUsuario = repository.findByUsuEmail(value.getUsername());
        if(optUsuario.isPresent()){
            Usuario usuario = optUsuario.get();
            if(usuario.getUsuSenha() != null && usuario.getUsuDataExclusao() == null && passwordEncoder.matches(value.getPassword(), usuario.getUsuSenha())){
                List<String> rolesGrupo = new ArrayList<>();
                rolesGrupo.add(usuario.getUsuPerfil());

                String tokenAuth = TokenAuthenticationService.generateToken(usuario.getUsuEmail(), rolesGrupo, usuario.getUsuNome(), usuario.getId(), Long.parseLong(Objects.requireNonNull(env.getProperty("security.expiration"))), env.getProperty("security.secret"));
                String tokenRefresh = TokenAuthenticationService.generateToken(usuario.getUsuEmail(), rolesGrupo, usuario.getUsuNome(), usuario.getId(), Long.parseLong(Objects.requireNonNull(env.getProperty("security.refresh.expiration"))), env.getProperty("security.secret"));

                HttpHeaders header = new HttpHeaders();
                header.add(TokenAuthenticationService.HEADER_AUTHORIZATION, tokenAuth);
                header.add(TokenAuthenticationService.HEADER_REFRESH, tokenRefresh);

                HashMap<String, String> loginStatus = new HashMap<>();
                loginStatus.put("message", "Login com sucesso");
                loginStatus.put("username", usuario.getUsuNome());
                return new ResponseEntity<>(loginStatus, header, HttpStatus.OK);
            }
        }
        throw new BusinessRuleException("Usuário/senha inválida");
    }
}
