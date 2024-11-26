package org.github.babkiniaa.scas.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.github.babkiniaa.scas.dto.GitDto;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;


public class GitUtil {

  public static GitDto cloneRepository(String url, String cloneDirectoryPath, String branch, String commit) throws GitAPIException, IOException {
    File cloneDirectory = new File(cloneDirectoryPath);
    GitDto gitDto = new GitDto();

    try {
      Git git = Git.cloneRepository()
              .setURI(url)
              .setDirectory(cloneDirectory)
              .call();
      if (commit != null){
        gitDto.setHash(commit);
        git.checkout().setName(commit).call();

      } else if (branch != null) {
        git.checkout().setName("origin/" + branch).call();
        gitDto.setHash(git.getRepository().getBranch());
        gitDto.setBranch("origin/" + branch);
      } else {
        gitDto.setHash(git.log().setMaxCount(1).call().iterator().next().getName());
      }

      git.close();
    } catch (GitAPIException | IOException e ) {
      throw e;
    }

      return gitDto;
  }

  public static void downloadUrl(String url, Integer idReport) throws GitAPIException {
    String currentDir = System.getProperty("user.dir") + "/backend/agent/src/main/java/" + idReport;
    String currentDirUser = System.getProperty("user.dir") + "/down/" + idReport;

    //GitUtil.cloneRepository(url, currentDirUser);
    //GitUtil.cloneRepository(url, currentDir);
  }
}