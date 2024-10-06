package com.thanu.llm

import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Reducer
import org.slf4j.{Logger, LoggerFactory}
import scala.collection.mutable.ArrayBuffer

class Word2VecReducer extends Reducer[Text, Text, Text, Text] {

  // Initialize logger
  val logger: Logger = LoggerFactory.getLogger(classOf[Word2VecReducer])

  override def setup(context: Reducer[Text, Text, Text, Text]#Context): Unit = {
    logger.info("Word2VecReducer setup: Starting the reducer phase.")
  }

  override def reduce(key: Text, values: java.lang.Iterable[Text], context: Reducer[Text, Text, Text, Text]#Context): Unit = {
    val embeddingList = ArrayBuffer[Array[Double]]()

    logger.debug(s"Processing token: ${key.toString}")

    // Gather all the embeddings for the same token (key)
    values.forEach { value =>
      val embeddingArray = value.toString.split(",").map(_.toDouble)
      embeddingList += embeddingArray
      logger.debug(s"Embedding added for token ${key.toString}: ${value.toString}")
    }

    if (embeddingList.isEmpty) {
      logger.warn(s"No embeddings found for token: ${key.toString}")
      return
    }

    // Output each embedding for the token
    embeddingList.foreach { embedding =>
      val embeddingString = embedding.mkString(",")
      context.write(new Text(s"Token: $key"), new Text(s"Embedding: $embeddingString")) // Emit the token and its embedding
      logger.info(s"Written token and embedding: $key -> $embeddingString")
    }

    // Since similar tokens are identified in the mapper, you don't need to calculate or average anything here.
    logger.info(s"Processing of token '${key.toString}' completed.")
  }

  override def cleanup(context: Reducer[Text, Text, Text, Text]#Context): Unit = {
    logger.info("Word2VecReducer cleanup: Reducer phase completed.")
  }
}
