package com.datastax.astra.client.database_admin;

import com.datastax.astra.client.databases.Database;

public class CreateKeyspace {
  public static void main(String[] args) {
    // Default initialization
    Database db = new Database("API_ENDPOINT", "TOKEN");

    // Create a new keyspace
    db.getDatabaseAdmin().createKeyspace("<keyspace_name>");

    // The database can be mutate on keyspace creation
    db.getDatabaseAdmin().createKeyspace("<keyspace2_name>", true);
  }
}
