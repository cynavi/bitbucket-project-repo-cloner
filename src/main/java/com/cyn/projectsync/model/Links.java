package com.cyn.projectsync.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Links(
    @JsonProperty("clone")
    List<Clone> clones
) {}
