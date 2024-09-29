package io.github.xiapxx.starter.code2enum.mybatistypehandler;

import io.github.xiapxx.starter.code2enum.enums.EnumCodeJdbcType;
import io.github.xiapxx.starter.code2enum.holder.Code2EnumHolder;
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
public class EnumMybatisTypeHandler<T extends Code2Enum> extends BaseTypeHandler<T> {

    private Class<T> enum2CodeClass;

    public EnumMybatisTypeHandler(Class<T> enum2CodeClass){
        this.enum2CodeClass = enum2CodeClass;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        EnumCodeJdbcType enumCodeJdbcType = parameter.enumCodeJdbcType();
        switch (enumCodeJdbcType) {
            case STRING:
                ps.setString(i, parameter.getCode());
                break;
            case LONG:
                ps.setLong(i, parameter.getLongCode());
                break;
            case INT:
                ps.setInt(i, parameter.getIntCode());
                break;
        }
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        return Code2EnumHolder.toEnum(code, enum2CodeClass);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return Code2EnumHolder.toEnum(code, enum2CodeClass);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        return Code2EnumHolder.toEnum(code, enum2CodeClass);
    }
}
