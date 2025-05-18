package org.example.model;

import org.bson.types.ObjectId;

@lombok.Getter
@lombok.Setter
public class LigneCmd {
    private ObjectId id;
    private ObjectId articleId;
    private ObjectId commandeId;
    private int quantite;

    public LigneCmd() {}

    public LigneCmd(ObjectId articleId, ObjectId commandeId, int quantite) {
        this.articleId = articleId;
        this.commandeId = commandeId;
        this.quantite = quantite;
    }

}
