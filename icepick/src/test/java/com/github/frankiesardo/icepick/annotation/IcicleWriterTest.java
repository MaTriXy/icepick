package com.github.frankiesardo.icepick.annotation;

import javax.lang.model.element.TypeElement;
import java.io.StringWriter;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class IcicleWriterTest {

    static final String SUFFIX = "$$Icicle";
    static final String PACKAGE = "com.frankiesardo";
    static final String SIMPLE_NAME = "TestActivity";
    static final String QUALIFIED_NAME = PACKAGE + "." + SIMPLE_NAME;
    static final IcicleField ICICLE_FIELD = new IcicleField("username", "somekey", "java.lang.String", "String");
    StringWriter stringWriter = new StringWriter();

    IcicleWriter icicleWriter = new IcicleWriter(stringWriter, SUFFIX);
    TypeElement classType = mock(TypeElement.class, RETURNS_DEEP_STUBS);

    Set<IcicleField> fields = new LinkedHashSet<IcicleField>();

    @Before
    public void setUp() throws Exception {
        when(classType.getQualifiedName().toString()).thenReturn(QUALIFIED_NAME);
        when(classType.getSimpleName().toString()).thenReturn(SIMPLE_NAME);

        fields.add(ICICLE_FIELD);
    }

    @Test
    public void shouldWriteBundlePutAndGetIntoATemplate() throws Exception {
        icicleWriter.writeClass(classType, fields);

        assertEquals(SIMPLE_CLASS, stringWriter.toString());
    }

    static final String SIMPLE_CLASS = "package " + PACKAGE + ";\n" +
            "\n" +
            "final class " + SIMPLE_NAME + SUFFIX + " {\n" +
            "  private static final String BASE_KEY = \"" + PACKAGE + "." + SIMPLE_NAME + SUFFIX + ".\";\n\n" +
            "  private " + SIMPLE_NAME + SUFFIX + "() {\n" +
            "  }\n" +
            "  public static void saveInstanceState(" + SIMPLE_NAME + " target, android.os.Bundle outState) {\n" +
            "    outState.put" + ICICLE_FIELD.getCommand() + "(BASE_KEY + \"" + ICICLE_FIELD.getName() + "\", target." + ICICLE_FIELD.getName() + ");\n" +
            "  }\n" +
            "  public static void restoreInstanceState(" + SIMPLE_NAME + " target, android.os.Bundle savedInstanceState) {\n" +
            "    if (savedInstanceState == null) {\n" +
            "      return;\n" +
            "    }\n" +
            "    target." + ICICLE_FIELD.getName() + " = savedInstanceState.get" + ICICLE_FIELD.getCommand() + "(BASE_KEY + \"" + ICICLE_FIELD.getName() + "\");\n" +
            "  }\n" +
            "}\n";
}
