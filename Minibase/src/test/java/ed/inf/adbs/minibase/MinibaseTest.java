package ed.inf.adbs.minibase;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for Minibase.
 */

public class MinibaseTest {

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void veryDirtyTest() throws IOException {

        String dbDir = "data/evaluation/db";
        for (int i = 8; i <10 ; i++) {

            System.out.println(i);
            String queryPath = "data/evaluation/input/query" + i + ".txt";
            String outputPath = "data/evaluation/output/query" + i + ".csv";
            File outputFile = new File(outputPath);
            outputFile.delete();
            String[] args = {dbDir, queryPath, outputPath};
            Minibase.main(args);
            String expectedOutputPath = "data/evaluation/expected_output/query" + i + ".csv";
            String output = new String(Files.readAllBytes(Paths.get(outputPath)), StandardCharsets.UTF_8);
//            String[] outputLines = output.split("\n");
            String expectedOutput = new String(Files.readAllBytes(Paths.get(expectedOutputPath)), StandardCharsets.UTF_8);
            expectedOutput += "\n";
            assertEquals(expectedOutput, output);
        }

    }
}

