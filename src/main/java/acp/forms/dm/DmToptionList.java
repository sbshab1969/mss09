package acp.forms.dm;

import java.util.ArrayList;
import java.util.List;

import acp.db.domain.*;
import acp.db.service.*;

public class DmToptionList extends DmPanel {
  private static final long serialVersionUID = 1L;

  private ToptionManagerList tableManager;
  private List<ToptionClass> cacheObj = new ArrayList<>();

  public DmToptionList(ToptionManagerList tblMng) {
    tableManager = tblMng;
    setHeaders();
  }

  public void setManager(ToptionManagerList tblMng) {
    tableManager = tblMng;
    setHeaders();
  }
  
  private void setHeaders() {
    if (tableManager != null) {
      headers = tableManager.getHeaders();
      types = tableManager.getTypes();
      colCount = headers.length;
    } else {
      headers = new String[] {};
      types = new Class<?>[] {};
      colCount = 0;
    }
  }
  
  // --- TableModel ---

  public int getRowCount() {
    return cacheObj.size();
  }

  public Object getValueAt(int row, int col) {
    ToptionClass obj = cacheObj.get(row); 
    ArrayList<String> pArr = obj.getPArray();
    int cnt = pArr.size();
    if (col == 0) {
      return obj.getId();
    } else if (col >= 1 && col <= cnt) {
      return pArr.get(col-1);
    } else if (col == cnt+1) {
      return obj.getDateBegin();
    } else if (col == cnt+2) {
      return obj.getDateEnd();
    }
    return null;
  }
  // --------------------------------------
  
  public void calcRecCount() {
    recCount = tableManager.countRecords();
  }

  public void queryAll() {
    cacheObj = tableManager.queryAll();
    fireTableChanged(null);
  }

  public void queryPage() {
    calcPageCount();
    if (currPage > pageCount) {
      currPage = pageCount;
    }  
    tableManager.openQuery();
    fetchPage(currPage);
  }

  public void fetchPage(int page) {
    int startRec = calcStartRec(page);
    cacheObj = tableManager.queryPart(startRec,recPerPage);
    fireTableChanged(null);
  }

  public void closeQuery() {
    tableManager.closeQuery();
  }

}
