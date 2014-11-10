package mobi.liason.mvvm.database.annotations;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

/**
 * Created by Emir on 14-09-28.
 */
public class AnnotationsProcessor extends AbstractProcessor {

    
    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {
        return false;
    }
}
