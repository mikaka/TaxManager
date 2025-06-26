package fr.pernisi.risf.taxmanager.catalogue.dto;

import fr.pernisi.risf.taxmanager.catalogue.model.TypeArticle;
import lombok.Data;

@Data
public class ArticleDto {


    private String title;
    private String description;
    private TypeArticle type;
    private Double unitPrice;
}
