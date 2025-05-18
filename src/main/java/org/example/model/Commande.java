package org.example.model;

import org.bson.types.ObjectId;
import java.util.Date;
import java.util.List;

@lombok.Getter
@lombok.Setter
public class Commande {
    private ObjectId id;
    private String num;
    private String date;
    private String adresseLivraison;
    private ObjectId clientId;
    private Double montant;

    public Commande() {}

    public Commande(String num, String date, String adresseLivraison, ObjectId clientId, Double montant) {
        this.num = num;
        this.date = date;
        this.adresseLivraison = adresseLivraison;
        this.clientId = clientId;
        this.montant = montant;
    }
}
