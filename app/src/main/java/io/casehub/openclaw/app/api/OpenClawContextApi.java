package io.casehub.openclaw.app.api;

import io.casehub.openclaw.app.ChannelContextWindowResource;
import io.casehub.platform.api.mcp.McpDomain;
import io.casehub.platform.api.mcp.PathParam;
import io.casehub.platform.api.mcp.PlatformQuery;
import io.casehub.platform.api.mcp.RestPath;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@McpDomain(value = "openclaw/context", basePath = "/api/openclaw/context")
@ApplicationScoped
public class OpenClawContextApi {

    @Inject ChannelContextWindowResource contextResource;

    @PlatformQuery("Get the channel context window for an agent")
    @RestPath("/{agentId}")
    public Object getContextWindow(@PathParam String agentId) {
        return contextResource.query(agentId, 0);
    }
}
