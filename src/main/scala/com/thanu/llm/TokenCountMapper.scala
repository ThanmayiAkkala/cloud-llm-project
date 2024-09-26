//package com.thanu.llm
//
//import org.apache.hadoop.io.{IntWritable, LongWritable, Text}
//import org.apache.hadoop.mapreduce.Mapper
//
//class TokenCountMapper extends Mapper[LongWritable, Text, Text, IntWritable] {
//  val one = new IntWritable(1)
//  val word = new Text()
//
//  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, IntWritable]#Context): Unit = {
//    // Split each line into words (tokens)
//    val tokens = value.toString.split("\\s+")
//    for (token <- tokens) {
//      word.set(token)
//      context.write(word, one) // Emit (token, 1)
//    }
//  }
//}
package com.thanu.llm

import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper
import com.knuddels.jtokkit.api.EncodingRegistry
import com.knuddels.jtokkit.api.Encoding
import com.knuddels.jtokkit.api.EncodingType
import com.knuddels.jtokkit.Encodings


class TokenCountMapper extends Mapper[LongWritable, Text, Text, Text] {


  val registry: EncodingRegistry = Encodings.newDefaultEncodingRegistry
  val enc: Encoding = registry.getEncoding(EncodingType.CL100K_BASE)

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, Text]#Context): Unit = {
    // Convert the input line to a string
    val sentence = value.toString

    // Tokenize the sentence using JTokkit
    val encodedTokens = enc.encode(sentence)

    // Convert tokens to a comma-separated string for output
    val tokenString = (0 until encodedTokens.size()).map(i => encodedTokens.get(i)).mkString(",")

    // Write the tokenized output to the context
    context.write(new Text("Tokens"), new Text(tokenString))
  }
}

