package io.casehub.openclaw.app.api;

import io.casehub.openclaw.app.api.dto.WorkitemResult;
import io.casehub.openclaw.app.scenario.ScenarioRestResource;
import io.casehub.platform.api.mcp.McpDomain;
import io.casehub.platform.api.mcp.PathParam;
import io.casehub.platform.api.mcp.PlatformMutation;
import io.casehub.platform.api.mcp.PlatformQuery;
import io.casehub.platform.api.mcp.RestPath;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@McpDomain(value = "openclaw/scenarios", basePath = "/api/openclaw/scenarios")
@ApplicationScoped
public class OpenClawScenarioApi {

    @Inject ScenarioRestResource scenarioResource;

    @PlatformQuery("List all scenarios")
    @RestPath("/")
    @PermitAll
    public List<?> listScenarios() {
        return scenarioResource.list();
    }

    @PlatformQuery("Get scenario state")
    @RestPath("/{id}/state")
    @PermitAll
    public Object getState(@PathParam String id) {
        return scenarioResource.state(id);
    }

    @PlatformMutation("Start a scenario")
    @RestPath("/{id}/start")
    @RolesAllowed("openclaw-admin")
    public WorkitemResult startScenario(@PathParam String id) {
        Response r = scenarioResource.start(id);
        return new WorkitemResult(r.getStatus() == 200, r.getEntity().toString());
    }

    @PlatformMutation("Complete a work item gate in a scenario")
    @RestPath("/{id}/workitems/{gateId}/complete")
    @RolesAllowed("openclaw-admin")
    public WorkitemResult completeWorkitem(@PathParam String id, @PathParam String gateId,
                                           Map<String, String> body) {
        Response r = scenarioResource.completeWorkitem(id, gateId,
            new ScenarioRestResource.WorkitemCompleteRequest(
                body.get("action"), body.get("note")));
        return new WorkitemResult(r.getStatus() == 200, "Completed gate " + gateId);
    }
}
