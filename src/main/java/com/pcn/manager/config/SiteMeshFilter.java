package com.pcn.manager.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.content.tagrules.html.DivExtractingTagRuleBundle;

public class SiteMeshFilter extends ConfigurableSiteMeshFilter {

    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        //Map default decorator. This shall be applied to all paths if no other paths match.
        builder.addDecoratorPath("/manager/**", "/WEB-INF/decorators/main.jsp")
            .addExcludedPath("/login/**")
            .addExcludedPath("/error/**")
            .addExcludedPath("/popup/**")
            .addExcludedPath("/form/**")
            .addExcludedPath("/plugin/**");
        /*
        //Map decorators to path patterns.
        builder.addDecoratorPath("/web/**.do*", "/WEB-INF/decorators/defaultDecorator.jsp")
        	   .addDecoratorPath("/backend/**.do", "/WEB-INF/decorators/adminDecorator.jsp");
        //Map multiple decorators to the a single path.
        builder.addDecoratorPaths("/articles/*", "/decorators/article.html",
        										 "/decoratos/two-page-layout.html",
        										 "/decorators/common.html");
        
         */

        //Exclude path from decoration.

        //add div tag rule
        builder.addTagRuleBundles(new DivExtractingTagRuleBundle());
    }
}
