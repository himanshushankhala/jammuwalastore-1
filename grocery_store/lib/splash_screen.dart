import 'dart:async';
import 'package:flutter/material.dart';
import 'package:grocery_store/login.dart';

class SplashScreen extends StatefulWidget {
  @override
  _SplashScreenState createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  void initState() {
    super.initState();

    _mockCheckForSession().then((status) {
      if (status) {
        _navigateToHome();
      } else {
        _navigateToLogin();
      }
    });
  }

  Future<bool> _mockCheckForSession() async {
    await Future.delayed(Duration(milliseconds: 4000), () {});
    return false;
  }

  void _navigateToHome() {
    Navigator.of(context).pushReplacement(
      MaterialPageRoute(
        builder: (BuildContext context) => Login(),
      ),
    );
  }

  void _navigateToLogin() {
    Navigator.of(context).pushReplacement(
      MaterialPageRoute(
        builder: (BuildContext context) => Login(),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Container(
          child: Stack(
            alignment: Alignment.center,
            children: <Widget>[
              Image.asset('assets/icons/ic_app_logo.png'),
            ],
          ),
        ),
      ),
    );
  }
}
