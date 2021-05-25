package com.yinlu.code.generator;

import com.yinlu.code.generator.model.TableInfo;
import com.yinlu.code.generator.utils.ZipUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.zip.ZipFile;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tools.zip.ZipUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@Slf4j
@SpringBootTest
class CodeGeneratorApplicationTests {
  //    @Resource
  //    FreeMarkerTemplateUtils freeMarkerTemplateUtils;
  @Resource Configuration configuration;
  //  public String mvn =
  //      " cd D:/a/ &&  mvn archetype:generate  \"-DinteractiveMode=false\"
  // \"-DarchetypeGroupId=com.yinlu\" \"-DarchetypeArtifactId=code-generator-archetype\"
  // \"-DarchetypeCatalog=local\" \"-DgroupId=com.example\" \"-DartifactId=demo\" \"-Dpackage=demo\"
  // \"-Dversion=1.0\"";
  //  String a="D:/ideaProject/new/code-generator/src/shell/test.sh";
  @Test
  void contextLoads() throws IOException, TemplateException {
    TableInfo tableInfo = new TableInfo();
    tableInfo.setTableCatalog("1");
    tableInfo.setTableSchema("2");
    tableInfo.setTableName("3");
    tableInfo.setTableType("4");
    Template template = configuration.getTemplate("java/model.ftl");
    String s = FreeMarkerTemplateUtils.processTemplateIntoString(template, tableInfo);
    File file = new File(tableInfo.getTableName() + ".java");
    FileUtils.touch(file);
    FileUtils.writeStringToFile(file, s, "UTF-8");
  }

  @Test
  void maven() throws IOException, InterruptedException {

    //当前目录
    String currentDirectory = System.getProperty("user.dir") + File.separator+"temp";

    //archetypeGroupId
    String archetypeGroupId="com.yinlu";
    //archetypeArtifactId
    String archetypeArtifactId="code-generator-archetype";
    //archetypeCatalog
    String archetypeCatalog="local";
    //groupId
    String demoGroupId="com.demo";
    //artifactId
    String demoArtifactId="demo";
    //package
    String demoPackage="com.demo";
    //version
    String demoVersion="1.0.0";
    // 生成原型  mvn archetype:generate
    String buildDemo =
        "cd "
            + currentDirectory
            + " &&  mvn archetype:generate \"-DinteractiveMode=false\" "
            + "\"-DarchetypeGroupId="
            + archetypeGroupId
            + "\" "
            + "\"-DarchetypeArtifactId="
            + archetypeArtifactId
            + "\" "
            + "\"-DarchetypeCatalog="
            + archetypeCatalog
            + "\" "
            + "\"-DgroupId="
            + demoGroupId
            + "\" "
            + "\"-DartifactId="
            + demoArtifactId
            + "\" "
            + "\"-Dpackage="
            + demoPackage
            + "\" "
            + "\"-Dversion="+demoVersion+"\" ";
//    String mvn =
//        "&&  mvn archetype:generate  \"-DinteractiveMode=false\" \"-DarchetypeGroupId=com.yinlu\" \"-DarchetypeArtifactId=code-generator-archetype\" \"-DarchetypeCatalog=local\" \"-DgroupId=com.example\" \"-DartifactId=demo\" \"-Dpackage=demo\" \"-Dversion=1.0\"";
    File file = new File(currentDirectory);
//    String command = "cd " + currentDirectory + mvn;
    if (!file.exists()) {
      file.mkdirs();
    }
    log.info(buildDemo);
    Process demoProcess = Runtime.getRuntime().exec(getCommand(buildDemo));
    demoProcess.waitFor();
    demoProcess.destroy();
    String zipPath=currentDirectory+File.separator+demoArtifactId;
    String buildZip=zipPath+".zip";
    FileOutputStream fileOutputStream = new FileOutputStream(buildZip);
    log.info(buildZip);
    ZipUtils.toZip(zipPath,fileOutputStream,true);
    fileOutputStream.close();

  }
  @Test
  void  zip() throws IOException {
    FileOutputStream outputStream =
        new FileOutputStream("D:\\ideaProject\\new\\code-generator\\temp\\demo1.zip");
    ZipUtils.toZip(
        "D:\\ideaProject\\new\\code-generator\\temp\\demo",outputStream,true
        );
    outputStream.close();
  }

  @Test
  void dir() {
    System.out.println(System.getProperty("user.dir"));
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
}
