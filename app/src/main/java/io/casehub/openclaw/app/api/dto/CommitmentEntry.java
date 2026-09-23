package io.casehub.openclaw.app.api.dto;

public record CommitmentEntry(String commitmentId, String state, String deadline, boolean watchdogArmed) {}
