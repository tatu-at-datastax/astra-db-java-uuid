package com.datastax.astra.test.model;

import com.datastax.astra.client.tables.mapping.Column;
import com.datastax.astra.client.tables.mapping.PartitionBy;
import com.datastax.astra.client.tables.mapping.EntityTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.datastax.astra.client.tables.columns.ColumnTypes.INT;
import static com.datastax.astra.client.tables.columns.ColumnTypes.TEXT;

@Data
@EntityTable("table_composite_pk_annotated")
@NoArgsConstructor
@AllArgsConstructor
public class TableCompositeAnnotatedRow {

    @PartitionBy(0)
    @Column(value="id", type=TEXT)
    private String idx;

    @PartitionBy(1)
    @Column(value="name", type=TEXT)
    private String namex;

    @Column(value="age", type=INT)
    private int agex;

}
