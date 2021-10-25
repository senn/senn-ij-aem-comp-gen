package {%PACKAGE_NAME%}.impl;

import {%SLING_MODEL_NAME_FULL%};
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(
        adaptables = {Resource.class},
        adapters = {{%COMP_NAME_SHORT%}.class},
        resourceType = {{%COMP_NAME_SHORT%}.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class {%COMP_NAME_SHORT%}Impl implements {%COMP_NAME_SHORT%} {

}