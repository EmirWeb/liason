package mobi.liason.mvvm;

import org.junit.runners.model.InitializationError;
import org.mockito.MockitoAnnotations;
import org.robolectric.DefaultTestLifecycle;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestLifecycle;


public class RobolectricTestRunnerWithInjection extends RobolectricTestRunner {

    public RobolectricTestRunnerWithInjection(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    protected Class<? extends TestLifecycle> getTestLifecycleClass() {
        return TestLifecycleWithInjection.class;
    }

    public static class TestLifecycleWithInjection extends DefaultTestLifecycle {
        @Override
        public void prepareTest(Object test) {
            MockitoAnnotations.initMocks(test);
        }
    }
}