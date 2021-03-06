package com.malinskiy.sheldon2.codegen.model;

import com.google.common.base.Preconditions;

import com.malinskiy.sheldon2.codegen.PrefType;
import com.malinskiy.sheldon2.codegen.ProcessingException;
import com.malinskiy.sheldon2.codegen.Utils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

public class ObservableGetter implements Generatable {

    @Nonnull private final String namespace;
    @Nonnull private final ExecutableElement method;
    @Nonnull private final String providerFieldName;
    @Nonnull private final DefaultValue defaultValue;
    @Nonnull private final ClassName repositoryClassName;

    public ObservableGetter(@Nonnull String namespace,
                            @Nonnull ExecutableElement method,
                            @Nonnull String providerFieldName,
                            @Nonnull DefaultValue defaultValue,
                            @Nonnull ClassName repositoryClassName) {

        this.namespace = Preconditions.checkNotNull(namespace);
        this.method = Preconditions.checkNotNull(method);
        this.providerFieldName = Preconditions.checkNotNull(providerFieldName);
        this.defaultValue = Preconditions.checkNotNull(defaultValue);
        this.repositoryClassName = Preconditions.checkNotNull(repositoryClassName);
    }

    @Override public void generateCode(TypeSpec.Builder builder) throws ProcessingException {
        TypeMirror returnType = method.getReturnType();

        String methodName = method.getSimpleName().toString();
        String prefName = Utils.getName(method);

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                                                     .addModifiers(Modifier.PUBLIC)
                                                     .addAnnotation(Override.class)
                                                     .returns(TypeName.get(returnType));

        String defaultFieldName = defaultValue.getElement().getSimpleName().toString();

        PrefType prefType = Utils.getType(method);
        switch (prefType) {
            case BOOLEAN:
                methodBuilder.addStatement(simpleStatement("Boolean", prefName, defaultFieldName));
                break;
            case FLOAT:
                methodBuilder.addStatement(simpleStatement("Float", prefName, defaultFieldName));
                break;
            case INT:
                methodBuilder.addStatement(simpleStatement("Integer", prefName, defaultFieldName));
                break;
            case LONG:
                methodBuilder.addStatement(simpleStatement("Long", prefName, defaultFieldName));
                break;
            case STRING:
                methodBuilder.addStatement(simpleStatement("String", prefName, defaultFieldName));
                break;
            case OBJECT:
                methodBuilder.addStatement("return $T.getInstance().get($T.class).observe(\"" + prefName + "\", " +
                                           defaultFieldName + ", " + providerFieldName + ")", repositoryClassName,
                        defaultValue.getElement().asType());
                break;
            default:
                throw new ProcessingException(method, "Unsupported preference type %s", prefType.name());
        }

        builder.addMethod(methodBuilder.build());
    }

    private String simpleStatement(String type, String prefName, String defaultFieldName) {
        return "return " + providerFieldName + ".observe" + type + "(\"" + prefName + "\", " + defaultFieldName + ")";
    }
}
