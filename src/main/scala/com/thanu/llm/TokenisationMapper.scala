package com.thanu.llm

import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper
import com.knuddels.jtokkit.api.EncodingRegistry
import com.knuddels.jtokkit.api.Encoding
import com.knuddels.jtokkit.Encodings
import com.knuddels.jtokkit.api.EncodingType

class TokenisationMapper extends Mapper[LongWritable, Text, Text, Text] {
  val registry: EncodingRegistry = Encodings.newDefaultEncodingRegistry
  val enc: Encoding = registry.getEncoding(EncodingType.CL100K_BASE)

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, Text]#Context): Unit = {
    val sentence = value.toString.trim

    // Tokenize the sentence using JTokkit
    val encodedTokens = enc.encode(sentence)

    // Process each token and emit token as a key, and the original sentence as value
    encodedTokens.toArray.foreach { token =>
      val tokenStr = token.toString // Convert token (int) to string
      context.write(new Text(tokenStr), new Text(sentence)) // Emit token as key and original sentence as value
    }
  }
}
