import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class WalkRouterTest {

    private static final String TEST_FILE_LARGE_STRING = "test\\test-graph-real-data.dat";
    private static final String TEST_FILE_SMALL_STRING = "test\\test-graph-simple.dat";

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpSystemOut() {
        System.setOut(new PrintStream(outContent));
    }

    /**
     * {@link WalkRouterTest #setUpNoUserInput} is called when a test does not test user input
     *
     * System.in is wrapped to input the exit string to exit program
     */

    public void setUpNoUserInput() {
        String input = "*";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }

    @Test
    public void mainTest_sameNode() throws Exception {
        setUpNoUserInput();
        String[] inputArr = {TEST_FILE_LARGE_STRING, "1522658960", "1522658960"};
        WalkRouter.main(inputArr);

        Assert.assertEquals(Integer.toString(0), outContent.toString().split(" ")[7]);
    }

    @Test
    public void mainTest_neighbours() throws Exception {
        setUpNoUserInput();
        String[] inputArr = {TEST_FILE_LARGE_STRING, "1522658960", "439984987"};
        WalkRouter.main(inputArr);

        Assert.assertEquals(Integer.toString(1), outContent.toString().split(" ")[7]);
    }

    @Test
    public void mainTest_notNeighbours() throws Exception {
        setUpNoUserInput();
        ByteArrayInputStream in = new ByteArrayInputStream("My string".getBytes());
        String[] inputArr = {TEST_FILE_LARGE_STRING, "1522658960", "1522658933"};
        WalkRouter.main(inputArr);

        Assert.assertEquals(Integer.toString(210), outContent.toString().split(" ")[7]);
    }

    @Test
    public void mainTest_multiplePaths() throws Exception {
        setUpNoUserInput();
        String[] inputArr = {TEST_FILE_SMALL_STRING, "1", "4"};
        WalkRouter.main(inputArr);

        Assert.assertEquals(Integer.toString(102), outContent.toString().split(" ")[7]);
    }

    @Test
    public void mainTest_stopoff() throws Exception {
        setUpNoUserInput();
        String[] inputArr = {TEST_FILE_SMALL_STRING, "1", "2", "3"};
        WalkRouter.main(inputArr);

        Assert.assertEquals(Integer.toString(4), outContent.toString().split(" ")[7]);
    }

    @Test
    public void mainTest_subsequentShortestDistanceCalls() throws Exception {
        String input = "1, 2" + "\n*";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        String[] inputArr = {TEST_FILE_SMALL_STRING, "1", "3"};
        WalkRouter.main(inputArr);

        String[] outContentByLine = outContent.toString().split("\r");
        Assert.assertEquals(Integer.toString(2), outContentByLine[0].split(" ")[7]);
        Assert.assertEquals(Integer.toString(1), outContentByLine[2].split(" ")[7]);
    }

    @Test (expected = IllegalArgumentException.class)
    public void mainTest_invalidNode() throws Exception {
        String[] inputArr = {TEST_FILE_LARGE_STRING, "", "1522658933"};
        WalkRouter.main(inputArr);
    }

    @Test (expected = FileNotFoundException.class)
    public void mainTest_invalidFile() throws Exception {
        String[] inputArr = {"", "1522658960", "1522658933"};
        WalkRouter.main(inputArr);
    }
}