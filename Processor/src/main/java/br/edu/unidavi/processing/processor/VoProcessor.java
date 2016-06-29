package br.edu.unidavi.processing.processor;

import br.edu.unidavi.processing.annotation.Vo;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Set;

/**
 * Created by zozfabio on 28/06/2016.
 */
@SupportedAnnotationTypes({"br.edu.unidavi.processing.annotation.Vo"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class VoProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> els = roundEnv.getElementsAnnotatedWith(Vo.class);
        for (Element el : els) {
            if (el instanceof TypeElement) {
                TypeElement typeEl = (TypeElement) el;
                PackageElement packageEl = (PackageElement) typeEl.getEnclosingElement();
                try {
                    String className = typeEl.getSimpleName() + "OfStringConverter";
                    JavaFileObject file = processingEnv.getFiler().createSourceFile(packageEl.getQualifiedName() + "." + className);

                    try (OutputStream os = file.openOutputStream()) {
                        try (PrintWriter out = new PrintWriter(os)) {
                            out.append("package ").append(packageEl.getQualifiedName()).append(";\n");
                            out.append("\n");
                            out.append("import ").append(typeEl.getQualifiedName()).append(";\n");
                            out.append("import org.springframework.core.convert.converter.Converter;\n");
                            out.append("import org.springframework.stereotype.Component;\n");
                            out.append("\n");
                            out.append("import static java.util.Objects.isNull;\n");
                            out.append("\n");
                            out.append("@Component\n");
                            out.append("class ").append(className).append(" implements Converter<String, ").append(typeEl.getSimpleName()).append("> {\n");
                            out.append("\n");
                            out.append("    @Override\n");
                            out.append("    public ").append(typeEl.getSimpleName()).append(" convert(String source) {\n");
                            out.append("        if (isNull(source)) return null;\n");
                            out.append("        source = source.trim();\n");
                            out.append("        if (source.isEmpty()) return null;\n");
                            out.append("        return new ").append(typeEl.getSimpleName()).append("(source);\n");
                            out.append("    }\n");
                            out.append("}");
                            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Created " + file.getName());
                        }
                    }
                } catch (IOException ex) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, ex.getMessage());
                }
            }
        }
        return true;
    }
}
