package com.api.politizei.repository;

import com.api.politizei.model.Partido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartidoRepository extends PaginationRepository<Partido>{

    Boolean existsByParNumero(String parNumero);

    Boolean existsByParNome(String parNome);

    Page<Partido> findAll(Pageable pageable);

    @Query("select CASE WHEN count(p.id) > 0 THEN true ELSE false END from Partido p where p.parNome = :parNome and p.id <> :id")
    Boolean validarNomeAtualizacao(String parNome, Long id);

    @Query("select CASE WHEN count(p.id) > 0 THEN true ELSE false END from Partido p where p.parNumero = :parNumero and p.id <> :id")
    Boolean validarNumeroAtualizacao(String parNumero, Long id);

    @Query("select new Partido(p.id, p.parNome) from Partido p")
    List<Partido> listaSimples();
}
