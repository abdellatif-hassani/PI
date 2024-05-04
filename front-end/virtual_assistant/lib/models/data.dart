//status of data
enum Status{
  NORMAL,
  LOADING,
  SUCCESS,
  ERORR
}
//class that wrapp the data for the controller to track it status
class Data<T>{
  Data({ this.data , this.status=Status.NORMAL});
  Status status;
  T? data;
  String errorMessage="";
}