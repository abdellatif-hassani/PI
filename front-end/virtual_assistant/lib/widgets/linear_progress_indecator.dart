import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:virtual_assistant/controllers/chat_controller.dart';
import 'package:virtual_assistant/models/data.dart';

class LinearProgressIndicatorWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    Status status = context.watch<ChatController>().status;
     switch (status) {
      case Status.LOADING:
        return LinearProgressIndicator(
          valueColor: AlwaysStoppedAnimation<Color>(Colors.white),
          backgroundColor: Colors.blue[100],
        );
      default:
        return Container();
    };
  }
}