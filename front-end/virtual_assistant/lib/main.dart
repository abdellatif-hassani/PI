// import 'package:chat_app/pages/chat.dart';

import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:virtual_assistant/controllers/chat_controller.dart';
import 'package:virtual_assistant/pages/MainPage.dart';
import 'package:provider/provider.dart';
import 'package:virtual_assistant/controllers/login_controller.dart';
import 'package:virtual_assistant/pages/chat_page.dart';
import 'package:virtual_assistant/pages/starter_page.dart';
import 'package:virtual_assistant/services/login_service.dart';
void main() async{
  await dotenv.load();
  runApp(ChatterApp());
}

class ChatterApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (context) => LoginController(loginService:  LoginService())),
        ChangeNotifierProvider(create: (context) => AnimationProvider(TickerProviderr())),
        ChangeNotifierProxyProvider<LoginController, ChatController>(
          create: (context) => ChatController(),
          update: (context, userController, _) {
            return ChatController()..setUser(userController.user);
          },
        ),
      ],
      child: MaterialApp(
        debugShowCheckedModeBanner: false,
        title: 'Chatter',
        theme: ThemeData(
          primaryColor: Color(0xFF1B97F3),
          textTheme: TextTheme(
            bodyLarge: TextStyle(
              fontFamily: 'Poppins',
            ),
          ),
        ),
        home: StarterPage(),
      ),
    );
  }
}