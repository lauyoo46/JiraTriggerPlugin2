package com.ceilfors.jenkins.plugins.jiratrigger.webhook

/**
 * @author ceilfors
 */
interface JiraWebhookListener {

    void commentCreated(WebhookCommentEvent commentEvent)

    void changelogCreated(WebhookChangelogEvent changelogEvent)

    void releaseCreated(WebhookReleaseEvent releaseEvent)
}
