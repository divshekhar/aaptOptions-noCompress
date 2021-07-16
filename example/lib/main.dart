import 'package:flutter/material.dart';
import 'dart:async';

import 'package:sentence_detection/sentence_detection.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  List<String> list = [];
  final String s =
      "Usually Sentence Detection is done before the text is tokenized and that's the way the pre-trained models on the web site are trained, but it is also possible to perform tokenization first and let the Sentence Detector process the already tokenized text. The OpenNLP Sentence Detector cannot identify sentence boundaries based on the contents of the sentence. A prominent example is the first sentence in an article where the title is mistakenly identified to be the first part of the first sentence. Most components in OpenNLP expect input which is segmented into sentences.";

  Future<List<String>> initPlatformState() async {
    List<String> k = [];
    try {
      String modelPath = "assets/models/opennlp-en-ud-ewt-sentence.bin";
      k = await SentenceDetection.sentenceDetection(s, modelPath);
      k.forEach((element) {
        print(element);
      });
    } catch (e) {
      print(e);
    }
    return k;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: [
            ElevatedButton(
              onPressed: () async {
                list = await initPlatformState();
                setState(() {});
              },
              child: Text("F1"),
            ),
            Text(list.toString()),
          ],
        ),
      ),
    );
  }
}
