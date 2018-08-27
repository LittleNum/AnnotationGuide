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
    private static final String FIELD_MAP_GROUP = "guideGroups";
    private static final String FIELD_MAP_PRIORITY = "guidePrioritys";

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

                //三个静态map初始化
                TypeName strMap = ParameterizedTypeName.get(Map.class, String.class, String.class);
                FieldSpec guideNames = FieldSpec.builder(strMap, FIELD_MAP_NAME, Modifier.PRIVATE, Modifier.STATIC).build();

                FieldSpec guideGroups = FieldSpec.builder(strMap, FIELD_MAP_GROUP, Modifier.PRIVATE, Modifier.STATIC).build();

                TypeName intMap = ParameterizedTypeName.get(Map.class, String.class, Integer.class);
                FieldSpec guidePrioritys = FieldSpec.builder(intMap, FIELD_MAP_PRIORITY, Modifier.PRIVATE, Modifier.STATIC).build();

                CodeBlock init = CodeBlock.builder()
                        .add("guideNames=new $L<>();\n", HashMap.class.getName())
                        .add("guideGroups=new $L<>();\n", HashMap.class.getName())
                        .add("guidePrioritys=new $L<>();\n", HashMap.class.getName())
                        .add("guideNames.put($S,$S);\n", ((TypeElement) element).getSimpleName(), ((TypeElement) element).getQualifiedName())
                        .add("guideGroups.put($S,$S);\n", ((TypeElement) element).getSimpleName(), group)
                        .add("guidePrioritys.put($S,$L);\n", ((TypeElement) element).getSimpleName(), priority)
                        .build();

                //实现GuideCollect接口的方法
                //①返回名称与完整类名的map
                MethodSpec.Builder nameMethod = MethodSpec.methodBuilder("getGuidesName")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("return $L", "guideNames")
                        .returns(strMap);

                //②返回名称与分组的map
                MethodSpec.Builder groupMethod = MethodSpec.methodBuilder("getGuidesGroup")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("return $L", "guideGroups")
                        .returns(strMap);

                //③返回名称与优先级的map
                MethodSpec.Builder priorityMethod = MethodSpec.methodBuilder("getGuidesPriority")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("return $L", "guidePrioritys")
                        .returns(intMap);

                //生成GuideManager.java
                TypeSpec clz = TypeSpec.classBuilder("GuideManager")
                        .addModifiers(Modifier.PUBLIC)
                        .addSuperinterface(ClassName.get("com.mobile.hero.api", "GuideCollect"))
                        .addField(guideNames)
                        .addField(guideGroups)
                        .addField(guidePrioritys)
                        .addMethod(nameMethod.build())
                        .addMethod(groupMethod.build())
                        .addMethod(priorityMethod.build())
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
