package mobi.liason.annotation;

import com.google.common.base.CaseFormat;

/**
 * Created by Emir Hasanbegovic on 10/10/14.
 */
public class VariableNameHelper {

    public static String getClassMemberVariableName(final String upperCaseUnderscore){
        return "m" + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, upperCaseUnderscore);
    }

    public static String getVariableNameFromClassName(final String className){
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, className);
    }

    public static String getConstructorParameterVariableName(final String upperCaseUnderscore){
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, upperCaseUnderscore);
    }

    public static String getGetMethodName(final String upperCaseUnderscore){
        return "get" + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, upperCaseUnderscore);
    }

    public static String getPublicStaticNameFromClassName(final String className){
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, className);
    }
}
