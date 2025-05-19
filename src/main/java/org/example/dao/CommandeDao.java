package org.example.dao;

import org.example.model.Commande;
import org.example.model.LigneCmd;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandeDao extends Component {
    private final MongoCollection<Document> collection;

    public CommandeDao(String connectionString, String dbName, String collectionName) {
        MongoClient client = MongoClients.create(connectionString);
        MongoDatabase database = client.getDatabase(dbName);
        this.collection = database.getCollection(collectionName);
    }

    public void createCommande(Commande cmd) {
        Document doc = new Document("num", cmd.getNum())
                .append("date", cmd.getDate())
                .append("adresse_livraison", cmd.getAdresseLivraison())
                .append("id_client", cmd.getClientId())
                .append("montant", cmd.getMontant());
        collection.insertOne(doc);
        cmd.setId(doc.getObjectId("_id"));
    }

    public List<Commande> getAllCommandes() {
        try{
        List<Commande> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            Commande c = new Commande();
            c.setId(doc.getObjectId("_id"));
            c.setNum(doc.getString("num"));
            c.setDate(doc.getDate("date"));
            c.setAdresseLivraison(doc.getString("adresse_livraison"));
            c.setClientId(doc.getObjectId("id_client"));
            c.setMontant(doc.getDouble("montant"));
            list.add(c);
        }
            return list;
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur de format de date", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void updateCommande(Commande cmd) {
        Document filter = new Document("_id", cmd.getId());
        Document update = new Document("$set", new Document()
                .append("num", cmd.getNum())
                .append("date", cmd.getDate())
                .append("adresse_livraison", cmd.getAdresseLivraison())
                .append("clientId", cmd.getClientId())
        );
        collection.updateOne(filter, update);
    }

    public void deleteCommande(ObjectId id) {
        collection.deleteOne(new Document("_id", id));
    }
}
