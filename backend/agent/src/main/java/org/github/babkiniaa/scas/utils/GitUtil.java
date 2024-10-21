package org.github.babkiniaa.scas.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Component;

import java.io.File;


public class GitUtil {

  public static void cloneRepository(String url, String cloneDirectoryPath) throws GitAPIException {
    File cloneDirectory = new File(cloneDirectoryPath);

    try {
      Git git = Git.cloneRepository()
              .setURI(url)
              .setDirectory(cloneDirectory)
              .call();
      git.close();
    } catch (GitAPIException e) {
      throw e;
    }
  }

  public static void downloadUrl(String url, Integer idReport) throws GitAPIException {
    String currentDir = System.getProperty("user.dir") + "/backend/agent/src/main/java/" + idReport;
    String currentDirUser = System.getProperty("user.dir") + "/down/" + idReport;

    GitUtil.cloneRepository(url, currentDirUser);
    GitUtil.cloneRepository(url, currentDir);
  }
}