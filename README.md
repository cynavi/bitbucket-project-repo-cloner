# Bitbucket Project Repo Cloner

Clones all repository within a Bitbucket project and saves them to a specified location.

## Run Instructions

- Update the application.yml file with the necessary Bitbucket credentials, project details, and synchronization options.
```yaml
bitbucket:  
  project-key: key            # Bitbucket project key  
  username: username          # Your Bitbucket username  
  password: password          # Your Bitbucket password  

sync:  
  base:  
    path: /path/to/save       # Base path where repositories will be cloned  
  type: FORCE_CLONE           # Sync type: CLONE (Clone only if the repository does not already exist locally)
                              # or FORCE_CLONE (Delete the existing repository and re-clone it)
```
- Simply, run it as Spring application
```shell
mvn spring-boot:run
```

Happy Coding :)
