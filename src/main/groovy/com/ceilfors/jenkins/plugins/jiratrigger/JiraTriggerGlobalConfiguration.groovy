package com.ceilfors.jenkins.plugins.jiratrigger

import hudson.Extension
import hudson.util.Secret
import jenkins.model.GlobalConfiguration
import net.sf.json.JSONObject
import org.kohsuke.stapler.StaplerRequest

/**
 * @author ceilfors
 */
@Extension
class JiraTriggerGlobalConfiguration extends GlobalConfiguration {

    String jiraRootUrl
    String jiraUsername
    Secret jiraPassword
    boolean jiraCommentReply = false

    JiraTriggerGlobalConfiguration() {
        load()
    }

    JiraTriggerGlobalConfiguration(String jiraRootUrl, String jiraUsername, String jiraPassword) {
        this.jiraRootUrl = jiraRootUrl
        this.jiraUsername = jiraUsername
        this.setJiraPassword(Secret.fromString(jiraPassword))
    }

    @Override
    boolean configure(StaplerRequest req, JSONObject formData) {
        req.bindJSON(this, formData)
        save()
        true
    }

    /**
     * Validates this global configuration. Do note that the validation must be called lazily as these
     * values are only mandatory when Jenkins is required to hit JIRA.
     */
    void validateConfiguration() {
        JiraTriggerException exception = new JiraTriggerException(JiraTriggerErrorCode.JIRA_NOT_CONFIGURED)
        String key = 'config'
        if (!jiraRootUrl) {
            throw exception.add(key, 'jiraRootUrl')
        }
        if (!jiraPassword) {
            throw exception.add(key, 'jiraPassword')
        }
        if (!jiraUsername) {
            throw exception.add(key, 'jiraUsername')
        }
    }

    String getEncodedAuthentication() {
        return (jiraUsername + ":" + jiraPassword).getBytes().encodeBase64().toString()
    }

    HttpURLConnection getJsonResponse(org.codehaus.jettison.json.JSONObject webhookEvent) {
        String authString = getEncodedAuthentication()
        String projectId = webhookEvent.getJSONObject('version').getString('projectId')
        def addr = "http://10.106.0.77:8081/rest/api/2/project/" + projectId
        def conn = addr.toURL().openConnection() as HttpURLConnection
        conn.setRequestProperty("Authorization", "Basic ${authString}")
        conn.requestMethod = 'GET'
        return conn
    }
}
