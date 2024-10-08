package io.github.xiapxx.starter.code2enum.core;

import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author xiapeng
 * @Date 2024-03-18 17:55
 */
public class Code2EnumTypeHandler<C, T extends Code2Enum<C>> extends BaseTypeHandler<T> {

    private Code2EnumContainer<C, T> code2EnumContainer;

    public Code2EnumTypeHandler(Code2EnumContainer<C, T> code2EnumContainer){
        this.code2EnumContainer = code2EnumContainer;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        C code = parameter.getCode();
        if(code == null){
            ps.setNull(i, jdbcType.TYPE_CODE);
            return;
        }
        if(code instanceof String){
            ps.setString(i, (String) code);
            return;
        }
        if(code instanceof Integer){
            ps.setInt(i, (Integer) code);
            return;
        }
        if(code instanceof Long){
            ps.setLong(i, (Long) code);
            return;
        }
        throw new IllegalArgumentException("不支持的编码类型 : " + code2EnumContainer.codeClass.getName());
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if(code2EnumContainer.isEmpty()){
            return null;
        }
        if(code2EnumContainer.isStringCode()){
            return code2EnumContainer.toEnum((C) rs.getString(columnName));
        }
        if(code2EnumContainer.isIntegerCode()){
            return code2EnumContainer.toEnum((C) Integer.valueOf(rs.getInt(columnName)));
        }
        if(code2EnumContainer.isLongCode()){
            return code2EnumContainer.toEnum((C) Long.valueOf(rs.getLong(columnName)));
        }
        throw new IllegalArgumentException("不支持的编码类型 : " + code2EnumContainer.codeClass.getName());
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if(code2EnumContainer.isEmpty()){
            return null;
        }
        if(code2EnumContainer.isStringCode()){
            return code2EnumContainer.toEnum((C) rs.getString(columnIndex));
        }
        if(code2EnumContainer.isIntegerCode()){
            return code2EnumContainer.toEnum((C) Integer.valueOf(rs.getInt(columnIndex)));
        }
        if(code2EnumContainer.isLongCode()){
            return code2EnumContainer.toEnum((C) Long.valueOf(rs.getLong(columnIndex)));
        }
        throw new IllegalArgumentException("不支持的编码类型 : " + code2EnumContainer.codeClass.getName());
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if(code2EnumContainer.isEmpty()){
            return null;
        }
        if(code2EnumContainer.isStringCode()){
            return code2EnumContainer.toEnum((C) cs.getString(columnIndex));
        }
        if(code2EnumContainer.isIntegerCode()){
            return code2EnumContainer.toEnum((C) Integer.valueOf(cs.getInt(columnIndex)));
        }
        if(code2EnumContainer.isLongCode()){
            return code2EnumContainer.toEnum((C) Long.valueOf(cs.getLong(columnIndex)));
        }
        throw new IllegalArgumentException("不支持的编码类型 : " + code2EnumContainer.codeClass.getName());
    }
}
