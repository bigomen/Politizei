package com.api.politizei.controller;

import com.api.politizei.base.AuthenticatedController;
import com.api.politizei.exception.BusinessRuleException;
import com.api.politizei.exception.BusinessSecurityException;
import com.api.politizei.exception.NotFoundException;
import com.api.politizei.model.Interesse;
import com.api.politizei.model.Seguidor;
import com.api.politizei.repository.InteresseRepository;
import com.api.politizei.repository.PersonaRepository;
import com.api.politizei.repository.SeguidorRepository;
import com.api.politizei.rest.RestInteresse;
import com.api.politizei.rest.RestPersona;
import com.api.politizei.rest.RestSeguidor;
import com.api.politizei.shared.Pagination;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
public class SeguidorController extends AuthenticatedController {

    @Autowired
    private SeguidorRepository repository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private InteresseRepository interesseRepository;

    @Transactional
    @PostMapping("seguidor/v1/novo")
    public ResponseEntity<Integer> novo(@RequestBody @Valid RestSeguidor value) throws NotFoundException, BusinessSecurityException, BusinessRuleException {
        if(!personaRepository.existsById(Mapper.decryptId(value.getPerId()))) throw new NotFoundException("Persona");
        if(StringUtils.hasText(value.getSegEmail())){
            if(repository.existsBySegEmail(value.getSegEmail())) throw new BusinessRuleException("Email já utilizado por outro seguidor");
        }
        if(StringUtils.hasText(value.getSegWhatsapp())){
            if(repository.existsBySegWhatsapp(value.getSegWhatsapp())) throw new BusinessRuleException("Número de whatsapp já utilizado por outro seguidor");
        }
        Seguidor model = mapper.copyToDbObject(value);
        if(!value.getInteresseList().isEmpty()){
            model.setInteresseList(new ArrayList<>());
            for(RestInteresse i : value.getInteresseList()){
                Long intId = Mapper.decryptId(i.getId());
                if(!interesseRepository.existsById(intId)) throw new NotFoundException("Interesse");
                Interesse interesse = new Interesse();
                interesse.setId(intId);
                model.getInteresseList().add(interesse);
            }
        }
        repository.save(model);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PutMapping("seguidor/v1/atualiza")
    public ResponseEntity<Integer> atualiza(@RequestBody @Valid RestSeguidor value) throws BusinessSecurityException, NotFoundException, BusinessRuleException {
        Long segId = Mapper.decryptId(value.getId());
        if(Objects.nonNull(segId)){
            Optional<Seguidor> optSeguidor = repository.findById(segId);
            if(optSeguidor.isPresent()){
                if(!personaRepository.existsById(Mapper.decryptId(value.getPerId()))) throw new NotFoundException("Persona");
                if(StringUtils.hasText(value.getSegEmail())){
                    if(repository.validarEmailAtualizacao(value.getSegEmail(), segId)) throw new BusinessRuleException("Email já utilizado por outro seguidor");
                }
                if(StringUtils.hasText(value.getSegWhatsapp())){
                    if(repository.validarWhatsappAtualizacao(value.getSegWhatsapp(), segId)) throw new BusinessRuleException("Número de whatsapp já utilizado por outro seguidor");
                }
                optSeguidor.get().getInteresseList().clear();
                if(!value.getInteresseList().isEmpty()){
                    for(RestInteresse i : value.getInteresseList()){
                        Long intId = Mapper.decryptId(i.getId());
                        if(!interesseRepository.existsById(intId)) throw new NotFoundException("Interesse");
                        Interesse interesse = new Interesse();
                        interesse.setId(intId);
                        optSeguidor.get().getInteresseList().add(interesse);
                    }
                }
                mapper.copyToDbObject(value, optSeguidor.get());
                repository.save(optSeguidor.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        throw new NotFoundException("Seguidor");
    }

    @GetMapping("seguidor/v1/recupera/{id}")
    public ResponseEntity<RestSeguidor> recupera(@PathVariable String id) throws BusinessSecurityException, NotFoundException {
        Optional<Seguidor> optSeguidor = repository.findById(Mapper.decryptId(id));
        if(optSeguidor.isPresent()){
            mapper.setMaxLevel(2);
            RestSeguidor rest = mapper.copyToRestObject(optSeguidor.get(), RestSeguidor.class);
            List<Interesse> interesses = interesseRepository.findByIntId(optSeguidor.get().getId());
            if(Objects.nonNull(interesses)){
                for(Interesse i : interesses){
                    RestInteresse restInteresse = mapper.copyToRestObject(i, RestInteresse.class);
                    rest.getInteresseList().add(restInteresse);
                }
            }
            return new ResponseEntity<>(rest, HttpStatus.OK);
        }
        throw new NotFoundException("Seguidor");
    }

    @GetMapping("seguidor/v1/lista/{pagina}")
    public ResponseEntity<Pagination> lista(@PathVariable Integer pagina) throws BusinessSecurityException {
        Page<Seguidor> seguidors = repository.findAll(pagination.queryWithPagination(pagina, Seguidor.class));
        mapper.setMaxLevel(1);
        List<RestSeguidor> rest = new ArrayList<>();
        for(Seguidor seg : seguidors.getContent()){
            RestSeguidor restSeguidor = mapper.copyToRestObject(seg, RestSeguidor.class);
            restSeguidor.setPersona(mapper.copyToRestObject(seg.getPersona(), RestPersona.class));
            rest.add(restSeguidor);
        }
        return new ResponseEntity<>(pagination.toResponse(pagina, seguidors.getTotalPages(), rest), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("seguidor/v1/remove/{id}")
    public ResponseEntity<Integer> remove(@PathVariable String id) throws NotFoundException, BusinessSecurityException {
        if(Objects.nonNull(id)){
            Long perId = Mapper.decryptId(id);
            if(repository.existsById(perId)){
                try {
                    repository.deleteById(perId);
                }catch (Exception e){
                    throw new BusinessSecurityException("Não é possivel apagar seguidor pois está em uso por outro registro");
                }
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        throw new NotFoundException("Seguidor");
    }

    @GetMapping("seguidor/v1/listaSimples")
    public ResponseEntity<List<RestSeguidor>> listaSimples() throws BusinessSecurityException {
        List<Seguidor> seguidors = repository.listaSimples();
        mapper.setMaxLevel(1);
        List<RestSeguidor> rest = mapper.copyToRestObject(seguidors, RestSeguidor.class);
        return new ResponseEntity<>(rest, HttpStatus.OK);
    }
}
