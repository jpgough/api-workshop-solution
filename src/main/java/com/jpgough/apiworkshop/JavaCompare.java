package com.jpgough.apiworkshop;

import com.deepoove.swagger.diff.SwaggerDiff;
import com.deepoove.swagger.diff.output.HtmlRender;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class JavaCompare {
    public static void main(String[] args) {
        String oldSpec = "swagger-diff-samples/v1.1.json";
        String newSpec = "swagger-diff-samples/v2.0.json";
        final var swaggerDiff = SwaggerDiff.compareV2(oldSpec, newSpec);
        String html = new HtmlRender("Changelog",
                "http://deepoove.com/swagger-diff/stylesheets/demo.css")
                .render(swaggerDiff);
        try {
            FileWriter fw = new FileWriter(
                    "swagger-diff-samples/testNewApi.html");
            fw.write(html);
            fw.close();
            System.out.println("Completed compare");
            System.out.println();
            System.out.println("file://"+ Paths.get("swagger-diff-samples/testNewApi.html").toAbsolutePath().toString());
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
