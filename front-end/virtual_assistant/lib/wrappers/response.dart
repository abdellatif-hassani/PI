class HttpResponse<T> {
  int? statusCode;
  T? data;
  String? error;

  HttpResponse({ this.statusCode,  this.data, this.error});

  bool get isSuccess => error==null;
}