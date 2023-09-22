package com.api.politizei.shared;

import com.api.politizei.constants.Constantes;
import com.api.politizei.exception.BusinessSecurityException;
import com.api.politizei.model.PageModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class Pagination {

    public int pageNumber;

    public int pageSize;

    public int totalPages;

    public boolean isPaged = false;

    public List<?> content;

    public Pagination(){

    }

    public Pagination toResponse(int pageNumber, int totalPages, List<?> content) {
        this.pageNumber = pageNumber;
        this.pageSize = Constantes.TAMHANHO_PAGINA;
        this.totalPages = totalPages;
        this.content = content;
        this.isPaged = true;
        return this;
    }

    public Pagination toResponse(int totalContents, List<?> content){
        this.pageNumber = 0;
        this.pageSize = Constantes.TAMHANHO_PAGINA;
        this.totalPages = (int) Math.ceil( (double)totalContents / (double)Constantes.TAMHANHO_PAGINA);
        this.content = content;
        this.isPaged = false;
        return this;
    }

    public Pageable queryWithPagination(int page, Class<?> sortClass) throws BusinessSecurityException {
        if(page < 1) throw new BusinessSecurityException("Index de pagina incorreto");
        this.isPaged = true;
        PageModel.SortField field = (PageModel.SortField) sortClass.getDeclaredAnnotation(PageModel.SortField.class);
        if(field == null) throw new BusinessSecurityException("Erro de paginacao, verificar anotacao do ordenador na classe");
        Sort sort = Sort.by(field.fieldName());
        return PageRequest.of(page-1, Constantes.TAMHANHO_PAGINA, sort);
    }

    public Sort queryWithoutPagination(Class<?> sortClass) throws BusinessSecurityException {
        PageModel.SortField field = (PageModel.SortField) sortClass.getDeclaredAnnotation(PageModel.SortField.class);
        if(field == null) throw new BusinessSecurityException("Erro de paginacao, verificar anotacao do ordenador na classe");
        this.isPaged = false;
        return Sort.by(field.fieldName());
    }
}
