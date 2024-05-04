

class User {
String id;
String name;
String email;
String tokenId;
String photoUrl;
String refreshToken;
  User({
   required this.id,
    required this.name,
    required this.email,
    required this.tokenId,
    required this.photoUrl,
    required this.refreshToken
  });

  void setTokenId(String s) {this.tokenId=s;}

  void setRefereshToken(String s) {this.refreshToken=s;}

}