import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:virtual_assistant/controllers/login_controller.dart';
import 'package:virtual_assistant/pages/MainPage.dart';
import 'package:virtual_assistant/pages/chat_page.dart';
import 'package:virtual_assistant/services/login_service.dart';

class StarterPage extends StatefulWidget {
  @override
  _StarterPageState createState() => _StarterPageState();
}
class _StarterPageState extends State<StarterPage> {
  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: context.read<LoginController>().isLoggedIn(), // Delay for 2 seconds
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          // While waiting for the delay, show the SplashScreen
          return Scaffold(
            body: Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  Hero(
                    tag: 'heroicon',
                    child: Icon(
                      Icons.textsms,
                      size: 1* 100,
                      color: Colors.deepPurple[900],
                    ),
            
                  ),
                  SizedBox(height: 20),
                  CircularProgressIndicator(),
                ],
              ),
            ),
          );

        } else {
          //After the delay, navigate to the MainPage or any other desired page
          if(snapshot.data!=null)
            return ChatterScreen();

        }
        return LoginPage();


      },
    );
  }
}