package com.datastax.astra.client.tables.columns;

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

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
public class ColumnDefinitionMap extends ColumnDefinition {

    private ColumnTypes keyType;

    private ColumnTypes valueType;

    public ColumnDefinitionMap() {
        super(ColumnTypes.MAP);
    }

    public ColumnDefinitionMap(ColumnTypes keyType, ColumnTypes valueType) {
        this();
        this.keyType = keyType;
        this.valueType = valueType;
    }

}
