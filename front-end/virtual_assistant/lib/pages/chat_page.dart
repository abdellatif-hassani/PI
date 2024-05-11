import 'package:chat_bubbles/bubbles/bubble_special_three.dart';
import 'package:chat_bubbles/message_bars/message_bar.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:provider/provider.dart';
import 'package:virtual_assistant/controllers/chat_controller.dart';
import 'package:virtual_assistant/controllers/login_controller.dart';
import 'package:virtual_assistant/models/user.dart';
import 'package:virtual_assistant/services/storage_serivce.dart';
import 'package:virtual_assistant/widgets/messages_list.dart';

import '../widgets/linear_progress_indecator.dart';

String username = 'User';
String email = 'user@example.com';
String? messageText;
User? loggedInUser;

const kSendButtonTextStyle = TextStyle(
  color: Colors.lightBlueAccent,
  fontWeight: FontWeight.bold,
  fontSize: 18.0,
);

const kMessageTextFieldDecoration = InputDecoration(
  contentPadding: EdgeInsets.symmetric(vertical: 10.0, horizontal: 20.0),
  hintText: 'Type your message here...',
  hintStyle: TextStyle(fontFamily: 'Poppins',fontSize: 14),
  border: InputBorder.none,
);

const kMessageContainerDecoration = BoxDecoration(
  // border: Border(
  //   top: BorderSide(color: Colors.lightBlueAccent, width: 2.0),
  // ),

);
class ChatterScreen extends StatefulWidget {
  @override
  _ChatterScreenState createState() => _ChatterScreenState();
}

class _ChatterScreenState extends State<ChatterScreen> {
  final chatMsgTextController = TextEditingController();
  final scrollController = ScrollController(); // Add this line


  @override
  void initState() {
    super.initState();

    // getMessages();
  }

    // void getMessages()async{
    //   final messages=await _firestore.collection('messages').getDocuments();
    //   for(var message in messages.documents){
    //     print(message.data);
    //   }
    // }

    // void messageStream() async {
    //   await for (var snapshot in _firestore.collection('messages').snapshots()) {
    //     snapshot.documents;
    //   }
    // }


    @override
    Widget build(BuildContext context) {
    ChatController chatController =context.read<ChatController>();

      return Scaffold(
        appBar: AppBar(
          iconTheme: IconThemeData(color: Colors.deepPurple),
          elevation: 0,
          bottom: PreferredSize(
            preferredSize: Size(5, 5),
            child: Container(
              child: LinearProgressIndicatorWidget(),
              decoration: BoxDecoration(
                // color: Colors.blue,

                // borderRadius: BorderRadius.circular(20)
              ),
              constraints: BoxConstraints.expand(height: 1),
            ),
          ),
          backgroundColor: Colors.white10,
          // leading: Padding(
          //   padding: const EdgeInsets.all(12.0),
          //   child: CircleAvatar(backgroundImage: NetworkImage('https://cdn.clipart.email/93ce84c4f719bd9a234fb92ab331bec4_frisco-specialty-clinic-vail-health_480-480.png'),),
          // ),
          title: Row(
            children: <Widget>[
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Text(
                    'Chatter',
                    style: TextStyle(
                        fontFamily: 'Poppins',
                        fontSize: 16,
                        color: Colors.deepPurple),
                  ),

                ],
              ),
            ],
          ),
          actions: <Widget>[
            PopupMenuButton<int>(
              itemBuilder: (context) => [
                const PopupMenuItem(
                  value: 1,
                  child: Text("clear chat"),
                ),
                PopupMenuItem(
                  value: 2,
                  child: Text("Second item"),
                ),
              ],
              onSelected: (value) {
                // Handle your menu item click here
                if (value == 1) {
                  // Handle first item click
                  chatController.clearChat();
                }
              },
            ),
          ],
        ),
        drawer: Drawer(
          child: ListView(
            children: <Widget>[
              UserAccountsDrawerHeader(
                decoration: BoxDecoration(
                  color: Colors.deepPurple[900],
                ),
                accountName: Text(chatController.getUser()?.name??" no one "),
                accountEmail: Text(chatController.getUser()?.email??""),

                onDetailsPressed: () {},
              ),
              ListTile(
                leading: Icon(Icons.exit_to_app),
                title: Text("Logout"),
                subtitle: Text("Sign out of this account"),
                onTap: () async {
                  await context.read<LoginController>().logout();
                  Navigator.pushReplacementNamed(context, '/');
                },
              ),
            ],
          ),
        ),
        body: Column(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          crossAxisAlignment: CrossAxisAlignment.stretch,

          children: <Widget>[
           Expanded(child: MessagesList()),
            Container(

              decoration: kMessageContainerDecoration,
              alignment: Alignment.bottomCenter,
              child: MessageBar(
                onSend: (_) => chatController.sendMessage(_),
                actions: [
                  InkWell(
                    child: const Icon(
                      Icons.add,
                      color: Colors.black,
                      size: 24,
                    ),
                    onTap: ()async {
                  var status = await Permission.storage.request();
                  if (status.isGranted) {
                    chatController.showPicker(context);
                  print('Storage permission granted');
                  } else if (status.isDenied) { 
                    context.read<LoginController>().showErrorSnackBar(context, "Storage permission denied");
                    
                  print('Storage permission denied');
                  }
                  },
                     
                      
                    
                  ),
                  Padding(
                    padding: EdgeInsets.only(left: 8, right: 8),
                    child: InkWell(
                      child: const Icon(
                        Icons.camera_alt,
                        color: Colors.green,
                        size: 24,
                      ),
                      onTap: () {},
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      );
    }
  }
