package com.mobile.hero.compile;

import com.google.auto.service.AutoService;
import com.mobile.hero.annotation.Guide;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class GuideAnnotationProcessor extends AbstractProcessor {
    private static final String FIELD_MAP_NAME = "guideNames";
    private static final String FIELD_MAP_GUIDE = "guideMaps";

    Filer filer;
    Elements elements;
    Messager messager;

    public GuideAnnotationProcessor() {
        super();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(Guide.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        elements = processingEnvironment.getElementUtils();
        messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        messager.printMessage(Diagnostic.Kind.NOTE, "guide start" + roundEnvironment);
        try {
            for (Element element : roundEnvironment.getElementsAnnotatedWith(Guide.class)) {
                messager.printMessage(Diagnostic.Kind.NOTE, "guide " + element);
                Guide guide = element.getAnnotation(Guide.class);
                String group = guide.group();
                int priority = guide.priority();

                MethodSpec.Builder register = MethodSpec.methodBuilder("register")
                        .addModifiers(Modifier.STATIC)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(String.class, "group")
                        .addCode("if(guideNames.containsKey(group)){\n" +
                                "String clz = guideNames.get(group);\n" +
                                "try{\n" +
                                "com.mobile.hero.api.IGuide guide = (com.mobile.hero.api.IGuide) Class.forName(clz).newInstance();\n" +
                                "guideMaps.put($S,guide);\n" +
                                "}catch(Exception e){\n" +
                                "e.printStackTrace();\n" +
                                "}\n" +
                                "}\n", group);

                TypeName typeName = ParameterizedTypeName.get(Map.class, String.class, String.class);
                FieldSpec guideNames = FieldSpec.builder(typeName, FIELD_MAP_NAME, Modifier.PUBLIC, Modifier.STATIC).build();

                TypeMirror iguide = null;
                for (TypeMirror mirror : ((TypeElement) element).getInterfaces()) {
                    messager.printMessage(Diagnostic.Kind.NOTE, "mirror:" + mirror);
                    if (mirror.toString().contains("IGuide")) {
                        iguide = mirror;
                        break;
                    }
                }

                TypeName map = ParameterizedTypeName.get(ClassName.get(Map.class),
                        TypeName.get(String.class),
                        TypeName.get(iguide));
                FieldSpec guideMaps = FieldSpec.builder(map, FIELD_MAP_GUIDE, Modifier.PUBLIC, Modifier.STATIC).build();

                CodeBlock init = CodeBlock.builder()
                        .add("guideNames=new $L<>();\n", HashMap.class.getName())
                        .add("guideMaps=new $L<>();\n", HashMap.class.getName())
                        .add("guideNames.put($S,$S);\n", group, ((TypeElement) element).getQualifiedName())
                        .build();

                TypeSpec clz = TypeSpec.classBuilder("GuideManager")
                        .addModifiers(Modifier.PUBLIC)
                        .addMethod(register.build())
                        .addField(guideNames)
                        .addField(guideMaps)
                        .addStaticBlock(init)
                        .build();

                String pkg = elements.getPackageOf(element).getQualifiedName().toString();
                messager.printMessage(Diagnostic.Kind.NOTE, "guide pkg= " + pkg);
                JavaFile.builder(pkg, clz).build().writeTo(filer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        return true;
    }
}
