package com.api.politizei.controller;

import com.api.politizei.base.AuthenticatedController;
import com.api.politizei.exception.BusinessRuleException;
import com.api.politizei.exception.BusinessSecurityException;
import com.api.politizei.exception.NotFoundException;
import com.api.politizei.model.Persona;
import com.api.politizei.repository.PartidoRepository;
import com.api.politizei.repository.PersonaRepository;
import com.api.politizei.rest.RestPartido;
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
public class PersonaController extends AuthenticatedController {

    @Autowired
    private PersonaRepository repository;

    @Autowired
    private PartidoRepository partidoRepository;

    @Transactional
    @PostMapping("admin/v1/persona/novo")
    public ResponseEntity<Integer> novo(@RequestBody @Valid RestPersona value) throws BusinessRuleException, BusinessSecurityException, NotFoundException {
        if(repository.existsByPerNome(value.getPerNome())) throw new BusinessRuleException("Nome já utilizado por outro persona");
        if(repository.existsByPerAlias(value.getPerAlias())) throw new BusinessRuleException("Alias já utilizado por outro persona");
        if(!partidoRepository.existsById(Mapper.decryptId(value.getParId()))) throw new NotFoundException("Partido");
        Persona model = mapper.copyToDbObject(value);
        repository.save(model);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PutMapping("admin/v1/persona/atualiza")
    public ResponseEntity<Integer> atualiza(@RequestBody @Valid RestPersona value) throws BusinessSecurityException, NotFoundException, BusinessRuleException {
        Long perId = Mapper.decryptId(value.getId());
        if(Objects.nonNull(perId)){
            Optional<Persona> optPersona = repository.findById(perId);
            if(optPersona.isPresent()){
                if(repository.validarNomeAtualizacao(value.getPerNome(), perId)) throw new BusinessRuleException("Nome já utilizado por outro persona");
                if(repository.validarAliasAtualizacao(value.getPerAlias(), perId)) throw new BusinessRuleException("Alias já utilizado por outro persona");
                if(!partidoRepository.existsById(Mapper.decryptId(value.getParId()))) throw new NotFoundException("Partido");
                mapper.copyToDbObject(value, optPersona.get());
                repository.save(optPersona.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        throw new NotFoundException("Persona");
    }

    @Transactional
    @GetMapping("admin/v1/persona/recupera/{id}")
    public ResponseEntity<RestPersona> recupera(@PathVariable String id) throws BusinessSecurityException, NotFoundException {
        Optional<Persona> optPersona = repository.findById(Mapper.decryptId(id));{
            if(optPersona.isPresent()){
                mapper.setMaxLevel(2);
                RestPersona rest = mapper.copyToRestObject(optPersona.get(), RestPersona.class);
                return new ResponseEntity<>(rest, HttpStatus.OK);
            }
            throw new NotFoundException("Persona");
        }
    }

    @Transactional
    @GetMapping("admin/v1/persona/lista/{pagina}")
    public ResponseEntity<Pagination> lista(@PathVariable Integer pagina) throws BusinessSecurityException {
        Page<Persona> personas = repository.findAll(pagination.queryWithPagination(pagina, Persona.class));
        mapper.setMaxLevel(1);
        List<RestPersona> rest = new ArrayList<>();
        for(Persona per : personas.getContent()){
            RestPersona restPersona = mapper.copyToRestObject(per, RestPersona.class);
            restPersona.setPartido(mapper.copyToRestObject(per.getPartido(), RestPartido.class));
            rest.add(restPersona);
        }
        return new ResponseEntity<>(pagination.toResponse(pagina, personas.getTotalPages(), rest), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("admin/v1/persona/remove/{id}")
    public ResponseEntity<Integer> remove(@PathVariable String id) throws NotFoundException, BusinessSecurityException {
        if(Objects.nonNull(id)){
            Long perId = Mapper.decryptId(id);
            if(repository.existsById(perId)){
                try {
                    repository.deleteById(perId);
                }catch (Exception e){
                    throw new BusinessSecurityException("Não é possivel apagar persona pois está em uso por outro registro");
                }
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        throw new NotFoundException("Partido");
    }

    @Transactional
    @GetMapping("persona/v1/listaSimples")
    public ResponseEntity<List<RestPersona>> listaSimples() throws BusinessSecurityException {
        List<Persona> personas = repository.listaSimples();
        mapper.setMaxLevel(1);
        List<RestPersona> rest = mapper.copyToRestObject(personas, RestPersona.class);
        return new ResponseEntity<>(rest, HttpStatus.OK);
    }
}
