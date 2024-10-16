package org.github.babkiniaa.scas.textReader;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Component;
import java.io.File;

@Component
public class GitStatus {

  public void cloneRepository(String url, String cloneDirectoryPath) throws GitAPIException {
    File cloneDirectory = new File(cloneDirectoryPath);

    try {
      Git git = Git.cloneRepository()
              .setURI(url)
              .setDirectory(cloneDirectory)
              .call();
      git.close();
      git = null;
    } catch (GitAPIException e) {
      throw e;
    }
  }
}