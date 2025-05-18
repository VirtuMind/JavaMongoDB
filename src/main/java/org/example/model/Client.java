package org.example.model;

import org.bson.types.ObjectId;

@lombok.Getter
@lombok.Setter
public class Client {
    private ObjectId id;
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private String codePostal;

    public Client() {}

    public Client(String nom, String prenom, String adresse, String telephone, String codePostal) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.codePostal = codePostal;
    }

}
