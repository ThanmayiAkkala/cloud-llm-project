//package com.thanu.llm
//
//import org.apache.hadoop.io.{IntWritable, Text}
//import org.apache.hadoop.mapreduce.Reducer
//
//import scala.collection.JavaConverters._
//
//class TokenisationReducer extends Reducer[Text, IntWritable, Text, IntWritable] {
//
//  override def reduce(key: Text, values: java.lang.Iterable[IntWritable], context: Reducer[Text, IntWritable, Text, IntWritable]#Context): Unit = {
//    // Sum all counts (1s) associated with the token (key)
//    val sum = values.asScala.foldLeft(0)((total, value) => total + value.get())
//
//    // Write the token and its total count
//    context.write(key, new IntWritable(sum))
//  }
//}
package com.thanu.llm
import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce.Reducer
import scala.collection.JavaConverters._

class TokenisationReducer extends Reducer[Text, Text, Text, IntWritable] {

  override def reduce(key: Text, values: java.lang.Iterable[Text], context: Reducer[Text, Text, Text, IntWritable]#Context): Unit = {
    // Count occurrences of the token
    val count = values.asScala.size

    // Emit (token, count)
    context.write(key, new IntWritable(count))
  }
}
