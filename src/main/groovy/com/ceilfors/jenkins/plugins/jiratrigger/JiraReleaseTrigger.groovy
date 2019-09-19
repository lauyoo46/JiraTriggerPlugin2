package com.ceilfors.jenkins.plugins.jiratrigger

import com.atlassian.jira.rest.client.api.domain.Project
import com.atlassian.jira.rest.client.api.domain.Version
import com.ceilfors.jenkins.plugins.jiratrigger.parameter.ProjectVersionParameterMapping
import groovy.util.logging.Log
import hudson.Extension
import hudson.model.Cause
import org.kohsuke.stapler.DataBoundConstructor
import org.kohsuke.stapler.DataBoundSetter
@Log
class JiraReleaseTrigger extends JiraProjectTrigger<Version> {

    public static final String DEFAULT_PROJECT_KEY = 'Project Key'

    @DataBoundSetter
    String projectKey = JiraReleaseTriggerDescriptor.DEFAULT_PROJECT_KEY_PATTERN

    @SuppressWarnings('UnnecessaryConstructor')
    @DataBoundConstructor
    JiraReleaseTrigger() {
    }

    @Override
    boolean filter(Project project, Version version) {
        if(project.key == projectKey) {
            return true
        }
        return false
    }

    @Override
    boolean run(Project project, Version version) {
        super.run(project, version)
    }

    @Override
    Cause getCause(Project project) {
        new JiraReleaseTriggerCause()
    }

    @Override
    ProjectVersionParameterMapping getProjectVersion(Project project, Version version) {
        return new ProjectVersionParameterMapping('JIRA_VERSION_NAME', 'name')
    }

    @SuppressWarnings('UnnecessaryQualifiedReference')
    @Extension
    static class JiraReleaseTriggerDescriptor extends JiraTrigger.JiraTriggerDescriptor {

        @SuppressWarnings('GroovyUnusedDeclaration')
        public static final String DEFAULT_PROJECT_KEY_PATTERN = "(?i)${DEFAULT_PROJECT_KEY}"

        @Override
        String getDisplayName() {
            'Build when a new version is released'
        }
    }

    static class JiraReleaseTriggerCause extends Cause {

        @Override
        String getShortDescription() {
            'JIRA version is released'
        }
    }

}
