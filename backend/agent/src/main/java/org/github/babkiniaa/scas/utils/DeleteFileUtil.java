package org.github.babkiniaa.scas.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class DeleteFileUtil {

  public static void del(String cloneDirectoryPath, String cloneDirectoryPathDeskstop) throws IOException {
    File f = new File(cloneDirectoryPath);
    File fDesktop = new File(cloneDirectoryPathDeskstop);

    if (f.exists()) {
      deleteDir(f);
    }
    if (fDesktop.exists()) {
      deleteDir(fDesktop);
    }
  }

  public static void deleteDir(File file) {
    File[] contents = file.listFiles();

    if (contents != null) {
      for (File f : contents) {
        if (!Files.isSymbolicLink(f.toPath())) {
          deleteDir(f);
        }
      }
    }
    file.delete();
  }

  public static void deleteFile(Integer reportId) throws IOException {
    String currentDir = System.getProperty("user.dir") + "/backend/agent/src/main/java/" + reportId;
    String currentDirUser = System.getProperty("user.dir") + "/down/" + reportId;

    DeleteFileUtil.del(currentDir, currentDirUser);
  }
}
