package fr.pernisi.risf.taxmanager.catalogue.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false, length = 100)
    private TypeArticle type;

    @Column(nullable = false)
    private Double unitPrice;

}
