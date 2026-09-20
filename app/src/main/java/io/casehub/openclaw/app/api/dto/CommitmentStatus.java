package io.casehub.openclaw.app.api.dto;

import java.time.Instant;

public record CommitmentStatus(String commitmentId, String agentId, String task,
    String state, Instant deadline, String outcome) {}
