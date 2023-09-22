package com.api.politizei.controller;

import com.api.politizei.base.AuthenticatedController;
import com.api.politizei.exception.BusinessSecurityException;
import com.api.politizei.exception.NotFoundException;
import com.api.politizei.model.Interesse;
import com.api.politizei.repository.InteresseRepository;
import com.api.politizei.repository.PersonaRepository;
import com.api.politizei.rest.RestInteresse;
import com.api.politizei.rest.RestPersona;
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
public class InteresseController extends AuthenticatedController {

    @Autowired
    private InteresseRepository repository;

    @Autowired
    private PersonaRepository personaRepository;

    @Transactional
    @PostMapping("interesse/v1/novo")
    public ResponseEntity<Integer> novo(@RequestBody @Valid RestInteresse value) throws BusinessSecurityException, NotFoundException {
        if(!personaRepository.existsById(Mapper.decryptId(value.getPerId()))) throw new NotFoundException("Persona");
        Interesse model = mapper.copyToDbObject(value);
        repository.save(model);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PutMapping("interesse/v1/atualiza")
    public ResponseEntity<Integer> atualiza(@RequestBody @Valid RestInteresse value) throws BusinessSecurityException, NotFoundException {
        Long intId = Mapper.decryptId(value.getId());
        if(Objects.nonNull(intId)){
            Optional<Interesse> optInteresse = repository.findById(intId);
            if(optInteresse.isPresent()){
                if(!personaRepository.existsById(Mapper.decryptId(value.getPerId()))) throw new NotFoundException("Persona");
                mapper.copyToDbObject(value, optInteresse.get());
                repository.save(optInteresse.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        throw new NotFoundException("Interesse");
    }

    @Transactional
    @GetMapping("interesse/v1/recupera/{id}")
    public ResponseEntity<RestInteresse> recupera(@PathVariable String id) throws BusinessSecurityException, NotFoundException {
        Optional<Interesse> optInteresse = repository.findById(Mapper.decryptId(id));
        if(optInteresse.isPresent()){
            mapper.setMaxLevel(2);
            RestInteresse rest = mapper.copyToRestObject(optInteresse.get(), RestInteresse.class);
            return new ResponseEntity<>(rest, HttpStatus.OK);
        }
        throw new NotFoundException("Interesse");
    }

    @Transactional
    @GetMapping("interesse/v1/lista/{pagina}")
    public ResponseEntity<Pagination> lista(@PathVariable Integer pagina) throws BusinessSecurityException {
        Page<Interesse> interesses = repository.findAll(pagination.queryWithPagination(pagina, Interesse.class));
        mapper.setMaxLevel(1);
        List<RestInteresse> rest = new ArrayList<>();
        for(Interesse i : interesses.getContent()){
            RestInteresse restInteresse = mapper.copyToRestObject(i, RestInteresse.class);
            restInteresse.setPersona(mapper.copyToRestObject(i.getPersona(), RestPersona.class));
            rest.add(restInteresse);
        }
        return new ResponseEntity<>(pagination.toResponse(pagina, interesses.getTotalPages(), rest), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("interesse/v1/remove/{id}")
    public ResponseEntity<Integer> remove(@PathVariable String id) throws NotFoundException, BusinessSecurityException {
        if (Objects.nonNull(id)) {
            Long perId = Mapper.decryptId(id);
            if (repository.existsById(perId)) {
                try {
                    repository.deleteById(perId);
                } catch (Exception e) {
                    throw new BusinessSecurityException("Não é possivel apagar interesse pois está em uso por outro registro");
                }
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        throw new NotFoundException("Interesse");
    }

    @Transactional
    @GetMapping("interesse/v1/listaSimples")
    public ResponseEntity<List<RestInteresse>> listaSimples() throws BusinessSecurityException {
        List<Interesse> interesses = repository.listaSimples();
        mapper.setMaxLevel(1);
        List<RestInteresse> rest = mapper.copyToRestObject(interesses, RestInteresse.class);
        return new ResponseEntity<>(rest, HttpStatus.OK);
    }
}
