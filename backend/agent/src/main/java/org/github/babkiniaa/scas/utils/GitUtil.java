package org.github.babkiniaa.scas.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
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
}