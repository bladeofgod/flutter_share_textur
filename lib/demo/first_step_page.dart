

import 'package:flutter/material.dart';
import 'package:flutter_share_texture/demo/native_plugin.dart';
///通过openGL 实现纹理共享
///
class FirstStepPage extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return FirstStepPageState();
  }

}

class FirstStepPageState extends State<FirstStepPage> {

  final NativePlugin nativePlugin = NativePlugin();

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    initPlugin();
  }
  void initPlugin()async{
    await nativePlugin.initialize(200, 200);
    setState(() {

    });
  }

  @override
  void dispose() {
    nativePlugin?.dispose();
    // TODO: implement dispose
    super.dispose();
  }


  @override
  Widget build(BuildContext context) {
    return Material(
      color: Colors.white,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          nativePlugin.isInitialized ?
          Container(width: 300,height: 300,child: Texture(textureId: nativePlugin.textureId),)
              :Container(width: 400,height: 400,color: Colors.red,)
        ],
      ),
    );
  }
}
















