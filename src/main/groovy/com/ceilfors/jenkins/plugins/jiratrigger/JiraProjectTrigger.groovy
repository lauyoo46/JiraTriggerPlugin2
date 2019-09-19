package com.ceilfors.jenkins.plugins.jiratrigger

import com.atlassian.jira.rest.client.api.domain.Project
import com.atlassian.jira.rest.client.api.domain.Version
import com.ceilfors.jenkins.plugins.jiratrigger.parameter.DefaultParametersAction
import com.ceilfors.jenkins.plugins.jiratrigger.parameter.ProjectVersionParameterMapping
import hudson.model.Cause
import hudson.model.Action
import hudson.model.CauseAction
import jenkins.model.ParameterizedJobMixIn

abstract class JiraProjectTrigger<T> extends JiraTrigger<T> {

    boolean run(Project project, T t) {
        log.fine("[${job.fullName}] - Processing project '${project.key}'")

        if(!filter(project, t)) {
            return false
        }

        List<Action> actions = []
        actions << new ParameterMappingAction((Version)t, [getProjectVersion(project, t)])
        actions << new DefaultParametersAction(this.job)
        actions << new JiraProjectEnvironmentContributingAction(project)
        actions << new CauseAction(getCause(project))
        log.fine("[${job.fullName}] - Scheduling build for project '${project.key}'")

        ParameterizedJobMixIn.scheduleBuild2(job, -1, *actions) != null
    }

    abstract ProjectVersionParameterMapping getProjectVersion(Project project, T t)

    abstract boolean filter(Project project, T t)

    abstract Cause getCause(Project project)
}
