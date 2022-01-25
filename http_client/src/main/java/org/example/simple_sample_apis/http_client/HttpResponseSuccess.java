package org.example.simple_sample_apis.http_client;

public interface HttpResponseSuccess extends HttpResponse {
  static Boolean isOk(Integer statusCode) { return 200 <= statusCode && statusCode < 300; }
  default Boolean isOk() { return isOk(getStatusCode()); }
  Integer getStatusCode();
  String getBody();
}
