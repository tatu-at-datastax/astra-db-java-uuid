package com.datastax.astra.client.collections;

import com.datastax.astra.client.collections.documents.Document;
import com.datastax.astra.client.databases.Database;

public class ClearCollection {
  public static void main(String[] args) {
    // Connect to running dn
    Database db = new Database("API_ENDPOINT", "TOKEN");

    // Accessing the collection
    Collection<Document> collection = db.getCollection("collection_simple");

    // Delete all rows from an existing collection
    collection.deleteAll();
  }
}
