package mobi.liason.loaders;

import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

@Config(manifest = "/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunnerWithInjection.class)
public class PathTest {

    @Test
    public void singlePathSegmentStringPathTest() {
        final Path path = new Path("EMIR");
        final String stringPath = path.getPath();
        assertThat(stringPath).isEqualTo("EMIR");
        final List<String> pathSegments = path.getPathSegments();
        assertThat(pathSegments).hasSize(1);
        assertThat(pathSegments).containsExactly("EMIR");
    }

    @Test
    public void multiplePathSegmentStringPathTest() {
        final Path path = new Path("EMIR1/EMIR2");
        final String stringPath = path.getPath();
        assertThat(stringPath).isEqualTo("EMIR1/EMIR2");
        final List<String> pathSegments = path.getPathSegments();
        assertThat(pathSegments).hasSize(2);
        assertThat(pathSegments).contains("EMIR1");
        assertThat(pathSegments).contains("EMIR2");
    }

    @Test
    public void multiplePathSegmentPathTest() {
        final Path path = new Path("EMIR1", "EMIR2");
        final String stringPath = path.getPath();
        assertThat(stringPath).isEqualTo("EMIR1/EMIR2");
        final List<String> pathSegments = path.getPathSegments();
        assertThat(pathSegments).hasSize(2);
        assertThat(pathSegments).contains("EMIR1");
        assertThat(pathSegments).contains("EMIR2");
    }

    @Test
    public void multiplePathSegmentPath_return() {
        final Path path = new Path("EMIR1", "EMIR2");
        final String stringPath = path.getPath();
        assertThat(stringPath).isEqualTo("EMIR1/EMIR2");
        final List<String> pathSegments = path.getPathSegments();
        assertThat(pathSegments).hasSize(2);
        assertThat(pathSegments).contains("EMIR1");
        assertThat(pathSegments).contains("EMIR2");
    }

    @Test
    public void whenUsingListGetPathWithNumbers_returnsPathWithNumbers() {
        final Path path = new Path("EMIR1", "#", "EMIR2", "#");
        final String pathWithValues = path.getPath(4, 4.0);
        assertThat(pathWithValues).isEqualTo("EMIR1/4/EMIR2/4.0");
    }

    @Test
    public void whenUsingStringGetPathWithNumbers_returnsPathWithNumbers() {
        final Path path = new Path("EMIR1/#/EMIR2/#");
        final String pathWithValues = path.getPath(4, 4.0);
        assertThat(pathWithValues).isEqualTo("EMIR1/4/EMIR2/4.0");
    }

    @Test
    public void whenUsingStringGetPathWithStrings_returnsPathWithStrings() {
        final Path path = new Path("EMIR1/*/EMIR2/*");
        final String pathWithValues = path.getPath("A", "B");
        assertThat(pathWithValues).isEqualTo("EMIR1/A/EMIR2/B");
    }

    @Test
    public void getPathWithStringWhenShouldBeNumeric_throwsException() {
        final Path path = new Path("EMIR1/#/EMIR2");
        try {
            path.getPath("EMIR");
            fail("Parsed 'EMIR' into Numeric when exception should have been thrown.");
        } catch (final IllegalArgumentException illegalArgumentException) {
            assertThat(illegalArgumentException).hasMessage("Expecting numeric value, cannot insert EMIR into '#'.");
        }
    }

    @Test
    public void passingMoreValuesThanBindingsThrowsException() {
        final Path path = new Path("EMIR1/EMIR2/*");
        try {
            path.getPath("EMIR", "EMIR");
            fail("Allowed empty value for star.");
        } catch (final IllegalArgumentException illegalArgumentException) {
            assertThat(illegalArgumentException).hasMessage("Incoming values does not match number of bindings.");
        }
    }

    @Test
    public void mathcingUris(){
        final Path path = new Path("EMIR1/EMIR2/*/#");
        final Uri uri = Uri.parse("http://www.emirweb.com/EMIR1/EMIR2/4/4");
        final Uri uri1 = Uri.parse("http://www.emirweb.com/EMIR1/EMIR2/4/d");
        final Uri uri2 = Uri.parse("http://www.emirweb.com/EMIR1/EMIR2/4");

        assertThat(path.matches(uri)).isTrue();
        assertThat(path.matches(uri1)).isFalse();
        assertThat(path.matches(uri2)).isFalse();

    }


}

