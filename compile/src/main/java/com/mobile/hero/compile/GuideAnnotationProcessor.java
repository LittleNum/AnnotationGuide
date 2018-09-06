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
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class GuideAnnotationProcessor extends AbstractProcessor {
    private static final String FIELD_MAP_NAME = "guideNames";
    private static final String FIELD_MAP_GROUP = "guideGroups";
    private static final String FIELD_MAP_PRIORITY = "guidePrioritys";
    private static final String FIELD_MAP_ANCHOR = "guideAnchors";

    private Filer filer;
    private Elements elements;
    private Messager messager;

    private Map<String, String> mGuideNames = new HashMap<>();
    private Map<String, String> mGuideGroup = new HashMap<>();
    private Map<String, Integer> mGuidePriority = new HashMap<>();
    private Map<String, Integer> mGuideAnchor = new HashMap<>();

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
            mGuideNames.clear();
            mGuideGroup.clear();
            mGuidePriority.clear();
            for (Element element : roundEnvironment.getElementsAnnotatedWith(Guide.class)) {
                messager.printMessage(Diagnostic.Kind.NOTE, "guide " + element);
                Guide guide = element.getAnnotation(Guide.class);
                String group = guide.group();
                int priority = guide.priority();
                int anchor = guide.anchor();

                String name = element.getSimpleName().toString();
                mGuideNames.put(name, ((TypeElement) element).getQualifiedName().toString());
                mGuideGroup.put(name, group);
                mGuidePriority.put(name, priority);
                mGuideAnchor.put(name, anchor);
            }

            if (mGuideNames.isEmpty()) {
                return false;
            }
            TypeName strMap = ParameterizedTypeName.get(Map.class, String.class, String.class);
            FieldSpec guideNames = FieldSpec.builder(strMap, FIELD_MAP_NAME, Modifier.PRIVATE, Modifier.STATIC).build();

            FieldSpec guideGroups = FieldSpec.builder(strMap, FIELD_MAP_GROUP, Modifier.PRIVATE, Modifier.STATIC).build();

            TypeName intMap = ParameterizedTypeName.get(Map.class, String.class, Integer.class);
            FieldSpec guidePrioritys = FieldSpec.builder(intMap, FIELD_MAP_PRIORITY, Modifier.PRIVATE, Modifier.STATIC).build();
            FieldSpec guideAnchors = FieldSpec.builder(intMap, FIELD_MAP_ANCHOR, Modifier.PRIVATE, Modifier.STATIC).build();

            CodeBlock.Builder staticBlockBuilder = CodeBlock.builder()
                    .add("guideNames=new $L<>();\n", HashMap.class.getName())
                    .add("guideGroups=new $L<>();\n", HashMap.class.getName())
                    .add("guidePrioritys=new $L<>();\n", HashMap.class.getName())
                    .add("guideAnchors=new $L<>();\n", HashMap.class.getName());
            for (String name : mGuideNames.keySet()) {
                staticBlockBuilder.add("guideNames.put($S,$S);\n", name, mGuideNames.get(name))
                        .add("guideGroups.put($S,$S);\n", name, mGuideGroup.get(name))
                        .add("guidePrioritys.put($S,$L);\n", name, mGuidePriority.get(name))
                        .add("guideAnchors.put($S,$L);\n", name, mGuideAnchor.get(name));
            }

            //①返回名称与完整类名的map
            MethodSpec.Builder nameMethod = MethodSpec.methodBuilder("getGuidesName")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return $L", FIELD_MAP_NAME)
                    .returns(strMap);

            //②返回名称与分组的map
            MethodSpec.Builder groupMethod = MethodSpec.methodBuilder("getGuidesGroup")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return $L", FIELD_MAP_GROUP)
                    .returns(strMap);

            //③返回名称与优先级的map
            MethodSpec.Builder priorityMethod = MethodSpec.methodBuilder("getGuidesPriority")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return $L", FIELD_MAP_PRIORITY)
                    .returns(intMap);

            //③返回名称与anchor的map
            MethodSpec.Builder anchorMethod = MethodSpec.methodBuilder("getGuidesAnchor")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return $L", FIELD_MAP_ANCHOR)
                    .returns(intMap);

            //生成GuideManager.java
            TypeSpec clz = TypeSpec.classBuilder("GuideCollector")
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ClassName.get("com.mobile.hero.api", "GuideCollect"))
                    .addField(guideNames)
                    .addField(guideGroups)
                    .addField(guidePrioritys)
                    .addField(guideAnchors)
                    .addMethod(nameMethod.build())
                    .addMethod(groupMethod.build())
                    .addMethod(priorityMethod.build())
                    .addMethod(anchorMethod.build())
                    .addStaticBlock(staticBlockBuilder.build())
                    .build();

            String pkg = "com.mobile.hero.annotationguide";
            messager.printMessage(Diagnostic.Kind.NOTE, "guide pkg= " + pkg);
            JavaFile.builder(pkg, clz).build().writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        return true;
    }
}
