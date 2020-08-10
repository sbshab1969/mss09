package acp.db.service;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManagerListStr extends ManagerList {
  protected List<String[]> cacheStr = new ArrayList<>();

  private static Logger logger = LoggerFactory.getLogger(ManagerListStr.class);

  public List<String[]> queryAll() {
    openCursor();  // forward
    cacheStr = fetchAll();
    closeCursor();
    return cacheStr;    
  }

  public List<String[]> queryPart(int startPos, int cntRows) {
    cacheStr = fetchPart(startPos,cntRows);
    return cacheStr;
  }  

  private List<String[]> fetchAll() {
    ArrayList<String[]> cache = new ArrayList<>();
    try {
      //-----------------------------------------
      while (rs.next()) {
        //---------------------------------------
        String[] record = new String[cntColumns];
        for (int i = 0; i < cntColumns; i++) {
          record[i] = rs.getString(i+1);
        }
        cache.add(record);
        //---------------------------------------
      }
      //-----------------------------------------
    } catch (SQLException e) {
      cache = new ArrayList<>();
      e.printStackTrace();
      logger.error(e.getMessage());
    }
    return cache;
  }

  private List<String[]> fetchPart(int startPos, int cntRows) {
    ArrayList<String[]> cache = new ArrayList<>();
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
        //----------------------------------------
        String[] record = new String[cntColumns];
        for (int i = 0; i < cntColumns; i++) {
          record[i] = rs.getString(i+1);
        }
        cache.add(record);
        //----------------------------------------
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

}
