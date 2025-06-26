package fr.pernisi.risf.taxmanager.catalogue.service;

import fr.pernisi.risf.taxmanager.catalogue.dto.ArticleDto;
import fr.pernisi.risf.taxmanager.catalogue.model.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public abstract class ArticleMapperService {

    @Mapping(target = "id", ignore = true)
    public abstract Article DTOtoEntity(ArticleDto articleDto) ;


    public abstract ArticleDto entityToDTO(Article article) ;
}
