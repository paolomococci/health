package local.health.research.config;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.init.ScriptUtils;

// Indicates that this class contains Spring bean definitions.
@Configuration
public class DataInitConfig implements ApplicationRunner {

  // Logger instance for logging messages.
  private static final Logger log = LoggerFactory.getLogger(DataInitConfig.class);

  // DataSource injected via constructor.
  private final DataSource dataSource;

  public DataInitConfig(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void run(ApplicationArguments args) {
    // This method is called automatically when the application context is ready.
    try (Connection conn = DataSourceUtils.getConnection(dataSource)) {
      // Obtain a JDBC Connection from the DataSource in a safe try-with-resources block.
      // Check if the DB needs seeding.
      if (needSeed(conn)) {
        // Log an INFO message.
        log.info("Empty database -> run fake-data.sql script");
        // Execute the SQL seed script.
        ScriptUtils.executeSqlScript(conn, new ClassPathResource("fake-data.sql"));
      } else {
        // Log a DEBUG message.
        log.debug("Tables already populated; seed not needed!");
      }
    } catch (SQLException sqle) {
      // Log the error that occurred while executing the seed script.
      log.error("Unable to execute seed script!", sqle);
      // Wrap and rethrow as unchecked exception.
      throw new RuntimeException(sqle);
    }
  }

  private boolean needSeed(Connection conn) throws SQLException {
    // Use uppercase - H2 defaults to uppercase table names.
    String table = "ARTICLES";
    // Return true if the table does not exist or is empty.
    return !tableExists(conn, table) || isTableEmpty(conn, table);
  }

  private boolean tableExists(Connection conn, String tableName) throws SQLException {
    // Retrieve DB metadata.
    DatabaseMetaData meta = conn.getMetaData();
    // Query metadata for tables matching the name.
    try (ResultSet rs = meta.getTables(null, null, tableName, new String[] { "TABLE" })) {
      // Returns true if at least one row is found (i.e., table exists).
      return rs.next();
    }
  }

  private boolean isTableEmpty(Connection conn, String tableName) throws SQLException {
    // Prepare a lightweight query that returns the first row if it exists.
    try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM " + tableName + " LIMIT 1")) {
      try (ResultSet rs = ps.executeQuery()) {
        // If no rows are returned, the table is empty.
        return !rs.next();
      }
    }
  }
}
