//package com.thanu.llm
//
//import org.apache.hadoop.conf.Configuration
//import org.apache.hadoop.fs.Path
//import org.apache.hadoop.io.Text
//import org.apache.hadoop.mapreduce.Job
//import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
//import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
//import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
//import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat
//
//object LLMEncoderDriver {
//  def main(args: Array[String]): Unit = {
//    if (args.length != 2) {
//      System.err.println("Usage: LLMEncoderDriver <input path> <output path>")
//      System.exit(-1)
//    }
//
//    // Set up Hadoop configuration and create a new job
//    val conf = new Configuration()
//    val job = Job.getInstance(conf, "LLM Tokenizer Job")
//    job.setJarByClass(LLMEncoderDriver.getClass)
//
//    // Set input and output paths
//    FileInputFormat.addInputPath(job, new Path(args(0)))
//    FileOutputFormat.setOutputPath(job, new Path(args(1)))
//
//    // Set the Mapper and Reducer classes
//    job.setMapperClass(classOf[TokenCountMapper])
//    job.setReducerClass(classOf[TokenCountReducer])
//
//    // Set the output key and value types
//    job.setOutputKeyClass(classOf[Text])
//    job.setOutputValueClass(classOf[Text])
//
//    // Specify input and output format
//    job.setInputFormatClass(classOf[TextInputFormat])
//    job.setOutputFormatClass(classOf[TextOutputFormat[Text, Text]])
//
//    // Submit the job and wait for it to complete
//    System.exit(if (job.waitForCompletion(true)) 0 else 1)
//  }
//}
package com.thanu.llm

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat

object LLMEncoderDriver {
  def main(args: Array[String]): Unit = {
    if (args.length != 3) {
      System.err.println("Usage: Word2VecDriver <input path> <temp output path> <final output path>")
      System.exit(-1)
    }

    val conf = new Configuration()

    // First Job: Tokenization
    val tokenizationJob = Job.getInstance(conf, "Tokenization Job")
    tokenizationJob.setJarByClass(LLMEncoderDriver.getClass)

    tokenizationJob.setMapperClass(classOf[TokenisationMapper])
    tokenizationJob.setReducerClass(classOf[TokenisationReducer])  // Token count reducer

    tokenizationJob.setOutputKeyClass(classOf[Text])
    tokenizationJob.setOutputValueClass(classOf[Text])

    tokenizationJob.setInputFormatClass(classOf[TextInputFormat])
    tokenizationJob.setOutputFormatClass(classOf[TextOutputFormat[Text, Text]])

    FileInputFormat.addInputPath(tokenizationJob, new Path(args(0)))  // Input path for tokenization
    FileOutputFormat.setOutputPath(tokenizationJob, new Path(args(1))) // Temporary output path

    if (!tokenizationJob.waitForCompletion(true)) {
      System.exit(1)
    }

    // Second Job: Word2Vec Embedding Generation
    val embeddingJob = Job.getInstance(conf, "Word2Vec Embedding Job")
    embeddingJob.setJarByClass(LLMEncoderDriver.getClass)

    embeddingJob.setMapperClass(classOf[Word2VecMapper])
    embeddingJob.setReducerClass(classOf[Word2VecReducer]) // Embedding aggregation reducer

    embeddingJob.setOutputKeyClass(classOf[Text])
    embeddingJob.setOutputValueClass(classOf[Text])

    embeddingJob.setInputFormatClass(classOf[TextInputFormat])
    embeddingJob.setOutputFormatClass(classOf[TextOutputFormat[Text, Text]])

    // Input for embedding job comes from the output of tokenization job
    FileInputFormat.addInputPath(embeddingJob, new Path(args(1))) // Temp output as input for embeddings
    FileOutputFormat.setOutputPath(embeddingJob, new Path(args(2))) // Final output path

    System.exit(if (embeddingJob.waitForCompletion(true)) 0 else 1)
  }
}
//package com.thanu.llm
//
//import org.apache.hadoop.conf.Configuration
//import org.apache.hadoop.fs.Path
//import org.apache.hadoop.io.{LongWritable, Text}
//import org.apache.hadoop.mapreduce.Job
//import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
//import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
//
//object LLMEncoderDriver {
//  def main(args: Array[String]): Unit = {
//    // Check that input and output paths are provided as arguments
//    if (args.length != 2) {
//      println("Usage: LLMEncoderDriver <input path> <output path>")
//      System.exit(-1)
//    }
//
//    // Create a new Hadoop job configuration
//    val conf = new Configuration()
//    val job = Job.getInstance(conf, "Token Embedding with Word2Vec")
//
//    // Set the jar file by class (the entry point of the Hadoop job)
//    job.setJarByClass(classOf[TokenEmbeddingMapper])
//
//    // Set the Mapper and Reducer classes
//    job.setMapperClass(classOf[TokenEmbeddingMapper])
//    job.setReducerClass(classOf[TokenEmbeddingReducer])
//
//    // Set the output key and value classes (Text for tokens, Text for embeddings/counts)
//    job.setOutputKeyClass(classOf[Text])
//    job.setOutputValueClass(classOf[Text])
//
//    // Specify input and output paths
//    FileInputFormat.addInputPath(job, new Path(args(0))) // Input directory or file path
//    FileOutputFormat.setOutputPath(job, new Path(args(1))) // Output directory path
//
//    // Submit the job and wait for completion
//    System.exit(if (job.waitForCompletion(true)) 0 else 1)
//  }
//}
