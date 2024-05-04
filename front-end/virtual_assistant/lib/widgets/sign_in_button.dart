import 'package:flutter/cupertino.dart';
import 'package:provider/provider.dart';

import '../controllers/login_controller.dart';
import '../models/data.dart';

class SignInButton extends StatelessWidget {

  Widget widget;

  SignInButton({

    required this.widget,

  }) ;

  @override
  Widget build(BuildContext context) {
    Status isLoading = context.watch<LoginController>().loginData.status ;
     switch(isLoading){
      case Status.LOADING :
        return CupertinoActivityIndicator();
      default:
        return widget;
    };
  }
}