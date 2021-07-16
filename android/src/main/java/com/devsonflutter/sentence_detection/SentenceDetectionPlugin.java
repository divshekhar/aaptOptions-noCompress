package com.devsonflutter.sentence_detection;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import io.flutter.FlutterInjector;
import io.flutter.Log;
import io.flutter.embedding.engine.loader.FlutterLoader;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/** SentenceDetectionPlugin */
@Keep
public class SentenceDetectionPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context context;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "sentence_detection");
    channel.setMethodCallHandler(this);
    context = flutterPluginBinding.getApplicationContext();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getSentences")) {
      sentenceDetection(call,result);
    }
  }

  @Keep
  private void sentenceDetection(MethodCall call, Result result) {
    List<String> args = call.arguments();
    String text = args.get(0);
    String modelPath = args.get(1);

    AssetManager assetManager = context.getResources().getAssets();
    FlutterLoader loader = FlutterInjector.instance().flutterLoader();
    String key = loader.getLookupKeyForAsset(modelPath);
    InputStream fd = null;

    try {
      fd = assetManager.open(key);
    } catch (IOException e) {
      e.printStackTrace();
      Log.e("sentence_detector", "Error loading asset model!");
    }

    try {
      assert fd != null;
      SentenceModel model = new SentenceModel(fd);

      SentenceDetectorME detector = new SentenceDetectorME(model);

      String[] sentences = detector.sentDetect(text);
      List<String> sentencesList = Arrays.asList(sentences);

      result.success(sentencesList);
    } catch (IOException e) {
      e.printStackTrace();
      Log.e("sentence_detector", "Error sentence detection!");
    }
  }


  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
