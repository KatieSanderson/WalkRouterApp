import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class WalkRouterTest {

    private static final String INPUT_FILE_STRING = "test\\test-graph-real-data.dat";
    private static final String TEST_FILE_STRING = "test\\test-graph-simple.dat";

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void mainTest_sameNode() throws Exception {
        String[] inputArr = {INPUT_FILE_STRING, "1522658960", "1522658960"};
        WalkRouter.main(inputArr);

        Assert.assertEquals(Integer.toString(0), outContent.toString().split(" ")[7]);
    }

    @Test
    public void mainTest_neighbours() throws Exception {
        String[] inputArr = {INPUT_FILE_STRING, "1522658960", "439984987"};
        WalkRouter.main(inputArr);

        Assert.assertEquals(Integer.toString(1), outContent.toString().split(" ")[7]);
    }

    @Test
    public void mainTest_notNeighbours() throws Exception {
        String[] inputArr = {INPUT_FILE_STRING, "1522658960", "1522658933"};
        WalkRouter.main(inputArr);

        Assert.assertEquals(Integer.toString(210), outContent.toString().split(" ")[7]);
    }

    @Test
    public void mainTest_multiplePaths() throws Exception {
        String[] inputArr = {TEST_FILE_STRING, "1", "4"};
        WalkRouter.main(inputArr);

        Assert.assertEquals(Integer.toString(102), outContent.toString().split(" ")[7]);
    }

    @Test (expected = IllegalArgumentException.class)
    public void mainTest_invalidNode() throws Exception {
        String[] inputArr = {INPUT_FILE_STRING, "", "1522658933"};
        WalkRouter.main(inputArr);
    }

    @Test (expected = FileNotFoundException.class)
    public void mainTest_invalidFile() throws Exception {
        String[] inputArr = {"", "1522658960", "1522658933"};
        WalkRouter.main(inputArr);
    }

}