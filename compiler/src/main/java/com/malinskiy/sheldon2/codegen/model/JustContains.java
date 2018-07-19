package com.malinskiy.sheldon2.codegen.model;

import com.google.common.base.Preconditions;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

public class JustContains implements Generatable {

    @Nonnull private final ExecutableElement method;
    @Nonnull private final String providerFieldName;

    public JustContains(@Nonnull ExecutableElement method,
                        @Nonnull String providerFieldName) {

        this.method = Preconditions.checkNotNull(method);
        this.providerFieldName = Preconditions.checkNotNull(providerFieldName);
    }

    @Override public void generateCode(TypeSpec.Builder builder) {
        builder.addMethod(MethodSpec.methodBuilder(method.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(String.class, "key")
                .returns(Boolean.class)
                .addStatement("return " + providerFieldName + ".contains($N)", "key")
                .build());
    }
}
