package com.api.politizei.repository;

import com.api.politizei.model.Persona;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonaRepository extends PaginationRepository<Persona> {

    Boolean existsByPerNome(String perNome);

    Boolean existsByPerAlias(String perAlias);

    @Query("select new Persona(p.id, p.perNome, p.perAlias, pa.parNome) from Persona p join p.partido pa")
    Page<Persona> findAll(Pageable pageable);

    @Query("select new Persona(p.id, p.perNome) from Persona p")
    List<Persona> listaSimples();

    @Query("select CASE WHEN count(p.id) > 0 THEN true ELSE false END from Persona p where p.perNome = :perNome and p.id <> :id")
    Boolean validarNomeAtualizacao(String perNome, Long id);

    @Query("select CASE WHEN count(p.id) > 0 THEN true ELSE false END from Persona p where p.perAlias = :perAlias and p.id <> :id")
    Boolean validarAliasAtualizacao(String perAlias, Long id);
}
