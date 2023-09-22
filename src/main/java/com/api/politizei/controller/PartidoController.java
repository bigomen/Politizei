package com.api.politizei.controller;

import com.api.politizei.base.AuthenticatedController;
import com.api.politizei.exception.BusinessRuleException;
import com.api.politizei.exception.BusinessSecurityException;
import com.api.politizei.exception.NotFoundException;
import com.api.politizei.model.Partido;
import com.api.politizei.repository.PartidoRepository;
import com.api.politizei.rest.RestPartido;
import com.api.politizei.shared.Pagination;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
public class PartidoController extends AuthenticatedController {

    @Autowired
    private PartidoRepository repository;

    private void validarNumeroPartido(String numero, Long id) throws BusinessRuleException {
        if(numero.length() > 2) throw new BusinessRuleException("O número não pode exceder os 2 dígitos");
        try {
            Integer i = Integer.parseInt(numero);
        }catch (Exception e){
            throw new BusinessRuleException("Número do partido inválido");
        }
        if(Objects.isNull(id)){
            if(repository.existsByParNumero(numero)) throw new BusinessRuleException("Numero já utilizado por outro partido");
        }else {
            if(repository.validarNumeroAtualizacao(numero, id)) throw new BusinessRuleException("Numero já utilizado por outro partido");
        }
    }

    @Transactional
    @PostMapping("admin/v1/partido/novo")
    public ResponseEntity<Integer> novo(@RequestBody @Valid RestPartido value) throws BusinessRuleException, BusinessSecurityException {
        if(repository.existsByParNome(value.getParNome())) throw new BusinessRuleException("Nome já utilizado por outro partido");
        validarNumeroPartido(value.getParNumero(), null);
        Partido model = mapper.copyToDbObject(value);
        repository.save(model);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PutMapping("admin/v1/partido/atualiza")
    public ResponseEntity<Integer> atualiza(@RequestBody @Valid RestPartido value) throws BusinessSecurityException, NotFoundException, BusinessRuleException {
        Long parId = Mapper.decryptId(value.getId());
        if(Objects.nonNull(parId)){
            Optional<Partido> optPartido = repository.findById(parId);
            if(optPartido.isPresent()){
                if(repository.validarNomeAtualizacao(value.getParNome(), parId)) throw new BusinessRuleException("Nome já utilizado por outro partido");
                validarNumeroPartido(value.getParNumero(), parId);
                mapper.copyToDbObject(value, optPartido.get());
                repository.save(optPartido.get());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        throw new NotFoundException("Partido");
    }

    @Transactional
    @GetMapping("admin/v1/partido/recupera/{id}")
    public ResponseEntity<RestPartido> recupera(@PathVariable String id) throws BusinessSecurityException, NotFoundException {
        Optional<Partido> optPartido = repository.findById(Mapper.decryptId(id));
        if(optPartido.isPresent()){
            mapper.setMaxLevel(2);
            RestPartido rest = mapper.copyToRestObject(optPartido.get(), RestPartido.class);
            return new ResponseEntity<>(rest, HttpStatus.OK);
        }
        throw new NotFoundException("Partido");
    }

    @Transactional
    @GetMapping("admin/v1/partido/lista/{pagina}")
    public ResponseEntity<Pagination> lista(@PathVariable Integer pagina) throws BusinessSecurityException {
        Page<Partido> partidos = repository.findAll(pagination.queryWithPagination(pagina, Partido.class));
        mapper.setMaxLevel(2);
        List<RestPartido> rest = mapper.copyToRestObject(partidos.getContent(), RestPartido.class);
        return new ResponseEntity<>(pagination.toResponse(pagina, partidos.getTotalPages(), rest), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("admin/v1/partido/remove/{id}")
    public ResponseEntity<Integer> remove(@PathVariable String id) throws NotFoundException, BusinessSecurityException {
        if(Objects.nonNull(id)){
            Optional<Partido> optPartido = repository.findById(Mapper.decryptId(id));
            if(optPartido.isPresent()){
                try {
                    repository.delete(optPartido.get());
                }catch (Exception e){
                    throw new BusinessSecurityException("Não é possivel apagar partido pois está em uso por outro registro");
                }
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        throw new NotFoundException("Partido");
    }

    @Transactional
    @GetMapping("partido/v1/listaSimples")
    public ResponseEntity<List<RestPartido>> listaSimples() throws BusinessSecurityException {
        mapper.setMaxLevel(1);
        List<Partido> partidos = repository.listaSimples();
        List<RestPartido> rest = mapper.copyToRestObject(partidos, RestPartido.class);
        return new ResponseEntity<>(rest, HttpStatus.OK);
    }
}
