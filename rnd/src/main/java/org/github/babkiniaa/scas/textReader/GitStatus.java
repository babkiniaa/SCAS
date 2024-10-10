package org.github.babkiniaa.scas.textReader;

import lombok.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GitStatus {
    private String url = "";
    private String cloneDirectoryPath = "";

    public void cloneRepository() throws GitAPIException {
        File cloneDirectory = new File(cloneDirectoryPath);
        try {
            // Клонирование репозитория
            Git git = Git.cloneRepository()
                    .setURI(url)  // Установка URL репозитория
                    .setDirectory(cloneDirectory)  // Установка директории для клонирования
                    .call();  // Выполнение операции клонирования
            git.close();
            git = null;
        } catch (GitAPIException e) {
            throw e;
        }

    }

}