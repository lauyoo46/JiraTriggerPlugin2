package com.ceilfors.jenkins.plugins.jiratrigger

import com.atlassian.jira.rest.client.api.AddressableEntity
import com.ceilfors.jenkins.plugins.jiratrigger.jira.JiraClient
import com.ceilfors.jenkins.plugins.jiratrigger.parameter.ParameterMapping
import groovy.util.logging.Log
import hudson.model.Item
import hudson.model.Job
import hudson.triggers.Trigger
import hudson.triggers.TriggerDescriptor
import jenkins.model.Jenkins
import jenkins.model.ParameterizedJobMixIn
import org.kohsuke.stapler.DataBoundSetter

import javax.inject.Inject
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author ceilfors
 */
@SuppressWarnings('Instanceof')
@Log
abstract class JiraTrigger<T> extends Trigger<Job> {

    @DataBoundSetter
    String jqlFilter = ''

    @DataBoundSetter
    List<ParameterMapping> parameterMappings = []

    @Override
    void start(Job project, boolean newInstance) {
        super.start(project, newInstance)
        jiraTriggerDescriptor.addTrigger(this)
    }

    @Override
    void stop() {
        super.stop()
        jiraTriggerDescriptor.removeTrigger(this)
    }

    Job getJob() {
        super.job
    }

    protected String getId(T t) {
        t instanceof AddressableEntity ? (t as AddressableEntity).self : t.toString()
    }

    JiraTriggerDescriptor getJiraTriggerDescriptor() {
        super.descriptor as JiraTriggerDescriptor
    }

    @SuppressWarnings('UnnecessaryTransientModifier')
    @Log
    static abstract class JiraTriggerDescriptor extends TriggerDescriptor {

        @Inject
        protected transient Jenkins jenkins

        @Inject
        transient JiraClient jiraClient

        private transient final List<JiraTrigger> triggers = new CopyOnWriteArrayList<>()

        @Override
        boolean isApplicable(Item item) {
            item instanceof Job && item instanceof ParameterizedJobMixIn.ParameterizedJob
        }

        @SuppressWarnings('GroovyUnusedDeclaration') // Jenkins jelly
        List<ParameterMapping.ParameterMappingDescriptor> getParameterMappingDescriptors() {
            jenkins.getDescriptorList(ParameterMapping)
        }

        protected void addTrigger(JiraTrigger jiraTrigger) {
            triggers.add(jiraTrigger)
            log.finest("Added [${jiraTrigger.job.fullName}]:[${jiraTrigger.class.simpleName}] to triggers list")
        }

        protected void removeTrigger(JiraTrigger jiraTrigger) {
            boolean result = triggers.remove(jiraTrigger)
            if (result) {
                log.finest("Removed [${jiraTrigger.job.fullName}]:[${jiraTrigger.class.simpleName}] from triggers list")
            } else {
                if (jiraTrigger.job) {
                    log.warning(
                            "Bug! Failed to remove [${jiraTrigger.job.fullName}]:[${jiraTrigger.class.simpleName}] " +
                                    'from triggers list. ' +
                                    'The job might accidentally be triggered by JIRA. Restart Jenkins to recover.')
                } else {
                    log.finest('Failed to remove trigger as it might not be started yet.' +
                            'This is normal for pipeline job.')
                }
            }
        }

        List<JiraTrigger> allTriggers() {
            Collections.unmodifiableList(triggers)
        }

        @Override
        abstract String getDisplayName()
    }
}
