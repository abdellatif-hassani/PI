import 'dart:io';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:virtual_assistant/models/data.dart';
import 'package:virtual_assistant/models/user.dart';
import 'package:virtual_assistant/pages/MainPage.dart';
import 'package:virtual_assistant/pages/chat_page.dart';
import 'package:virtual_assistant/services/login_service.dart';
import 'package:virtual_assistant/services/user_service.dart';

class LoginController with ChangeNotifier {
  Data<GoogleSignInAccount> _loginData = Data(status: Status.NORMAL);
  Data<GoogleSignInAccount> get loginData => _loginData;

  User? user;
  UserService userService = UserService();
  LoginService loginService;
  LoginController({required this.loginService});
  Future login(BuildContext context) async {
    _loginData.status = Status.LOADING;
    notifyListeners();
    user = await userService.getUser();
    if (user != null) {
      Navigator.pushReplacementNamed(context, '/chat');
      return;
    } else {
      _loginData.data ??= await loginService.signInWithGoogle();

      if (loginData.data?.authentication != null) {
        GoogleSignInAuthentication googleAuth =
            await _loginData.data!.authentication;
        String? idToken = googleAuth.idToken;
        String? refreshToken = googleAuth.accessToken;
        user = googleSignInAccountToUser(
            _loginData.data!, idToken!, refreshToken!);
        loginData.status = Status.SUCCESS;
        navigateToChat(context);
        if (user != null) userService.saveUser(user!);
      } else {
        loginData.status = Status.ERORR;
        showErrorSnackBar(context, "Error has accured while login in");
      }
    }

    notifyListeners();
  }

  Future logout() async {
    await loginService.googleLogout();
    _loginData.data = null;
    userService.deleteUser();
    user = null;
    notifyListeners();
  }

  User googleSignInAccountToUser(
      GoogleSignInAccount account, String idToken, String refreshToken) {
    return User(
      id: account.id,
      name: account.displayName ?? '',
      email: account.email,
      tokenId: idToken,
      refreshToken: refreshToken,
      photoUrl: account.photoUrl ?? '',
    );
  }

  void showErrorSnackBar(BuildContext context, String message) {
    final snackBar = SnackBar(
      content: Text(message),
      backgroundColor: Colors.red, // Change this color to match your app
    );

    ScaffoldMessenger.of(context).showSnackBar(snackBar);
  }


  // checkUserAlreadyLoggedIn(BuildContext context) async {
  //   user = await userService.getUser();
  //   if (user != null) {
  //     Navigator.push(
  //         context,
  //         MaterialPageRoute(
  //             builder: (BuildContext context) =>
  //                 Chatr()));
  //   }    }
  //}
    void navigateToChat(BuildContext context) {
      Navigator.push(
          context,
          MaterialPageRoute(
              builder: (BuildContext context) =>
                  ChatterScreen()));
    }

    navigateToLogin(BuildContext context) {
      Navigator.push(
          context,
          MaterialPageRoute(
              builder: (BuildContext context) =>
                  LoginPage()));
    }

  Future<User?> isLoggedIn() async{
   await Future.delayed(Duration(seconds: 2));
   user = await userService.getUser();
    return user!;
  }
}
