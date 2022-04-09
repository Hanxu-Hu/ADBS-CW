package ed.inf.adbs.minibase;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class CQMinimizerTest {
    @Test
    public void aSimpleTest() throws IOException {
        for (int i = 4; i < 5; i++) {
            String inputPath = "data_1/minimization/input/query" + i + ".txt";
            String outputPath = "data_1/minimization/output/query" + i + ".txt";
            //String = "data/evaluation_extra/extra_01/output/query" + i + ".csv";
            File directory = new File("data_1/minimization/output");
            boolean hasSucceeded = directory.mkdir();
            //outputFile.delete();
            String[] args = {inputPath, outputPath};
            CQMinimizer.main(args);
            String expectedOutputPath = "data_1/minimization/expected_output/query" + i + ".txt";
            String output = new String(Files.readAllBytes(Paths.get(outputPath)), StandardCharsets.UTF_8);
//            String[] outputLines = output.split("\n");
            String expectedOutput = new String(Files.readAllBytes(Paths.get(expectedOutputPath)), StandardCharsets.UTF_8);
            expectedOutput += "\n";
            assertEquals(expectedOutput, output);

        }
    }


}

