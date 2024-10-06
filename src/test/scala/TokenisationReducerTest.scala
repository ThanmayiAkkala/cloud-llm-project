import org.scalatest.funsuite.AnyFunSuite
import org.mockito.Mockito.{mock, verify}
import com.thanu.llm.Word2VecReducer
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Reducer
import org.scalatestplus.mockito.MockitoSugar
import scala.collection.JavaConverters._

class TokenisationReducerTest extends AnyFunSuite with MockitoSugar {

  test("Word2VecReducer should emit the embeddings without averaging") {
    val reducer = new Word2VecReducer
    val context = mock[Reducer[Text, Text, Text, Text]#Context]

    // Key and values to test
    val key = new Text("word")
    val embeddings = List(
      new Text("0.1,0.2,0.3"),
      new Text("0.1,0.2,0.3")
    ).asJava

    // Run the reducer
    reducer.reduce(key, embeddings, context)

    // Verify that the context.write method was called with the correct values
    verify(context).write(key, new Text("0.1,0.2,0.3")) // Ensure only one output per key
  }
}

