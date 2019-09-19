package com.ceilfors.jenkins.plugins.jiratrigger

import com.atlassian.jira.rest.client.api.domain.Issue
import com.ceilfors.jenkins.plugins.jiratrigger.parameter.DefaultParametersAction
import hudson.model.Cause
import hudson.model.Action
import hudson.model.CauseAction
import jenkins.model.ParameterizedJobMixIn

abstract class JiraIssueTrigger<T> extends JiraTrigger<T> {

    boolean run(Issue issue, T t) {
        log.fine("[${job.fullName}] - Processing ${issue.key} - ${getId(t)}")

        if (!filter(issue, t)) {
            return false
        }
        if (jqlFilter) {
            if (!jiraTriggerDescriptor.jiraClient.validateIssueKey(issue.key, jqlFilter)) {
                log.fine("[${job.fullName}] - Not scheduling build: The issue ${issue.key} doesn't " +
                        "match with the jqlFilter [$jqlFilter]")
                return false
            }
        }

        List<Action> actions = []
        if (parameterMappings) {
            actions << new ParameterMappingAction(issue, parameterMappings)
        }
        actions << new DefaultParametersAction(this.job)
        actions << new JiraIssueEnvironmentContributingAction(issue)
        actions << new CauseAction(getCause(issue, t))
        log.fine("[${job.fullName}] - Scheduling build for ${issue.key} - ${getId(t)}")

        ParameterizedJobMixIn.scheduleBuild2(job, -1, *actions) != null
    }

    abstract boolean filter(Issue issue, T t)

    abstract Cause getCause(Issue issue, T t)
}
