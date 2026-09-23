package io.casehub.openclaw.app.api;

import io.casehub.openclaw.context.ChannelContextWindowService;
import io.casehub.openclaw.context.WindowContent;
import io.casehub.platform.api.mcp.McpDomain;
import io.casehub.platform.api.mcp.PathParam;
import io.casehub.platform.api.mcp.PlatformQuery;
import io.casehub.platform.api.mcp.RestPath;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@McpDomain(value = "openclaw/context", basePath = "/api/openclaw/context")
@ApplicationScoped
public class OpenClawContextApi {

    @Inject ChannelContextWindowService contextService;

    @PlatformQuery("Get the channel context window for an agent")
    @RestPath("/{agentId}")
    public WindowContent getContextWindow(@PathParam String agentId) {
        return contextService.query(agentId, 0);
    }
}
