import 'package:shared_preferences/shared_preferences.dart';
import 'package:virtual_assistant/exceptions/authentication_exception.dart';
import 'package:virtual_assistant/models/user.dart';

class UserService {
  Future<void> saveUser(User user) async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    await prefs.setString('idToken', user.tokenId);
    await prefs.setString('name', user.name);
    await prefs.setString('id',user.id);
    await prefs.setString('email', user.email);
    await prefs.setString('photoUrl', user.photoUrl);
    await prefs.setString('refreshToken', user.refreshToken);
  }

  Future<User?> getUser() async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();

    String? id = prefs.getString('id');
    String? userName = prefs.getString('name');
    String? email = prefs.getString('email');
    String? tokenId = prefs.getString('idToken');
    String? photoUrl = prefs.getString('photoUrl');
    String refreshToken = prefs.getString('refreshToken') ?? '';
    if (id != null && userName != null && email != null && tokenId != null && photoUrl != null) {
      return User(id: id, name: userName, email: email, tokenId: tokenId, photoUrl: photoUrl, refreshToken: refreshToken);
    }
   return null;
  }

  void deleteUser() {

    SharedPreferences.getInstance().then((prefs) {
      prefs.remove('id');
      prefs.remove('name');
      prefs.remove('email');
      prefs.remove('idToken');
      prefs.remove('photoUrl');
      prefs.remove('refreshToken');
    });
  }
}