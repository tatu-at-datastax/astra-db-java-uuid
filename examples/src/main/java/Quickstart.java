import com.datastax.astra.client.collections.Collection;
import com.datastax.astra.client.DataAPIClient;
import com.datastax.astra.client.collections.documents.Document;
import com.datastax.astra.client.core.options.DataAPIOptions;
import com.datastax.astra.client.core.query.Filter;
import com.datastax.astra.client.databases.Database;
import com.datastax.astra.client.core.paging.FindIterable;
import com.datastax.astra.client.collections.options.CollectionFindOptions;

import java.util.List;

import static com.datastax.astra.client.core.query.Sort.vector;
import static com.datastax.astra.client.core.vector.SimilarityMetric.COSINE;

public class Quickstart {

  public static void main(String[] args) {
    // Loading Arguments
    String astraToken = System.getenv("ASTRA_DB_APPLICATION_TOKEN");
    String astraApiEndpoint = System.getenv("ASTRA_DB_API_ENDPOINT");

    // Initialize the client. The keyspace parameter is optional if you use
    // "default_keyspace".
    DataAPIClient client = new DataAPIClient(astraToken, DataAPIOptions.builder().build());
    System.out.println("Connected to AstraDB");

    Database db = client.getDatabase(astraApiEndpoint, "default_keyspace");
    System.out.println("Connected to Database.");
// end::init[]

// tag::collection[]
    // Create a collection. The default similarity metric is cosine.
    Collection<Document> collection = db
            .createCollection("vector_test", 5, COSINE);
    System.out.println("Created a collection");
// end::collection[]

// tag::data[]
    // Insert documents into the collection
    collection.insertMany(
            new Document("1")
                    .append("text", "ChatGPT integrated sneakers that talk to you")
                    .vector(new float[]{0.1f, 0.15f, 0.3f, 0.12f, 0.05f}),
            new Document("2")
                    .append("text", "An AI quilt to help you sleep forever")
                    .vector(new float[]{0.45f, 0.09f, 0.01f, 0.2f, 0.11f}),
            new Document("3")
                    .append("text", "A deep learning display that controls your mood")
                    .vector(new float[]{0.1f, 0.05f, 0.08f, 0.3f, 0.6f}));
    System.out.println("Inserted documents into the collection");
// end::data[]

// tag::search[]
    // Perform a similarity search
    Filter filter = null;
    CollectionFindOptions options = new CollectionFindOptions()
            .sort(vector(new float[]{0.15f, 0.1f, 0.1f, 0.35f, 0.55f}))
            .limit(10);
    FindIterable<Document> resultsSet = collection.find(filter,options);
    resultsSet.forEach(System.out::println);
// end::search[]

// tag::cleanup[]
    // Delete the collection
    collection.drop();
    System.out.println("Deleted the collection");
// end::cleanup[]

// tag::end[]
  }
}
// end::end[]