// Import necessary libraries
import org.scalatest.funsuite.AnyFunSuite
import com.thanu.llm.LLMEncoderDriver

class MainClassTest extends AnyFunSuite {

  test("LLMEncoderDriver should run successfully with valid input") {
    val inputPath = "D:\\IdeaProjects\\LLMCloudProject\\src\\main\\resources\\dataset\\ikigai.txt"  // Your sample input file path
    val outputPath1 = "src/test/TestOutputs/output1"  // Path for the first output folder
    val outputPath2 = "src/test/TestOutputs/output2"  // Path for the second output folder

    // Call the main method of LLMEncoderDriver with sample input arguments
    LLMEncoderDriver.main(Array(inputPath, outputPath1, outputPath2))

    // Add assertions to check if the output files are generated and contain valid data
    val outputFile1 = new java.io.File(outputPath1)
    val outputFile2 = new java.io.File(outputPath2)

    assert(outputFile1.exists(), "Output directory 1 should exist")
    assert(outputFile2.exists(), "Output directory 2 should exist")

  }
}
