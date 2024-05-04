import 'package:flutter/widgets.dart';
import 'package:provider/provider.dart';

class ControllerFactory {


  T getControllerWatch<T>(BuildContext context ){
    return context.watch<T>();
  }
  T getControllerRead<T>(BuildContext context ){
    return context.read()<T>();
  }
}