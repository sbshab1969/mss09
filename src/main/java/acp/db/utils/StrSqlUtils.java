package acp.db.utils;

import acp.utils.StrUtils;

public class StrSqlUtils {

  public static boolean emptyString(String str) {
    return StrUtils.emptyString(str);
  }
  
  public static String strAddAnd(String str1, String str2) {
    String str = "";
    if (emptyString(str1) && emptyString(str2)) {
      str = "";
    } else if (!emptyString(str1) && emptyString(str2)) {
      str = str1;
    } else if (emptyString(str1) && !emptyString(str2)) {
      str = str2;
    } else {
      str = str1 + " and " + str2;
    }
    return str;
  }

  public static int[] getFieldNums(String[] fields) {
    int res[] = new int[fields.length];
    for (int i = 0; i < fields.length; i++) {
      res[i] = i;
    }
    return res;
  }

  public static String buildSelectFields(String[] fields, String[] fieldnames) {
    StringBuilder query = new StringBuilder("select ");
    if (fields != null) {
      for (int i = 0; i < fields.length; i++) {
        query.append(fields[i]);
        if (fieldnames != null)
          query.append(" " + "\"" + fieldnames[i] + "\"");
        if (i != fields.length - 1)
          query.append(", ");
      }
    } else {
      query.append("*");
    }
    return query.toString();
  }

  public static String buildSelectFrom(String[] fields, String[] fieldnames,
      String tblFrom) {
    String query = buildSelectFields(fields, fieldnames);
    query = query + " from " + tblFrom;
    return query;
  }

  public static java.util.Date sql2util(java.sql.Date dtSql) {
    java.util.Date dtUtil = null;
    if (dtSql != null) {
      dtUtil = new java.util.Date(dtSql.getTime());
    }
    return dtUtil;
  }

  public static java.sql.Date util2sql(java.util.Date dtUtil) {
    java.sql.Date dtSql = null;
    if (dtUtil != null) {
      dtSql = new java.sql.Date(dtUtil.getTime());
    }
    return dtSql;
  }

  public static java.util.Date ts2util(java.sql.Timestamp ts) {
    java.util.Date dtUtil = null;
    if (ts != null) {
      dtUtil = new java.util.Date(ts.getTime());
    }
    return dtUtil;
  }

  public static java.sql.Timestamp util2ts(java.util.Date dtUtil) {
    java.sql.Timestamp ts = null;
    if (dtUtil != null) {
      ts = new java.sql.Timestamp(dtUtil.getTime());
    }
    return ts;
  }

}
