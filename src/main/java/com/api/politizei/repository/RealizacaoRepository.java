package com.api.politizei.repository;

import com.api.politizei.model.Realizacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RealizacaoRepository extends PaginationRepository<Realizacao>{

    Boolean existsByRelTitulo(String relTitulo);

    @Query("select CASE WHEN count(r.id) > 0 THEN true ELSE false END from Realizacao r where r.relTitulo = :relTitulo and r.id <> :id")
    Boolean validarTituloAtualizacao(String relTitulo, Long id);

    @Query("select new Realizacao(r.id, r.relTitulo, p.perNome) from Realizacao r join r.persona p")
    Page<Realizacao> findAll(Pageable pageable);

    @Query("select new Realizacao(r.id, r.relTitulo) from Realizacao r")
    List<Realizacao> listaSimples();
}
