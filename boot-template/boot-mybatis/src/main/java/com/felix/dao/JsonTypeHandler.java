package com.felix.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@MappedTypes(ObjectNode.class)
@MappedJdbcTypes(value = JdbcType.LONGVARCHAR, includeNullJdbcType = true)
public class JsonTypeHandler<T extends Object> extends BaseTypeHandler<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Class<T> clazz;

    public JsonTypeHandler(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.clazz = clazz;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        String str;
        try {
            str = objectMapper.writeValueAsString(parameter);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ps.setString(i, str);
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return this.toObject(rs.getObject(columnName), clazz);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.toObject(rs.getObject(columnIndex), clazz);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.toObject(cs.getObject(columnIndex), clazz);
    }

    private T toObject(Object content, Class<T> clazz) {
        if (content == null) {
            return null;
        }
        try {
            return objectMapper.readValue(content.toString(), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
