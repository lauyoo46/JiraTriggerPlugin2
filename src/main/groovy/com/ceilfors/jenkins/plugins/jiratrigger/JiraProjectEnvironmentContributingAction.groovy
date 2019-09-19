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
    @Deprecated
    void buildEnvVars(AbstractBuild<?, ?> build, EnvVars env) {
        buildEnvironment(build, env)
    }

    @Override
    void buildEnvironment(@Nonnull Run<?, ?> run, @Nonnull EnvVars env) {
        env.put('JIRA_PROJECT_KEY', projectKey)
    }

    @Override
    String getIconFileName() {
        null
    }

    @Override
    String getDisplayName() {
        null
    }

    @Override
    String getUrlName() {
        null
    }
}
