//package com.thanu.llm
//
//import org.apache.hadoop.io.{IntWritable, Text}
//import org.apache.hadoop.mapreduce.Reducer
//
//class TokenCountReducer extends Reducer[Text, IntWritable, Text, IntWritable] {
//  val result = new IntWritable()
//
//  override def reduce(key: Text, values: java.lang.Iterable[IntWritable], context: Reducer[Text, IntWritable, Text, IntWritable]#Context): Unit = {
//    var sum = 0
//    val iter = values.iterator()
//    while (iter.hasNext) {
//      sum += iter.next().get()
//    }
//    result.set(sum)
//    context.write(key, result) // Emit (token, total count)
//  }
//}
package com.thanu.llm

import org.apache.hadoop.io.{Text}
import org.apache.hadoop.mapreduce.Reducer
import scala.jdk.CollectionConverters._

// A Reducer class that aggregates token embeddings and outputs them
class TokenCountReducer extends Reducer[Text, Text, Text, Text] {
  override def reduce(key: Text, values: java.lang.Iterable[Text], context: Reducer[Text, Text, Text, Text]#Context): Unit = {
    // Collect all embeddings for this token
    val embeddings = values.asScala.map(_.toString.split(",").map(_.toDouble))

    // Optionally, you can average the embeddings if the same token appears multiple times
    val avgEmbedding = embeddings.reduce((a, b) => a.zip(b).map { case (x, y) => (x + y) / 2 })

    // Convert the averaged embedding back to a string and output it
    val embeddingString = avgEmbedding.mkString(",")
    context.write(key, new Text(embeddingString))
  }
}
