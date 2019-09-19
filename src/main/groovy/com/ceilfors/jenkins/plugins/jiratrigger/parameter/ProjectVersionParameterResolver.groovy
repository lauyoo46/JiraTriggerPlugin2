package com.ceilfors.jenkins.plugins.jiratrigger.parameter

import com.atlassian.jira.rest.client.api.domain.Issue
import com.atlassian.jira.rest.client.api.domain.Version
import com.ceilfors.jenkins.plugins.jiratrigger.JiraTriggerException

class ProjectVersionParameterResolver implements ParameterResolver {

    ProjectVersionParameterMapping projectVersionParameterMapping

    ProjectVersionParameterResolver(ProjectVersionParameterMapping projectVersionParameterMapping) {
        this.projectVersionParameterMapping = projectVersionParameterMapping
    }

    @Override
    String resolve(Issue issue) {
        return null
    }

    @Override
    String resolve(Version version) {
        resolveProperty(version.properties, projectVersionParameterMapping.releaseVersion)
    }

    /**
     * Resolves nested property from a Map.
     *
     * @param map the map which property to be resolved
     * @param property
     * @return the resolved property, null otherwise
     */
    static String resolveProperty(Map map, String property) {
        try {
            if (!property.contains('.') && !map.containsKey(property)) {
                // If property is not nested, Eval.x returns null instead of throwing NPE
                throw new JiraTriggerException(ParameterErrorCode.FAILED_TO_RESOLVE)
            }
            Eval.x(map, 'x.' + property)
        } catch (MissingPropertyException e) {
            throw new JiraTriggerException(ParameterErrorCode.FAILED_TO_RESOLVE, e)
        }
    }

}
