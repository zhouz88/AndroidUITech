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
//            // 开始 SSL 握手，一旦TCP连接建立成功，你就可以调用startHandshake()方法来启动SSL握手过程。在这个过程中，客户端和服务器将交换证书、密钥等信息，以协商一个安全的通信会话。
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
//Java中，使用Socket进行通信时，你需要手动解析HTTP响应以获取状态码、响应头和响应体。以下是一个简单的示例，展示如何使用Socket连接到HTTP服务器，并解码响应以获取这些组件：

    //   java
import java.io.*;
import java.net.Socket;

//public class HttpSocketClient {
//    public static void main(String[] args) {
//        String host = "example.com";
//        int port = 80;
//        String path = "/path/to/resource";
//
//        try (Socket socket = new Socket(host, port);
//             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
//
//            // 构建HTTP请求
//            String request = "GET " + path + " HTTP/1.1\r\n" +
//                    "Host: " + host + "\r\n" +
//                    "Connection: close\r\n" +
//                    "\r\n";
//            out.print(request);
//
//            // 读取响应行（包含状态码）
//            String responseLine = in.readLine();
//            if (responseLine == null) {
//                throw new IOException("Server closed connection without sending a response");
//            }
//            System.out.println("Response Line: " + responseLine);
//            String[] parts = responseLine.split(" ");
//            int statusCode = Integer.parseInt(parts[1]);
//            System.out.println("Status Code: " + statusCode);
//
//            // 读取响应头部
//            System.out.println("Response Headers:");
//            Map<String, String> headers = new HashMap<>();
//            String headerLine;
//            while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
//                String[] headerParts = headerLine.split(": ", 2);
//                if (headerParts.length == 2) {
//                    headers.put(headerParts[0], headerParts[1]);
//                    System.out.println(headerLine);
//                }
//            }
//
//            // 获取响应体InputStream
//            InputStream responseBodyStream = socket.getInputStream();
//            System.out.println("Response Body InputStream:");
//            // 这里可以根据需要处理InputStream，例如写入文件或转换为字符串等
//            // 注意：这里只是展示了如何获取InputStream，并未实际读取它的内容
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
//在这个示例中，我们首先创建了一个到指定主机和端口的Socket连接。然后，我们构建了一个简单的HTTP GET请求，并通过PrintWriter发送到服务器。接下来，我们使用BufferedReader从服务器的输入流中读取响应行，解析出状态码。之后，我们继续读取响应头，并将它们存储在一个Map中。最后，我们获取了响应体的InputStream，但并未实际读取它的内容。你可以根据需要处理这个InputStream，例如将其写入文件，或者转换为字符串等。
//
//        请注意，这个示例非常基础，没有处理可能的连接错误、超时或重定向等情况。在实际应用中，你可能需要添加更多的错误处理和功能。此外，对于更复杂的HTTP交互，建议使用现有的HTTP客户端库（如Apache HttpClient或OkHttp），它们提供了更强大和灵活的功能。

//在标准的Socket通信中，服务器不会主动联系客户端。相反，是客户端主动连接到服务器。在连接过程中，客户端确实会把自己的IP地址和端口号告诉服务器，但这个过程是自动完成的，无需客户端显式地发送这些信息。
//
//当客户端创建一个Socket对象并指定服务器的IP地址和端口号时，操作系统会负责底层的网络通信。它会为客户端分配一个可用的本地端口号（如果客户端没有指定），并将客户端的IP地址和端口号与服务器的IP地址和端口号进行匹配，从而建立起连接。这个过程中，客户端的IP地址和端口号会自动成为连接的一部分，而无需客户端显式发送。
//
//一旦连接建立成功，服务器就可以通过该连接与客户端进行通信。服务器可以使用Socket对象的输入/输出流来读取和写入数据，而无需关心客户端的IP地址和端口号的具体细节。这些底层细节是由操作系统和网络协议栈自动处理的。
//
//因此，总结来说，客户端在连接到服务器时，会自动将其IP地址和端口号告诉服务器，但这个过程是隐式的，无需客户端显式发送这些信息。服务器通过已经建立的连接与客户端进行通信，而无需知道或关心客户端的IP地址和端口号的具体值。
