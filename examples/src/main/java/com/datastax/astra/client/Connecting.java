package com.datastax.astra.client;

import com.datastax.astra.client.core.options.DataAPIOptions;
import com.datastax.astra.client.databases.Database;

import java.util.UUID;

public class Connecting {
    public static void main(String[] args) {
        // Preferred Access with DataAPIClient (default options)
        DataAPIClient client = new DataAPIClient("TOKEN");

        // Overriding the default options
        DataAPIClient client1 = new DataAPIClient("TOKEN", DataAPIOptions
                .builder()
                .build());

        // Access the Database from its endpoint
        Database db1 = client1.getDatabase("*API_ENDPOINT*");
        Database db2 = client1.getDatabase("*API_ENDPOINT*", "*KEYSPACE*");

        // Access the Database from its endpoint
        UUID databaseId = UUID.fromString("f5abf92f-ff66-48a0-bbc2-d240bc25dc1f");
        Database db3 = client.getDatabase(databaseId);
        Database db4 = client.getDatabase(databaseId, "*KEYSPACE*");
        Database db5 = client.getDatabase(databaseId, "*KEYSPACE*", "us-east-2");

        db5.useKeyspace("yet_another");

    }
}