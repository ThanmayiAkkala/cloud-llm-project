package com.thanu.llm

import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper
import com.knuddels.jtokkit.api.{EncodingRegistry, Encoding, EncodingType}
import com.knuddels.jtokkit.Encodings
import org.slf4j.{Logger, LoggerFactory}

class TokenisationMapper extends Mapper[LongWritable, Text, Text, Text] {

  // Initialize logger
  val logger: Logger = LoggerFactory.getLogger(classOf[TokenisationMapper])

  val registry: EncodingRegistry = Encodings.newDefaultEncodingRegistry
  val enc: Encoding = registry.getEncoding(EncodingType.CL100K_BASE)

  override def setup(context: Mapper[LongWritable, Text, Text, Text]#Context): Unit = {
    logger.info("TokenisationMapper setup: Starting tokenization process.")
  }

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, Text]#Context): Unit = {
    val sentence = value.toString.trim
    logger.debug(s"Processing sentence: $sentence")

    // Tokenize the sentence using JTokkit
    val encodedTokens = enc.encode(sentence)

    // Process each token and emit token as a key, and the original sentence as value
    encodedTokens.toArray.foreach { token =>
      val tokenStr = token.toString // Convert token (int) to string
      logger.debug(s"Token: $tokenStr generated from sentence: $sentence")
      context.write(new Text(tokenStr), new Text(sentence)) // Emit token as key and original sentence as value
    }
  }

  override def cleanup(context: Mapper[LongWritable, Text, Text, Text]#Context): Unit = {
    logger.info("TokenisationMapper cleanup: Completed processing.")
  }
}
