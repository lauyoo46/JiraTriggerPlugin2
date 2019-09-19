package com.ceilfors.jenkins.plugins.jiratrigger.parameter

import com.atlassian.jira.rest.client.api.domain.Issue
import com.atlassian.jira.rest.client.api.domain.Version

/**
 * Responsible for providing a parameter value for Jenkins by digesting JIRA information.
 *
 * @author ceilfors
 */
interface ParameterResolver {

    String resolve(Issue issue)

    String resolve(Version version)
}
