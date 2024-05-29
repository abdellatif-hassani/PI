
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:virtual_assistant/models/data.dart';
import 'package:virtual_assistant/models/user.dart';
import 'package:virtual_assistant/pages/MainPage.dart';
import 'package:virtual_assistant/pages/chat_page.dart';
import 'package:virtual_assistant/services/login_service.dart';
import 'package:virtual_assistant/services/storage_serivce.dart';
import 'package:virtual_assistant/services/user_service.dart';

class LoginController with ChangeNotifier {
  Data<GoogleSignInAccount> _loginData = Data(status: Status.NORMAL);
  Data<GoogleSignInAccount> get loginData => _loginData;

  User? user;
  UserService userService = UserService();
  LoginService loginService;
   LoginController({required this.loginService});

  Future logout() async {
    await StorageService.instance.clear();
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


    void navigateToChat(BuildContext context) {
      Navigator.pushReplacement(
          context,
          MaterialPageRoute(
              builder: (BuildContext context) =>
                  ChatterScreen()));
    }

    navigateToLogin(BuildContext context) {
      Navigator.pushReplacement(
          context,
          MaterialPageRoute(
              builder: (BuildContext context) =>
                  LoginPage()));
    }

  Future<bool> isLoggedIn() async{
    _loginData.data=await loginService.getGoogleSignInAccountSilenlty();
    _loginData.data= loginService.getCurrentUser();

    if(_loginData.data!=null){
      Map<String, String> authHeaders = await _loginData.data!.authHeaders;
      setUser(googleSignInAccountToUser(
          _loginData.data!, authHeaders['Authorization']!.split(" ")[1], ""));
      print(" HIIIIII${authHeaders['Authorization']!.split(" ")[1]}");
      await userService.saveUser(user!);
      return true;
    }
    return false;

  }

  Future login(BuildContext  context)async{
    try {
      _loginData.status = Status.LOADING;
      notifyListeners();
      _loginData.data=await loginService.getGoogleSignInAccountSilenlty();
      //_loginData.data = loginService.getCurrentUser();
     _loginData.data ??= await loginService.signInWithGoogle();
      Map<String, String> authHeaders = await _loginData.data!.authHeaders;
      setUser(googleSignInAccountToUser(
          _loginData.data!, authHeaders['Authorization']!.split(" ")[1], ""));
      userService.saveUser(user!);

      _loginData.status = Status.SUCCESS;
      navigateToChat(context);
    }catch(e){
      _loginData.status = Status.ERORR;
      notifyListeners();
      showErrorSnackBar(context, "Error has accured while login in");
    }

  }
  setUser(User? user) {
    this.user = user;
    notifyListeners();
  }
}
