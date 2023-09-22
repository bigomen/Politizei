package com.api.politizei.controller;

import com.api.politizei.base.AuthenticatedController;
import com.api.politizei.exception.BusinessRuleException;
import com.api.politizei.exception.BusinessSecurityException;
import com.api.politizei.exception.NotFoundException;
import com.api.politizei.model.Realizacao;
import com.api.politizei.repository.PersonaRepository;
import com.api.politizei.repository.RealizacaoRepository;
import com.api.politizei.rest.RestPersona;
import com.api.politizei.rest.RestRealizacao;
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
public class RealizacaoController extends AuthenticatedController {

    @Autowired
    private RealizacaoRepository repository;

    @Autowired
    private PersonaRepository personaRepository;

    @Transactional
    @PostMapping("realizacao/v1/novo")
    public ResponseEntity<Integer> novo(@RequestBody @Valid RestRealizacao value) throws BusinessSecurityException, NotFoundException, BusinessRuleException {
        if(!personaRepository.existsById(Mapper.decryptId(value.getPerId()))) throw new NotFoundException("Persona");
        if(repository.existsByRelTitulo(value.getRelTitulo())) throw new BusinessRuleException("Titulo já utilizado por outra realização");
        Realizacao realizacao = mapper.copyToDbObject(value);
        repository.save(realizacao);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PutMapping("realizacao/v1/atualiza")
    public ResponseEntity<Integer> atualiza(@RequestBody @Valid RestRealizacao value) throws BusinessSecurityException, NotFoundException, BusinessRuleException {
        Long relId = Mapper.decryptId(value.getId());
        if(Objects.nonNull(relId)){
            Optional<Realizacao> optRealizacao = repository.findById(relId);
            if(optRealizacao.isPresent()){
                if(!personaRepository.existsById(Mapper.decryptId(value.getPerId()))) throw new NotFoundException("Persona");
                if(repository.validarTituloAtualizacao(value.getRelTitulo(), relId)) throw new BusinessRuleException("Titulo já utilizado por outra realização");
                mapper.copyToDbObject(value, optRealizacao.get());
                repository.save(optRealizacao.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        throw new NotFoundException("Realização");
    }

    @GetMapping("realizacao/v1/lista/{pagina}")
    public ResponseEntity<Pagination> lista(@PathVariable Integer pagina) throws BusinessSecurityException {
        Page<Realizacao> realizacoes = repository.findAll(pagination.queryWithPagination(pagina, Realizacao.class));
        List<RestRealizacao> rest = new ArrayList<>();
        if(!realizacoes.getContent().isEmpty()){
            mapper.setMaxLevel(1);
            for(Realizacao r : realizacoes.getContent()){
                RestRealizacao restRealizacao = mapper.copyToRestObject(r, RestRealizacao.class);
                restRealizacao.setPersona(mapper.copyToRestObject(r.getPersona(), RestPersona.class));
                rest.add(restRealizacao);
            }
        }
        return new ResponseEntity<>(pagination.toResponse(pagina, realizacoes.getTotalPages(), rest), HttpStatus.OK);
    }

    @GetMapping("realizacao/v1/recupera/{id}")
    public ResponseEntity<RestRealizacao> recupera(@PathVariable String id) throws BusinessSecurityException, NotFoundException {
        Long relId = Mapper.decryptId(id);
        if(Objects.nonNull(relId)){
            mapper.setMaxLevel(2);
            Optional<Realizacao> optRealizacao = repository.findById(relId);
            if(optRealizacao.isPresent()){
                RestRealizacao rest = mapper.copyToRestObject(optRealizacao.get(), RestRealizacao.class);
                return new ResponseEntity<>(rest, HttpStatus.OK);
            }
        }
        throw new NotFoundException("Realização");
    }

    @GetMapping("realizacao/v1/listaSimples")
    public ResponseEntity<List<RestRealizacao>> listaSimples() throws BusinessSecurityException {
        List<Realizacao> realizacaos = repository.listaSimples();
        if(!realizacaos.isEmpty()){
            mapper.setMaxLevel(1);
            List<RestRealizacao> rest = mapper.copyToRestObject(realizacaos, RestRealizacao.class);
            return new ResponseEntity<>(rest, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("realizacao/v1/remove/{id}")
    public ResponseEntity<Integer> remove(@PathVariable String id) throws NotFoundException, BusinessSecurityException {
        if(Objects.nonNull(id)){
            Long perId = Mapper.decryptId(id);
            if(repository.existsById(perId)){
                try {
                    repository.deleteById(perId);
                }catch (Exception e){
                    throw new BusinessSecurityException("Não é possivel apagar realização pois está em uso por outro registro");
                }
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        throw new NotFoundException("Realização");
    }
}
