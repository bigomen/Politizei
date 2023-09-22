package com.api.politizei.repository;

import com.api.politizei.model.Seguidor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeguidorRepository extends PaginationRepository<Seguidor>{

    Boolean existsBySegEmail(String segEmail);

    Boolean existsBySegWhatsapp(String segWhatsapp);

    @Query("select CASE WHEN count(s.id) > 0 THEN true ELSE false END from Seguidor s where s.segEmail = :segEmail and s.id <> :id")
    Boolean validarEmailAtualizacao(String segEmail, Long id);

    @Query("select CASE WHEN count(s.id) > 0 THEN true ELSE false END from Seguidor s where s.segWhatsapp = :segWhatsapp and s.id <> :id")
    Boolean validarWhatsappAtualizacao(String segWhatsapp, Long id);

    @Query("select new Seguidor(s.id, s.segNome, s.segEmail, p.perNome) from Seguidor s join s.persona p")
    Page<Seguidor> findAll(Pageable pageable);

    @Query("select new Seguidor(s.id, s.segNome) from Seguidor s")
    List<Seguidor> listaSimples();
}
