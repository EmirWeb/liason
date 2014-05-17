package mobi.liason.mvvm.providers;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import mobi.liason.mvvm.RobolectricTestRunnerWithInjection;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class PathTest {

    @Test
    public void singlePathSegmentStringPathTest(){
        final Path path = new Path("EMIR");
        final String stringPath = path.toString();
        assertThat(stringPath).isEqualTo("EMIR");
        final List<String> pathSegments = path.getPathSegments();
        assertThat(pathSegments).hasSize(1);
        assertThat(pathSegments).containsExactly("EMIR");
    }

    @Test
    public void multiplePathSegmentStringPathTest(){
        final Path path = new Path("EMIR1/EMIR2");
        final String stringPath = path.toString();
        assertThat(stringPath).isEqualTo("EMIR1/EMIR2");
        final List<String> pathSegments = path.getPathSegments();
        assertThat(pathSegments).hasSize(2);
        assertThat(pathSegments).contains("EMIR1");
        assertThat(pathSegments).contains("EMIR2");
    }

    @Test
    public void multiplePathSegmentPathTest(){
        final Path path = new Path("EMIR1","EMIR2");
        final String stringPath = path.toString();
        assertThat(stringPath).isEqualTo("EMIR1/EMIR2");
        final List<String> pathSegments = path.getPathSegments();
        assertThat(pathSegments).hasSize(2);
        assertThat(pathSegments).contains("EMIR1");
        assertThat(pathSegments).contains("EMIR2");
    }

}
