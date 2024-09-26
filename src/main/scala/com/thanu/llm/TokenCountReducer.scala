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
import scala.collection.JavaConverters._

class TokenCountReducer extends Reducer[Text, Text, Text, Text] {

  override def reduce(key: Text, values: java.lang.Iterable[Text], context: Reducer[Text, Text, Text, Text]#Context): Unit = {
    // Collect all tokenized data from the Mapper
    val tokenLists = values.asScala.map(_.toString)

    // Join all tokenized results into a single string
    val aggregatedTokens = tokenLists.mkString(",")

    // Output the combined tokens for each key
    context.write(key, new Text(aggregatedTokens))
  }
}

