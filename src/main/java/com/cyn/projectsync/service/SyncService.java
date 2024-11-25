package com.cyn.projectsync.service;

import com.cyn.projectsync.model.BitbucketRepository;
import com.cyn.projectsync.model.Repository;
import com.cyn.projectsync.model.Sync;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;

@Service
public class SyncService {

    private static final Logger logger = LoggerFactory.getLogger(SyncService.class);

    @Value("${bitbucket.project-key}")
    private String projectKey;

    @Value("${bitbucket.username}")
    private String username;

    @Value("${bitbucket.password}")
    private String password;

    @Value("${sync.base.path}")
    private String basePath;

    @Value("${sync.type}")
    private Sync sync;

    private final WebClient webClient;

    public SyncService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://bitbucket.cotiviti.com").build();
    }

    public Mono<BitbucketRepository> getRepositories() {
        return webClient.get()
                .uri("/rest/api/1.0/projects/{projectKey}/repos", projectKey)
                .headers(headers -> headers.setBasicAuth(username, password))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(BitbucketRepository.class);
    }

    public void clone(Repository repo) {
        String path = basePath + File.separator + projectKey + File.separator + repo.name();
        File directory = new File(path);
        logger.info("Clone initiated for {} on path {}", repo.name(), path);
        if (directory.exists() && sync == Sync.FORCE_CLONE) {
            try {
                FileUtils.delete(directory, 1);
            } catch (IOException e) {
                logger.error("Unable to delete directory {}", directory.getAbsolutePath(), e);
            }
        }
        if (directory.exists() && sync == Sync.CLONE) {
            logger.info("Skipping cloning of {} on path {} as directory with same name already exists", repo.name(), path);
        } else {
            try (Git unused = Git.cloneRepository()
                    .setURI(repo.links().clones().stream().filter(r -> r.name().equals("http")).findFirst().get().href())
                    .setDirectory(new File(path))
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                    .call()) {
                logger.info("Cloned repository {} to {}", repo.name(), path);
            } catch (GitAPIException e) {
                logger.error("Unable to clone repository {} to {}", repo.name(), path, e);
            }
        }
    }
}
