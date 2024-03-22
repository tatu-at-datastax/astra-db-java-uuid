package com.datastax.astra.documentation;

import com.datastax.astra.client.Collection;
import com.datastax.astra.client.DataAPIClient;
import com.datastax.astra.client.Database;
import com.datastax.astra.client.model.Document;
import com.datastax.astra.client.model.iterable.FindIterable;

import java.util.List;

import static com.datastax.astra.client.model.filter.Filters.eq;
import static com.datastax.astra.client.model.find.SimilarityMetric.cosine;

public class QuickStart {
  public static void main(String[] args) {

    // Initializing the client with a token
    DataAPIClient client = new DataAPIClient("TOKEN");

    // Accessing the Database
    Database db = client.getDatabase("API_ENDPOINT");

    // Create collection with vector search
    Collection<Document> col = db.createCollection("demo", 14, cosine);

   // Insert vectors
   col.insertMany(List.of(
       new Document("doc1")
           .vector(new float[]{1f, 0f, 1f, 1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f})
           .append("product_name", "HealthyFresh - Beef raw dog food")
           .append("product_price", 12.99),
       new Document("doc2")
           .vector(new float[]{1f, 1f, 1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f})
           .append("product_name", "HealthyFresh - Chicken raw dog food")
           .append("product_price", 9.99),
       new Document("doc3")
           .vector(new float[]{1f, 1f, 1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f}),
       new Document()
           .id("doc4")
           .vector(new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f})
           .append("product_name", "HealthyFresh - Chicken raw dog food")
           .append("product_price", 9.99)));

    // Perform a similarity search
    float[] embeddings = new float[] {1f, 1f, 1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    FindIterable<Document> docs = col.find(eq("product_price", 9.99), embeddings, 10);

    // Print result
    for (Document doc : docs) System.out.println(doc);
  }
}
