/*
 * Copyright DataStax, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datastax.astra.internal.serdes.tables;

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

import com.datastax.astra.client.core.vector.DataAPIVector;
import com.datastax.astra.client.core.vector.SimilarityMetric;
import com.datastax.astra.client.tables.TableDuration;
import com.datastax.astra.client.tables.columns.ColumnTypes;
import com.datastax.astra.internal.serdes.Base64FloatVectorSerializer;
import com.datastax.astra.internal.serdes.DataAPISerializer;
import com.datastax.astra.internal.serdes.DataAPIVectorSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Custom implementation of serialization : faster + no jackson dependency
 * 
 * @author Cedrick Lunven (@clunven)
 */
@SuppressWarnings("deprecation")
public class RowSerializer implements DataAPISerializer {

    /** Object mapper with customization fo data API. */
    private ObjectMapper objectMapper;

    /**
     * Default constructor
     */
    public RowSerializer() {
        // left blank, hiding constructor for utility class
    }

    /**
     * Building the data api specific object mapper.
     *
     * @return
     *      object mapper.
     */
    public ObjectMapper getMapper() {
        if (objectMapper == null) {
            JsonFactory jsonFactory = JsonFactory.builder()
                    .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
                    .enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES)
                    .enable(StreamReadFeature.USE_FAST_BIG_NUMBER_PARSER)
                    .enable(StreamReadFeature.USE_FAST_DOUBLE_PARSER)
                    .enable(StreamWriteFeature.USE_FAST_DOUBLE_WRITER)
                    .build();
            objectMapper = new ObjectMapper(jsonFactory)
                    .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
                    .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .setAnnotationIntrospector(new JacksonAnnotationIntrospector());

            SimpleModule module = new SimpleModule();

            // Serialization
            module.addSerializer(ColumnTypes.class, new ColumnTypeSerializer());
            module.addSerializer(Double.class, new DoubleSerializer());
            module.addSerializer(double.class, new DoubleSerializer());
            module.addSerializer(Float.class, new FloatSerializer());
            module.addSerializer(float.class, new FloatSerializer());
            module.addSerializer(byte[].class, new ByteArraySerializer());
            module.addSerializer(Duration.class, new DurationSerializer());
            module.addSerializer(TableDuration.class, new TableDurationSerializer());
            module.addSerializer(DataAPIVector.class, new DataAPIVectorSerializer());
            module.addSerializer(SimilarityMetric.class, new SimilarityMetricSerializer());
             //.addDeserializer(float[].class, new Base64FloatVectorDeserializer())
            module.addSerializer(float[].class, new Base64FloatVectorSerializer());

            // De-Serialization
            module.addDeserializer(ColumnTypes.class, new ColumnTypeDeserializer());
            module.addDeserializer(Float.class, new FloatDeserializer());
            module.addDeserializer(float.class, new FloatDeserializer());
            module.addDeserializer(Double.class, new DoubleDeserializer());
            module.addDeserializer(double.class, new DoubleDeserializer());
            module.addDeserializer(byte[].class, new ByteArrayDeserializer());
            module.addDeserializer(Duration.class, new DurationDeserializer());
            module.addDeserializer(SimilarityMetric.class, new SimilarityMetricDeserializer());
            objectMapper.registerModule(module);

            // Java 8 Time
            // Create a JavaTimeModule
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
            LocalTimeSerializer localTimeSerializer = new LocalTimeSerializer(timeFormatter);
            javaTimeModule.addSerializer(LocalTime.class, localTimeSerializer);
            objectMapper.registerModule(javaTimeModule);
        }
        return objectMapper;
    }



}