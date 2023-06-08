package com.synectiks.asset.config;

import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import org.hibernate.dialect.PostgreSQL10Dialect;

import java.sql.Types;

public class PostgresDialectForJsonb
    extends PostgreSQL10Dialect {

    public PostgresDialectForJsonb() {
        super();
        this.registerHibernateType(
            Types.OTHER, JsonNodeBinaryType.class.getName()
        );
    }
}

