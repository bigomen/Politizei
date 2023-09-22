package com.api.politizei.controller;

import com.api.politizei.base.AuthenticatedController;
import com.api.politizei.exception.BusinessRuleException;
import com.api.politizei.exception.BusinessSecurityException;
import com.api.politizei.exception.NotFoundException;
import com.api.politizei.model.Persona;
import com.api.politizei.model.Projeto;
import com.api.politizei.repository.PersonaRepository;
import com.api.politizei.repository.ProjetoRepository;
import com.api.politizei.rest.RestPersona;
import com.api.politizei.rest.RestProjeto;
import com.api.politizei.shared.Pagination;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
public class ProjetoController extends AuthenticatedController {

    @Autowired
    private ProjetoRepository repository;

    @Autowired
    private PersonaRepository personaRepository;

    @Transactional
    @PostMapping("projeto/v1/novo")
    public ResponseEntity<Integer> novo(@RequestBody @Valid RestProjeto value) throws BusinessRuleException, NotFoundException, BusinessSecurityException {
        if(repository.existsByPrjTitulo(value.getPrjTitulo())) throw new BusinessRuleException("Já existe um projeto com este título");
        if(!personaRepository.existsById(Mapper.decryptId(value.getPerId()))) throw new NotFoundException("Persona");
        Projeto model = mapper.copyToDbObject(value);
        repository.save(model);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Transactional
    @PutMapping("projeto/v1/atualiza")
    public ResponseEntity<Integer> atualiza(@RequestBody @Valid RestProjeto value) throws BusinessSecurityException, NotFoundException, BusinessRuleException {
        Long prjId = Mapper.decryptId(value.getId());
        if(Objects.nonNull(prjId)){
            Optional<Projeto> optProjeto = repository.findById(prjId);
            if(optProjeto.isPresent()){
                if(repository.validarTituloAtualizacao(value.getPrjTitulo(), prjId)) throw new BusinessRuleException("Já existe um projeto com este título");
                if(!personaRepository.existsById(Mapper.decryptId(value.getPerId()))) throw new NotFoundException("Persona");
                mapper.copyToDbObject(value, optProjeto.get());
                repository.save(optProjeto.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        throw new NotFoundException("Projeto");
    }

    @GetMapping("projeto/v1/recupera/{id}")
    public ResponseEntity<RestProjeto> recupera(@PathVariable String id) throws BusinessSecurityException, NotFoundException {
        Optional<Projeto> optProjeto = repository.findById(Mapper.decryptId(id));
        if(optProjeto.isPresent()){
            RestProjeto rest = mapper.copyToRestObject(optProjeto.get(), RestProjeto.class);
            return new ResponseEntity<>(rest, HttpStatus.OK);
        }
        throw new NotFoundException("Projeto");
    }

    @GetMapping("projeto/v1/lista/{pagina}")
    public ResponseEntity<Pagination> lista(@PathVariable Integer pagina) throws BusinessSecurityException {
        Page<Projeto> projetos = repository.findAll(pagination.queryWithPagination(pagina, Projeto.class));
        List<RestProjeto> rest = new ArrayList<>();
        for(Projeto p : projetos.getContent()){
            Persona persona = new Persona();
            RestProjeto restProjeto = mapper.copyToRestObject(p, RestProjeto.class);
            restProjeto.setPersona(mapper.copyToRestObject(p.getPersona(), RestPersona.class));
            rest.add(restProjeto);
        }
        return new ResponseEntity<>(pagination.toResponse(pagina, projetos.getTotalPages(), rest), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("projeto/v1/remove/{id}")
    public ResponseEntity<Integer> remove(@PathVariable String id) throws NotFoundException, BusinessSecurityException {
        if(Objects.nonNull(id)){
            Long perId = Mapper.decryptId(id);
            if(repository.existsById(perId)){
                try {
                    repository.deleteById(perId);
                }catch (Exception e){
                    throw new BusinessSecurityException("Não é possivel apagar projeto pois está em uso por outro registro");
                }
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        throw new NotFoundException("Projeto");
    }

    @GetMapping("projeto/v1/listaSimples")
    public ResponseEntity<List<RestProjeto>> listaSimples() throws BusinessSecurityException {
        List<Projeto> projetos = repository.listaSimples();
        List<RestProjeto> rest = mapper.copyToRestObject(projetos, RestProjeto.class);
        return new ResponseEntity<>(rest, HttpStatus.OK);
    }
}
