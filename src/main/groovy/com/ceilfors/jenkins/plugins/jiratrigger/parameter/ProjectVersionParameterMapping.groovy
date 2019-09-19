package com.ceilfors.jenkins.plugins.jiratrigger.parameter

import groovy.transform.EqualsAndHashCode
import org.kohsuke.stapler.DataBoundConstructor

@EqualsAndHashCode(callSuper = true)
class ProjectVersionParameterMapping extends ParameterMapping {

    final String releaseVersion

    @DataBoundConstructor
    ProjectVersionParameterMapping(String jenkinsParameter, String releaseVersion) {
        super(jenkinsParameter)
        this.releaseVersion = releaseVersion
    }

    @Override
    ParameterResolver getParameterResolver() {
        new ProjectVersionParameterResolver(this)
    }
}
