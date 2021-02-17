



import 'package:flutter/services.dart';

class NativePlugin{
  static const MethodChannel channel = const MethodChannel('egl_plugin_alpha');

  int textureId;

  bool get isInitialized => textureId != null;

  Future<int> initialize(int width,int height)async{
    textureId = await channel.invokeMethod('create',{
      'width':width,'height':height
    });
    return textureId;
  }

  Future dispose() => channel.invokeMethod('dispose',{'textureId':textureId});


  ///list view
  /// * fetch texture for list view's item;
  static Future<Map<dynamic,dynamic>> fetchTexture(int width,int height,int itemId)async{
    return await channel.invokeMethod('fetch',{
      'width':width,'height':height,'id':itemId
    });
  }

}


























