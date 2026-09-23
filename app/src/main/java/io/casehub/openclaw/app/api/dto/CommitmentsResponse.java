package io.casehub.openclaw.app.api.dto;

import java.util.List;

public record CommitmentsResponse(List<CommitmentEntry> open, int count) {}
