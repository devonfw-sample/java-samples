package com.devonfw.quarkus.productmanagement.utils;

public class StringUtils {
  public static boolean isEmpty(String str) {

    return (str == null || "".equals(str));
  }
}
