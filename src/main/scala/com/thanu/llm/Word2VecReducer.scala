package com.thanu.llm

import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Reducer
import scala.collection.mutable.ArrayBuffer

class Word2VecReducer extends Reducer[Text, Text, Text, Text] {

  override def reduce(key: Text, values: java.lang.Iterable[Text], context: Reducer[Text, Text, Text, Text]#Context): Unit = {
    val embeddingList = ArrayBuffer[Array[Double]]()

    // Gather all the embeddings for the same token (key)
    values.forEach { value =>
      val embeddingArray = value.toString.split(",").map(_.toDouble)
      embeddingList += embeddingArray
    }

    // Average the embeddings if there are multiple embeddings for the same token
    val averagedEmbedding = embeddingList.reduce { (emb1, emb2) =>
      emb1.zip(emb2).map { case (x, y) => (x + y) / 2 }
    }

    // Convert averaged embedding to a string
    val averagedEmbeddingString = averagedEmbedding.mkString(",")

    // Output the averaged embedding for the token
    context.write(key, new Text(averagedEmbeddingString))
  }
}
