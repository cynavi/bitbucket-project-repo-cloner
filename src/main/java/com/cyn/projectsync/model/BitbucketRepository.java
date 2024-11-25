package com.cyn.projectsync.model;

import java.util.List;

public record BitbucketRepository(
        int size,
        List<Repository> values
) {
}
