//package com.thanu.llm
//import org.apache.hadoop.io.{IntWritable, Text}
//import org.apache.hadoop.mapreduce.Reducer
//import scala.collection.JavaConverters._
//
//class TokenisationReducer extends Reducer[Text, Text, Text, IntWritable] {
//
//  override def reduce(key: Text, values: java.lang.Iterable[Text], context: Reducer[Text, Text, Text, IntWritable]#Context): Unit = {
//    // Count occurrences of the token
//    val count = values.asScala.size
//
//    // Emit (token, count)
//    context.write(key, new IntWritable(count))
//  }
//}
package com.thanu.llm

import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce.Reducer
import org.slf4j.{Logger, LoggerFactory}
import scala.collection.JavaConverters._

class TokenisationReducer extends Reducer[Text, Text, Text, IntWritable] {

  // Initialize logger
  val logger: Logger = LoggerFactory.getLogger(classOf[TokenisationReducer])

  override def setup(context: Reducer[Text, Text, Text, IntWritable]#Context): Unit = {
    logger.info("TokenisationReducer setup: Starting reduction process.")
  }

  override def reduce(key: Text, values: java.lang.Iterable[Text], context: Reducer[Text, Text, Text, IntWritable]#Context): Unit = {
    // Count occurrences of the token
    val count = values.asScala.size
    logger.debug(s"Token: ${key.toString}, Occurrences: $count")

    // Emit (token, count)
    context.write(key, new IntWritable(count))
  }

  override def cleanup(context: Reducer[Text, Text, Text, IntWritable]#Context): Unit = {
    logger.info("TokenisationReducer cleanup: Completed reducing tokens.")
  }
}
