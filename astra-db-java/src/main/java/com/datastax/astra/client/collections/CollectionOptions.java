package com.datastax.astra.client.collections;

/*-
 * #%L
 * Data API Java Client
 * --
 * Copyright (C) 2024 DataStax
 * --
 * Licensed under the Apache License, Version 2.0
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.datastax.astra.client.core.vector.SimilarityMetric;
import com.datastax.astra.client.core.vector.VectorOptions;
import com.datastax.astra.client.core.vectorize.VectorServiceOptions;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Set of options to define and initialize a collection.
 */
@Getter
@Setter
public class CollectionOptions {

    /**
     * The 'defaultId' to allow working with different types of identifiers.
     */
    private DefaultIdOptions defaultId;

    /**
     * Vector options.
     */
    private VectorOptions vector;

    /**
     * Indexing options
     */
    private IndexingOptions indexing;

    /**
     * Default constructor.
     */
    public CollectionOptions() {
        // left blank on purpose, built with builder
    }

    /**
     * Subclass representing the indexing options.
     */
    @Getter @Setter
    public static class DefaultIdOptions {

        /** Type for the default id. */
        private String type;

        /**
         * Default constructor.
         */
        public DefaultIdOptions() {
            // marshalled by jackson
        }

        /**
         * Default constructor.
         *
         * @param type
         *      type for the default id
         */
        public DefaultIdOptions(String type) {
            this.type = type;
        }
    }

    /**
     * Subclass representing the indexing options.
     */
    @Getter @Setter
    public static class IndexingOptions {

        /**
         * If not empty will index everything but those properties.
         */
        private List<String> deny;

        /**
         * If not empty will index just those properties.
         */
        private List<String> allow;

        /**
         * Default constructor.
         */
        public IndexingOptions() {
            // left blank, serialization with jackson
        }
    }


    /**
     * Gets a builder.
     *
     * @return a builder
     */
    public static CollectionOptionsBuilder builder() {
        return new CollectionOptionsBuilder();
    }

    /**
     * Builder for {@link CollectionDefinition}.
     */
    public static class CollectionOptionsBuilder {

        /**
         * Options for Vector
         */
        VectorOptions vector;

        /**
         * Options for Indexing
         */
        IndexingOptions indexing;

        /**
         * Options for Default Id
         */
        String defaultId;

        /**
         * Access the vector options.
         *
         * @return
         *      vector options
         */
        private VectorOptions getVector() {
            if (vector == null) {
                vector = new VectorOptions();
            }
            return vector;
        }

        /**
         * Access the indexing options.
         *
         * @return
         *      indexing options
         */
        private IndexingOptions getIndexing() {
            if (indexing == null) {
                indexing = new IndexingOptions();
            }
            return indexing;
        }

        /**
         * Default constructor.
         */
        public CollectionOptionsBuilder() {
            // left blank, builder pattern
        }

        /**
         * Builder Pattern with the Identifiers.
         *
         * @param idType
         *      type of ids
         * @return
         *      self reference
         */
        public CollectionOptionsBuilder defaultIdType(CollectionIdTypes idType) {
            this.defaultId = idType.getValue();
            return this;
        }

        /**
         * Builder pattern.
         *
         * @param size size
         * @return self reference
         */
        public CollectionOptionsBuilder vectorDimension(int size) {
            getVector().setDimension(size);
            return this;
        }

        /**
         * Builder pattern.
         *
         * @param function function
         * @return self reference
         */
        public CollectionOptionsBuilder vectorSimilarity(@NonNull SimilarityMetric function) {
            getVector().setMetric(function.getValue());
            return this;
        }

        /**
         * Builder pattern.
         *
         * @param properties size
         * @return self reference
         */
        public CollectionOptionsBuilder indexingDeny(@NonNull String... properties) {
            if (getIndexing().getAllow() != null) {
                throw new IllegalStateException("'indexing.deny' and 'indexing.allow' are mutually exclusive");
            }
            getIndexing().setDeny(Arrays.asList(properties));
            return this;
        }

        /**
         * Builder pattern.
         *
         * @param properties size
         * @return self reference
         */
        public CollectionOptionsBuilder indexingAllow(String... properties) {
            if (getIndexing().getDeny() != null) {
                throw new IllegalStateException("'indexing.deny' and 'indexing.allow' are mutually exclusive");
            }
            getIndexing().setAllow(Arrays.asList(properties));
            return this;
        }

        /**
         * Builder pattern.
         *
         * @param dimension dimension
         * @param function  function
         * @return self reference
         */
        public CollectionOptionsBuilder vector(int dimension, @NonNull SimilarityMetric function) {
            vectorSimilarity(function);
            vectorDimension(dimension);
            return this;
        }

        /**
         * Enable Vectorization within the collection.
         *
         * @param provider
         *      provider Name (LLM)
         * @param modeName
         *      mode name
         * @return
         *      self reference
         */
        public CollectionOptionsBuilder vectorize(String provider, String modeName) {
            return vectorize(provider, modeName, null);
        }

        /**
         * Enable Vectorization within the collection.
         *
         * @param provider
         *      provider Name (LLM)
         * @param modeName
         *      mode name
         * @param sharedSecretKey
         *      name of the key in the system
         * @return
         *      self reference
         */
        public CollectionOptionsBuilder vectorize(String provider, String modeName, String sharedSecretKey) {
            VectorServiceOptions embeddingService = new VectorServiceOptions();
            embeddingService.provider(provider).modelName(modeName);
            if (sharedSecretKey != null) {
                // --> Since 1.3.1 the suffix is not needed anymore
                //embeddingService.setAuthentication(Map.of("providerKey", keyName + ".providerKey"));
                embeddingService.authentication(Map.of("providerKey", sharedSecretKey));
                // <--- Since 1.3.1 the suffix is not needed anymore
            }
            getVector().setService(embeddingService);
            return this;
        }

        /**
         * Enable Vectorization within the collection.
         *
         * @param provider
         *      provider Name (LLM)
         * @param modeName
         *      mode name
         * @param parameters
         *      expected parameters for vectorize
         * @param sharedSecretKey
         *      name of the key in the system
         * @return
         *      self reference
         */
        public CollectionOptionsBuilder vectorize(String provider, String modeName, String sharedSecretKey, Map<String, Object> parameters) {
            vectorize(provider, modeName, sharedSecretKey);
            getVector().getService().parameters(parameters);
            return this;
        }

        /**
         * Build the output.
         *
         * @return collection definition
         */
        public CollectionOptions build() {
            CollectionOptions req = new CollectionOptions();
            req.vector    = this.vector;
            req.indexing  = this.indexing;
            if (defaultId != null) {
                req.defaultId = new DefaultIdOptions(defaultId);
            }
            return req;
        }
    }

}
