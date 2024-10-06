import org.scalatest.funsuite.AnyFunSuite
import org.mockito.Mockito.{mock, verify}
import com.thanu.llm.TokenisationMapper
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper
import org.scalatestplus.mockito.MockitoSugar

class TokenisationMapperTest extends AnyFunSuite with MockitoSugar {

  test("TokenisationMapper should emit tokens from a sentence") {
    val mapper = new TokenisationMapper
    val context = mock[Mapper[LongWritable, Text, Text, Text]#Context]

    // Example sentence to tokenize
    val sentence = new Text("This is a test")
    val key = new LongWritable(1)

    // Run the mapper
    mapper.map(key, sentence, context)

    // Verify that context.write was called for each token
    verify(context).write(new Text("This"), sentence)
    verify(context).write(new Text("is"), sentence)
    verify(context).write(new Text("a"), sentence)
    verify(context).write(new Text("test"), sentence)
  }
}
