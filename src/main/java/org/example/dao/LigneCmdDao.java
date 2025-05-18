package org.example.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.model.LigneCmd;

import java.util.ArrayList;
import java.util.List;

public class LigneCmdDao {
    private final MongoCollection<Document> collection;

    public LigneCmdDao(String connectionString, String dbName, String collectionName) {
        MongoClient client = MongoClients.create(connectionString);
        MongoDatabase database = client.getDatabase(dbName);
        this.collection = database.getCollection(collectionName);
    }

    public void createLigneCmd(LigneCmd lc) {
        Document doc = new Document("id_article", lc.getArticleId())
                .append("id_commande", lc.getCommandeId())
                .append("quantite", lc.getQuantite());
        collection.insertOne(doc);
        lc.setId(doc.getObjectId("_id"));
    }

    public LigneCmd getLigneCmdById(ObjectId id) {
        Document doc = collection.find(new Document("_id", id)).first();
        if (doc == null) return null;
        LigneCmd lc = new LigneCmd();
        lc.setId(doc.getObjectId("_id"));
        lc.setArticleId(doc.getObjectId("id_article"));
        lc.setCommandeId(doc.getObjectId("id_commande"));
        lc.setQuantite(doc.getInteger("quantite", 0));
        return lc;
    }

    public List<LigneCmd> getAllLigneCmds() {
        List<LigneCmd> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            LigneCmd lc = new LigneCmd();
            lc.setId(doc.getObjectId("_id"));
            lc.setArticleId(doc.getObjectId("article_id"));
            lc.setCommandeId(doc.getObjectId("commande_id"));
            lc.setQuantite(doc.getInteger("quantite", 0));
            list.add(lc);
        }
        return list;
    }

    public void updateLigneCmd(LigneCmd lc) {
        Document filter = new Document("_id", lc.getId());
        Document update = new Document("$set", new Document()
                .append("article_id", lc.getArticleId())
                .append("id_commande", lc.getCommandeId())
                .append("quantite", lc.getQuantite()));
        collection.updateOne(filter, update);
    }

    public void deleteLigneCmd(ObjectId id) {
        collection.deleteOne(new Document("_id", id));
    }

    public void deleteLigneCmdByCommandeId(ObjectId commandeId) {
        collection.deleteMany(new Document("id_commande", commandeId));
    }
}
