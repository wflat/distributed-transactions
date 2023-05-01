package de.mathema.springboot.common.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Objects;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.support.JdbcUtils;

import de.mathema.springboot.common.exception.InternalServerErrorException;

public final class DatabaseUtils {

  private DatabaseUtils() {
  }

  static final String GET_DATABASE_PRODUCT_NAME = "getDatabaseProductName";

  public static boolean isH2Db(final Context coontext) {
    return Objects.equals("h2", getDbVendor(coontext));
  }

  public static String getDbVendor(final Context context) {
    try {
      return ermittleDbVendor(context.getConnection());
    } catch (final Exception e) {
      throw new InternalServerErrorException(
          String.format("DB Vendor konnte nicht ermittelt werden : %s", e.getMessage()));
    }
  }

  public static String getDbVendor(final DataSource dataSource) {
    try {
      return ermittleDbVendor(dataSource);
    } catch (final Exception e) {
      throw new InternalServerErrorException(
          String.format("DB Vendor konnte nicht ermittelt werden : %s", e.getMessage()));
    }
  }

  static String ermittleDbVendor(final DataSource dataSource) {
    Connection con = null;
    try {
      con = org.springframework.jdbc.datasource.DataSourceUtils.getConnection(dataSource);
      return ermittleDbVendor(con);
    } finally {
      org.springframework.jdbc.datasource.DataSourceUtils.releaseConnection(con, dataSource);
    }
  }

  static String ermittleDbVendor(final Connection connection) {
    final String productName = ermittleDatabaseProductName(connection);
    return StringUtils.lowerCase(JdbcUtils.commonDatabaseName(productName));
  }

  private static <T> T ermittleDatabaseProductName(final Connection connection) {
    try {
      final DatabaseMetaData metaData = connection.getMetaData();
      return (T) DatabaseMetaData.class.getMethod(GET_DATABASE_PRODUCT_NAME).invoke(metaData);
    } catch (final Exception ex) {
      throw new InternalServerErrorException(
          String.format("Fehler beim Aufruf der DatabaseMetaData Methode '%s' => %s",
              GET_DATABASE_PRODUCT_NAME, ex.getMessage()));
    }
  }
}
