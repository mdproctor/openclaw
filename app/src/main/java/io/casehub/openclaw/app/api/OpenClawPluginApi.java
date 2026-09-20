package io.casehub.openclaw.app.api;

import io.casehub.openclaw.app.PluginCommitResource;
import io.casehub.platform.api.mcp.McpDomain;
import io.casehub.platform.api.mcp.PathParam;
import io.casehub.platform.api.mcp.PlatformMutation;
import io.casehub.platform.api.mcp.PlatformQuery;
import io.casehub.platform.api.mcp.RestPath;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@McpDomain(value = "openclaw/plugin", basePath = "/api/openclaw/plugin")
@ApplicationScoped
@RolesAllowed("openclaw-plugin")
public class OpenClawPluginApi {

    @Inject PluginCommitResource pluginResource;

    @PlatformMutation("Open an auto-commit from a plugin")
    @RestPath("/commit")
    public Object commit(PluginCommitResource.CommitRequest request) {
        return pluginResource.commit(request);
    }

    @PlatformMutation("Close an auto-commit from a plugin")
    @RestPath("/done")
    public Object done(PluginCommitResource.DoneRequest request) {
        return pluginResource.done(request);
    }

    @PlatformQuery("List open commitments for an agent")
    @RestPath("/commitments/{agentId}")
    public PluginCommitResource.CommitmentsResponse listCommitments(@PathParam String agentId) {
        return pluginResource.commitments(agentId);
    }
}
