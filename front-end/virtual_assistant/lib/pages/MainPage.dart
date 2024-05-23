import 'package:flutter/animation.dart';
import 'package:flutter/material.dart';
import 'package:animated_text_kit/animated_text_kit.dart';
import 'package:flutter/scheduler.dart';
import 'package:provider/provider.dart';
import 'package:virtual_assistant/controllers/login_controller.dart';
import 'package:virtual_assistant/services/login_service.dart';
import 'package:virtual_assistant/widgets/costum_button.dart';

import '../models/data.dart';
import '../widgets/sign_in_button.dart';
class AnimationProvider with ChangeNotifier {
  late AnimationController mainController;
  late Animation mainAnimation;

  AnimationProvider(TickerProvider vsync) {
    mainController = AnimationController(
      duration: Duration(milliseconds: 500),
      vsync: vsync,
    );
    mainAnimation =
        ColorTween(begin: Colors.deepPurple[900], end: Colors.grey[100])
            .animate(mainController);
    mainController.forward();
  }

  double get controllerValue => mainController.value;
  Color get animationValue => mainAnimation.value;

  @override
  void dispose() {
    mainController.dispose();
    super.dispose();
  }
}
class TickerProviderr extends ChangeNotifier implements TickerProvider {
  List<Ticker> _tickers = [];

  @override
  Ticker createTicker(TickerCallback onTick) {
    final ticker = Ticker(onTick);
    _tickers.add(ticker);
    return ticker;
  }

  void disposeTickers() {
    _tickers.forEach((ticker) => ticker.dispose());
  }

  @override
  void dispose() {
    disposeTickers();
    super.dispose();
  }
}

class LoginPage extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    LoginController loginController = context.read<LoginController>();
    return Scaffold(
            backgroundColor:Colors.grey[100],
            body: SafeArea(
              child: Container(
                child: Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: <Widget>[
                      Hero(
                        tag: 'heroicon',
                        child:Image.asset("assets/logo.png",width: 160,)

                      ),
                      SizedBox(
                        height: MediaQuery.of(context).size.height * 0.02,
                      ),
                      Hero(
                        tag: 'HeroTitle',
                        child: Text(
                          'Ai Assistant',
                          style: TextStyle(
                              color:  Color(0xFF2E58FF),
                              fontFamily: 'Poppins',
                              fontSize: 26,
                              fontWeight: FontWeight.w700),
                        ),
                      ),
                      SizedBox(
                        height: MediaQuery.of(context).size.height * 0.01,
                      ),
                      TyperAnimatedTextKit(
                        isRepeatingAnimation: false,
                        speed:Duration(milliseconds: 60),
                        text:["Your virtual Assistant".toUpperCase()],
                        textStyle: TextStyle(
                            fontFamily: 'Poppins',
                            fontSize: 12,
                            color:  Color(0xFF2E58FF)),
                      ),
                      SizedBox(
                        height: MediaQuery.of(context).size.height * 0.15,
                      ),


                      Hero(
                        tag: 'signupbutton',

                        child:SignInButton(widget:  CustomButton(

                          text: 'Signup',

                          accentColor: Colors.white,
                          mainColor: Color(0xFF2E58FF),
                          onpress: () async {
                            print("abdo");
                            await context.read<LoginController>().login(context);
                          },
                        ),
                      )),
                      SizedBox(
                        height: MediaQuery.of(context).size.height * 0.1,
                      ),

                      // ... rest of your widgets
                    ],
                  ),
                ),
              ),
            ),

          );

  }
}


//
//
//
// class ChatterHome extends StatefulWidget {
//   @override
//   _ChatterHomeState createState() => _ChatterHomeState();
// }
//
// class _ChatterHomeState extends State<ChatterHome>
//     with TickerProviderStateMixin {
//   late AnimationController mainController;
//   late Animation mainAnimation;
//   @override
//   void initState() {
//     super.initState();
//     mainController = AnimationController(
//       duration: Duration(milliseconds: 500),
//       vsync: this,
//     );
//     mainAnimation =
//         ColorTween(begin: Colors.deepPurple[900], end: Colors.grey[100])
//             .animate(mainController);
//     mainController.forward();
//     mainController.addListener(() {
//       setState(() {});
//     });
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       backgroundColor: mainAnimation.value,
//       body: SafeArea(
//         child: Container(
//           child: Center(
//             child: Column(
//               mainAxisAlignment: MainAxisAlignment.center,
//               crossAxisAlignment: CrossAxisAlignment.center,
//               children: <Widget>[
//                 Hero(
//                   tag: 'heroicon',
//                   child: Icon(
//                     Icons.textsms,
//                     size: mainController.value * 100,
//                     color: Colors.deepPurple[900],
//                   ),
//                 ),
//                 SizedBox(
//                   height: MediaQuery.of(context).size.height * 0.02,
//                 ),
//                 Hero(
//                   tag: 'HeroTitle',
//                   child: Text(
//                     'Chatter',
//                     style: TextStyle(
//                         color: Colors.deepPurple[900],
//                         fontFamily: 'Poppins',
//                         fontSize: 26,
//                         fontWeight: FontWeight.w700),
//                   ),
//                 ),
//                 SizedBox(
//                   height: MediaQuery.of(context).size.height * 0.01,
//                 ),
//                 TyperAnimatedTextKit(
//                   isRepeatingAnimation: false,
//                   speed:Duration(milliseconds: 60),
//                   text:["World's most private chatting app".toUpperCase()],
//                   textStyle: TextStyle(
//                       fontFamily: 'Poppins',
//                       fontSize: 12,
//                       color: Colors.deepPurple),
//                 ),
//                 SizedBox(
//                   height: MediaQuery.of(context).size.height * 0.15,
//                 ),
//                 Hero(
//                   tag: 'loginbutton',
//                   child: CustomButton(
//                     text: 'Login',
//                     accentColor: Colors.deepPurple,
//                     onpress: () {
//                       Navigator.pushReplacementNamed(context, '/login');
//                     },
//                   ),
//                 ),
//                 SizedBox(
//                   height: 10,
//                 ),
//                 Hero(
//                   tag: 'signupbutton',
//                   child: CustomButton(
//                     text: 'Signup',
//                     accentColor: Colors.white,
//                     mainColor: Colors.deepPurple,
//                     onpress: () {
//                       Navigator.pushReplacementNamed(context, '/signup');
//                     },
//                   ),
//                 ),
//                 SizedBox(
//                   height: MediaQuery.of(context).size.height * 0.1,
//                 ),
//                 Text('Made with ♥ by ishandeveloper')
//               ],
//             ),
//           ),
//         ),
//       ),
//     );
//   }
// }