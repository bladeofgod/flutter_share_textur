



import 'package:flutter/services.dart';

class NativePlugin{
  final MethodChannel channel = MethodChannel('egl_plugin_alpha');

  int textureId;

  bool get isInitialized => textureId != null;

  Future<int> initialize(int width,int height)async{
    textureId = await channel.invokeMethod('create',{
      'width':width,'height':height
    });
    return textureId;
  }

  Future dispose() => channel.invokeMethod('dispose',{'textureId':textureId});

}


























