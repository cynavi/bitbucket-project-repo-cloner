package com.cyn.projectsync;

import com.cyn.projectsync.service.SyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	@Autowired
	private SyncService syncService;

	@Autowired
	private ApplicationContext context;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) {
		logger.info("Starting repo cloner");
		syncService.getRepositories()
				.subscribe(bitbucketRepo -> bitbucketRepo.values().forEach(repo -> syncService.clone(repo)));
		logger.info("Exiting repo cloner");
		SpringApplication.exit(context, () -> 0);
	}
}
