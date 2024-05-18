//package com.example.myapplication.chapter10;
//
//public class baiduai {
//    import javax.net.ssl.*;
//import java.io.*;
//import java.net.Socket;
//import java.security.KeyStore;
//
//    public class HttpsWithCertificate {
//
//        public static void main(String[] args) throws Exception {
//            // 加载服务器的信任库（TrustStore），这里使用Java的默认信任库
//            System.setProperty("javax.net.ssl.trustStore", "path/to/truststore.jks");
//            System.setProperty("javax.net.ssl.trustStorePassword", "truststore_password");
//
//            // 创建 SSL 上下文并初始化
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, null, new java.security.SecureRandom());
//
//            // 创建 SSL 套接字工厂
//            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//            // 连接到服务器  HTTPS则使用443端
//            Socket socket = sslSocketFactory.createSocket("example.com", 443); 、、
//            SSLSocket sslSocket = (SSLSocket) socket;
//
//            // 开始 SSL 握手，这会验证服务器的证书 在建立安全连接的过程中，先进行TCP三次握手，再进行SSL握手
//            sslSocket.startHandshake()；
//
//            // 发送 HTTP 请求
//            String request = "GET / HTTP/1.1\r\n" +
//                    "Host: example.com\r\n" +
//                    "Connection: close\r\n\r\n";
//            OutputStream outputStream = sslSocket.getOutputStream();
//            outputStream.write(request.getBytes());
//            outputStream.flush();
//
//            // 读取响应
//            InputStream inputStream = sslSocket.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//
//            // 关闭连接
//            socket.close();
//        }
//    }
//}

//suspend fun <T : Any> Call<T>.await(): T {
//        return suspendCancellableCoroutine { continuation ->
//        continuation.invokeOnCancellation {
//        cancel()
//        }
//        enqueue(object : Callback<T> {
//        override fun onResponse(call: Call<T>, response: Response<T>) {
//        if (response.isSuccessful) {
//        val body = response.body()
//        if (body == null) {
//        val invocation = call.request().tag(Invocation::class.java)!!
//        val method = invocation.method()
//        val e = KotlinNullPointerException("Response from " +
//        method.declaringClass.name +
//        '.' +
//        method.name +
//        " was null but response body type was declared as non-null")
//        continuation.resumeWithException(e)
//        } else {
//        continuation.resume(body)
//        }
//        } else {
//        continuation.resumeWithException(HttpException(response))
//        }
//        }d
//
//        override fun onFailure(call: Call<T>, t: Throwable) {
//        continuation.resumeWithException(t)
//        }
//        })
//        }


//在标准的Socket通信中，服务器不会主动联系客户端。相反，是客户端主动连接到服务器。在连接过程中，客户端确实会把自己的IP地址和端口号告诉服务器，但这个过程是自动完成的，无需客户端显式地发送这些信息。
//
//当客户端创建一个Socket对象并指定服务器的IP地址和端口号时，操作系统会负责底层的网络通信。它会为客户端分配一个可用的本地端口号（如果客户端没有指定），并将客户端的IP地址和端口号与服务器的IP地址和端口号进行匹配，从而建立起连接。这个过程中，客户端的IP地址和端口号会自动成为连接的一部分，而无需客户端显式发送。
//
//一旦连接建立成功，服务器就可以通过该连接与客户端进行通信。服务器可以使用Socket对象的输入/输出流来读取和写入数据，而无需关心客户端的IP地址和端口号的具体细节。这些底层细节是由操作系统和网络协议栈自动处理的。
//
//因此，总结来说，客户端在连接到服务器时，会自动将其IP地址和端口号告诉服务器，但这个过程是隐式的，无需客户端显式发送这些信息。服务器通过已经建立的连接与客户端进行通信，而无需知道或关心客户端的IP地址和端口号的具体值。
