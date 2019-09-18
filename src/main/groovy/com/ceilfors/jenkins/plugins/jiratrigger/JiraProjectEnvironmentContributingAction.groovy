package com.ceilfors.jenkins.plugins.jiratrigger

import com.atlassian.jira.rest.client.api.domain.Project
import hudson.EnvVars
import hudson.model.AbstractBuild
import hudson.model.EnvironmentContributingAction
import hudson.model.Run

import javax.annotation.Nonnull

class JiraProjectEnvironmentContributingAction implements EnvironmentContributingAction {

    String projectKey

    JiraProjectEnvironmentContributingAction(Project project) {
        this.projectKey = project.key
    }

    @Override
    String getIconFileName() {
       return null
    }

    @Override
    String getDisplayName() {
        return null
    }

    @Override
    String getUrlName() {
        return null
    }
}
