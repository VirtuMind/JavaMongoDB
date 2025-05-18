package org.example.model;

import org.bson.types.ObjectId;

@lombok.Getter
@lombok.Setter
public class Article {
    private ObjectId id;
    private String code;
    private String designation;
    private double prix_u;
    private String rayon;
    private String ss_rayon;

    public Article() {}

    public Article(String code, String designation, double prix_u, String rayon, String ss_rayon) {
        this.code = code;
        this.designation = designation;
        this.prix_u = prix_u;
        this.rayon = rayon;
        this.ss_rayon = ss_rayon;
    }
}
