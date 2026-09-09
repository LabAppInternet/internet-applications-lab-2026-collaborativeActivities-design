package cat.tecnocampus.projections;

import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Records every SQL statement Hibernate sends, so the tests can assert which columns a
 * read selects. Registered via
 * {@code hibernate.session_factory.statement_inspector} in application.properties.
 * Given infrastructure — students do not touch it.
 */
public class SqlInspector implements StatementInspector {

    private static final List<String> STATEMENTS = new CopyOnWriteArrayList<>();

    @Override
    public String inspect(String sql) {
        STATEMENTS.add(sql);
        return sql;
    }

    public static void clear() {
        STATEMENTS.clear();
    }

    public static String captured() {
        return String.join(" | ", STATEMENTS).toLowerCase();
    }
}
