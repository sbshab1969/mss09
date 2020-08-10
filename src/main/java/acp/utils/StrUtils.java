package acp.utils;

public class StrUtils {

  public static boolean emptyString(String str) {
    if (str == null || str.equals("")) {
      return true;
    }
    return false;
  }

}
