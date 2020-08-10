package acp.db.service;

import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Timestamp;
//import java.sql.Date;
import java.util.Date;

import acp.db.DbConnect;
import acp.db.domain.ConfigClass;
import acp.db.utils.*;
import acp.utils.*;

public class ConfigManagerEdit {
  private Connection dbConn;

  public ConfigManagerEdit() {
    dbConn = DbConnect.getConnection();
  }

  public String selectSources() {
    String query = "select msss_id, msss_name from mss_source order by msss_name";
    return query;
  }

  public ConfigClass select(Long objId) {
    // ------------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("select msso_id, msso_name,msso_dt_begin,msso_dt_end,msso_comment,msso_msss_id");
    sbQuery.append("  from mss_options");
    sbQuery.append(" where msso_id=?");
    String query = sbQuery.toString();
    // ------------------------------------------------------
    ConfigClass configObj = null;
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setLong(1, objId);
      ResultSet rsq = ps.executeQuery();
      if (rsq.next()) {
        String rsqName = rsq.getString("msso_name");
//        Date rsqDateBegin = rsq.getDate("msso_dt_begin");
//        Date rsqDateEnd = rsq.getDate("msso_dt_end");
        Date rsqDateBegin = rsq.getTimestamp("msso_dt_begin");
        Date rsqDateEnd = rsq.getTimestamp("msso_dt_end");
        String rsqComment = rsq.getString("msso_comment");
        Long rsqSourceId = rsq.getLong("msso_msss_id");
        // ---------------------
        configObj = new ConfigClass();
        configObj.setId(objId);
        configObj.setName(rsqName);
        configObj.setDateBegin(rsqDateBegin);
        configObj.setDateEnd(rsqDateEnd);
        configObj.setComment(rsqComment);
        configObj.setSourceId(rsqSourceId);
        // ---------------------
      }
      rsq.close();
      ps.close();
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    // ------------------------------------------------------
    return configObj;
  }
  
  public String getCfgName(Long objId) {
    // ------------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("select msso_name from mss_options t where msso_id=?");
    String query = sbQuery.toString();
    // ------------------------------------------------------
    String configName = "";
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setLong(1, objId);
      ResultSet rsq = ps.executeQuery();
      if (rsq.next()) {
        configName = rsq.getString("msso_name");
      }
      rsq.close();
      ps.close();
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    // ------------------------------------------------------
    return configName;
  }

  public Clob getCfgClob(Long objId) {
    // ------------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("select t.msso_config.getClobval() msso_conf");
    sbQuery.append("  from mss_options t where msso_id=?");
    String query = sbQuery.toString();
    // ------------------------------------------------------
    Clob configClob = null;
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setLong(1, objId);
      ResultSet rsq = ps.executeQuery();
      if (rsq.next()) {
        configClob = rsq.getClob("msso_conf");
      }
      rsq.close();
      ps.close();
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    // ------------------------------------------------------
    return configClob;
  }

  public String getCfgString(Long objId) {
    Clob clob = getCfgClob(objId);
    String strClob = null;
    try {
      strClob = DbUtils.clob2String(clob);
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    return strClob;
  }

  public Reader getCfgReader(Long objId) {
    Clob clob = getCfgClob(objId);
    Reader rdrClob = null;
    try {
      rdrClob = clob.getCharacterStream();
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    return rdrClob;
  }

  public int insert(ConfigClass newObj) {
    int res = -1;
    // ------------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("insert into mss_options (");
    sbQuery.append("msso_id, msso_name, msso_config");
    sbQuery.append(",msso_dt_begin, msso_dt_end, msso_comment");
    sbQuery.append(",msso_dt_create, msso_dt_modify, msso_owner, msso_msss_id)");
    sbQuery.append(" values (msso_seq.nextval, ?, XMLType(?), ?, ?, ?");
    sbQuery.append(", sysdate, sysdate, user, ?)");
    String query = sbQuery.toString();
    // System.out.println(query);
    // ------------------------------------------------------
    String emptyXml = "<?xml version=\"1.0\"?><config><sverka.ats/></config>";
    Timestamp tsBegin = StrSqlUtils.util2ts(newObj.getDateBegin());
    Timestamp tsEnd = StrSqlUtils.util2ts(newObj.getDateEnd());
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setString(1, newObj.getName());
      ps.setString(2, emptyXml);
//      ps.setDate(3, newObj.getDateBegin());
//      ps.setDate(4, newObj.getDateEnd());
      ps.setTimestamp(3, tsBegin);
      ps.setTimestamp(4, tsEnd);
      ps.setString(5, newObj.getComment());
      ps.setLong(6, newObj.getSourceId());
      // --------------------------
      int ret = ps.executeUpdate();
      // --------------------------
      ps.close();
      res = ret;
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    // -----------------------------------------------------
    return res;
  }

  public int update(ConfigClass newObj) {
    int res = -1;
    // -----------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("update mss_options");
    sbQuery.append("   set msso_name=?");
    sbQuery.append("      ,msso_dt_begin=?");
    sbQuery.append("      ,msso_dt_end=?");
    sbQuery.append("      ,msso_comment=?");
    sbQuery.append("      ,msso_dt_modify=sysdate");
    sbQuery.append("      ,msso_owner=user");
    sbQuery.append("      ,msso_msss_id=?");
    sbQuery.append(" where msso_id=?");
    String query = sbQuery.toString();
    // -----------------------------------------
    Timestamp tsBegin = StrSqlUtils.util2ts(newObj.getDateBegin());
    Timestamp tsEnd = StrSqlUtils.util2ts(newObj.getDateEnd());
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setString(1, newObj.getName());
//      ps.setDate(2, newObj.getDateBegin());
//      ps.setDate(3, newObj.getDateEnd());
      ps.setTimestamp(2, tsBegin);
      ps.setTimestamp(3, tsEnd);
      ps.setString(4, newObj.getComment());
      ps.setLong(5, newObj.getSourceId());
      ps.setLong(6, newObj.getId());
      // --------------------------
      int ret = ps.executeUpdate();
      // --------------------------
      ps.close();
      res = ret;
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    // -----------------------------------------------------
    return res;
  }

  public int updateCfgObj(ConfigClass newObj) {
    int res = -1;
    // -----------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("update mss_options");
    // sbQuery.append("   set msso_config=?"); // error
    sbQuery.append("   set msso_config=XMLType(?)");
    sbQuery.append("      ,msso_dt_modify=sysdate");
    sbQuery.append("      ,msso_owner=user");
    sbQuery.append(" where msso_id=?");
    String query = sbQuery.toString();
    // System.out.println(query);
    // -----------------------------------------
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setClob(1, newObj.getConfig());
      ps.setLong(2, newObj.getId());
      // --------------------------
      int ret = ps.executeUpdate();
      // --------------------------
      ps.close();
      res = ret;
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    // -----------------------------------------------------
    return res;
  }

  public int updateCfgStr(Long objId, String txtConf) {
    int res = -1;
    // -----------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("update mss_options");
    // sbQuery.append("   set msso_config=?"); // OK
    sbQuery.append("   set msso_config=XMLType(?)");
    sbQuery.append("      ,msso_dt_modify=sysdate");
    sbQuery.append("      ,msso_owner=user");
    sbQuery.append(" where msso_id=?");
    String query = sbQuery.toString();
    // System.out.println(query);
    // -----------------------------------------
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setString(1, txtConf);
      ps.setLong(2, objId);
      // --------------------------
      int ret = ps.executeUpdate();
      // --------------------------
      ps.close();
      res = ret;
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    // -----------------------------------------------------
    return res;
  }

  public int delete(Long objId) {
    int res = -1;
    // -----------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("delete from mss_options where msso_id=?");
    String query = sbQuery.toString();
    // System.out.println(query);
    // -----------------------------------------------------
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setLong(1, objId);
      // --------------------------
      int ret = ps.executeUpdate();
      // --------------------------
      ps.close();
      res = ret;
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    // -----------------------------------------------------
    return res;
  }

  public int copy(Long objId) {
    int res = -1;
    // -----------------------------------------------------
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append("insert into mss_options");
    sbQuery.append(" (select msso_seq.nextval, msso_name || '_copy'");
    sbQuery.append(", msso_config");
    sbQuery.append(", msso_dt_begin, msso_dt_end, msso_comment");
    sbQuery.append(", sysdate, sysdate, user, msso_msss_id");
    sbQuery.append(" from mss_options where msso_id=?)");
    String query = sbQuery.toString();
    // System.out.println(query);
    // -----------------------------------------------------
    try {
      PreparedStatement ps = dbConn.prepareStatement(query);
      ps.setLong(1, objId);
      // --------------------------
      int ret = ps.executeUpdate();
      // --------------------------
      ps.close();
      res = ret;
    } catch (SQLException e) {
      DialogUtils.errorPrint(e);
    }
    // -----------------------------------------------------
    return res;
  }

}
