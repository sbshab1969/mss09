package acp.forms.dm;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class DmPanel extends AbstractTableModel {
  private static final long serialVersionUID = 1L;

  private List<String[]> cache = new ArrayList<>();

  protected String[] headers = new String[] {};
  protected Class<?>[] types = new Class<?>[] {};
  protected int colCount = 0;

  protected int recCount = 0;
  protected int recPerPage = 20;
  protected int firstRecOnPage = 1;

  protected int pageCount;
  protected int currPage;

  public DmPanel() {
  }

  // --- AbstractTableModel ---
  public String getColumnName(int col) {
    return headers[col];
  }

  public Class<?> getColumnClass(int col) {
    return types[col];
  }

  public boolean isCellEditable(int row, int col) {
    return false;
  }

  // --- TableModel ---
  public int getColumnCount() {
    return colCount;
  }

  public int getRowCount() {
    return cache.size();
  }

  public Object getValueAt(int row, int col) {
    return cache.get(row)[col];
  }
  // --------------------------------------
  
  public int getRecPerPage() {
    return recPerPage;
  }

  public void setRecPerPage(int recOnPage) {
    this.recPerPage = recOnPage;
  }

  public int getPageCount() {
    return pageCount;
  }

  public int getCurrPage() {
    return currPage;
  }

  public void setCurrPage(int currPg) {
    currPage = currPg;
  }

  protected void calcRecCount() {
    recCount = 0;
  }

  protected void calcPageCount() {
    calcRecCount();
    if (recCount > 0) {
      int fullPageCount = recCount / recPerPage;
      int tail = recCount - fullPageCount*recPerPage;
      if (tail == 0) {
        pageCount = fullPageCount;
      } else {
        pageCount = fullPageCount + 1;
      }
    } else {
      pageCount = 0;
    }
  }

  protected int calcStartRec(int page) {
    int startRec = 0;
    if (page > 0) {
      startRec = (page-1)*recPerPage + firstRecOnPage;
    }
    return startRec;
  }

  public void firstPage() {
    calcPageCount();
    if (currPage > 1) {
      currPage = 1;
    }
    fetchPage(currPage);
  }

  public void previousPage() {
    calcPageCount();
    if (currPage > 1) {
      currPage--;
    }
    fetchPage(currPage);
  }

  public void nextPage() {
    calcPageCount();
    if (currPage < pageCount) {
      currPage++;
    } else {
      currPage = pageCount;
    }
    fetchPage(currPage);
  }

  public void lastPage() {
    calcPageCount();
    currPage = pageCount;
    fetchPage(currPage);
  }

  public void fetchPage(int page) {
  }

  public void queryPage() {
  }

  public void queryAll() {
  }

  public void queryClose() {
  }

}
