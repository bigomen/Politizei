package com.api.politizei.repository;

import com.api.politizei.model.Projeto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjetoRepository extends PaginationRepository<Projeto>{

    Boolean existsByPrjTitulo(String prjTitulo);

    @Query("select new Projeto(p.id, p.prjTitulo, per.perNome) from Projeto p join p.persona per")
    Page<Projeto> findAll(Pageable pageable);

    @Query("select CASE WHEN count(p.id) > 0 THEN true ELSE false END from Projeto p where p.prjTitulo = :prjTitulo and p.id <> :id")
    Boolean validarTituloAtualizacao(String prjTitulo, Long id);

    @Query("select new Projeto(p.id, p.prjTitulo) from Projeto p")
    List<Projeto> listaSimples();
}
