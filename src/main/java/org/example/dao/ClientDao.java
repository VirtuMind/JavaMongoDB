package org.example.dao;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.model.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientDao {
    private final MongoCollection<Document> collection;

    public ClientDao(String connectionString, String dbName, String collectionName) {
        MongoClient client = MongoClients.create(connectionString);
        MongoDatabase database = client.getDatabase(dbName);
        this.collection = database.getCollection(collectionName);
    }

    public void createClient(Client client) {
        Document doc = new Document("nom", client.getNom())
                .append("prenom", client.getPrenom())
                .append("adresse", client.getAdresse())
                .append("telephone", client.getTelephone())
                .append("code_postal", client.getCodePostal());
        collection.insertOne(doc);
        client.setId(doc.getObjectId("_id"));
    }

    public List<Client> getAllClients() {
        List<Client> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            Client c = new Client();
            c.setId(doc.getObjectId("_id"));
            c.setNom(doc.getString("nom"));
            c.setPrenom(doc.getString("prenom"));
            c.setAdresse(doc.getString("adresse"));
            c.setTelephone(doc.getString("telephone"));
            c.setCodePostal(doc.getInteger("code_postal"));
            list.add(c);
        }
        return list;
    }

    public void updateClient(Client client) {
        Document filter = new Document("_id", client.getId());
        Document update = new Document("$set", new Document("nom", client.getNom())
                .append("prenom", client.getPrenom())
                .append("adresse", client.getAdresse())
                .append("telephone", client.getTelephone())
                .append("code_postal", client.getCodePostal()));
        collection.updateOne(filter, update);
    }

    public void deleteClient(ObjectId id) {
        collection.deleteOne(new Document("_id", id));
    }
}
