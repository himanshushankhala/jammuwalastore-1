import 'package:carousel_pro/carousel_pro.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class BannerSlider extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      height: 200,
      child: Carousel(
        boxFit: BoxFit.cover,
        images: [
          AssetImage('assets/images/fruits.png'),
          AssetImage('assets/images/vegetables.jpg'),
          AssetImage('assets/images/dry_fruits.jpg'),
          AssetImage('assets/icons/ic_app_logo.png'),
          AssetImage('assets/icons/ic_app_logo.png'),
        ],
        autoplay: true,
        animationCurve: Curves.fastOutSlowIn,
        animationDuration: Duration(milliseconds: 1000),
        dotSize: 4.0,
        indicatorBgPadding: 2.0,
      ),
    );
  }
}
