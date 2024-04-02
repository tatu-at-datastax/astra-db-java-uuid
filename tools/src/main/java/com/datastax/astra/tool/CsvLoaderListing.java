package com.datastax.astra.tool;

import com.datastax.astra.client.Collection;
import com.datastax.astra.client.DataAPIClient;
import com.datastax.astra.client.Database;
import com.datastax.astra.client.model.DataAPIKeywords;
import com.datastax.astra.client.model.Document;
import com.datastax.astra.tool.csv.CsvLoader;
import com.datastax.astra.tool.csv.CsvLoaderSettings;
import com.datastax.astra.tool.csv.CsvRowMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * Sample Code to load a CSV file into Astra using the DataStax Java Driver
 */
@Slf4j
public class CsvLoaderListing {


    public static void main(String[] args) throws Exception {
        // Get an empty Collection
        DataAPIClient        client      = new DataAPIClient("<replace_me>");
        Database             db         = client.getDatabase("<replace_me>");
        Collection<Document> collection = db.createCollection("airbnb");
        collection.deleteAll();

        // Zou !
        String csvFilename = "/Users/cedricklunven/Downloads/listings.csv";
        CsvLoader.load(csvFilename, collection, new CsvRowMapper() {
            @Override
            public Document map(Document doc) {
                doc.put(DataAPIKeywords.ID.getKeyword(), doc.get("id"));
                doc.remove("id");
                return doc;
            }
        });

    }

}
