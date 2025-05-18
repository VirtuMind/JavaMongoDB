package org.example.dao;

import org.example.model.Article;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ArticleDao {
    private final MongoCollection<Document> collection;

    public ArticleDao(String connectionString, String dbName, String collectionName) {
        MongoClient client = MongoClients.create(connectionString);
        MongoDatabase database = client.getDatabase(dbName);
        this.collection = database.getCollection(collectionName);
    }

    public void createArticle(Article article) {
        Document doc = new Document("code", article.getCode())
                .append("designation", article.getDesignation())
                .append("prix_u", article.getPrix_u())
                .append("rayon", article.getRayon())
                .append("ss_rayon", article.getSs_rayon());
        collection.insertOne(doc);
        article.setId(doc.getObjectId("_id"));
    }

    public List<Article> getAllArticles() {
        List<Article> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            Article a = new Article();
            a.setId(doc.getObjectId("_id"));
            a.setCode(doc.getString("code"));
            a.setDesignation(doc.getString("designation"));
            a.setPrix_u(doc.getDouble("prix_u"));
            a.setRayon(doc.getString("rayon"));
            a.setSs_rayon(doc.getString("ss_rayon"));
            list.add(a);
        }
        return list;
    }

    public void updateArticle(Article article) {
        Document filter = new Document("_id", article.getId());
        Document update = new Document("$set", new Document("code", article.getCode())
                .append("designation", article.getDesignation())
                .append("prix_u", article.getPrix_u())
                .append("rayon", article.getRayon())
                .append("ss_rayon", article.getSs_rayon()));
        collection.updateOne(filter, update);
    }

    public void deleteArticle(ObjectId id) {
        collection.deleteOne(new Document("_id", id));
    }
}
