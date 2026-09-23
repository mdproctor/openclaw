package io.casehub.openclaw.app.api;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.casehub.openclaw.app.api.dto.CommitRequest;
import io.casehub.openclaw.app.api.dto.CommitResponse;
import io.casehub.openclaw.app.api.dto.CommitmentEntry;
import io.casehub.openclaw.app.api.dto.CommitmentsResponse;
import io.casehub.openclaw.app.api.dto.DoneRequest;
import io.casehub.openclaw.app.api.dto.DoneResponse;
import io.casehub.platform.api.mcp.McpDomain;
import io.casehub.platform.api.mcp.PathParam;
import io.casehub.platform.api.mcp.PlatformMutation;
import io.casehub.platform.api.mcp.PlatformQuery;
import io.casehub.platform.api.mcp.RestPath;
import io.casehub.qhorus.api.message.Commitment;
import io.casehub.qhorus.api.message.CommitmentState;
import io.casehub.qhorus.api.message.MessageType;
import io.casehub.qhorus.api.store.CommitmentStore;
import io.casehub.qhorus.runtime.message.CommitmentService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@McpDomain(value = "openclaw/plugin", app = "openclaw", basePath = "/api/openclaw/plugin")
@ApplicationScoped
@RolesAllowed("openclaw-plugin")
public class OpenClawPluginApi {

    @Inject CommitmentService commitmentService;
    @Inject CommitmentStore commitmentStore;

    @PlatformMutation("Open an auto-commit from a plugin")
    @RestPath("/commit")
    public CommitResponse commit(CommitRequest request) {
        Instant deadline = null;
        if (request.deadline() != null) {
            deadline = Instant.parse(request.deadline());
        }

        Commitment c = commitmentService.open(
                UUID.randomUUID(),
                UUID.randomUUID().toString(),
                null,
                MessageType.COMMAND,
                request.agentId(),
                request.agentId(),
                deadline);

        return new CommitResponse(
                c.correlationId(),
                c.expiresAt() != null ? c.expiresAt().toString() : "none");
    }

    @PlatformMutation("Close an auto-commit from a plugin")
    @RestPath("/done")
    public DoneResponse done(DoneRequest request) {
        commitmentService.fulfill(request.commitmentId());
        return new DoneResponse(true);
    }

    @PlatformQuery("List open commitments for an agent")
    @RestPath("/commitments/{agentId}")
    public CommitmentsResponse listCommitments(@PathParam String agentId) {
        List<CommitmentEntry> open = commitmentStore.findAllOpen().stream()
                .filter(c -> agentId.equals(c.obligor()))
                .filter(c -> c.state() == CommitmentState.OPEN
                        || c.state() == CommitmentState.ACKNOWLEDGED)
                .map(c -> new CommitmentEntry(
                        c.correlationId(),
                        c.state().name(),
                        c.expiresAt() != null ? c.expiresAt().toString() : "none",
                        true))
                .collect(Collectors.toList());

        return new CommitmentsResponse(open, open.size());
    }
}
