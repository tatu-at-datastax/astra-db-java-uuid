package com.datastax.astra.client.model;

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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Encode the presence of a field in the result.
 */
public class Projections {

    /**
     * Hide constructor
     */
    private Projections() {}

    /**
     * Include a field in the result.
     *
     * @param field
     *      include field
     * @return
     *      name to include
     */
    public static Projection[] include(String... field) {
        return Arrays.stream(field)
                .map(f -> new Projection(f, true))
                .toArray(Projection[]::new);
    }

    /**
     * Exclude  a field in the result.
     *
     * @param field
     *      field name to exclude
     * @return
     *      list of projection
     */
    public static Projection[] exclude(String... field) {
        return Arrays.stream(field)
                .map(f -> new Projection(f, false))
                .toArray(Projection[]::new);
    }
}
