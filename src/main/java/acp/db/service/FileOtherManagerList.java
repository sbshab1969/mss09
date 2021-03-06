package acp.db.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import acp.db.domain.*;
import acp.db.utils.*;
import acp.utils.*;

public class FileOtherManagerList extends ManagerList {
  private Long fileId;

  protected List<FileOtherClass> cacheObj = new ArrayList<>();
  private static Logger logger = LoggerFactory.getLogger(FileOtherManagerList.class);

  public FileOtherManagerList(Long file_id) {
    fields = new String[] { "mssl_id", "mssl_dt_event", "mssl_desc" };

    headers = new String[] { "ID"
      , Messages.getString("Column.Time")
      , Messages.getString("Column.Desc") 
    };

    types = new Class<?>[] { 
        Long.class
      , Timestamp.class
      , String.class
    };

    cntColumns = headers.length;
    
    fileId = file_id;

    tableName = "mss_logs";
    pkColumn = "mssl_id";
    strAwhere = "mssl_ref_id=" + fileId;
    seqId = 1000L;

    strFields = StrSqlUtils.buildSelectFields(fields, null);
    strFrom = tableName;
    strWhere = strAwhere;
    strOrder = pkColumn;
    // ------------
    prepareQuery();
    // ------------
  }
  
  public List<FileOtherClass> queryAll() {
    openCursor();  // forward
    cacheObj = fetchAll();
    closeCursor();
    return cacheObj;    
  }

  public List<FileOtherClass> queryPart(int startPos, int cntRows) {
    cacheObj = fetchPart(startPos,cntRows);
    return cacheObj;
  }  

  private List<FileOtherClass> fetchAll() {
    ArrayList<FileOtherClass> cache = new ArrayList<>();
    try {
      while (rs.next()) {
        FileOtherClass record = getObject(rs);
        cache.add(record);
      }
    } catch (SQLException e) {
      cache = new ArrayList<>();
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    return cache;
  }

  private List<FileOtherClass> fetchPart(int startPos, int cntRows) {
    ArrayList<FileOtherClass> cache = new ArrayList<>();
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
        FileOtherClass record = getObject(rs);
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
  
  private FileOtherClass getObject(ResultSet rs) throws SQLException {
    //---------------------------------------
    Long rsId = rs.getLong("mssl_id");
    Timestamp rsDateEvent = rs.getTimestamp("mssl_dt_event");
    String rsDesc = rs.getString("mssl_desc");
    //---------------------------------------
    FileOtherClass obj = new FileOtherClass();
    obj.setId(rsId);
    obj.setDateEvent(rsDateEvent);
    obj.setDesc(rsDesc);
    //---------------------------------------
    return obj;
  }
  
}
