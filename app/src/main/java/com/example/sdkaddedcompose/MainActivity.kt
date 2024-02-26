package com.example.sdkaddedcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sdkaddedcompose.ui.theme.SDKAddedComposeTheme
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SDKAddedComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.LightGray,
                ) {
//                    Greeting("Android")
                    AccessFlutterSDK()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray), // Optional background for visualization
        contentAlignment = Alignment.Center
    ) {
        Text("Center Me")
    }
}

@Composable
fun AccessFlutterSDK() {
    val context = LocalContext.current

    val flutterEngine = (context.applicationContext as AddFlutterApplication).flutterEngine
    val methodChannel =
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "CHANNEL_100110")
    var parameterValue = "I'm Coming from Android Native Project !!!!!! First One"
    var parameterValue2 = "I'm Coming from Android Native Project !!!!!! Second One"
    val colorValue = Color.Cyan

    Column(
        modifier = Modifier.fillMaxHeight()

    ) {

        DisposableEffect(Unit) {
            val handler = MethodChannel.MethodCallHandler { call, result ->
                if (call.method == "methodName") {
                    println("Successfully 'MethodName' Channel is  is work in Android Side")
                    result.success(parameterValue)
                } else if (call.method == "sendArgsToNative") {
                    println(call.arguments)
                    print("Successfully 'sendArgsToNative' Channel is work in Android Side")
                    result.success(parameterValue2)
                } else if (call.method == "sendSuccessMessageToNative") {
                    println(call.arguments)
                    println("Successfully 'sendSuccessMessageToNative' Channel is work in Android Side")
                    result.success(null)
                } else {
                    result.notImplemented()
                }
            }

            methodChannel.setMethodCallHandler(handler)

            onDispose {
                methodChannel.setMethodCallHandler(null)
            }
        }

        Text(
            text = "This is some text",
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = {

                try {
                    val employeeInfo: Map<String, Any> = mapOf(
                        "name" to "John Doe",
                        "age" to 30,
                        "isEmployee" to true
                    )
                    methodChannel.invokeMethod("sendDataToFlutter", employeeInfo)
                    println("Data sent successfully send to the Flutter side!")
                } catch (e: Exception) {
                    println("Error sending success message to flutter: ${e.message}");
                }

                context.startActivity(
                    FlutterActivity
                        .withCachedEngine(AddFlutterApplication.FLUTTER_ENGINE_NAME)
                        .build(context)
                )
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Click Me")
        }

    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SDKAddedComposeTheme {
        Greeting("Android")
    }
}



/***Todo
 *
 *
 *
//  static const platform = MethodChannel('CHANNEL_100110');
//  String parameterValue = "";
//
//  //String imagePath="";
//
//  Map<dynamic, dynamic> receivedArgs = {};
//
//
//  @override
//  void initState() {
//    super.initState();
//
//    _receiveDataFromNative();
//
//    _sendDataToNativeSide("Hello from Flutter", 42, true);
//
//    platform.setMethodCallHandler((call) async {
//      if (call.method == 'sendDataToFlutter') {
//        print("****************@@@@@@@@@@@@@ ${call.arguments} @@@@@@@@@@@@***************");
//        try {
//          print("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ${call.arguments.runtimeType}");
//          receivedArgs = call.arguments as Map<dynamic, dynamic>;
//          print("DONE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        } on Exception catch (e) {
//          print("Error is : $e");
//        }
//        await _sendSuccessMessageToNative();
//        print('******************RECIEVED ARGS ********************  ${receivedArgs["name"]}');
//        print('******************RECIEVED ARGS ********************  ${receivedArgs["age"]}');
//        print('******************RECIEVED ARGS ********************  ${receivedArgs["isEmployee"]}');
//      }
//    });
//
//  }
//
//  Future<void> _receiveDataFromNative() async {
//    try {
//      final String receivedValue = await platform.invokeMethod('methodName');
//      print("@@@@@@@@  @@@@@@@@@ @@@@@@@ Received Data from Native Side $receivedValue");
//      setState(() {
//        parameterValue = receivedValue;
//      });
//    } catch (e) {
//      print("@@@@@@@  @@@@@@@  @@@@@@@ Error while Receiving Data From Native Side To Flutter SDK : $e");
//    }
//  }
//
//  Future<void> _sendDataToNativeSide(String arg1, int arg2, bool arg3) async {
//    try {
//      final Map<String, dynamic> arguments = {
//        'arg1': arg1,
//        'arg2': arg2,
//        'arg3': arg3,
//      };
//      final String receivedValue = await platform.invokeMethod('sendArgsToNative', arguments);
//      print("@@@@@@@@  @@@@@@@@@ @@@@@@@ Received Data from Native Side ${receivedValue}");
//    } on PlatformException catch (e) {
//      print('@@@@@@@  @@@@@@@  @@@@@@@ Error while Sending Data to Native Side from Flutter SDK : ${e.message}');
//    }
//  }
//
//  Future<void> _sendSuccessMessageToNative() async {
//    try {
//      await platform.invokeMethod('sendSuccessMessageToNative', 'Success from Flutter!');
//    } on PlatformException catch (e) {
//      print("Error sending success message to native: ${e.message}");
//    }
//
}
 *
 *
 *
 * */

