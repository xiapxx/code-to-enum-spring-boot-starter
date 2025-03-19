package io.github.xiapxx.starter.code2enum.mybatistypehandler;

import io.github.xiapxx.starter.code2enum.enums.EnumCodeJdbcType;
import io.github.xiapxx.starter.code2enum.holder.Code2EnumHolder;
import io.github.xiapxx.starter.code2enum.interfaces.Code2Enum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.springframework.util.StringUtils;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

/**
 * @Author xiapeng
 * @Date 2024-03-18 17:55
 */
public class EnumMybatisTypeHandler<T extends Code2Enum> extends BaseTypeHandler<T> {

    private Class<T> enum2CodeClass;

    private String jdbcDefaultCode;

    private EnumCodeJdbcType enumCodeJdbcType;

    public EnumMybatisTypeHandler(Class<T> enum2CodeClass){
        this.enum2CodeClass = enum2CodeClass;
        T anyEnum = Stream.of(enum2CodeClass.getEnumConstants()).findAny().orElse(null);
        if (anyEnum != null) {
            this.jdbcDefaultCode = anyEnum.jdbcDefaultCode();
            this.enumCodeJdbcType = anyEnum.enumCodeJdbcType();
        }
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            if(jdbcDefaultCode != null){
                setJdbcDefaultCodeParameter(ps, i);
                return;
            }

            if (jdbcType == null) {
                throw new TypeException("JDBC requires that the JdbcType must be specified for all nullable parameters.");
            }

            try {
                ps.setNull(i, jdbcType.TYPE_CODE);
            } catch (SQLException e) {
                throw new TypeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . "
                        + "Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. "
                        + "Cause: " + e, e);
            }
            return;
        }

        setNonNullParameter(ps, i, parameter, jdbcType);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
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

    /**
     * 设置默认的jdbc code
     *
     * @param ps ps
     * @param i i
     */
    private void setJdbcDefaultCodeParameter(PreparedStatement ps, int i) throws SQLException {
        switch (enumCodeJdbcType) {
            case STRING:
                ps.setString(i, jdbcDefaultCode);
                break;
            case LONG:
                ps.setLong(i, getLongJdbcDefaultCode());
                break;
            case INT:
                ps.setInt(i, getIntJdbcDefaultCode());
                break;
        }
    }

    private Integer getIntJdbcDefaultCode() {
        if(!StringUtils.hasLength(jdbcDefaultCode)){
            return null;
        }
        return Integer.valueOf(jdbcDefaultCode);
    }

    private Long getLongJdbcDefaultCode() {
        if(!StringUtils.hasLength(jdbcDefaultCode)){
            return null;
        }
        return Long.valueOf(jdbcDefaultCode);
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
