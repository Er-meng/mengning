package cn.mn.mn;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class FreemarkerUtils {
    public static String getTemplate(File template, String templateFile,Map<String,Object> map) throws IOException {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
            cfg.setDirectoryForTemplateLoading(template);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            Template temp = cfg.getTemplate(templateFile);
            StringWriter stringWriter = new StringWriter();
            temp.process(map, stringWriter);
            return stringWriter.toString();
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
}
