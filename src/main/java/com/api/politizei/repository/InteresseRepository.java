package com.api.politizei.repository;

import com.api.politizei.model.Interesse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InteresseRepository extends PaginationRepository<Interesse>{

    @Query("select new Interesse(i.id, i.intDescricao, p.perNome) from Interesse i join i.persona p")
    Page<Interesse> findAll(Pageable pageable);

    @Query("select new Interesse(i.id, i.intDescricao) from Interesse i")
    List<Interesse> listaSimples();

    @Query("select i from Seguidor s join s.interesseList i where s.id = :intId")
    List<Interesse> findByIntId(Long intId);
}
