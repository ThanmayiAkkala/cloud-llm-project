import com.thanu.llm.Word2VecMapper
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers.any

class Word2VecMapperTest extends AnyFlatSpec with Matchers {

  "Word2VecMapper" should "generate embeddings for tokens" in {
    // Mock the context: This creates a mock context, which is needed to simulate the behavior of the real Hadoop context
    val mockContext = mock(classOf[Mapper[LongWritable, Text, Text, Text]#Context])

    val mapper = new Word2VecMapper()

    mapper.setup(mockContext)  // Simulates what happens before the map process starts.

    // Call the map function with a simple sentence that will trigger embeddi
    mapper.map(new LongWritable(1), new Text("test sentence example"), mockContext)
    mapper.cleanup(mockContext)
    verify(mockContext, atLeastOnce()).write(any[Text], any[Text])
  }
}
