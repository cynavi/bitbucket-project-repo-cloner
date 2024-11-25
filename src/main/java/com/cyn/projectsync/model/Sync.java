package com.cyn.projectsync.model;

public enum Sync {

    CLONE, // clones the repo while skipping existing repo
    FORCE_CLONE // overrides if any existing repo
}
