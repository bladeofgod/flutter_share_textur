
import 'package:flutter/material.dart';
import 'native_plugin.dart';

class ListViewPage extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return ListViewPageState();
  }

}

class ListViewPageState extends State<ListViewPage> {
  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    return Material(
      color: Colors.white,
      child: ListView(
        children: List.generate(10, (index) => ShareTextureItem(id: index,width: size.width,height: size.height/3,) ),
      ),
    );
  }
}


class ShareTextureItem extends StatefulWidget{

  final int id;
  final double width,height;

  const ShareTextureItem({Key key, this.id,this.width,this.height}) : super(key: key);


  @override
  State<StatefulWidget> createState() {
    return ShareTextureItemState();
  }

}

class ShareTextureItemState extends State<ShareTextureItem> {

  int textureId;
  int bmW,bmH;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    loadImg();
  }

  void loadImg()async{
    final map = await NativePlugin.fetchTexture(widget.width.floor(),widget.height.floor(),widget.id);
    textureId = map["textureId"];
    bmW = map['bmW'];
    bmH = map['bmH'];
    setState(() {

    });
  }

  @override
  Widget build(BuildContext context) {

    return Container(
      color: Colors.lightBlueAccent,
      width: widget.width,height: widget.height,
      margin: EdgeInsets.only(bottom: 20),
      alignment: Alignment.center,
      child: textureId == null ?
              Text('加载中') :AspectRatio(aspectRatio: bmW/bmH,child:  Texture(textureId: textureId,),),
    );
  }
}

















