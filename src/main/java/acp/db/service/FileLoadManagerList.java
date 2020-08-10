package acp.db.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.domain.FileLoadClass;
import acp.db.utils.*;
import acp.utils.*;

public class FileLoadManagerList extends ManagerList {

  protected List<FileLoadClass> cacheObj = new ArrayList<>();
  private static Logger logger = LoggerFactory.getLogger(FileLoadManagerList.class);

  public FileLoadManagerList() {
    fields = new String[] { "mssf_id", "mssf_name", "mssf_md5", "mssf_owner",
      "mssf_dt_work", "mssf_rec_all"
//      ,"extract(mssf_statistic,'statistic/records/all/text()').getStringval() rec_count"
    };

    headers = new String[] { 
        "ID"
      , Messages.getString("Column.FileName")
      , "MD5"
      , Messages.getString("Column.Owner")
      , Messages.getString("Column.DateWork")
      , Messages.getString("Column.RecordCount") 
    };

    types = new Class<?>[] { 
        Long.class
      , String.class
      , String.class
      , String.class
      , Timestamp.class
      , int.class
    };

    cntColumns = headers.length;

    tableName = "mss_files";
    pkColumn = "mssf_id";
    strAwhere = null;
    seqId = 1000L;

    strFields = StrSqlUtils.buildSelectFields(fields, null);
    strFrom = tableName;
    strWhere = strAwhere;
    strOrder = pkColumn;
    // ------------
    prepareQuery();
    // ------------
  }

  public void setWhere(Map<String,String> mapFilter) {
    // ----------------------------------
    String vFileName = mapFilter.get("file_name");
    String vOwner = mapFilter.get("owner");
    String vDtBegin = mapFilter.get("dt_begin");
    String vDtEnd = mapFilter.get("dt_end");
    String vRecBegin = mapFilter.get("rec_begin");
    String vRecEnd = mapFilter.get("rec_end");
    // ----------------------------------
    String phWhere = null;
    String str = null;
    // ---
    if (!DbUtils.emptyString(vFileName)) {
      str = "upper(mssf_name) like upper('" + vFileName + "%')";
      phWhere = StrSqlUtils.strAddAnd(phWhere, str);
    }
    // ---
    if (!DbUtils.emptyString(vOwner)) {
      str = "upper(mssf_owner) like upper('" + vOwner + "%')";
      phWhere = StrSqlUtils.strAddAnd(phWhere, str);
    }
    //---
    String vField = "";
    String valueBeg = "";
    String valueEnd = "";
    //---
    vField = "trunc(mssf_dt_work)";
    valueBeg = "to_date('" + vDtBegin + "','dd.mm.yyyy')";
    valueEnd = "to_date('" + vDtEnd + "','dd.mm.yyyy')";
    if (!DbUtils.emptyString(vDtBegin) || !DbUtils.emptyString(vDtEnd)) {
      if (!DbUtils.emptyString(vDtBegin) && !DbUtils.emptyString(vDtEnd)) {
        str = vField + " between " + valueBeg + " and " + valueEnd;
      } else if (!DbUtils.emptyString(vDtBegin) && DbUtils.emptyString(vDtEnd)) {
        str = vField + " >= " + valueBeg;
      } else if (DbUtils.emptyString(vDtBegin) && !DbUtils.emptyString(vDtEnd)) {
        str = vField + " <= " + valueEnd;
      }
      phWhere = StrSqlUtils.strAddAnd(phWhere, str);
    }
    //---
    vField = "mssf_rec_all";
//    vField="to_number(extract(mssf_statistic,'statistic/records/all/text()').getstringval())";
    valueBeg = vRecBegin;
    valueEnd = vRecEnd;
    if (!DbUtils.emptyString(vRecBegin) || !DbUtils.emptyString(vRecEnd)) {
      if (!DbUtils.emptyString(vRecBegin) && !DbUtils.emptyString(vRecEnd)) {
        str = vField + " between " + valueBeg + " and " + valueEnd;
      } else if (!DbUtils.emptyString(vRecBegin) && DbUtils.emptyString(vRecEnd)) {
        str = vField + " >= " + valueBeg;
      } else if (DbUtils.emptyString(vRecBegin) && !DbUtils.emptyString(vRecEnd)) {
        str = vField + " <= " + valueEnd;
      }
      phWhere = StrSqlUtils.strAddAnd(phWhere, str);
    }
    // ---
    strWhere = StrSqlUtils.strAddAnd(strAwhere, phWhere);
  }

  public List<FileLoadClass> queryAll() {
    openCursor();  // forward
    cacheObj = fetchAll();
    closeCursor();
    return cacheObj;    
  }

  public List<FileLoadClass> queryPart(int startPos, int cntRows) {
    cacheObj = fetchPart(startPos,cntRows);
    return cacheObj;
  }  

  private List<FileLoadClass> fetchAll() {
    ArrayList<FileLoadClass> cache = new ArrayList<>();
    try {
      while (rs.next()) {
        FileLoadClass record = getObject(rs);
        cache.add(record);
      }
    } catch (SQLException e) {
      cache = new ArrayList<>();
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    return cache;
  }

  private List<FileLoadClass> fetchPart(int startPos, int cntRows) {
    ArrayList<FileLoadClass> cache = new ArrayList<>();
    if (startPos <= 0 || cntRows<=0) { 
      return cache;
    }
    try {
      boolean res = rs.absolute(startPos);
      if (res == false) {
        return cache;
      }
      int curRow = 0;
      //------------------------------------------
      do {
        curRow++;
        FileLoadClass record = getObject(rs);
        cache.add(record);
        if (curRow>=cntRows) break;
        //----------------------------------------
      } while (rs.next());
      //------------------------------------------
    } catch (SQLException e) {
      cache = new ArrayList<>();
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    return cache;
  }

  private FileLoadClass getObject(ResultSet rs) throws SQLException {
    //---------------------------------------
    Long rsId = rs.getLong("mssf_id");
    String rsName = rs.getString("mssf_name");
    String rsMd5 = rs.getString("mssf_md5");
    String rsOwner = rs.getString("mssf_owner");
    Timestamp rsDateWork = rs.getTimestamp("mssf_dt_work");
    int rsRecAll = rs.getInt("mssf_rec_all");
    //---------------------------------------
    FileLoadClass obj = new FileLoadClass();
    obj.setId(rsId);
    obj.setName(rsName);
    obj.setMd5(rsMd5);
    obj.setOwner(rsOwner);
    obj.setDateWork(rsDateWork);
    obj.setRecAll(rsRecAll);
    //---------------------------------------
    return obj;
  }

}
