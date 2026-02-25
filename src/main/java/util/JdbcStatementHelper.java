package util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;

import entities.OrderStatus;
import entities.RoleType;

public class JdbcStatementHelper {

    public JdbcStatementHelper() {
    }

    public static void setStatementParams(PreparedStatement statement, Object... args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            int paramIndex = i + 1;
            Object arg = args[i];

            if (arg == null) {
                statement.setNull(paramIndex, Types.VARCHAR);
                continue;
            }

            if (arg instanceof Integer) {
                statement.setInt(paramIndex, (Integer) arg);
            } else if (arg instanceof Long) {
                statement.setLong(paramIndex, (Long) arg);
            } else if (arg instanceof Double) {
                statement.setDouble(paramIndex, (Double) arg);
            } else if (arg instanceof Float) {
                statement.setFloat(paramIndex, (Float) arg);
            } else if (arg instanceof Boolean) {
                statement.setBoolean(paramIndex, (Boolean) arg);
            } else if (arg instanceof String) {
                statement.setString(paramIndex, (String) arg);
            } else if (arg instanceof LocalDate) {
                statement.setObject(paramIndex, arg, Types.DATE);
            } else if (arg instanceof LocalDateTime) {
                statement.setObject(paramIndex, arg, Types.TIMESTAMP);
            } else if (arg instanceof OrderStatus) {
                statement.setString(paramIndex, ((OrderStatus) arg).name().toLowerCase());
            } else if (arg instanceof RoleType) {
                statement.setString(paramIndex, ((RoleType) arg).name().toLowerCase());
            } else {
                statement.setObject(paramIndex, arg);
            }
        }
    }
}
