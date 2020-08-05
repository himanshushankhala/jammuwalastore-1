import 'package:flutter/material.dart';
import 'package:grocery_store/splash_screen.dart';
import 'package:grocery_store/login.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Groceries App' ,
      home: SplashScreen(),
    );
  }
}

