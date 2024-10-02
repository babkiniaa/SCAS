package org.example.textReader;

import lombok.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GitStatus {
    private String url = "";
    private String cloneDirectoryPath = "";

    public void cloneRepository() throws IOException {
        File cloneDirectory = new File(cloneDirectoryPath);
        try {
            // Клонирование репозитория
            System.out.println("Cloning repository from " + url + " to " + cloneDirectoryPath);
            Git git = Git.cloneRepository()
                    .setURI(url)  // Установка URL репозитория
                    .setDirectory(cloneDirectory)  // Установка директории для клонирования
                    .call();  // Выполнение операции клонирования
            git.close();
            git = null;
            System.out.println("Repository cloned successfully!");
        } catch (GitAPIException e) {
            System.out.println("Error during cloning repository: " + e.getMessage());
        }

    }

}

