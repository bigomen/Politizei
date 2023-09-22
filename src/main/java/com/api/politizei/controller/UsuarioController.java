package com.api.politizei.controller;

import com.api.politizei.base.AuthenticatedController;
import com.api.politizei.constants.Constantes;
import com.api.politizei.exception.BusinessRuleException;
import com.api.politizei.exception.BusinessSecurityException;
import com.api.politizei.exception.NotFoundException;
import com.api.politizei.model.Usuario;
import com.api.politizei.repository.PersonaRepository;
import com.api.politizei.repository.UsuarioRepository;
import com.api.politizei.rest.RestPersona;
import com.api.politizei.rest.RestUsuario;
import com.api.politizei.shared.EnviaEmail;
import com.api.politizei.shared.Pagination;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@RestController
public class UsuarioController extends AuthenticatedController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PersonaRepository personaRepository;

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
    @PostMapping("admin/v1/usuario/novo")
    public ResponseEntity<Integer> novo(@RequestBody @Valid RestUsuario value) throws BusinessRuleException, BusinessSecurityException, NotFoundException {
        if(repository.existsByUsuEmail(value.getUsuEmail().toLowerCase())) throw new BusinessRuleException("Email em uso por outro usuário");
        if(!Arrays.asList(Constantes.ROLES).contains(value.getUsuPerfil())) throw new BusinessRuleException("Perfil inválido");
        Usuario model = mapper.copyToDbObject(value);
        model.setUsuEmail(model.getUsuEmail().toLowerCase());
        if(Objects.nonNull(model.getPerId())){
            if(!personaRepository.existsById(model.getPerId())) throw new NotFoundException("Persona");
        }
        repository.save(model);
        _resetSenha(model);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PutMapping("admin/v1/usuario/atualiza")
    public ResponseEntity<Integer> atualiza(@RequestBody @Valid RestUsuario value) throws BusinessSecurityException, NotFoundException, BusinessRuleException {
        Long usuId = Mapper.decryptId(value.getId());
        if(Objects.nonNull(usuId)){
            Optional<Usuario> optUsuario = repository.findById(usuId);
            if(optUsuario.isPresent()){
                if(Objects.nonNull(optUsuario.get().getUsuDataExclusao())) throw new BusinessSecurityException("Usuário desativado, não pode ser editado");
                if(!Arrays.asList(Constantes.ROLES).contains(value.getUsuPerfil())) throw new BusinessRuleException("Perfil inválido");
                if(repository.validarEmailAtualizacao(value.getUsuEmail().toLowerCase(), usuId)) throw new BusinessRuleException("Email já utilizado por outro usuário");
                if(Objects.nonNull(value.getPerId())){
                    if(!personaRepository.existsById(Mapper.decryptId(value.getPerId()))) throw new NotFoundException("Persona");
                }
                mapper.copyToDbObject(value, optUsuario.get());
                optUsuario.get().setUsuEmail(optUsuario.get().getUsuEmail().toLowerCase());
                repository.save(optUsuario.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        throw new NotFoundException("Usuário");
    }

    @Transactional
    @GetMapping("admin/v1/usuario/recupera/{id}")
    public ResponseEntity<RestUsuario> recupera(@PathVariable String id) throws BusinessSecurityException, NotFoundException {
        Optional<Usuario> optUsuario = repository.findById(Mapper.decryptId(id));
        if(optUsuario.isPresent()){
            mapper.setMaxLevel(2);
            RestUsuario rest = mapper.copyToRestObject(optUsuario.get(), RestUsuario.class);
            return new ResponseEntity<>(rest, HttpStatus.OK);
        }
        throw new NotFoundException("Usuário");
    }

    @Transactional
    @GetMapping("admin/v1/usuario/lista/{pagina}")
    public ResponseEntity<Pagination> lista(@PathVariable Integer pagina) throws BusinessSecurityException {
        Page<Usuario> usuarios = repository.findAll(pagination.queryWithPagination(pagina, Usuario.class));
        mapper.setMaxLevel(1);
        List<RestUsuario> rest = new ArrayList<>();
        for(Usuario u : usuarios.getContent()){
            RestUsuario restUsuario = mapper.copyToRestObject(u, RestUsuario.class);
            restUsuario.setPersona(mapper.copyToRestObject(u.getPersona(), RestPersona.class));
            rest.add(restUsuario);
        }
        return new ResponseEntity<>(pagination.toResponse(pagina, usuarios.getTotalPages(), rest), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("admin/v1/usuario/remove/{id}")
    public ResponseEntity<Integer> remove(@PathVariable String id) throws NotFoundException, BusinessSecurityException {
        if(Objects.nonNull(id)){
            Long perId = Mapper.decryptId(id);
            if(repository.existsById(perId)){
                repository.desativarUsuario(perId);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        throw new NotFoundException("Usuário");
    }

    @Transactional
    @GetMapping("usuario/v1/listaSimples")
    public ResponseEntity<List<RestUsuario>> listaSimples() throws BusinessSecurityException {
        List<Usuario> usuarios = repository.listaSimples();
        mapper.setMaxLevel(1);
        List<RestUsuario> rest = mapper.copyToRestObject(usuarios, RestUsuario.class);
        return new ResponseEntity<>(rest, HttpStatus.OK);
    }

    private void _resetSenha(Usuario usu) {

        usu.setUsuToken(UUID.randomUUID().toString());
        usu.setUsuTokenExpiracao(LocalDateTime.now().plusMinutes(30));
        repository.save(usu);

        try {
            EnviaEmail.Mensagem mensagem = enviaEmail.novaMensagem();

            mensagem.setAssunto(config.getProperty("email.resetsenha.assunto"));

            String corpo = config.getProperty("email.resetsenha.corpo");
            if (corpo != null) {
                corpo = corpo.replace("#usuario#", usu.getUsuNome());
                corpo = corpo.replace("#token#", usu.getUsuToken());
            }
            mensagem.setMensagem(corpo);

            mensagem.setEmailOrigem(config.getProperty("email.resetsenha.emailOrigem"));
            mensagem.setNomeOrigem(config.getProperty("email.resetsenha.nomeOrigem"));

            mensagem.setNomeDestinatario(usu.getUsuNome());
            mensagem.setEmailDestinatario(usu.getUsuEmail());

            enviaEmail.enviar(mensagem);
        } catch (Exception e) {
        }

    }
}
