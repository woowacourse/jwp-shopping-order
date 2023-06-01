package cart.dao.support;

public class SqlHelper {

    private final StringBuilder sql;

    private SqlHelper(final StringBuilder sql) {
        this.sql = sql;
    }

    public static SqlHelper sqlHelper() {
        return new SqlHelper(new StringBuilder());
    }

    public SqlHelper select() {
        sql.append("SELECT");
        return this;
    }

    public SqlHelper delete() {
        sql.append("DELETE");
        return this;
    }

    public SqlHelper insert() {
        sql.append("INSERT INTO");
        return this;
    }

    public SqlHelper update() {
        sql.append("UPDATE");
        return this;
    }

    public SqlHelper values(final String columns) {
        sql.append(" VALUES(").append(columns).append(")");
        return this;
    }

    public SqlHelper columns(final String columns) {
        sql.append(" ").append(columns);
        return this;
    }

    public SqlHelper from() {
        sql.append(" FROM");
        return this;
    }

    public SqlHelper table(String tables) {
        sql.append(" ").append(tables);
        return this;
    }

    public SqlHelper where() {
        sql.append(" WHERE");
        return this;
    }

    public SqlHelper condition(final String condition) {
        sql.append(" ").append(condition);
        return this;
    }

    public SqlHelper in(final String columnName, final String values) {
        sql.append(" ").append(columnName).append(" IN (").append(values).append(")");
        return this;
    }

    public SqlHelper set(final String condition) {
        sql.append(" SET ").append(condition);
        return this;
    }

    public SqlHelper innerJoin(final String table) {
        sql.append(" INNER JOIN ").append(table).append(" ");
        return this;
    }

    public SqlHelper join(final String table) {
        sql.append(" JOIN ").append(table);
        return this;
    }

    public SqlHelper on(final String condition) {
        sql.append(" ON ").append(condition);
        return this;
    }

    public String toString() {
        return sql.toString();
    }
}
