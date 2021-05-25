package com.yinlu.code.generator.utils;

import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * MySQL备份还原工具类
 *
 * @author kuroneko
 */
@Slf4j
public class MySqlBackupRestoreUtils {

  /**
   * 备份数据库
   *
   * @param host host地址，可以是本机也可以是远程
   * @param userName 数据库的用户名
   * @param password 数据库的密码
   * @param backupFolderPath 备份的路径
   * @param fileName 备份的文件名
   * @param database 需要备份的数据库的名称
   * @return
   * @throws IOException
   */
  public static boolean backup(
      String host,
      String userName,
      String password,
      String backupFolderPath,
      String fileName,
      String database)
      throws Exception {
    File backupFolderFile = new File(backupFolderPath);
    if (!backupFolderFile.exists()) {
      // 如果目录不存在则创建
      backupFolderFile.mkdirs();
    }
    if (!backupFolderPath.endsWith(File.separator) && !backupFolderPath.endsWith("/")) {
      backupFolderPath = backupFolderPath + File.separator;
    }
    // 拼接命令行的命令
    String backupFilePath = backupFolderPath + fileName;
    StringBuilder stringBuilder = new StringBuilder();

    //		本地环境数据库 star
    stringBuilder
        .append("mysqldump --opt ")
        .append(" --add-drop-database ")
        .append(" --add-drop-table ");
    stringBuilder
        .append(" -h")
        .append(host)
        .append(" -u")
        .append(userName)
        .append(" -p")
        .append(password);
    stringBuilder
        .append(" --result-file=")
        .append(backupFilePath)
        .append(" --default-character-set=utf8 ")
        .append(database);
    //		本地环境数据库 end

    // 调用外部执行 exe 文件的 Java API
    Process process = Runtime.getRuntime().exec(getCommand(stringBuilder.toString()));
    if (process.waitFor() == 0) {
      // 0 表示线程正常终止
      System.out.println("数据已经备份到 " + backupFilePath + " 文件中");
      log.info("数据已经备份到 {} 文件中", backupFilePath);
      return true;
    }
    return false;
  }

  /**
   * docker环境
   *
   * @param host
   * @param userName
   * @param password
   * @param backupFolderPath
   * @param fileName
   * @param database
   * @param dockerName
   * @return
   * @throws Exception
   */
  public static boolean backup(
      String host,
      String userName,
      String password,
      String backupFolderPath,
      String fileName,
      String database,
      String dockerName)
      throws Exception {
    File backupFolderFile = new File(backupFolderPath);
    if (!backupFolderFile.exists()) {
      // 如果目录不存在则创建
      backupFolderFile.mkdirs();
    }
    if (!backupFolderPath.endsWith(File.separator) && !backupFolderPath.endsWith("/")) {
      backupFolderPath = backupFolderPath + File.separator;
    }
    // 拼接命令行的命令
    String backupFilePath = backupFolderPath + fileName;
    StringBuilder stringBuilder = new StringBuilder();

    //		docker环境数据库 star
    //		String dockerName="mysql8";
    //		docker exec -i mysql8  mysqldump kuroneko -u root -proot  >
    // /Users/kuroneko/backup/kuroneko.sql
    stringBuilder
        .append("docker exec -i ")
        .append(dockerName)
        .append(" ")
        .append("mysqldump ")
        .append(database)
        .append(" ")
        .append("-u")
        .append(userName)
        .append(" ")
        .append("-p")
        .append(password)
        .append(" ")
        .append("> ")
        .append(backupFilePath);
    //		docker环境数据库 end
    // 调用外部执行 exe 文件的 Java API
    Process process = Runtime.getRuntime().exec(getCommand(stringBuilder.toString()));
    if (process.waitFor() == 0) {
      // 0 表示线程正常终止
      System.out.println("数据已经备份到 " + backupFilePath + " 文件中");
      log.info("数据已经备份到 {} 文件中", backupFilePath);
      return true;
    }
    return false;
  }

  /**
   * 还原数据库
   *
   * @param restoreFilePath 数据库备份的脚本路径
   * @param host IP地址
   * @param database 数据库名称
   * @param userName 用户名
   * @param password 密码
   * @return
   */
  public static boolean restore(
      String restoreFilePath, String host, String userName, String password, String database)
      throws Exception {
    File restoreFile = new File(restoreFilePath);
    if (restoreFile.isDirectory()) {
      for (File file : restoreFile.listFiles()) {
        if (file.exists() && file.getPath().endsWith(".sql")) {
          restoreFilePath = file.getAbsolutePath();
          break;
        }
      }
    }
    StringBuilder stringBuilder = new StringBuilder();
    //		本地环境数据库 star
    stringBuilder
        .append("mysql -h")
        .append(host)
        .append(" -u")
        .append(userName)
        .append(" -p")
        .append(password);
    stringBuilder.append(" ").append(database).append(" < ").append(restoreFilePath);
    //		本地环境数据库 end

    try {
      Process process = Runtime.getRuntime().exec(getCommand(stringBuilder.toString()));
      if (process.waitFor() == 0) {
        System.out.println("数据已从 " + restoreFilePath + " 导入到数据库中");
        log.info("数据已从 {} 导入到数据库中", restoreFilePath);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * docker环境
   *
   * @param restoreFilePath
   * @param host
   * @param userName
   * @param password
   * @param database
   * @param dockerName
   * @return
   * @throws Exception
   */
  public static boolean restore(
      String restoreFilePath,
      String host,
      String userName,
      String password,
      String database,
      String dockerName)
      throws Exception {
    File restoreFile = new File(restoreFilePath);
    if (restoreFile.isDirectory()) {
      for (File file : restoreFile.listFiles()) {
        if (file.exists() && file.getPath().endsWith(".sql")) {
          restoreFilePath = file.getAbsolutePath();
          break;
        }
      }
    }
    StringBuilder stringBuilder = new StringBuilder();
    //		String dockerName="mysql8";
    //		docker环境数据库 star
    //		docker exec -i mysql8 mysql -uroot -proot kuroneko < /Users/kuroneko/backup/kuroneko.sql
    stringBuilder
        .append("docker exec -i ")
        .append(dockerName)
        .append(" mysql -u")
        .append(userName)
        .append(" -p")
        .append(password);
    stringBuilder.append(" ").append(database).append(" < ").append(restoreFilePath);
    //		docker环境数据库 end
    try {
      Process process = Runtime.getRuntime().exec(getCommand(stringBuilder.toString()));
      if (process.waitFor() == 0) {
        System.out.println("数据已从 " + restoreFilePath + " 导入到数据库中");
        log.info("数据已从 {} 导入到数据库中", restoreFilePath);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  private static String[] getCommand(String command) {
    String os = System.getProperty("os.name");
    String shell = "/bin/bash";
    String c = "-c";
    if (os.toLowerCase().startsWith("win")) {
      shell = "cmd";
      c = "/c";
    }
    String[] cmd = {shell, c, command};
    return cmd;
  }

  public static void main(String[] args) throws Exception {
    String host = "localhost";
    String userName = "root";
    String password = "root";
    String database = "kuroneko";

    System.out.println("开始备份");
    String backupFolderPath = "/Users/kuroneko/";
    String fileName = "kuroneko.sql";
    backup(host, userName, password, backupFolderPath, fileName, database);
    System.out.println("备份成功");

    System.out.println("开始还原");
    String restoreFilePath = "/Users/kuroneko/kuroneko.sql";
    restore(restoreFilePath, host, userName, password, database);
    System.out.println("还原成功");
  }
}
