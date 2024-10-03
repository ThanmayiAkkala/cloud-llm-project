package com.thanu.llm

import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.Mapper
import org.deeplearning4j.models.word2vec.Word2Vec
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer
import scala.collection.mutable.ArrayBuffer
import scala.jdk.CollectionConverters._  // Import to use asJava

class Word2VecMapper extends Mapper[LongWritable, Text, Text, Text] {

  val sentencesBuffer: ArrayBuffer[String] = ArrayBuffer[String]()

  override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, Text]#Context): Unit = {
    // Collect tokens for training
    val sentence = value.toString
    sentencesBuffer += sentence
  }

  override def cleanup(context: Mapper[LongWritable, Text, Text, Text]#Context): Unit = {
    // Once all tokens are gathered, create the Word2Vec model
    val tokenizerFactory = new DefaultTokenizerFactory()
    val sentenceIterator = new CollectionSentenceIterator(sentencesBuffer.asJava) // Convert to Java collection

    // Build the Word2Vec model
    val word2Vec = new Word2Vec.Builder()
      .minWordFrequency(1)
      .iterations(10)
      .layerSize(10)
      .seed(42)
      .windowSize(5)
      .iterate(sentenceIterator)
      .tokenizerFactory(tokenizerFactory)
      .build()

    // Train the model
    word2Vec.fit()

    // Save the model for later use
    WordVectorSerializer.writeWord2VecModel(word2Vec, new java.io.File("word2vec_model.bin"))

    // Output the embeddings for each token
    sentencesBuffer.foreach { sentence =>
      sentence.split("\\s+").foreach { token =>
        if (word2Vec.hasWord(token)) {
          val embedding = word2Vec.getWordVector(token)
          val embeddingString = embedding.mkString(",")
          context.write(new Text(token), new Text(embeddingString))
        }
      }
    }
  }
}
