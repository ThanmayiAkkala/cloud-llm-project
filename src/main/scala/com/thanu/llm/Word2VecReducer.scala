//package com.thanu.llm
//
//import org.apache.hadoop.io.Text
//import org.apache.hadoop.mapreduce.Reducer
//import scala.collection.mutable.ArrayBuffer
//
//class Word2VecReducer extends Reducer[Text, Text, Text, Text] {
//
//  override def reduce(key: Text, values: java.lang.Iterable[Text], context: Reducer[Text, Text, Text, Text]#Context): Unit = {
//    val embeddingList = ArrayBuffer[Array[Double]]()
//
//    // Gather all the embeddings for the same token (key)
//    values.forEach { value =>
//      val embeddingArray = value.toString.split(",").map(_.toDouble)
//      embeddingList += embeddingArray
//    }
//
//    // Average the embeddings if there are multiple embeddings for the same token
//    val averagedEmbedding = embeddingList.reduce { (emb1, emb2) =>
//      emb1.zip(emb2).map { case (x, y) => (x + y) / 2 }
//    }
//
//    // Convert averaged embedding to a string
//    val averagedEmbeddingString = averagedEmbedding.mkString(",")
//
//    // Output the averaged embedding for the token
//    context.write(key, new Text(averagedEmbeddingString))
//  }
//}
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

    // Average the embeddings if there are multiple embeddings for the same token
    val averagedEmbedding = embeddingList.reduce { (emb1, emb2) =>
      emb1.zip(emb2).map { case (x, y) => (x + y) / 2 }
    }

    logger.debug(s"Averaged embedding for token ${key.toString}: ${averagedEmbedding.mkString(",")}")

    // Convert averaged embedding to a string
    val averagedEmbeddingString = averagedEmbedding.mkString(",")

    // Output the averaged embedding for the token
    context.write(key, new Text(averagedEmbeddingString))
    logger.info(s"Written token and averaged embedding: $key -> $averagedEmbeddingString")
  }

  override def cleanup(context: Reducer[Text, Text, Text, Text]#Context): Unit = {
    logger.info("Word2VecReducer cleanup: Reducer phase completed.")
  }
}
