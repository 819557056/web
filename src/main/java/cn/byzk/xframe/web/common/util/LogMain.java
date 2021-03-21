package cn.byzk.xframe.web.common.util;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;

public class LogMain {

    public static void main(String[] args) {
        DocsConfig config = new DocsConfig();
        config.setProjectPath("E:\\work\\workspaces\\idea\\api-cert"); // root project path
        config.setProjectName("api-cert"); // project name
        config.setApiVersion("V1.0");       // api version
        config.setDocsPath("E:\\work\\workspaces\\idea\\api-cert\\docs"); // api docs target path
        config.setAutoGenerate(Boolean.TRUE);  // auto generate
        Docs.buildHtmlDocs(config); // execute to generate
    }
}
